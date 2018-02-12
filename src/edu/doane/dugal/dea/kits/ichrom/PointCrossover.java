package edu.doane.dugal.dea.kits.ichrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Class implementing single-point crossover on IntegerChromosome individuals.
 * Iterate through the population; with probability chi, each individual mates.
 * If mating, pick a mate at random, then swap genes between the two after a
 * randomly selected point on the chromosome.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class PointCrossover implements Operator {

    /**
     * Probability of a crossover event taking place. Default value is 0.6.
     */
    private double chi;

    /**
     * Random number generator used by the operator.
     */
    final private PRNG prng;

    /**
     * Default constructor. Create a new point crossover operator, with
     * probability of crossover set to 0.6.
     */
    public PointCrossover() {
        setChi(0.6);
        prng = PRNG.getInstance();
    }
    
    /**
     * Initializing constructor. Create a new point crossover operator, with
     * the specified probability of crossover.
     * 
     * @param chi Probability of crossover. Must be in [0, 1]. 
     */
    public PointCrossover(double chi) {
        setChi(chi);
        prng = PRNG.getInstance();
    }

    /**
     * Get the probability of a crossover event.
     *
     * @return chi, the probability of a crossover event.
     */
    public double getChi() {
        return chi;
    }

    /**
     * Set the probability of a crossover event.
     *
     * @param chi the probability of a crossover event.
     * @throws IllegalArgumentException if the parameter isn't in the allowed
     * range.
     */
    final public void setChi(double chi) throws IllegalArgumentException {
        if (chi < 0.0 || chi > 1.0) {
            throw new IllegalArgumentException("Illegal mu to setChi: " + chi);
        }
        this.chi = chi;
    }

    /**
     * Perform the crossover operation on the specified population.
     *
     * @param population Array of IntegerChromosome individuals to operate
     * on.
     */
    @Override
    public void operate(Individual[] population) {
        for (Individual ind : population) {
            // do a crossover?
            if (prng.nextDouble() <= chi) {
                // if so, swap genes between current individual and another,
                // randomly selected individual
                int[] dad = ((IntegerChromosome) ind).chromosome;
                int[] mom = ((IntegerChromosome) population[prng.nextInt(0, population.length - 1)]).chromosome;

                int point = prng.nextInt(0, dad.length - 1);
                for (int i = point; i < dad.length; i++) {
                    int t = dad[i];
                    dad[i] = mom[i];
                    mom[i] = t;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "IntegerChromosome PointCrossover, chi = " + chi;
    }
}
