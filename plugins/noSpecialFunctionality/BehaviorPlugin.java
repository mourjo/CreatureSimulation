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
	
	//this version has no special function. This version of the MadCreature does not interact with energy point (can't sense them)
	//basically it is like the StupidCreature
	
	
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
