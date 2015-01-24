package creatures;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import creatures.visual.CreatureSimulator;

public class MadCreatureTest {
	/*
	 * Please note:
	 * This test is intended for the ALL FEATURES plugin of Mad creature
	 * Also, there is a probability of 3/50 that this test will fail (1/50 chance of fail for each act() call )
	 * That is because of the random nature of MadCreature
	 * This is why there are two test classes for MadCreature: To reduce the probability of failure
	 * */

	CreatureSimulator environment = mock(CreatureSimulator.class);
	final double w = 1000;
	final double h = 1000;
	
	@Before
	public void setup() {
		when(environment.getSize()).thenReturn(new Dimension((int)w, (int)h));
		when(environment.getEnergyPoints()).thenReturn(new ArrayList<EnergyPoint>());
	}
	
	@Test
	public void testAvoidingWalls() throws Exception {
		double angle = 150;
		int minDist = 70;
		MadCreature creature = new MadCreature(environment, new Point2D.Double(430, 0), 1, toRadians(angle), Color.RED);
		creature.act();		
		assertEquals(-toRadians(angle+180), creature.getDirection(), 0.01);
		assertEquals(w/2 - minDist, creature.getPosition().getX(), 1);
		assertEquals(creature.getPosition().getY(), creature.getPosition().getY(), .01);

		angle = 45;
		minDist = 70;
		creature = new MadCreature(environment, new Point2D.Double(0, 450), 1, toRadians(angle), Color.RED);
		creature.act();
		assertEquals(toRadians(360 - angle), creature.getDirection(), 0.01);
		assertEquals(h/2 - minDist, creature.getPosition().getY(), 1);
		assertEquals(creature.getPosition().getX(), creature.getPosition().getX(), .01);
		
		angle = 50;
		minDist = 70;
		creature = new MadCreature(environment, new Point2D.Double(430, 430), 1, toRadians(angle), Color.RED);
		creature.act();
		assertEquals(toRadians(270 - angle), creature.getDirection(), 1);
		assertEquals(h/2 - minDist, creature.getPosition().getY(), 1);
		assertEquals(w/2 - minDist, creature.getPosition().getX(), 1);
    }	

}
