package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plug.creatures.CreaturePluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import creatures.ICreature;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;
import creatures.visual.EnergyPointVisualizer;

/**
 * Just a simple test of the simulator.
 * 
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private final CreaturePluginFactory factory;
	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final EnergyPointVisualizer energyVisualizer;
	private final CreatureSimulator simulator;
	
	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();	
	private Constructor<? extends ICreature> currentConstructor = null;
	private int noOfCreatures = 15;
	private int noOfEnergyPts = 5;
	int influenceOfEnergyPts = 10;
	  
	public Launcher() {
		
		factory = CreaturePluginFactory.getInstance();
		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel();
		
		JSlider noOfEneryPtSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, noOfEnergyPts);
		noOfEneryPtSlider.setBorder(BorderFactory.createTitledBorder("Number of Energy Points: " + noOfEneryPtSlider.getValue()));
		noOfEneryPtSlider.setMajorTickSpacing(2);
		noOfEneryPtSlider.setPaintTicks(true);
		noOfEneryPtSlider.setPaintLabels(true);
		noOfEneryPtSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider)e.getSource();
				if(!slider.getValueIsAdjusting())
				{
					noOfEnergyPts = (int) slider.getValue();
					slider.setBorder(BorderFactory.createTitledBorder("Number of Energy Points: " + noOfEnergyPts));
					
				}
				
			}
		});
		buttons.add(noOfEneryPtSlider);
		
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.load();
				buildPluginMenus();
			}
		});
		buttons.add(loader);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				buildPluginMenus();
			}
		});
		buttons.add(reloader);

		JButton restart = new JButton("(Re-)start simulation");
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();
					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, noOfCreatures, currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.setEnergyPts(noOfEnergyPts, influenceOfEnergyPts);
					
					simulator.start();
				}
			}
		});
		buttons.add(restart);
		
		JSlider noOfCreaturesSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, noOfCreatures);
		noOfCreaturesSlider.setBorder(BorderFactory.createTitledBorder("Number of Creatures: " + noOfCreaturesSlider.getValue()));
		noOfCreaturesSlider.setMajorTickSpacing(10);
		noOfCreaturesSlider.setMinorTickSpacing(5);
		noOfCreaturesSlider.setPaintTicks(true);
		noOfCreaturesSlider.setPaintLabels(true);
		noOfCreaturesSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider)e.getSource();
				if(!slider.getValueIsAdjusting())
				{
					noOfCreatures = (int) slider.getValue();
					slider.setBorder(BorderFactory.createTitledBorder("Number of Creatures: " + noOfCreatures));
				}
			}
		});
		buttons.add(noOfCreaturesSlider);
		
		
		add(buttons, BorderLayout.SOUTH);
		
		
		simulator = new CreatureSimulator(new Dimension(1024, 430));		
		inspector = new CreatureInspector();
		inspector.setFocusableWindowState(false);
		
		visualizer = new CreatureVisualizer(simulator);
		visualizer.setDebug(false);
		visualizer.setPreferredSize(simulator.getSize());
		
		
		energyVisualizer = new EnergyPointVisualizer(simulator);	
		energyVisualizer.setOpaque(false);

		visualizer.add(energyVisualizer);
		
		add(visualizer, BorderLayout.CENTER);
		
		buildPluginMenus();

	    pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit(evt);
			}
		});
		
		
		
	}
	
	private void exit(WindowEvent evt) {
		System.exit(0);
	}

	public void buildPluginMenus() {	
		mb.removeAll();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				currentConstructor = factory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());
			}
		};
		menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();
		mb.add(menuBuilder.getMenu());
		setJMenuBar(mb);
	}
	
	
	public static void main(String args[]) {
	    Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}
	
}


