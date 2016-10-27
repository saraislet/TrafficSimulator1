import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class Car extends JPanel {
	private static final long serialVersionUID = 8765911154107486041L;

	// set initial conditions for the car, unless called by a method that overrides these default values
	private double x = 0;
	private double v = 1;
	private double vMax = 8;
	private double a = 0;
	private int laneIndex = 0;
	private int laneOffset = 0;
	private static int carWidth=30;
	private static int carHeight=18;
	private Color preferredColor = Color.RED;
	private Color color = Color.RED;
	private double preferredDistance = carWidth * 5;
	private double preferredVelocity = vMax;
	private double minDistance = carWidth * 3;
	private Lane myLane;
	private Random rand = new Random();

	public boolean flagLaneChange = false;
	public int flagLaneChanged = 0;
	public int newLaneIndex;


	// generate a car at a given x coordinate
	public Car(Lane lane, double xPosition) {
		x = xPosition;
		myLane = lane;
	}

	// generate a car with a given x coordinate and velocity
	public Car(Lane lane, double xPosition, double velocity) {
		this(lane, xPosition);
		v = velocity;
	}

	// generate a car with a given x coordinate, velocity, and acceleration
	public Car(Lane lane, double xPosition, double velocity, double acceleration) {
		this(lane, xPosition, velocity);
		a = acceleration;
	}

	// 
	public void update(int windowWidth) {
		// move back to start if right edge of car passes the right edge of the frame
		if (x > windowWidth - carWidth) {
			x=0;
		}

		// increment the velocity by the acceleration if the magnitude of the velocity is less than vmax and preferredVelocity
		// but always allow deceleration
		if (a >= 0 && Math.abs(v) < vMax && Math.abs(v) < preferredVelocity) {
			v = Math.max(0, v + a);
		} else if (a < 0) {
			v = v + a;
		}

		x = x + v;
		
		// check if lane changes are allowed and the random gaussian passes the test
		double laneChangeChance = rand.nextGaussian();
		if (TrafficSimulator.allowLaneChanges == true && myLane.flagLaneChange == false && Math.abs(laneChangeChance) > 3.5) {
			// allow laneIndex increases if there is a higher lane index, then check that lane for an open spot
			if (laneChangeChance > 0 && TrafficSimulator.numLanes > laneIndex + 1) {
				if (TrafficSimulator.lanes.get(laneIndex + 1).checkLane(x - preferredDistance, x + preferredDistance) == true) {
//					myLane.changeLane(this, laneIndex + 1, 1);
					System.out.println("Car in lane " + laneIndex + " changed to lane " + (laneIndex + 1));
					myLane.flagLaneChange(this, laneIndex + 1);
					flagLaneChange = true;
					newLaneIndex = laneIndex + 1;
				}
				// allow laneIndex decreases if there is a lower lane index, then check that lane for an open spot
			} else if (laneChangeChance < 0 && laneIndex > 0) {
				if (TrafficSimulator.lanes.get(laneIndex - 1).checkLane(x - preferredDistance, x + preferredDistance) == true) {
//					myLane.changeLane(this, laneIndex - 1, -1);
					System.out.println("Car in lane " + laneIndex + " changed to lane " + (laneIndex - 1));
					myLane.flagLaneChange(this, laneIndex - 1);
					flagLaneChange = true;
					newLaneIndex = laneIndex - 1;
				}
			}
		}
		
		// if lane was changed, handle updating the y-coordinate to smoothly transition between lanes
		if (flagLaneChanged != 0) {
			laneOffset = flagLaneChanged * (10 + carHeight);
			flagLaneChanged = 0;
		}
		if (laneOffset > 0) {
			laneOffset--;
		} else if (laneOffset < 0) {
			laneOffset++;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(color);
		g.fillRect((int) x, 30 + laneOffset + laneIndex * (carHeight + 10), carWidth, carHeight);
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

	// methods to get or set the car's lane index
	public int getLaneIndex() {
		return laneIndex;
	}

	public void setLaneIndex(int newLaneIndex) {
		laneIndex = newLaneIndex;
	}
	
	// methods to get or set the car's lane
	public Lane getLane() {
		return myLane;
	}

	public void setLane(Lane newLane) {
		myLane = newLane;
	}
	
	// methods to return the car's width and height
	public int getCarWidth() {
		return carWidth;
	}

	public int getCarHeight() {
		return carHeight;
	}
	
	// method to flag that a car changed lanes
	public void setFlagLaneChanged(int direction) {
		flagLaneChanged = direction;
	}

	// method to set the car's color
	public void setColor(Color colorChoice) {
		preferredColor = colorChoice;
		color = colorChoice;
	}

	public void updateAcceleration(double distance, double frontVelocity) {
		if (distance < minDistance) {
			a = 0.003 * (distance - preferredDistance);
			preferredVelocity = frontVelocity;
			color = Color.RED;
		} else if (distance < preferredDistance) {
			if (v > frontVelocity) {
				a = 0.002 * (distance - preferredDistance);
				preferredVelocity = frontVelocity;
				color = Color.ORANGE;
			} else {
				a = 0;
				preferredVelocity = frontVelocity;
				v = frontVelocity;
				color = Color.CYAN;
			}
		} else if (distance >= preferredDistance && color == Color.ORANGE) {
			a = 0;
			v = preferredVelocity;
			color = preferredColor;
		} else if (v <= 0) {
			a = 0.02;
			color = Color.PINK;
		}
	}

}