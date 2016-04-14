package edu.doane.dugal.dea;

/**
 * Interface representing an operation performed on a population of individuals
 * in the evolutionary algorithm. Samples include crossover, mutation,
 * selection, statistics reporting, and so on.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
public interface Operator {

    /**
     * Perform this operation on the population.
     *
     * @param population Array of Individual objects, representing the
     * population for the evolutionary algorithm.
     */
    public void operate(Individual[] population);
}
