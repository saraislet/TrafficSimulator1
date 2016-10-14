import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Car extends JPanel {
	private static final long serialVersionUID = 8765911154107486041L;
	
	// set initial conditions for the car, unless called by a method that overrides these default values
	private double x = 0;
	private double v = 1;
	private double vmax = 8;
	private double a = 0;
	private int carWidth=30;
	private int carHeight=18;
	private Color preferredColor = Color.RED;
	private Color color = Color.RED;
	private double preferredDistance = carWidth * 5;
	private double minDistance = carWidth * 3;
	
	public static void main(String[] args) {
		new Car();
	}
	
	public Car() {

	}
	
	// generate a car at a given x coordinate
	public Car(double xPosition) {
		x = xPosition;
	}
	
	// generate a car with a given x coordinate and velocity
	public Car(double xPosition, double velocity) {
		x = xPosition;
		v = velocity;
	}
	
	// generate a car with a given x coordinate, velocity, and acceleration
	public Car(double xPosition, double velocity, double acceleration) {
		x = xPosition;
		v = velocity;
		a = acceleration;
	}
	
	// 
	public void update(int windowWidth) {
		// move back to start if right edge of car passes the right edge of the frame
		if (x > windowWidth - carWidth) {
			x=0;
		}
		
		// increment the velocity by the acceleration if the magnitude of the velocity is less than vmax
		if (Math.abs(v) < vmax) {
			v = Math.max(0, v + a);
//			color = preferredColor;
		} else {
			color = Color.BLUE;
		}
		
		x = x + (int) v;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(color);
		g.fillRect((int) x, 30, carWidth, carHeight);
	}
	
	// methods to get or set the x position
	public double getXPosition() {
		return x;
	}
	
	public void setXPosition(double newXPosition) {
		x = newXPosition;
	}
	
	// methods to get or set the velocity
	public double getVelocity() {
		return v;
	}
	
	public void setVelocity(double newVelocity) {
		v = newVelocity;
	}
	
	// methods to get or set the acceleration
	public double getAcceleration() {
		return a;
	}
	
	public void setAcceleration(double newAcceleration) {
		a = newAcceleration;
	}
	
	// methods to get or set the car's preferred distance
	public double getPreferredDistance() {
		return preferredDistance;
	}
	
	public void setPreferredDistance(double newPreferredDistance) {
		preferredDistance = newPreferredDistance;
	}
	
	// method to return the car's width and height
	public int getCarWidth() {
		return carWidth;
	}
	
	public int getCarHeight() {
		return carHeight;
	}
	
	// method to set the car's color
	public void setColor(Color colorChoice) {
		preferredColor = colorChoice;
		color = colorChoice;
	}

	public void updateAcceleration(double distance, double velocity) {
		if ( distance < minDistance ) {
			a = 0.003*(distance - preferredDistance);
			color = Color.RED;
		} else if ( distance < preferredDistance ) {
			a = 0.001*(distance - preferredDistance);
			color = Color.ORANGE;
		} else if ( distance >= preferredDistance && color == Color.ORANGE ) {
			a = 0;
			v = velocity;
			color = preferredColor;
		} else if ( v <= 0 ) {
				a = 0.02;
		}
	}
	
}