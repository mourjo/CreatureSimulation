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

public class MadEnergyPointTest {
	
	/*
	 * Please note:
	 * This test is intended for the ALL FEATURES plugin of Mad creature
	 * Also, there is a probability of 3/50 that this test will fail (1/50 chance of fail for each act() call )
	 * That is because of the random nature of MadCreature
	 * This is why there are two test classes for MadCreature: To reduce the probability of failure
	 * */

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
	public void testMadEnergyBehavior() throws Exception {
		
		MadCreature creature = new MadCreature(environment, new Point2D.Double(1, 1), 6, toRadians(180), Color.RED);

		creature.act();
		assertEquals(Math.toRadians(0), creature.direction, 0);
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new MadCreature(environment, new Point2D.Double(1, 1), 6, toRadians(270), Color.RED);
		creature.act();
		assertEquals(Math.toRadians(90), creature.direction, 0);
		assertEquals(creature.speed, creature.speed, 0);
		
		creature = new MadCreature(environment, new Point2D.Double(100, 100), 6, toRadians(270), Color.RED);
		creature.act();
		assertEquals(creature.speed, creature.speed, 0);
		assertEquals(creature.direction, creature.direction, 0);

		
	}

}
