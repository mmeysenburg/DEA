package edu.doane.dugal.dea.kits.bchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;
import edu.doane.dugal.dea.Operator;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * Class to perform single-point crossover on BinaryChromosome individuals.
 * Iterate through a population. With probability chi, an individual undergoes
 * crossover. When this happens, select a random mate, select a random crossover
 * point, and exchange bits between the individuals after that point.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class PointCrossover implements Operator {

    /**
     * Random number generator used by this operator.
     */
    private PRNG prng;

    /**
     * Probability an individual undergoes crossover. Default value is 0.6.
     */
    private double chi;

    /**
     * Default constructor. Create a PointCrossover operator with probability of
     * crossover 0.6.
     */
    public PointCrossover() {
        prng = PRNG.getInstance();
        setChi(0.6);
    }

    /**
     * Initializing constructor. Create a PointCrossover operator with the
     * specified probability of crossover.
     *
     * @param chi Probability that an individual undergoes crossover.
     */
    public PointCrossover(double chi) {
        prng = PRNG.getInstance();
        setChi(chi);
    }

    /**
     * Get the probability that an individual undergoes crossover.
     *
     * @return chi, the probability that an individual undergoes crossover.
     */
    public double getChi() {
        return chi;
    }

    /**
     * Set the probability that an individual undergoes crossover.
     *
     * @param chi The probability that an individual undergoes crossover, in [0,
     * 1].
     * @throws IllegalArgumentException If chi is not in [0, 1].
     */
    final public void setChi(double chi) throws IllegalArgumentException {
        if (chi < 0 || chi > 1) {
            throw new IllegalArgumentException("Invalid chi to setChi: " + chi);
        }

        this.chi = chi;
    }

    /**
     * Perform the point crossover operation on a population of BinaryChromosome
     * individuals. Iterate through the population. With probability chi, an
     * individual undergoes crossover. When this happens, select a random mate,
     * select a random crossover point, and exchange bits between the
     * individuals after that point.
     *
     * @param population ArrayList of BinaryChromosome individuals to work on.
     */
    @Override
    public void operate(ArrayList<Individual> population) {
        for (Individual ind : population) {
            // perform crossover?
            if (prng.nextDouble() <= chi) {
                BitSet dad = ((BinaryChromosome) ind).chromosome;
                BitSet mom = ((BinaryChromosome) population.get(prng.nextInt(0, population.size() - 1))).chromosome;

                int length = ((BinaryChromosome) ind).getLength();
                int point = prng.nextInt(0, length - 1);

                // exchange bits
                for (int i = point; i < length; i++) {
                    boolean t = dad.get(i);
                    dad.set(i, mom.get(i));
                    mom.set(i, t);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BinaryChromosome PointCrossover, chi = " + chi;
    }
}
