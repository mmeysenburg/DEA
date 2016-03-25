package edu.doane.dugal.dea;

/**
 * Abstract class representing an individual in an evolutionary algorithm. Make
 * sure extending subclasses implement the copy method, and that it produces
 * a deep copy of the individual.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
abstract public class Individual {

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
     * Create a deep copy copy of the Individual and return it.
     * 
     * @return A deep copy of this Individual, as a new Individual object.
     */
    abstract public Individual copy();
}
