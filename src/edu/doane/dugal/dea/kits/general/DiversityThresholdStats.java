package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.Problem;
import java.util.TreeSet;

/**
 * Class to do standard statistics (min, max, average, and best-ever fitness),
 * plus population diversity. Includes a threshold on diversity; if that 
 * value is crossed, the population is re-seeded with new random values, along
 * with a certain percentage of the best-ever individual. Do not re-use objects
 * of this class; use a new one for each run, so that best-ever values are
 * reset. 
 *
 * @author Mark M. Meysenburg
 * @version 02/12/2018
 */
public class DiversityThresholdStats implements Operator {

    /**
     * Fitness of the best individual ever seen.
     */
    private double bestEverFitness;

    /**
     * Reference to the best individual ever seen.
     */
    private Individual bestEverIndividual;

    /**
     * String used to format stats output. 
     */
    private String format;

    /**
     * Generation number to include in output.
     */
    private int generationNumber;
    
    /**
     * Threshold for re-seeding population. If the diversity of the population
     * falls below this value, re-seed the population with new random individuals,
     * keeping a certain percentage of the population for copies of the best-
     * ever individual.
     */
    private double threshold;
    
    /**
     * Percentage of the population that is copies of the best-ever individual
     * after re-seeding.
     */
    private double keepPercentage;
    
    /**
     * Problem used to create new random individuals.
     */
    private Problem problem;
    
    /**
     * Initializing constructor. Create an instance with the best-ever fitness
     * set to negative infinity, the best-ever Individual reference set to null,
     * the number of decimal places to display in output, threshold, and keep
     * percentage set to the specified values.
     *
     * @param decimalPlaces Number of decimal places to show in output.
     * @param threshold Diversity threshold value for re-seeding the population,
     * in [0, 1].
     * @param keepPercentage Percent of population that contains copy of best-
     * ever individual after re-seeding.
     * @param problem Problem used to create new random individuals.
     */
    public DiversityThresholdStats(int decimalPlaces, double threshold, 
            double keepPercentage, Problem problem) {
        
        bestEverFitness = Double.NEGATIVE_INFINITY;
        bestEverIndividual = null;
        makeFormat(decimalPlaces);
        this.threshold = threshold;
        this.keepPercentage = keepPercentage;
        this.problem = problem;
    }

    /**
     * Create the format string used for output.
     */
    private void makeFormat(int decimalPlaces) {
        format = "Gen: %d" +                    // generation number
                "\t%." + decimalPlaces + "f" +  // min fitness
                "\t%." + decimalPlaces + "f" +  // max fitness
                "\t%." + decimalPlaces + "f" +  // avg fitness
                "\t%." + decimalPlaces + "f" +  // diversity
                "\t%." + decimalPlaces + "f\n";
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
     * Find min, max, and average fitness, plus diversity, for the specified 
     * population, and output to standard output. If the population falls
     * below the threshold, re-seed.
     *
     * @param population Array of Individuals to get statistics for.
     */
    @Override
    public void operate(Individual[] population) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double avg = 0.0;
        
        // diversity: use a set to count distinct individuals
        TreeSet<Individual> set = new TreeSet<>();
        for(Individual i : population) {
            set.add(i);
        }
        double diversity = set.size() / ((double)population.length);
        
        // if we're below the diversity threshold, re-seed the population
        if(diversity < threshold) {
            int idx = (int)(keepPercentage * population.length);
            
            // keepPercentage% of the population is the best-ever individual
            for(int i = 0; i < idx; i++) {
                population[i] = Individual.copy(bestEverIndividual);
            }
            
            // the rest of the population are new, random individuals
            for(int i = idx; i < population.length; i++) {
                population[i] = problem.createRandomIndividual();
                problem.evaluateIndividual(population[i]);
            }
            
            // recalculate diversity
            set.clear();
            for(Individual i : population) {
                set.add(i);
            }
            diversity = set.size() / ((double)population.length);
        }

        // now evaluate fitness
        for (Individual i : population) {

            double f = i.getFitness();

            if (f < min) { min = f; }

            if (f > max) { max = f; }

            if (f > bestEverFitness) {
                bestEverFitness = f;
                bestEverIndividual = Individual.copy(i);
            }

            avg += f;
        }

        avg /= population.length;

        System.out.printf(format, 
                generationNumber, 
                min, 
                max, 
                avg, 
                diversity, 
                bestEverFitness);
        
        generationNumber++;
    }

    @Override
    public String toString() {
        return "DiversityThresholdStats, threshold = " +
                threshold + ", keepPerc = " + 
                keepPercentage;
    }
}
