package creatures;

import static commons.Utils.filter;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import commons.Utils.Predicate;


/**
 * Smart creature that implements following behavior:
 * 
 * Looking at the nearby creatures that are within FOV and a certain distance
 * defined in the environment, it
 * <ul>
 * <li>tries to align its speed with the speed of the creatures around.
 * <li>goes in the same direction as the creatures around.
 * <li>maintains some minimal distance from the creatures around.
 * </ul>
 * 
 * Additionally to that, it tries to maintain some minimum speed so the
 * creatures always moves.
 * 
 */
public class SmartCreature extends AbstractCreature {
	
	static class CreaturesAroundCreature implements Predicate<ICreature> {
		private final SmartCreature observer;

		public CreaturesAroundCreature(SmartCreature observer) {
			this.observer = observer;
		}

		@Override
		public boolean apply(ICreature input) {
			if (input == observer) {
				return false;
			}
			double dirAngle = input.directionFormAPoint(observer.getPosition(),
					observer.getDirection());

			return abs(dirAngle) < (observer.getFieldOfView() / 2)
					&& observer.distanceFromAPoint(input.getPosition()) <= observer
							.getLengthOfView();

		}
	}

	/** Minimum distance between this creature and the ones around. */
	private final static double MIN_DIST = 10d;


	public SmartCreature(IEnvironment environment, Point2D position, double speed, double direction,
			Color color) {
		super(environment, position);
		this.direction = direction;
		this.speed = speed;
		this.color = color;
	}
	
	

	public void act() {
		
		if(closeToEnergyPt() && cycleCountForIncreaseEnergy >= 10)
		{
			cycleCountForIncreaseEnergy = 1;
			increaseEnergy();
			
		}
		// speed - will be used to compute the average speed of the nearby
		// creatures including this instance
		double avgSpeed = speed;
		// direction - will be used to compute the average direction of the
		// nearby creatures including this instance
		double avgDir = direction;
		// distance - used to find the closest nearby creature
		double minDist = Double.MAX_VALUE;
		
		// iterate over all nearby creatures
		Iterable<ICreature> creatures = creaturesAround(this);
		int count = 0;
		for (ICreature c : creatures) {
			avgSpeed += c.getSpeed();
			avgDir += c.getDirection();
			minDist = Math.min(minDist, c.distanceFromAPoint(getPosition()));
			count++;
		}
		
		// average
		avgSpeed = avgSpeed / (count + 1);
		// min speed check
		if (avgSpeed < MIN_SPEED) {
			avgSpeed = MIN_SPEED;
		}
		
		// min speed check
		if (avgSpeed > MAX_SPEED) {
			avgSpeed = MAX_SPEED;
		}
		// average
		avgDir = avgDir / (count + 1);
		
		// apply - change this creature state
		
		this.speed = avgSpeed;
		
		int noOfEnergyPts = count(environment.getEnergyPoints());
		
		if ( noOfEnergyPts != 0 && energy > 50 && countGroupMembers() > 0 )	//energy not too less and in a group
			this.direction = avgDir;
		else if ( noOfEnergyPts != 0 && senseEnergyPoint()) // energy point in sight + low energy
			this.direction = getEnergyPointDirection();
		else
			this.direction = avgDir;
		
		if(cycleCountForIncreaseEnergy < 10)
			cycleCountForIncreaseEnergy++;
		
		cycleCountForDecreaseEnergy++;
		cycleCountForDecreaseEnergy = cycleCountForDecreaseEnergy % cyclesBeforeDecrease; 
		
		if(cycleCountForDecreaseEnergy % cyclesBeforeDecrease == 0)
		{
			decreaseEnergy();
			cycleCountForDecreaseEnergy = 1;
		}
		// if we are not too close move closer
		if (minDist > MIN_DIST) {
			// we move always the maximum
			double incX = speed * Math.cos(avgDir);
			double incY = - speed * Math.sin(avgDir);

			// we should not moved closer than a dist - MIN_DIST
			move(incX, incY);
		}		
	}
	
	public int count(Iterable<EnergyPoint> pts)
	{
		int i = 0;
		Iterator<EnergyPoint> npts = pts.iterator();
		while(npts.hasNext())
		{
			i++;
			npts.next();
		}
		return i;
	}
	
	public Iterable<ICreature> creaturesAround(SmartCreature smartCreature) 
	{
		return filter(environment.getCreatures(), new CreaturesAroundCreature(this));
	}
	
	public int countGroupMembers()
	{
		Iterable<ICreature> groupCreatures = creaturesAround(this);
		Iterator<ICreature> it = groupCreatures.iterator();
		int groupCount = 0;
		while(it.hasNext())
		{
			it.next();
			groupCount++;
		}
		return groupCount;
	}
	
	public void decreaseEnergy()
	{		
		int groupCount = countGroupMembers();	
		
		//int oldEnergy = energy;
		
		if (groupCount > 5)
			changeEnergyLevel(Math.min(0, energyDecreaseParam + 3));	//no decrease in energy
		else if (groupCount > 3)
			changeEnergyLevel(Math.min(0, energyDecreaseParam + 2));	//a little decrease in energy
		else if (groupCount > 1)
			changeEnergyLevel(Math.min(0, energyDecreaseParam + 1));	//a bit more decrease in energy
		else
			changeEnergyLevel(energyDecreaseParam);						//not a group -> max energy decrease
		
		/*if(oldEnergy - energy == 0 && oldEnergy != 0)
			System.out.println(groupCount);*/	
	}
	
}
