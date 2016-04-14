package edu.doane.dugal.dea.kits.dchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Class to perform single point crossover on a population of DoubleChromosome
 * individuals. Iterate through the population; with probability chi, each
 * individual mates. If mating, pick a mate at random, then swap genes between
 * the two after a randomly selected point on the chromosome.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
public class PointCrossover implements Operator {

    /**
     * Chance for a crossover event, in [0, 1].
     */
    private double chi;

    /**
     * Random number generator used by the operation.
     */
    final private PRNG prng;

    /**
     * Default constructor; chi is set to 0.6.
     */
    public PointCrossover() {
        chi = 0.6;
        prng = PRNG.getInstance();
    }

    /**
     * Initializing constructor; create a crossover operation with the specified
     * probability of crossover.
     *
     * @param chi Probability for a crossover event, in [0, 1].
     */
    public PointCrossover(double chi) {
        setChi(chi);
        prng = PRNG.getInstance();
    }

    /**
     * Perform the single point crossover operation on the specified population.
     *
     * @param population Array of DoubleChromosome individuals
     */
    @Override
    public void operate(Individual[] population) {
        int n = population.length;
        for (int i = 0; i < n; i++) {
            // do a crossover?
            if (prng.nextDouble() <= chi) {
                DoubleChromosome dad = (DoubleChromosome) population[i];
                DoubleChromosome mom = (DoubleChromosome) population[prng.nextInt(0, n - 1)];

                // exchange genes
                int iLength = dad.getLength();
                int xpoint = prng.nextInt(0, iLength - 1);
                for (int j = xpoint; j < iLength; j++) {
                    double t = dad.getGene(j);
                    dad.setGene(j, mom.getGene(j));
                    mom.setGene(j, t);
                } // exchanging genes
            } // if crossover
        } // for each individual
    } // operate

    /**
     * Get the crossover probability for this operator.
     *
     * @return the crossover probability.
     */
    final public double getChi() {
        return chi;
    }

    /**
     * Set the crossover probability for this operator.
     *
     * @param chi the crossover probability to set, in [0, 1]
     * @throws IllegalArgumentException if the parameter isn't in the allowed
     * range.
     */
    final public void setChi(double chi) throws IllegalArgumentException {
        if (chi < 0.0 || chi > 1.0) {
            throw new IllegalArgumentException("Illegal chi to setChi: " + chi);
        }
        this.chi = chi;
    }

    @Override
    public String toString() {
        return "DoubleChromosome PointCrossover, chi = " + chi;
    }

}
