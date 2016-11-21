# TrafficSimulator1
Java application to simulate behavior of cars driving on an expressway.

This application can generate a number of lanes, and a number of cars randomly distributed among the lanes. Cars are given random colors. Cars will accelerate to approach a set maximum velocity, but will slow down to tail other cars. Cars can also change lanes randomly, and are more likely to change lanes if they are behind another car.

Some cars are generated with red shades with nonzero aggression levels. Aggressive drivers tail cars more closely, drive faster, and are more likely to change lanes.

## Lanes:
Each lane is composed of an ordered list of cars. Upon changing lanes, a binary search algorithm identifies the car with the current maximum x-coordinate, then uses binary search to find the appropriate insertion into the list of cars.

## Cars:
On update(), each car determines its distance to the next car in the lane. Acceleration is set based on that distance and its current velocity.

On update(), each car checks a random number to determine if it will try to change lanes. If that chance succeeds, then a lane change check is run in adjacent lanes to determine if there is sufficient space for a lane change. The lane change decision is flagged in the current lane, and the lane change methods are run after the lane completes all remaining update() methods. The car is then removed from its current lane and added to its new lane, inserted appropriately into the list of cars in that lane. The y-position is smoothly moved into the new lane using a cubic easing function.

## Todo:
-Add JUnit tests.
-Cars and lanes should be drawn on a JPanel, and a separate JPanel should hold buttons and other input mechanisms. Currently, cars and lanes are drawn directly on the JFrame.
-Click to select a car, pull the current velocity, acceleration, color, aggression level, lane number, and index in lane. Allow user to change the car's acceleration, color, and aggression level, or flag a lane change (to be completed when allowable by lane change checks). Potentially also allow changes to preferred velocity and preferred tailing distance.
-Improve acceleration formulas when car is tailing too closely and moving too quickly.
-Model different forms of driving behavior. Possibly replace aggression level with different modes of aggression (drivers with higher tendency to change lanes, tail closely, or accelerate quickly).
-Model reaction times. Should cars change acceleration immediately if a slower car changes lanes in front of them?
