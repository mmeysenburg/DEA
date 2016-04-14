package edu.doane.dugal.dea.kits.ichrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Single-point mutation operator for integer chromosomes. The operator iterates
 * through the population. Each individual is mutated with probability mu. If
 * the individual is mutated, a random gene in it's chromosome is changed to a
 * new number between the individual's lo and hi values.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class PointMutation implements Operator {

    /**
     * Probability that an individual is mutated. Default value is 0.01.
     */
    private double mu;

    /**
     * Random number generator used by the operator.
     */
    private final PRNG prng;

    /**
     * Default constructor. Create a point mutation operator with probability of
     * mutation set to 0.01.
     */
    public PointMutation() {
        setMu(0.01);
        prng = PRNG.getInstance();
    }

    /**
     * Perform the point mutation operation on the specified population.
     *
     * @param population Array of IntegerChromosome individuals
     */
    @Override
    public void operate(Individual[] population) {
        for (Individual ind : population) {
            if (prng.nextDouble() <= getMu()) {
                int i = prng.nextInt(0, ((IntegerChromosome) ind).chromosome.length - 1);
                ((IntegerChromosome) ind).randomizeGene(i);
            } // if mutating
        } // for each individual
    }

    /**
     * Get the probability of mutation for this operator.
     *
     * @return mu, the probability of mutation.
     */
    public double getMu() {
        return mu;
    }

    /**
     * Set the probability of mutation for this operator.
     *
     * @param mu the probability of mutation, in [0, 1].
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
        return "IntegerChromosome PointMutation, mu = " + mu;
    }

}
