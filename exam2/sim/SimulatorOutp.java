package sim;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location 
 * representing its contents. It uses a default background color.
 * Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class SimulatorOutp extends JFrame
{
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    
    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    
    static Color[] pickColors = { Color.blue, Color.red, Color.green };
    static int used_colors = 0;
    
    // A statistics object computing and storing simulation information
    
    private Simulator sim;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public SimulatorOutp(Simulator sim)
    {
        colors = new LinkedHashMap<>();
        this.sim = sim;

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        
        setLocation(100, 50);
        
        fieldView = new FieldView(sim.getField().getDepth(), 
                                    sim.getField().getWidth());

        Container contents = getContentPane();

        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        setResizable(false);
    }
    
    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            if(used_colors >= pickColors.length) used_colors = 0;
            if( used_colors < pickColors.length ){
                colors.put(animalClass, pickColors[used_colors++]);
                return getColor(animalClass);
            }
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(String message)
    {
        int step = sim.getStep();
        Field field = sim.getField();
        
        if(!isVisible()) {
            setVisible(true);
        }
            
        stepLabel.setText(STEP_PREFIX + step);


        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {

                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                }
                else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
      

        population.setText(POPULATION_PREFIX + message);
        fieldView.doText();
    }
    
    /**
     * Provide a graphical view of a rectangular field. This is 
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this 
     * for your project if you like.
     */
    private class FieldView extends JTextArea
    {
        

        private int gridWidth, gridHeight;
        
        char[] fieldSymbols;
        
        /**
         * Create a new FieldView component.
         * Rows=height, Cols = Width
         */
        public FieldView(int height, int width)
        {
            super(height, width+1);
            gridHeight = height;
            gridWidth = width;
            // size = new Dimension(0, 0);
            fieldSymbols = new char[height*width];
            this.setLineWrap(true);
            this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 6));
            this.setPreferredSize(new Dimension(height,width+1));
            this.setMaximumSize(new Dimension(height,width+1));
            
            
        }

       private char charForColor(Color color)
       {
            if( color == Color.RED ) return '+';
            if( color == Color.BLUE ) return '*';
            if( color == Color.GREEN ) return 'x';
            return ' ';
       }
        
        /**
         * Paint on grid location on this field in a given color. Substitute a character for a color.
         */
        public void drawMark(int x, int y, Color color)
        {
                fieldSymbols[y*gridWidth + x] = charForColor(color);
        }
        
        public void doText(){
            
            this.setText(String.valueOf(fieldSymbols));
        }
    }
}
