package sim; 

import java.util.Random;

/**
 * A class to de-couple animal types from the Simulator.
 *
 * @author E Brown
 * @version Nov 2022
 */
public abstract class AnimalCollector
{
    /**
     * number of Animal Types for the simulator
     */
    
    private static Random rand = Randomizer.getRandom();
    
    // Types of Animals
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;   
 
    
    public static Animal randAnimal(Field field)
    {
            if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                return new Fox(true, field, null);
            }
            else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                return new Rabbit(true, field, null);
            }
            return null;
    }
}
