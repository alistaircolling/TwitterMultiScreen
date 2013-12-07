import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.util.Timer;
import java.util.TimerTask;

import processing.core.PApplet;

import com.hookedup.led.LEDMatrix;
import com.hookedup.processing.EQLevels;
import com.hookedup.processing.ExtraWindow;
import com.hookedup.processing.ProcessingAppLauncherMinim;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextField;

public class TwitterMultiScreen extends BaseSwingFrameApp {
	Minim minim;
	AudioPlayer song;
	LoadFromCanvasTask loadFromCanvasTask = new LoadFromCanvasTask();

	Timer timer;

	// ExtraWindow win;
	ExtraWindow win;

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

	/**
	 * CONSTRUCTOR
	 */
	public TwitterMultiScreen() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				loadFromCanvasTask = null;

				matrix.end();
				timer.cancel();
				timer.purge();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

				System.exit(0);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnDemo = new JButton("Fullscreen");
		btnDemo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				launchFS();
			}

		});
		btnDemo.setBounds(10, 11, 89, 23);
		contentPane.add(btnDemo);

		JButton btnPlayDemoSong = new JButton("Play Demo Song");
		btnPlayDemoSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playDemoSong();
			}
		});
		btnPlayDemoSong.setBounds(133, 11, 195, 23);
		contentPane.add(btnPlayDemoSong);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (eq.song == null)
					return;

				if (eq.song.isPlaying())
					eq.song.close();
			}
		});
		btnStop.setBounds(10, 45, 89, 23);
		contentPane.add(btnStop);

		txtSongName = new JTextField();
		txtSongName.setText("/letithappen.mp3");
		txtSongName.setBounds(133, 33, 195, 20);
		contentPane.add(txtSongName);
		txtSongName.setColumns(10);

		setupExtraWindow();

		// moved -- matrixSetup();

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
		win = new MyExtraWindow(proc, "EXTRA WINDOW", 600, 600);
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

		 this.setExtendedState(Frame.MAXIMIZED_BOTH);

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
