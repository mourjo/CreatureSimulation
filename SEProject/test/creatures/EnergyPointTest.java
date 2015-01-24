package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import creatures.visual.CreatureSimulator;

public class EnergyPointTest {

	CreatureSimulator environment = mock(CreatureSimulator.class);
	List<EnergyPoint> energyPts = new ArrayList<EnergyPoint>();
	final double w = 400;
	final double h = 400;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		energyPts.add(new EnergyPoint(0, 0, 20));
		when(environment.getEnergyPoints()).thenReturn(energyPts);
		
	}
	
	@Test
	public void testSmartEnergyBehavior() throws Exception {
		SmartCreature creature = new SmartCreature(environment, new Point2D.Double(0, 1), 6.0, toRadians(0), Color.RED);
		ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		when(environment.getCreatures()).thenReturn(creaturesAround);
		
		creature.act();
		assertEquals(toRadians(90), creature.getDirection(), .01);
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new SmartCreature(environment, new Point2D.Double(1, 0), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(-180), creature.getDirection(), .01); //-ve sign because of inversion of Y axis in the canvas
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new SmartCreature(environment, new Point2D.Double(1, 1), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(-225.0), creature.getDirection(), .01); //-ve sign because of inversion of Y axis in the canvas
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new SmartCreature(environment, new Point2D.Double(0, 0), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(0), creature.getDirection(), .01); 
		assertEquals(creature.speed, creature.speed, 0);
		
	}
	
	@Test
	public void testBouncingEnergyBehavior() throws Exception {
		BouncingCreature creature = new BouncingCreature(environment, new Point2D.Double(0, 1), 6.0, toRadians(0), Color.RED);
		//ArrayList<ICreature> creaturesAround = new ArrayList<ICreature>();
		//when(environment.getCreatures()).thenReturn(creaturesAround);
		
		creature.act();
		assertEquals(toRadians(90), creature.getDirection(), .01);
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new BouncingCreature(environment, new Point2D.Double(1, 0), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(-180), creature.getDirection(), .01); //-ve sign because of inversion of Y axis in the canvas
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new BouncingCreature(environment, new Point2D.Double(1, 1), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(-225.0), creature.getDirection(), .01); //-ve sign because of inversion of Y axis in the canvas
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new BouncingCreature(environment, new Point2D.Double(0, 0), 8.0, toRadians(0), Color.RED);
		creature.act();
		assertEquals(toRadians(0), creature.getDirection(), .01); 
		assertEquals(creature.speed, creature.speed, 0);
		
	}
	
	
	
}
