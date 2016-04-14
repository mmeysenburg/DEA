package edu.doane.dugal.dea.kits.dchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Class to perform point mutation on a population of DoubleChromosome
 * individuals. The operator iterates through the population. Each individual is
 * mutated with probability mu. If the individual is mutated, a random gene in
 * it's chromosome is changed to a new number between the individual's lo and hi
 * values.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
public class PointMutation implements Operator {

    /**
     * Change that an individual will undergo mutation, in the range [0, 1]
     */
    private double mu;

    /**
     * Random number generator used by the operator.
     */
    final private PRNG prng;

    /**
     * Default constructor; create a mutation operator with mu = 0.01.
     */
    public PointMutation() {
        mu = 0.01;
        prng = PRNG.getInstance();
    }

    /**
     * Initializing constructor; create a mutation operator with mu set by the
     * parameter.
     *
     * @param mu Chance to mutate an individual, in [0, 1].
     */
    public PointMutation(double mu) {
        setMu(mu);
        prng = PRNG.getInstance();
    }

    /**
     * Perform the point mutation operation on the specified population.
     *
     * @param population Array of DoubleChromosome individuals
     */
    @Override
    public void operate(Individual[] population) {
        for (Individual ind : population) {
            if (prng.nextDouble() <= getMu()) {
                int i = prng.nextInt(0, ((DoubleChromosome) ind).getLength() - 1);
                ((DoubleChromosome) ind).randomizeGene(i);
            } // if mutating
        } // for each individual
    }

    /**
     * Get the probability of mutation for this operator.
     *
     * @return the mu
     */
    public double getMu() {
        return mu;
    }

    /**
     * Set the probability of mutation for this operator.
     *
     * @param mu Chance to mutate an individual, in [0, 1].
     * @throws IllegalArgumentException if the parameter isn't in the allowed
     * range.
     */
    final public void setMu(double mu) throws IllegalArgumentException {
        if (mu < 0.0 || mu > 1.0) {
            throw new IllegalArgumentException("Illegal mu to setMu: " + mu);
        }
        this.mu = mu;
    }

    @Override
    public String toString() {
        return "DoubleChromosome PointMutation, mu = " + mu;
    }

}
