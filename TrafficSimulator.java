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
	public static final int windowWidth = 1000;
	public static final int windowHeight = 200;
	ArrayList<Car> cars = new ArrayList<Car>();
	private Timer t = new Timer(15, (ActionListener) this);
	private Random rand = new Random();
	
	public static void main(String[] args) {
		TrafficSimulator sim = new TrafficSimulator(2);
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
			// generate random color, generate a car, then set the color
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);
			Car newCar = new Car(200*i);
			newCar.setColor(randomColor);
			newCar.setVelocity(1+3*Math.abs(rand.nextFloat()));
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
		
		// draw the road using the car height
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 25, windowWidth, (int)(cars.get(0).getCarHeight() + 10));
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
			int minDistance = windowWidth;
			double appropriateVelocity = carA.getVelocity();
			
			// find the closest car
			for (int j = 0; j < totalCars; j++) {
				Car carB = cars.get(j);
				int distance = carB.getXPosition() - carA.getXPosition();
				if ( distance > 0 && distance < minDistance) {
					minDistance = distance;
					appropriateVelocity = carB.getVelocity();
				}
			carA.updateAcceleration(minDistance, appropriateVelocity);
			}
			
			carA.update(windowWidth);
		}
	}
	
}