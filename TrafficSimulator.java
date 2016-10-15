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
	private static int numCars = 10;
	private static int numLanes = 4;
	public static final int windowWidth = 1000;
	public static final int windowHeight = Math.max(200, 100 + 28*numLanes);
	ArrayList<Car> cars = new ArrayList<Car>();
	private Timer t = new Timer(15, (ActionListener) this);
	private Random rand = new Random();

	
	public static void main(String[] args) {
		TrafficSimulator sim = new TrafficSimulator(numCars);
		JFrame f = new JFrame();
		f.add(sim);
		f.setVisible(true);
		f.setSize(windowWidth, windowHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Traffic Simulator");
		f.setBackground(Color.LIGHT_GRAY);
		
//		while (true) {
//			sim.update();
//			sim.repaint();
//		}
	}
	
	public TrafficSimulator() {
		cars.add(new Car());
	}
	
	public TrafficSimulator(int k) {
		for (int i = 0; i < k; i++) {
			// generate random color, a random lane, generate a car, then set the color and lane
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			int lane = rand.nextInt(numLanes);
			Color randomColor = new Color(r, g, b);
			Car newCar = new Car(200*i);
			newCar.setColor(randomColor);
			newCar.setVelocity(1+3*Math.abs(rand.nextFloat()));
			newCar.setLane(lane);
			cars.add(newCar);
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
		int carHeight = cars.get(0).getCarHeight();
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 25, windowWidth, carHeight + 10);
		for ( int i = 1; i < numLanes; i++) {		
			graphics.setColor(Color.DARK_GRAY);
			graphics.fillRect(0, 25 + i * (carHeight + 10), windowWidth, carHeight + 10);
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(0, 25 + i * (carHeight + 10), windowWidth, 1);	
		}
		
		// call methods to draw each car
		for (Car myCar : cars) {
			myCar.paintComponent(graphics);
		}
		
		t.start();
	}
	
	// call update method for each car in the list
	public void update() {
		int totalCars = cars.size();
		for (int i = 0; i < totalCars; i++) {
			Car carA = cars.get(i);
			double minDistance = windowWidth;
			double appropriateVelocity = carA.getVelocity();
			int currentLane = carA.getLane();
			
			// find the closest car in the same lane
			for (int j = 0; j < totalCars; j++) {
				Car carB = cars.get(j);
				double distance = carB.getXPosition() - carA.getXPosition();
				if ( currentLane == carB.getLane() && distance > 0 && distance < minDistance ) {
					minDistance = distance;
					appropriateVelocity = carB.getVelocity();
				}
			carA.updateAcceleration(minDistance, appropriateVelocity);
			}
			
			carA.update(windowWidth);
		}
	}
	
}