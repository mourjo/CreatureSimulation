package plug.creatures;

import java.awt.geom.Point2D;

import plug.IPlugin;

public interface IBehavior extends IPlugin{
	public void act();
	public void move(double incX, double incY);
}
