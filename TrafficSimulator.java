import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrafficSimulator extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static int numCars = 24;
	public static int numLanes = 8;
	public static boolean allowLaneChanges = true;
	public static double laneChangeChange = 0.999;
	public static double aggressionChance = 0.90;
	public static ArrayList<Lane> lanes = new ArrayList<Lane>();
	public static final int windowWidth = 1000;
	public static final int windowHeight = Math.max(200, 200 + 28*numLanes);
	public static final int carHeight = 18;

	public static boolean paused = false;
	public static JButton pauseButton;
	
	private Timer t = new Timer(15, (ActionListener) this);
	private Random rand = new Random();

	public static void main(String[] args) {
		TrafficSimulator sim = new TrafficSimulator(numCars, numLanes);
		
		JFrame frame = new JFrame();
		frame.add(sim);
		frame.setVisible(true);
		frame.setSize(windowWidth, windowHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Traffic Simulator");
//		frame.setBackground(Color.LIGHT_GRAY);
		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//		mainPanel.setBackground(Color.cyan);
		frame.add(mainPanel);
				
		JPanel lanePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
//		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		frame.add(sim);
		mainPanel.add(lanePanel);
		mainPanel.add(buttonPanel);
//		lanePanel.setBackground(Color.LIGHT_GRAY);
		lanePanel.setOpaque(false);
		lanePanel.setMinimumSize(new Dimension(windowWidth, windowHeight-100));
//		buttonPanel.setOpaque(false);
//		mainPanel.setOpaque(false);
		buttonPanel.setBackground(Color.red);
		
		pauseButton = new JButton("Pause");
		mainPanel.add(pauseButton);
		ButtonListener pauseButtonListener = sim.new ButtonListener();
		pauseButton.addActionListener(pauseButtonListener);
	}

	public TrafficSimulator(int numCars, int numLanes) {
		int[] carsPerLane = new int[numLanes];
		
		for (int i = 0; i < numCars; i++) {
			carsPerLane[rand.nextInt(numLanes)]++;
		}
		
		for (int i = 0; i < numLanes; i++) {
			Lane newLane = new Lane(carsPerLane[i]);
			newLane.setIndex(i);
			lanes.add(newLane);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (paused == false) {
			this.update();
			this.repaint();
		}
	}

	// paint cars to the screen
	public void paintComponent(Graphics graphics) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, windowWidth, windowHeight-150);

		// draw the lanes using the car height
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 25, windowWidth, carHeight + 10);
		for ( int i = 1; i < numLanes; i++) {		
			graphics.setColor(Color.DARK_GRAY);
			graphics.fillRect(0, 25 + i * (carHeight + 10), windowWidth, carHeight + 10);
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(0, 25 + i * (carHeight + 10), windowWidth, 1);	
		}

		// call methods to draw each car
		for (Lane myLane : lanes) {
			myLane.paintComponent(graphics);
		}

		t.start();
	}

	// call update method for each car in the list
	public void update() {
		for (Lane myLane : lanes) {
			myLane.update();
		}
	}
	
	public class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent evt) {
			Object actionSource = evt.getSource(); // Label of the button clicked in evt
			if (actionSource == pauseButton && paused == false) {
				System.out.println("Pause button pressed: pause");
				paused = true;
			} else if (actionSource == pauseButton && paused == true) {
				System.out.println("Pause button pressed: resume");
				paused = false;
			}
		}
	}
	
	public Lane getLane(int i) {
		return lanes.get(i);
	}

}