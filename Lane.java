import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class Lane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	ArrayList<Car> cars = new ArrayList<Car>();
	private int windowWidth = 1000;
	private int numCars = 0;
	private int laneIndex = 0;
	private Random rand = new Random();
	
	public Lane() {
		
	}
	
	public Lane(int k) {
		int numCars = k;
		for (int i = 0; i < numCars; i++) {
			// generate random color, a random lane, generate a car, then set the color and lane
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);
			Car newCar = new Car();
			newCar.setColor(randomColor);
			newCar.setLane(laneIndex);
			newCar.setVelocity(1 + 3 * Math.abs(rand.nextFloat()));
			newCar.setXPosition(windowWidth * i / numCars);
			cars.add(newCar);
		}
	}
	
	// method to generate a car in the lane
	public void addCar() {
		numCars++;
		
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		Color randomColor = new Color(r, g, b);
		Car newCar = new Car();
		newCar.setColor(randomColor);
		newCar.setLane(laneIndex);
		newCar.setVelocity(1 + 3 * Math.abs(rand.nextFloat()));
		newCar.setXPosition(findGap()); // To do: write a method to find an open spot.
		cars.add(newCar);
	}
	
	public void paintComponent(Graphics graphics) {
		for (Car myCar : cars) {
			myCar.paintComponent(graphics);
		}
	}
	
	public void update() {
		int totalCars = cars.size();
		for (int i = 0; i < totalCars; i++) {
			Car carA = cars.get(i);
			double minDistance = Double.MAX_VALUE;

			// find the closest car in the same lane
			for (int j = 0; j < totalCars; j++) {
				Car carB = cars.get(j);
				Car front = null;
				double distance = carB.getXPosition() - carA.getXPosition();
				if (i != j && distance > 0 && distance < minDistance) {
					minDistance = distance;
					front = carB;
				}
				if (front != null) {
					carA.updateAcceleration(minDistance, front.getVelocity());
				}
			}

			carA.update(windowWidth);
		}
	}
	
	// method to find the starting x coordinate of the largest gap between cars
	public double findGap() {
		double xMin = 0.0;
		if (numCars == 0) {
			return xMin;
		} else if (numCars == 1) {
			return cars.get(0).getXPosition();
		} else {
			// create an array of positions of cars in the ArrayList cars
			double[] position = new double[numCars];
			for (int i = 0; i < numCars; i++) {
				position[i] = cars.get(i).getXPosition();
			}
			
			this.sort(position);
			
			// find largest gap from sorted array of length at least 2, and set xMin to the start of the gap
			double maxGap = 0.0;
			for (int i = 0; i < numCars - 1; i++) {
				if ( maxGap <= position[i+1] - position[i]) {
					maxGap = position[i+1] - position[i];
					xMin = position[i];
				}
			}
			
			return xMin;
		}
	}
	
	public double[] sort(double[] position)	{
		// To do: write heapsort or quicksort algorithm
		
		return position;
	}
	
	// methods to get the number of cars in this lane
	public int getLength() {
		return numCars;
	}
	
	//methods to get and set the index of this lane
	public int getIndex() {
		return laneIndex;
	}
	
	public void setIndex(int newIndex) {
		laneIndex = newIndex;
		
		for (Car myCar : cars) {
			myCar.setLane(laneIndex);
		}
	}
	
	// methods to get a Car from this lane
	public Car getCar(int i) {
		return cars.get(i);
	}
}
