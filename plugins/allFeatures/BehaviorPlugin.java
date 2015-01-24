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
	
	/*
	 * this version has all the features:
	 * random speed change
	 * random direction change
	 * avoiding walls
	 * avoiding energy points
	 * changing color randomly
	 * 
	 * this is the full feature version of MadCreature 
	 * (it is named so because of all of its random features)
	 * */ 
	
	
	public BehaviorPlugin(ParameterWrapper param) 
	{	
		this.param = param;
		rand = new Random();
		param.color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		if(param.speed <= 0.0)
			param.speed = param.minSpeed;
	
	}

	@Override
	public void act() 
	{		
		avoidWall(70);
		
		if(cycles > 20)
		{
			avoidEnergyPoint();			
		}
		
		if(cycles <= 20) cycles++;
		
		double incX = param.speed * cos(param.direction);
		double incY = - param.speed * sin(param.direction);
		move(incX, incY);
		
		if(rand.nextInt() % 50 == 0)	//approx probability = 1/50
			changeSpeed();
		
		if(rand.nextInt() % 50 == 0)	//approx probability = 1/50
			changeDirection();
		
		if(rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean())
		param.color = new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200));

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
	
	void changeSpeed()
	{
		param.speed = rand.nextFloat() * 10;
		if (param.speed < param.minSpeed) {
			param.speed = param.minSpeed;
		} else if (param.speed > param.maxSpeed) {
			param.speed = param.maxSpeed;
		}
	}
	
	
	void avoidWall(int minDist)
	{
		if (Math.abs(Math.abs(param.position.getX()) - (param.environment.getSize().getWidth()/2)) <= minDist)
		{
			if(param.position.getX() > 0)
			{
				param.position.setLocation((param.environment.getSize().getWidth()/2) - minDist, param.position.getY());
				param.direction = -PI - param.direction;
			}
			else
			{
				param.position.setLocation(-(param.environment.getSize().getWidth()/2) + minDist, param.position.getY());
				param.direction = -PI - param.direction;
			}
		}
		
		
		if (Math.abs(Math.abs(param.position.getY()) - (param.environment.getSize().getHeight()/2)) <= minDist)
		{
			if(param.position.getY() > 0)
			{
				param.position.setLocation(param.position.getX(), (param.environment.getSize().getHeight()/2) - minDist);
				param.direction = 2 * Math.PI - param.direction;
			}
			else
			{
				param.position.setLocation(param.position.getX(), -(param.environment.getSize().getHeight()/2) + minDist);
				param.direction = PI * 2 - param.direction;
			}
		}
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
