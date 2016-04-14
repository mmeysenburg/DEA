package edu.doane.dugal.dea.kits.bchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Class implementing single-point mutation for BinaryChromosome individuals.
 * The operator iterates through the population. Each individual is mutated with
 * probability mu. If the individual is mutated, a random bit in its chromosome
 * is flipped.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class PointMutation implements Operator {

    /**
     * Random number generator used by this operator.
     */
    private PRNG prng;

    /**
     * Probability than an individual will undergo mutation. Default value is
     * 0.01.
     */
    private double mu;

    /**
     * Default constructor. Create a PointMutation object with probability of
     * mutating an individual set to 0.01.
     */
    public PointMutation() {
        prng = PRNG.getInstance();
        setMu(0.01);
    }

    /**
     * Initializing constructor. Create a PointMutation object with the
     * specified probability of mutating an individual.
     *
     * @param mu Probability for an individual to undergo mutation, in [0, 1].
     */
    public PointMutation(double mu) {
        prng = PRNG.getInstance();
        setMu(mu);
    }

    /**
     * Perform single point mutation on the population. Iterate through each
     * individual; with probability mu, mutate each one. If mutating, flip one
     * randomly selected bit in the individual.
     *
     * @param population Array of BinaryChromosome individuals to mutate.
     */
    @Override
    public void operate(Individual[] population) {
        for (Individual ind : population) {
            // do a mutation?
            if (prng.nextDouble() <= mu) {
                // if so, flip one of the current individual's bits
                ((BinaryChromosome) ind).chromosome.flip(prng.nextInt(0, ((BinaryChromosome) ind).getLength() - 1));
            }
        }
    }

    /**
     * Get the probability of mutation.
     *
     * @return mu, the probability an individual will undergo mutation.
     */
    public double getMu() {
        return mu;
    }

    /**
     * Set the probability of mutation.
     *
     * @param mu the probability to set, in [0, 1]
     * @throws IllegalArgumentException if mu is not in the correct range.
     */
    final public void setMu(double mu) throws IllegalArgumentException {
        if (mu < 0 || mu > 1) {
            throw new IllegalArgumentException("Bad value to setMu: " + mu);
        }
        this.mu = mu;
    }

    @Override
    public String toString() {
        return "BinaryChromosome PointMutation, mu = " + mu;
    }
}
