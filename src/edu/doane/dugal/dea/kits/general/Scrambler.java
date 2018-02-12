package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.ichrom.IntegerChromosome;
import java.util.Set;
import java.util.TreeSet;

/**
 * Operator to scramble things up if population diversity becomes too low.
 * 
 * @author Mark M. Meysenburg
 * @version 2/12/2018
 */
public class Scrambler implements Operator {
    
    /** Diversity threshold for scrambling. */
    double threshold;
    
    /** Problem used to create new random individuals. */
    Problem problem;
    
    /** Stats object to get best-ever individual to keep. */
    Operator stats;
    
    /** Percentage of new population that will be best-ever individual. */
    double keepPercent;
    
    public Scrambler(double threshold, Problem problem, Operator stats, 
            double keepPercent) {
        this.threshold = threshold;
        this.problem = problem;
        this.stats = stats;
        this.keepPercent = keepPercent; 
    }
    
    @Override
    public String toString() {
        return "Scrambler (" +
                threshold + ", " +
                problem.toString() + ", " +
                stats.toString() + ", " + 
                keepPercent + ")";
    }

    @Override
    public void operate(Individual[] population) {
        //determine population diversity
        Set<Individual> set = new TreeSet<>();
        for(Individual i : population) {
            set.add((IntegerChromosome)i);
        }
        double diversity = set.size() / (double)population.length;
        
        // have we gone below the threshold?
        if(diversity < threshold) {
            // keep a certain percentage of the best individual
            int idx = (int)(keepPercent * population.length);
            Individual bestEver = ((StandardStats)stats).getBestEverIndividual();
            if(bestEver == null) {
                bestEver = problem.createRandomIndividual();
                bestEver.setFitness(Double.MIN_VALUE);
            }
            for(int i = 0; i < idx; i++) {
                population[i] = Individual.copy(bestEver);
            }
            
            // and fill the rest with random indiviudals
            for(int i = idx; i < population.length; i++) {
                population[i] = problem.createRandomIndividual();
            }
        }
    }
    
}
