package sim;

import java.util.Random; 
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.JFrame;
import java.io.IOException;
import java.nio.file.*;
import java.io.BufferedWriter;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 * @version Mods 2022 E Brown
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;


    // List of animals in the field.
    private List<Animal> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    
    // Update views of the simulation.
    boolean viewOn = true;
    boolean outpOn = false;
 
    private SimulatorView view = null;
    private SimulatorOutp outp = null;

    private FieldStats stats;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        createViews();
        
        // Setup a valid starting point.
        reset();
    }
    
    /** The following methods help decouple views from the simulator and stats methods.
     * If you want to alter the views, turn them on or off in these methods.
     *
     * Create the stats object and views 
     */
    private void createViews(){
        // Create a view of the state of each location in the field.
        stats = new FieldStats();
        if(viewOn) view = new SimulatorView(this);
        if(outpOn) outp = new SimulatorOutp(this);
    }
    
    /** 
     * update the views
     */
    private void updateViews(){ 
        stats.reset();
        if(view != null) view.showStatus(stats.getPopulationDetails(field));
        if(outp != null) outp.showStatus(stats.getPopulationDetails(field));
    }
    
    /**
     * delete the views
     */
    public void endSimulation(){
        if(view != null) view.setVisible(false);
        if(outp != null) outp.setVisible(false);
        if(view != null) view.dispose();
        if(outp != null) outp.dispose();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && stats.isViable(field); step++) {
            simulateOneStep();
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);

        updateViews();
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        Randomizer.reset();
        populate();
        
        // Show the starting state in the view.
        updateViews();

    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Animal a = AnimalCollector.randAnimal(field);
                if( a != null ){
                    Location location = new Location(row, col);
                    field.place(a, row,col);
                    a.setLocation(location);
                    animals.add(a);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Accessor for private field.
     */
    public Field getField(){
        return field;
    }
    
    /**
     * Accessor for private step.
     */
    public int getStep(){
        return step;
    }
    
    /**
     * Accessor for field stats details.
     */
    public String getDetails() {
        return stats.getPopulationDetails(field);
    }
    
    public boolean log(String fname){
        return false;
    }

}
