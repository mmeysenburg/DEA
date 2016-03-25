package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import java.util.ArrayList;

/**
 * Class to do standard population statistics (min, max, average, and best- ever
 * fitness), and dump them to standard output. Do not re-use objects of this
 * class; use a new one for each run, so that best-ever values are reset.
 *
 * @author Mark M. Meysenburg
 * @version 12/26/2014
 */
public class StandardStats implements Operator {

    /**
     * Fitness of the best individual ever seen.
     */
    private double bestEverFitness;

    /**
     * Reference to the best individual ever seen.
     */
    private Individual bestEverIndividual;

    /**
     * String used to format stats output. Defaults to 4 decimal places for each
     * numeric value.
     */
    private String format;

    /**
     * Generation number to include in output.
     */
    private int generationNumber;

    /**
     * Default constructor. Create an instance with the best-ever fitness set to
     * negative infinity, the best-ever Individual reference set to null, and
     * the number of decimal places to display in output set to 4.
     */
    public StandardStats() {
        bestEverFitness = Double.NEGATIVE_INFINITY;
        bestEverIndividual = null;
        makeFormat(4);
    }

    /**
     * Initializing constructor. Create an instance with the best-ever fitness
     * set to negative infinity, the best-ever Individual reference set to null,
     * and the number of decimal places to display in output set to the
     * specified value.
     *
     * @param decimalPlaces Number of decimal places to show in output.
     */
    public StandardStats(int decimalPlaces) {
        bestEverFitness = Double.NEGATIVE_INFINITY;
        bestEverIndividual = null;
        makeFormat(decimalPlaces);
    }

    /**
     * Create the format string used for output.
     */
    private void makeFormat(int decimalPlaces) {
        format = "Gen: %d\t" + "%." + decimalPlaces + "f\t%." + decimalPlaces + "f\t%." + decimalPlaces + "f\t%."
                + decimalPlaces + "f\n";
    }

    /**
     * Get a reference to the best individual ever seen during a run.
     *
     * @return Individual reference to the best critter seen during the run.
     */
    public Individual getBestEverIndividual() {
        return bestEverIndividual;
    }

    /**
     * Find min, max, and average fitness for the specified population, and
     * output to standard output.
     *
     * @param population ArrayList of Individuals to get statistics for.
     */
    @Override
    public void operate(ArrayList<Individual> population) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double avg = 0.0;

        for (Individual i : population) {

            double f = i.getFitness();

            if (f < min) {
                min = f;
            }

            if (f > max) {
                max = f;
            }

            if (f > bestEverFitness) {
                bestEverFitness = f;
                bestEverIndividual = i.copy();
            }

            avg += f;
        }

        avg /= population.size();

        System.out.printf(format, generationNumber, min, max, avg, bestEverFitness);
        generationNumber++;
    }

    @Override
    public String toString() {
        return "StandardStats";
    }
}
