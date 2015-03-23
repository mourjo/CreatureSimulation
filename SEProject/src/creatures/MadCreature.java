package creatures;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import plug.IPlugin;
import plug.PluginLoader;
import plug.creatures.IBehavior;

public class MadCreature extends AbstractCreature {
	IBehavior behavior;	//strategy design pattern - this is the interface to access the available algorithm (plugin)
	ParameterWrapper param;

	@SuppressWarnings("unchecked")
	public MadCreature(IEnvironment environment, Point2D position, double sp, double dir, Color col) {
		super(environment, position);

		speed = sp;
		direction = dir;
		color = col;
		
		param = new ParameterWrapper(environment, position, dir, sp, col, MIN_SPEED, MAX_SPEED);
		
		try
		{
			PluginLoader PL = new PluginLoader("myplugins/repository/", IBehavior.class);
			PL.loadPlugins();
			List<Class<IPlugin>> x = PL.getPluginClasses();
			for (@SuppressWarnings("rawtypes") Class y : x)
			{
				if(y.getName().equals("creatures.BehaviorPlugin")) //just to put a check if there are other plugins implementing the IBehavior interface (not required by default)
				{
					@SuppressWarnings("rawtypes")
					Class[] argTypes = {ParameterWrapper.class};
					behavior = (IBehavior) y.getDeclaredConstructor(argTypes).newInstance(param);
				}	
				
			}
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		
		
	}
	
	@Override
	public void act() {
		param.position = position;
		param.direction = direction % (2 * Math.PI);
		
		behavior.act();
		
		setPosition(param.position);
		setDirection(param.direction  % (2 * Math.PI));
		color = param.color;
	}

}
