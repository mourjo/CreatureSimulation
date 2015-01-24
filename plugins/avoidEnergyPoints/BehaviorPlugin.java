package creatures;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Random;

import plug.IPlugin;
import plug.creatures.IBehavior;

public class BehaviorPlugin implements IBehavior, IPlugin { 
	ParameterWrapper param;
	Random rand;
	int cycles = 21;
	
	//this version of the MadCreature avoids going near energy points, and changes direction with a probability of 1/50
	
	
	public BehaviorPlugin(ParameterWrapper param) 
	{	
		this.param = param;
		rand = new Random();
		param.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		if(param.speed <= param.minSpeed)
			param.speed = param.minSpeed;
	
	}

	@Override
	public void act() 
	{		
		if(cycles > 20)
		{
			avoidEnergyPoint();			
		}
		
		if(cycles <= 20) cycles++;
		
		double incX = param.speed * cos(param.direction);
		double incY = - param.speed * sin(param.direction);
		move(incX, incY);
		
		if(rand.nextInt() % 50 == 0)	//approx probability = 1/50
			changeDirection();
		

	}
	
	void changeDirection()
	{
		param.direction = (rand.nextDouble() * 2 * Math.PI) % (2 * Math.PI);
	}
	
	void avoidEnergyPoint()
	{
		for(EnergyPoint x : param.environment.getEnergyPoints())
		{
			if(distFrom(x.getPoint()) < x.getEventHorizon()) 
			{
				param.direction = param.direction + Math.PI;
				cycles = 0;
			}
		}
	}
	
	double distFrom(Point2D pt)
	{
		return Math.sqrt(Math.pow((param.position.getY() - pt.getY()), 2) + Math.pow((param.position.getX() - pt.getX()), 2));
	}
	
	@Override
	public void move(double incX, double incY) {
		param.position = new Point2D.Double(param.position.getX() + incX, param.position.getY() + incY);

	}

	@Override
	public String getName() {
		return "BehaviorPlugin";
	}

}
