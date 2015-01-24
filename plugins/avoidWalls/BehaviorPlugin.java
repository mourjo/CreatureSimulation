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
	
	//this version of the MadCreature avoids doing near walls
	
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
		avoidWall(70);
		
		double incX = param.speed * cos(param.direction);
		double incY = - param.speed * sin(param.direction);
		move(incX, incY);
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
