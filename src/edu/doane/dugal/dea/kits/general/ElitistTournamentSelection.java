package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;

/**
 * Class representing an elitist tournament selection operator. The operator
 * guarantees that the best individual in a population survives into the next
 * generation.
 *
 * @author Mark M. Meysenburg
 * @version 12/28/2014
 */
public class ElitistTournamentSelection implements Operator {

    /**
     * Random number generator used in the selection process.
     */
    final private PRNG prng;

    /**
     * Tournament size; default is 2.
     */
    private int k;

    /**
     * Default constructor. Create an ElitistTournamentSelection object with
     * tournament size 2.
     */
    public ElitistTournamentSelection() {
        prng = PRNG.getInstance();
        setK(2);
    }

    /**
     * Default constructor. Create an ElitistTournamentSelection object with
     * tournament size k.
     *
     * @param k Tournament size.
     */
    public ElitistTournamentSelection(int k) {
        prng = PRNG.getInstance();
        setK(k);
    }

    /**
     * Get the tournament size for this operator.
     *
     * @return the tournament size
     */
    public int getK() {
        return k;
    }

    /**
     * Set the tournament size for this operator.
     *
     * @param k the tournament size to set; must be greater than 0.
     */
    final public void setK(int k) throws IllegalArgumentException {
        if (k <= 0) {
            throw new IllegalArgumentException("Illegal k in setK: " + k);
        }
        this.k = k;
    }

    @Override
    public String toString() {
        return "ElitistTournamentSelection, k = " + k;
    }

    /**
     * Perform an elitist tournament selection operation on the population, with
     * tournament size k. The best individual in the population is guaranteed to
     * live into the next generation.
     *
     * @param population Array of Individuals to perform selection on.
     */
    @Override
    public void operate(Individual[] population) {
        int n = population.length;
        Individual[] newPop = new Individual[n];

        // find best individual and make her live into the next generation
        double maxFitness = Double.NEGATIVE_INFINITY;
        Individual winner = null;
        for (Individual i : population) {
            double f = i.getFitness();
            if (f > maxFitness) {
                maxFitness = f;
                winner = i;
            }
        }
        newPop[0] = Individual.copy(winner);

        // fill rest of the new population using k-tournament selection
        for (int i = 1; i < n; i++) {
            // pick a winner for spot i
            maxFitness = Double.NEGATIVE_INFINITY;
            winner = null;
            // look at k individuals and pick the best one
            for (int j = 0; j < k; j++) {
                Individual c = population[prng.nextInt(0, n - 1)];
                if (c.getFitness() > maxFitness) {
                    maxFitness = c.getFitness();
                    winner = c;
                } // if c is better than our best yet
            } // look at k individuals

            // clone winner into spot i
            newPop[i] = Individual.copy(winner);
        } // fill new population

        // copy new population back to original one
        for(int i = 0; i < population.length; i++) {
            population[i] = null;
            population[i] = newPop[i];
        }
    }

}
