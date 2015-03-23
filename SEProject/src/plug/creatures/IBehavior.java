package plug.creatures;

import plug.IPlugin;

public interface IBehavior extends IPlugin{
	public void act();
	public void move(double incX, double incY);
}
