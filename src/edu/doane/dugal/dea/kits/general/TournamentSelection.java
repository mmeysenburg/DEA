package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.PRNG;
import java.util.ArrayList;

/**
 * Class implementing non-elitist tournament selection.
 *
 * @author Mark M. Meysenburg
 * @version 12/26/2014
 */
public class TournamentSelection implements Operator {

    /**
     * Tournament size. Defaults to 2.
     */
    private int k;

    /**
     * Random number generator used for the selection process.
     */
    final private PRNG prng;

    /**
     * Create a new TournamentSelection operator with default tournament size 2.
     */
    public TournamentSelection() {
        prng = PRNG.getInstance();
        setK(2);
    }

    /**
     * Create a new TournamentSelection operator with the specified tournament
     * size.
     *
     * @param k Tournament size for the operator.
     */
    public TournamentSelection(int k) {
        setK(k);
        prng = PRNG.getInstance();
    }

    /**
     * Perform a tournament selection operation on the population, with
     * tournament size k.
     *
     * @param population ArrayList of Individuals to perform selection on.
     */
    @Override
    public void operate(ArrayList<Individual> population) {
        int n = population.size();
        ArrayList<Individual> newPop = new ArrayList<>(n);

        // fill new population using k-tournament selection
        for (int i = 0; i < n; i++) {
            // pick a winner for spot i
            double maxFitness = Double.NEGATIVE_INFINITY;
            Individual winner = null;
            // look at k individuals and pick the best one
            for (int j = 0; j < k; j++) {
                Individual c = population.get(prng.nextInt(0, n - 1));
                if (c.getFitness() > maxFitness) {
                    maxFitness = c.getFitness();
                    winner = c;
                } // if c is better than our best yet
            } // look at k individuals

            // clone winner into spot i
            newPop.add(winner.copy());
        } // fill new population

        // copy new population back to original one
        population.clear();
        for (Individual i : newPop) {
            population.add(i.copy());
        }
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
        return "TournamentSelection, k = " + k;
    }

}
