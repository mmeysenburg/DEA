package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.Problem;
import java.util.concurrent.ForkJoinPool;

/**
 * Class to evaluate all the individuals in a population.
 *
 * @author Mark M. Meysenburg
 * @version 12/26/2014
 */
public class Evaluate implements Operator {

    /**
     * Problem used to evaluate individuals.
     */
    private Problem prob;
    
    /**
     * ForkJoinPool used to spread evaluation of individuals across 
     * multiple threads.
     */
    private ForkJoinPool pool;
    
    /**
     * Threshold to stop dividing into subthreads; if the number of individuals
     * to evaluate is less than this number, do the evaluation directly.
     */
    private int threshold;

    /**
     * Initializing constructor. Create an Evaluate operator using the specified
     * problem to evaluate individuals.
     *
     * @param prob Problem used to evaluate individuals.
     * @param threshold Limit for evaluating individuals in-place, without
     * subthreads
     */
    public Evaluate(Problem prob, int threshold) {
        this.prob = prob;
        this.threshold = threshold;
        pool = new ForkJoinPool();
    }

    /**
     * Evaluate all the individuals in the specified population.
     *
     * @param population Population to evaluate.
     */
    @Override
    public void operate(Individual[] population) {
        // kick off threads to evaluate the population
        EvaluateThread et = new EvaluateThread(prob, population, 0, 
            population.length - 1);
        
        et.setThreshold(threshold);
        
        pool.invoke(et);
    }

    @Override
    public String toString() {
        return "Evaluate, using Problem: " + prob + ", threshold: " + threshold;
    }

}
