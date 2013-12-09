import java.awt.EventQueue;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import processing.core.PApplet;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.hookedup.led.LEDMatrix;
import com.hookedup.processing.EQLevels;
import com.hookedup.processing.ProcessingAppLauncherMinim;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class TwitterMultiScreen extends BaseSwingFrameApp {

	Timer timer;

	// ExtraWindow win;

	private JFrame frame;
	private MyApplet applet;
	ArrayList<Map.Entry<String, Integer>> mapList;

	/**
	 * CONSTRUCTOR
	 */
	public TwitterMultiScreen() {
		final JFrame frame = new JFrame("Brand Live Realtime Tracking");
		// make sure to shut down the application, when the frame is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create a panel for the applet and the button panel
		JPanel panel = new JPanel();

		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();

		// create an instance of your processing applet
		applet = new MyApplet();

		// start the applet
		applet.init();

		// Buttons
		// create a button labled "create new ball"
		JButton buttonCreate = new JButton("create new ball");
		// assing a tooltip
		buttonCreate.setToolTipText("creates a new ball ");
		// give a name for the command
		// if this is not assigned the actionCommand equals the button label
		buttonCreate.setActionCommand("create ball");

		// create a button lable "load file"
		JButton buttonLoad = new JButton("load file");
		buttonLoad.setToolTipText("loads a new background image");

		// button actions

		// the create button is simply linked to the applet
		// the action is executed inside applet.actionPerformed()
		buttonCreate.addActionListener(applet);

		// store the two buttons in the button panel
		buttonPanel.add(buttonCreate);
		buttonPanel.add(buttonLoad);

		// store the applet in panel

		// store the buttonPanel in panel
		// panel.add(buttonPanel);

		// store the panel in the frame
		frame.add(panel);
		// assign a size for the frame
		// reading the size from the applet
		frame.setSize(1024, 868);
		panel.add(applet);

		// display the frame
		frame.setVisible(true);
		createDataStore();
		setupTwitter();
		setupTimer();
	}

	private void setupTwitter() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("hBMv81DL5k3kWAskc5H6jg");
		cb.setOAuthConsumerSecret("MnQiXLobTXTSejYeScJPhM8e4uJy1Bg8mzgnw30BMA");
		cb.setOAuthAccessToken("16739856-1gzgagMQ8QtPR0zwQkOYceNWYQpJZCKTFnKhyem1Z");
		cb.setOAuthAccessTokenSecret("PgXSKaGAsMQAtCBRj5V9S630HzF8U1AFuFvqcswOD6tX1");

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();

		StatusListener listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatus(Status status) {
				User user = status.getUser();

				// gets Username
				String username = status.getUser().getScreenName();

				String profileLocation = user.getLocation();

				long tweetId = status.getId();
				String content = status.getText();
				applet.setTweet(returnTime() + ":  " + status.getText());
				addKeywords(status);
				traceTotals();

			}

			// returns the hms time
			private String returnTime() {
				Calendar cal = Calendar.getInstance();
				cal.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String retS = sdf.format(cal.getTime());
				return retS;
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};
		FilterQuery fq = new FilterQuery();

		String keywords[] = { "xfactor", "cats" };

		fq.track(keywords);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

	}

	private void createDataStore() {

		mapList = new ArrayList<Map.Entry<String, Integer>>();
		

	}

	protected void addKeywords(Status status) {
		// get tweet
		String str = status.getText();
		// str = "one, two three";
		// split into an array remove punctuation and make lower case
		String[] splited = str.replaceAll("[^a-zA-Z ]", "").toLowerCase()
				.split("\\s+");
		// vars used in loop
		String thisStr;
		int wordTot;

		Entry<String, Integer> newEntry = null;
		boolean isInList = false;

		for (int i = 0; i < splited.length; i++) {
			// get word from array
			thisStr = splited[i];

			// thisStr = "hi";
			// if this is the first word to be added
			if (mapList.size() == 0) {
				// this is the syntax that I don't know!
				mapList.add(new AbstractMap.SimpleEntry<String, Integer>(
						thisStr, 1));
			} else {
				// iterate through mapList

				for (Entry<String, Integer> entry : mapList) {
					// already exists
					if (entry.getKey().equals(thisStr)) {
						wordTot = entry.getValue();
						// increment the value
						wordTot++;
						entry.setValue(wordTot);
						mapList.remove(entry);
						mapList.add(new AbstractMap.SimpleEntry<String, Integer>(
								thisStr, wordTot));
						isInList = true;
						break;
					}

				}
				if (!isInList) {
					// if we have reached here the value must not be in the
					// arraylist so add it
					if (thisStr.length() > 3 && !thisStr.equals("xfactor")) {
						mapList.add(new AbstractMap.SimpleEntry<String, Integer>(
								thisStr, 1));
					}
				}
			}

		}
		sortMap();
	}

	private void sortMap() {
		Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				if (o1.getValue() > o2.getValue()) {
					return -1;
				} else if (o1.getValue() < o2.getValue()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	private void traceTotals() {
		applet.printList(mapList);
	}

	void setupExtraWindow() {

		// win = new DropsWindow(proc, "Matrix Setup", 500, 300);
		// win.setVisible(false);

	}

	private void launchFS() {

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		// TODO Auto-generated method stub
		System.out.println("launch fs");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ProcessingAppLauncherMinim procLaunch = new ProcessingAppLauncherMinim();
					procLaunch.launch("TwitterMultiScreen");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void runDemo1() {
		if (proc == null) {
			System.out.print("NULL");
		} else {
			System.out.print("ACTIVE");
		}
	}

	Runnable helloRunnable = new Runnable() {
		public void run() {
			System.out.println("Hello world");
		}

	};

	private ArrayList<Snapshot> snapShots;

	// executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

	void setupTimer() {
		// loadFromCanvasTask = new LoadFromCanvasTask();
		timer = new Timer();
		timer.scheduleAtFixedRate(new SnapShotTask(), 10000, 5000);
		snapShots = new ArrayList<Snapshot>();
	}

	public void snapshotData() {
		
		Calendar cal = Calendar.getInstance();
		Date time = cal.getTime();
		Snapshot snapShot = new Snapshot(time, mapList, snapShots.size()-1);
		snapShots.add(snapShot);
		System.out.println("snapshot! total now:"+snapShots.size());
		applet.setNewSnapShotList(snapShots);
		
		
	}

	class SnapShotTask extends TimerTask {
		public void run() {
			snapshotData();
		}

	}
}
