package creatures.visual;

import static commons.Utils.filter;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import simulator.Simulator;
import commons.Utils.Predicate;
import creatures.EnergyPoint;
import creatures.ICreature;
import creatures.IEnvironment;


/**
 * Environment for the creatures together with the visualization facility.
 */
public class CreatureSimulator extends Simulator<ICreature> implements IEnvironment {

	static class CreaturesNearbyPoint implements Predicate<ICreature> {
		private final Point2D point;
		private final double margin;

		public CreaturesNearbyPoint(Point2D point, double margin) {
			this.point = point;
			this.margin = margin;
		}

		@Override
		public boolean apply(ICreature input) {
			return input.distanceFromAPoint(point) <= margin;
		}
	}

	private Dimension size;
	
	protected List<EnergyPoint> energyPts;
	
	public Iterable<EnergyPoint> getEnergyPoints()
	{
		return energyPts;
	}


	public CreatureSimulator(Dimension initialSize) 
	{
		super(new CopyOnWriteArrayList<ICreature>(), 10);
		energyPts = new ArrayList<EnergyPoint>();
		
		this.size = initialSize;
	}
	
	private boolean distributionCheck(int x, int y)
	{
		for (EnergyPoint aPt : energyPts)
		{
			if(Math.sqrt(Math.pow((aPt.getPoint().getY() - (double)y), 2) + Math.pow((aPt.getPoint().getX() - (double)x), 2)) <= (2 * aPt.getEventHorizon() + 20d))
				return false;
		}
		return true;
	}
	
	/**
	 * @return a copy of current size
	 */
	public synchronized Dimension getSize() {
		return new Dimension(size);
	}
	
	public synchronized void setSize(Dimension size) {
		this.size = size;
	}
	
	/**
	 * @return a copy of the current creature list.
	 */
	@Override
	public Iterable<ICreature> getCreatures() {
		return new ArrayList<ICreature>(actionables);
	}
	
	public int creatureSize() {
		return actionables.size();
	}
	
	public void addCreature(ICreature creature) {
		actionables.add(creature);
	}
	
	public void removeCreature(ICreature creature) {
		actionables.remove(creature);
	}
	
	public Iterable<ICreature> creaturesNearByAPoint(Point2D point,  double radius) {
		return filter(actionables, new CreaturesNearbyPoint(point, radius));
	}

	public void addAllCreatures(Collection<? extends ICreature> creatures) {
		actionables.addAll(creatures);
	}
	
	public void clearCreatures() {
		actionables.clear();
	}
	
	public void setEnergyPts(int noOfEnergyPts, int influenceOfEnergyPts)
	{
		energyPts = new ArrayList<EnergyPoint>();
		Random generator = new Random();
		int i = 0;
		
		while (i < noOfEnergyPts)
		{
			
			int xSign = (int) Math.pow(-1, (2 - generator.nextInt(2)));
			int x1 = xSign * ((size.width/2) - generator.nextInt(size.width/2));
			
			int ySign = (int) Math.pow(-1, (2 - generator.nextInt(2)));
			int y1 = ySign * ((size.height/2) - generator.nextInt(size.height/2));
			
			if( distributionCheck(x1, y1) )
			{
				energyPts.add(new EnergyPoint(x1, y1, influenceOfEnergyPts));
				i++;
			}
			
		}
	}

}
