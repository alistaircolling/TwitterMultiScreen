import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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

import com.hookedup.led.LEDMatrix;
import com.hookedup.processing.EQLevels;
import com.hookedup.processing.ExtraWindow;
import com.hookedup.processing.ProcessingAppLauncherMinim;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class TwitterMultiScreen extends BaseSwingFrameApp {
	Minim minim;
	AudioPlayer song;
	LoadFromCanvasTask loadFromCanvasTask = new LoadFromCanvasTask();

	Timer timer;

	// ExtraWindow win;
	// ExtraWindow win;

	// --- music fun
	EQLevels eq;
	int lowEQColBuffer = 4;
	int highEQColBuffer = 4;

	float eqDrawFactor = (float) .06;
	float lowEQDrawFactor = (float) .02;
	float highEQDrawFactor = (float) .35;

	int eqFallDelayPos = 0;
	int eqFallDelayAt = 12;
	int musicTop = 400;

	float eqInputAdj = (float) .00;
	int eqLeftOffset = 1;
	int eqRightOffset = 3;

	float colorEQBrt = (float) 1;
	float colorEQBackBrt = (float) .1;

	int eqPos[];

	// --- added for matrix
	int MATRIX_COLS = 40;
	int MATRIX_ROWS = 25;
	LEDMatrix matrix;
	private JFrame frame;

	/**
	 * CONSTRUCTOR
	 */
	public TwitterMultiScreen() {
		final JFrame frame = new JFrame("PApplet in Java Application");
		// make sure to shut down the application, when the frame is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// create a panel for the applet and the button panel
		JPanel panel = new JPanel();

		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		
		// create an instance of your processing applet
		final MyApplet applet = new MyApplet();

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
		panel.add(applet);
		// store the buttonPanel in panel
		panel.add(buttonPanel);
		

		// store the panel in the frame
		frame.add(panel);
		// assign a size for the frame
		// reading the size from the applet
		frame.setSize(1200, 800);

		// display the frame
		frame.setVisible(true);

		setupTwitter();
	}

	private void setupTwitter() {
		
		  ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true);
	        cb.setOAuthConsumerKey("hBMv81DL5k3kWAskc5H6jg");
	        cb.setOAuthConsumerSecret("MnQiXLobTXTSejYeScJPhM8e4uJy1Bg8mzgnw30BMA");
	        cb.setOAuthAccessToken("16739856-1gzgagMQ8QtPR0zwQkOYceNWYQpJZCKTFnKhyem1Z");
	        cb.setOAuthAccessTokenSecret("PgXSKaGAsMQAtCBRj5V9S630HzF8U1AFuFvqcswOD6tX1");

	        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

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
	                System.out.println(username);
	                String profileLocation = user.getLocation();
	                System.out.println(profileLocation);
	                long tweetId = status.getId(); 
	                System.out.println(tweetId);
	                String content = status.getText();
	                System.out.println(content +"\n");

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
	    
	        String keywords[] = {"xfactor"};

	        fq.track(keywords);

	        twitterStream.addListener(listener);
	        twitterStream.filter(fq);  

		
	}

	void loadDefaultMatrix() {
		String tmpResult = matrix
		// .loadMatrixFile("/Users/acolling/Desktop/default.xml");
				.loadMatrixFile("c:/matrix/default.xml");
		if (tmpResult.equals("")) {
			// System.out.println("File Loaded.");
			return;
		}
		System.out.println(tmpResult);
	}

	void matrixSetup() {
		matrix = new LEDMatrix(MATRIX_COLS, MATRIX_ROWS, 24, 24, 1);
		loadDefaultMatrix();

		// -- TO CONNECT --->>>
		matrix.connectToController();

		matrix.refresh();
		matrix.emulatorDelay = 20;
		matrix.ui.setLocation(this.getLocation().x + this.getWidth(),
				this.getLocation().y);

		matrix.ui.setVisible(true);

		eqPos = new int[200];

		setupTimer();

	}

	void setupExtraWindow() {

		// win = new DropsWindow(proc, "Matrix Setup", 500, 300);
		// win.setVisible(false);

	}

	// --- overrides the set process event to include loading minim from the
	// base
	public void setProc(PApplet theproc) {
		super.setProc(theproc);
		minim = ((ProcessingAppLauncherMinim) proc).minim;
		eq = new EQLevels(minim, 20);
	}

	private void launchFS() {

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

		// TODO Auto-generated method stub
		System.out.println("launch fs");
	}

	void playDemoSong() {
		if (minim == null) {
			System.out.println("NULL Minim");
			return;
		}
		eq.loadSong(txtSongName.getText());
		eq.song.play();
	}

	private JPanel contentPane;
	private JTextField txtSongName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// AppSoundDemoSwing frame = new AppSoundDemoSwing();
					// frame.setVisible(true);

					// ProcessingAppLauncher procLaunch = new
					// ProcessingAppLauncher();
					// NOTE: Using Minim version
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

	void setupTimer() {
		timer = new Timer();
		timer.schedule(loadFromCanvasTask, 0, // initial delay
				100);
	}

	class LoadFromCanvasTask extends TimerTask {
		public void run() {
			while (true) {
				// run timer task here
			}

		}
	}
}
