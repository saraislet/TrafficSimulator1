import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TrafficSimulator extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int numCars = 15;
	private static int numLanes = 5;
	public static final int windowWidth = 1000;
	public static final int windowHeight = Math.max(200, 100 + 28*numLanes);
	ArrayList<Lane> lanes = new ArrayList<Lane>();
	private Timer t = new Timer(15, (ActionListener) this);
	private Random rand = new Random();


	public static void main(String[] args) {
		TrafficSimulator sim = new TrafficSimulator(numCars, numLanes);
		JFrame f = new JFrame();
		f.add(sim);
		f.setVisible(true);
		f.setSize(windowWidth, windowHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Traffic Simulator");
		f.setBackground(Color.LIGHT_GRAY);
	}

	public TrafficSimulator() {
		lanes.add(new Lane());
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
		this.update();
		this.repaint();
	}

	// paint cars to the screen
	public void paintComponent(Graphics graphics) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, windowWidth, windowHeight);

		// draw the lanes using the car height
		int carHeight = lanes.get(0).getCar(0).getCarHeight();
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

}