package edu.doane.dugal.dea;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class representing an individual in an evolutionary algorithm. Make
 * sure extending subclasses implement a deep-copy copy constructor, because
 * the static copy method of this class assumes one will be there.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
abstract public class Individual implements Comparable {

    /**
     * Fitness of this individual; higher is better.
     */
    private double fitness;

    /**
     * Default constructor.
     */
    public Individual() {
        setFitness(Double.NEGATIVE_INFINITY);
    }

    /**
     * Get this individual's fitness.
     *
     * @return the fitness
     */
    final public double getFitness() {
        return fitness;
    }

    /**
     * Set this individual's fitness.
     *
     * @param fitness the fitness to set
     */
    final public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Factory method to copy Individuals. Use reflection to find the copy 
     * constructor of the parameter Individual, invoke it, and return the result.
     * 
     * @param ind Individual to create a copy of
     * @return A new, deep-copy Individual, identical to the parameter
     */
    public static Individual copy(Individual ind) {
        // get specific type of Individual
        Class<?> cls = ind.getClass();
        
        // get constructors
        Constructor<?> [] cons = cls.getDeclaredConstructors();
        
        Constructor<?> copyCons = null;
        // find the copy constructor
        for(Constructor<?> c : cons) {
            Class<?> [] parms = c.getParameterTypes();
            
            // 1 parameter, of same class as ind? that's our copy 
            // constructor!
            if(parms.length == 1 && parms[0].equals(cls)) {
                copyCons = c;
                break;
            } 
        }
        
        // now use the copy constructor to return a new individual just
        // like the parameter Individual
        try {
            return (Individual)copyCons.newInstance(ind);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        // if the reflection didn't work, return a null to satisfy the compiler
        return null;
    }
    
    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
