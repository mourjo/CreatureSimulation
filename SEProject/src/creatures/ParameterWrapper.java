package creatures;

import java.awt.Color;
import java.awt.geom.Point2D;

public class ParameterWrapper {
	
	public IEnvironment environment;
	public Point2D position;
	public double direction;
	public double speed;
	public Color color;
	public double maxSpeed;
	public double minSpeed;
	public ParameterWrapper(IEnvironment environment, Point2D position, double dir, double sp, Color col, double MinSpeed, double MaxSpeed)
	{
		this.environment = environment;
		this.position = position;
		this.direction = dir;
		this.speed = sp;
		this.color = col;
		this.minSpeed = MinSpeed;
		this.maxSpeed = MaxSpeed;
		
	}/*
	public IEnvironment getEnv()
	{
		return environment;
	}
	public Point2D getPosition()
	{
		return position;
	}
	public double getDirection()
	{
		return direction;
	}
	public double getSpeed()
	{
		return speed;
	}
	public Color getColor()
	{
		return color;
	}
	
	public void setPosition(double x, double y)
	{
		position = new Point2D.Double(x,y);
	}
	public void setDirection(double d)
	{
		direction = d;
	}
	public void setSpeed(double s)
	{
		speed = s;
	}
	public void setColor(Color c)
	{
		color = c;
	}*/
	


}
