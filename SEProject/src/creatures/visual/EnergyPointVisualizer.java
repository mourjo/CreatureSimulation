package creatures.visual;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Iterator;
import creatures.EnergyPoint;
import creatures.ICreature;
import simulator.ISimulationListener;
import visual.Visualizer;

@SuppressWarnings("serial")
public class EnergyPointVisualizer extends Visualizer {

	private final CreatureSimulator simulator;
	private CreatureInspector inspector;
	private boolean debug;
	
	public EnergyPointVisualizer (CreatureSimulator simulator) {
		this.simulator = simulator;
		setPreferredSize(simulator.getSize());
	
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				handleResize();
			}
		});

		simulator.addSimulationListener(new ISimulationListener() {
			@Override
			public void simulationCycleComputed() {
				repaint();
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleMousePressed(e);
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
				handleMouseMoved(e);
			}
		});
	
	}
	
	protected void handleResize() {
		
		synchronized (simulator) {
			simulator.setSize(getSize());
		};		
		setPreferredSize(simulator.getSize());
		
	}

	@Override
	protected void paint(Graphics2D g2) {
		if (debug) {
			paintDebuggingFrame(g2);
		}

		super.paint(g2);
	}
	
	protected void handleMouseMoved(MouseEvent event) {
		synchronized (simulator) {
			if (simulator.isRunning()) {
				return;
			}

			if (inspector == null) {
				inspector = new CreatureInspector();
			}

			Point2D point;
			try {
				point = getTransform().inverseTransform(event.getPoint(), null);
			} catch (NoninvertibleTransformException e) {
				throw new RuntimeException(e);
			}
			
			point = new Point2D.Double(point.getX(), point.getY() * -1);
			Iterator<ICreature> nearBy = simulator.creaturesNearByAPoint(point, 10.0).iterator();
			
			if (!nearBy.hasNext()) {
				inspector.setCreature(null);
				return;
			}

			if (!inspector.isVisible()) {
				inspector.setVisible(true);
			}

			inspector.setCreature(nearBy.next());
		}
	}

	protected void handleMousePressed(MouseEvent e) {
		synchronized (simulator) {
			if (simulator.isRunning()) {
				simulator.stop();
			} else {
				simulator.start();
			}
		}
	}

	
	@Override
	protected Iterable<EnergyPoint> getDrawables() {
		return simulator.getEnergyPoints();
	}
	
	protected void paintDebuggingFrame(Graphics2D g2) {
		// save color
		Color oldColor = g2.getColor();
		g2.setColor(Color.BLACK);

		// draw coordinates
		g2.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0);
		g2.drawLine(0, -getHeight() / 2, 0, getHeight() / 2);

		// restore color
		g2.setColor(oldColor);
	}

	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		repaint();
	}
}
