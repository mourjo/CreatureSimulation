package creatures;

import static java.lang.Math.toDegrees;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import visual.IDrawable;

public class EnergyPoint implements IDrawable {
	protected Point2D.Double energyPt;
	protected int influence;
	protected int eventHorizon = 65;
	
	public EnergyPoint(int x, int y, int i) {
		influence = i;
		energyPt =  new Point2D.Double(x, y);
	}
	
	public EnergyPoint(int x, int y) {
		influence = 20;
		energyPt =  new Point2D.Double(x, y);
	}
	
	public int getEventHorizon()
	{
		return eventHorizon;
	}
	
	public int getInfluence()
	{
		return influence;
	}
	
	public void setInfluence(int i)
	{
		influence = i;
	}
	
	public Point2D.Double getPoint()
	{
		return energyPt;
	}

	@Override
	public Color getColor() {
		return Color.black;
	}

	@Override
	public int getSize() {
		return influence;
	}

	@Override
	public void paint(Graphics2D g2) {
		
		g2.setColor(getColor());
		
		double x = (int)energyPt.getX() - influence;
		double y = (int)energyPt.getY() - influence;
		g2.fillOval((int)x, (int) y, 2 * influence, 2 * influence);
		
		g2.setColor(new Color(198,198,198));
		x = (int)energyPt.getX() - eventHorizon;
		y = (int)energyPt.getY() - eventHorizon;
		g2.drawOval((int)x, (int) y, 2 * eventHorizon, 2 * eventHorizon);
		
	}
}
