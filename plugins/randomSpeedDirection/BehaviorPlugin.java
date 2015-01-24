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
	
	//this version allows the MadCreature to change its speed and direction each with a probability of 1/50
	//this MadCreature does not interact with Energy Points
	
	
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
		double incX = param.speed * cos(param.direction);
		double incY = - param.speed * sin(param.direction);
		move(incX, incY);
		
		if(rand.nextInt() % 50 == 0)	//approx probability = 1/50
			changeSpeed();
		
		if(rand.nextInt() % 50 == 0)	//approx probability = 1/50
			changeDirection();

	}
	
	void changeDirection()
	{
		param.direction = (rand.nextDouble() * 2 * Math.PI) % (2 * Math.PI);
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

	@Override
	public void move(double incX, double incY) {
		param.position = new Point2D.Double(param.position.getX() + incX, param.position.getY() + incY);

	}

	@Override
	public String getName() {
		return "BehaviorPlugin";
	}

}
