import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JPanel;

public class Car extends JPanel {
	private static final long serialVersionUID = 8765911154107486041L;

	// set initial conditions for the car, unless called by a method that overrides these default values
	private double x = 0;
	private double v = 1;
	private double vMax = 5;
	private double a = 0;
	private int laneIndex = 0;
	private double laneOffset = 0;
	private static int carWidth=30;
	private static int carHeight=18;
	private Color preferredColor = Color.RED;
	private Color color = Color.RED;
	private double aggressionLevel = 0;
	private double preferredDistance = carWidth * 5;
	private double preferredVelocity = 3.5;
	private double minDistance = carWidth * 3;
	private double changeLaneDistance = preferredDistance;
	private double laneChangeChanceOffset = 0;
	private double laneChangeMultiplier = 1;
	private long timeToChangeLane = 3000; // this is how long the lane change should take in milliseconds
	private long timeOfLaneChange;
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
			x=-carWidth;
		}

		// don't allow velocities over vMax, nor over preferredVelocity
		if (v > vMax || v > preferredVelocity) {
			v = Math.min(vMax, preferredVelocity);
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
		double laneChangeChance = 1 - 2 * rand.nextFloat();
		if (TrafficSimulator.allowLaneChanges == true && myLane.flagLaneChange == false && Math.abs(laneChangeChance) + laneChangeChanceOffset > TrafficSimulator.laneChangeChange) {
			// allow laneIndex increases if there is a higher lane index, then check that lane for an open spot
			if (laneChangeChance > 0 && TrafficSimulator.numLanes > laneIndex + 1) {
				if (TrafficSimulator.lanes.get(laneIndex + 1).checkLane(x - changeLaneDistance, x + changeLaneDistance) == true) {
//					myLane.changeLane(this, laneIndex + 1, 1);
					System.out.println("Car in lane " + laneIndex + " changed to lane " + (laneIndex + 1));
					myLane.flagLaneChange(this, laneIndex + 1);
					flagLaneChange = true;
					newLaneIndex = laneIndex + 1;
				}
				// allow laneIndex decreases if there is a lower lane index, then check that lane for an open spot
			} else if (laneChangeChance < 0 && laneIndex > 0) {
				if (TrafficSimulator.lanes.get(laneIndex - 1).checkLane(x - changeLaneDistance, x + changeLaneDistance) == true) {
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
			timeOfLaneChange = System.currentTimeMillis();
		}
		if (laneOffset != 0) {
//			laneOffset--;
			long timeSinceLaneChange = System.currentTimeMillis() - timeOfLaneChange;
			laneChangeMultiplier = 1 - easeInOutCubic( (double) timeSinceLaneChange / timeToChangeLane );
			laneOffset = laneOffset * laneChangeMultiplier;
			if (Math.abs(laneOffset) < 0.5) {
				laneOffset = 0;
			}
//		 	System.out.println("Lane offset: " + (int) laneOffset + "; laneChangeMultiplier: " + laneChangeMultiplier + "; timeSinceLaneChange: " + timeSinceLaneChange);
//			System.out.println("timeSinceLaneChange / timeToChangeLane: " + (double) timeSinceLaneChange / timeToChangeLane);
//			System.out.println("easeInOutCubic( timeSinceLaneChange / timeToChangeLane ): " + easeInOutCubic( (double) timeSinceLaneChange / timeToChangeLane ));
		}
	}
	
	public void updateAcceleration(double distance, double frontVelocity) {
		laneChangeChanceOffset = aggressionLevel / 3000;
		if (distance < minDistance) {
			a = 0.005 * (distance - preferredDistance);
			color = Color.RED;
			laneChangeChanceOffset += 0.0002;
		} else if (distance < preferredDistance) {
			if (v > frontVelocity) {
				a = 0.0015 * (distance - preferredDistance);
				color = Color.ORANGE;
//				laneChangeChanceOffset += 0.0002;
			} else {
				a = 0;
//				preferredVelocity = frontVelocity;
				v = frontVelocity;
				color = Color.CYAN;
				laneChangeChanceOffset += 0.00005;
			}
		} else if (distance >= preferredDistance && color != preferredColor) {
			a = 0.005;
			color = preferredColor;
		} else if (v <= 0) {
			a = 0.02;
			color = Color.PINK;
		}
		
		// if velocity is below 80% of preferredVelocity, increase lane change chance
		if (v < 0.8 * preferredVelocity) {
//			laneChangeChanceOffset += .0001;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(color);
		g.fillRect((int) x, 30 + ((int) laneOffset) + laneIndex * (carHeight + 10), carWidth, carHeight);
	}
	
	// easing functions
	public double easeInCubic(double t) {
		return Math.pow(t, 3);
	}
	
	public double easeOutCubic(double t) {
		return 1 - easeInCubic(t);
	}
	
	public double easeInOutCubic(double t) {
		if (t < 0.5) {
			return easeInCubic(t * 2.0) / 2.0;
		} else {
			return 1 - easeInCubic((1-t) * 2.0) / 2.0;
		}
	}
	
	// methods to get or set the car's aggression level
	public double getAggressionLevel() {
		return aggressionLevel;
	}

	// only allow values between 0-3
	public void setAggressionLevel(double newAggressionLevel) {
		if (newAggressionLevel > 2) {
			newAggressionLevel = 2;
		} else if (newAggressionLevel < 0) {
			newAggressionLevel = 0;
		}
			aggressionLevel = newAggressionLevel;
			minDistance = carWidth * (3 - aggressionLevel / 3);
			preferredDistance = carWidth * (5 - aggressionLevel * 2/3);
			preferredVelocity = preferredVelocity + aggressionLevel * 2/3;
			changeLaneDistance = preferredDistance;
			v = 1.2 * v;
			a = 0.01;
			
			float red = (float) aggressionLevel / 3; // car will be a shade of red
//			float green = preferredColor.getGreen() / 255;
//			float blue = preferredColor.getBlue() / 255;
			preferredColor = new Color(red, 0, 0);
			color = preferredColor;
			DecimalFormat twoPlaces = new DecimalFormat("0.00");
			System.out.println("Car has aggression level " + twoPlaces.format(aggressionLevel) + ".");
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
	// direction should be a nonzero value, oldLaneIndex - newLaneIndex
	public void setFlagLaneChanged(int direction) {
		flagLaneChanged = direction;
		
		// if lane was changed, handle updating the y-coordinate to smoothly transition between lanes
		if (flagLaneChanged != 0) {
			laneOffset = flagLaneChanged * (10 + carHeight);
			flagLaneChanged = 0;
			timeOfLaneChange = System.currentTimeMillis();
		}
	}

	// method to set the car's color
	public void setColor(Color colorChoice) {
		preferredColor = colorChoice;
		color = colorChoice;
	}
}