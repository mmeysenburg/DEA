package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import java.util.concurrent.RecursiveAction;

/**
 * Extension of RecursiveAction to evaluate the population using multiple 
 * threads. 
 * 
 * @author Mark M. Meysenburg
 * @version 04/13/2016
 */
public class EvaluateThread extends RecursiveAction {

    /**
     * Problem used to evaluate individuals in the population.
     */
    private Problem prob;
    
    /**
     * Population to evaluate.
     */
    private Individual[] pop;
    
    /** 
     * Index of first individual in the population to evaluate.
     */
    private int start;
    
    /**
     * Index of last individual in the population to evaluate.
     */
    private int end;
    
    /**
     * Threshold to stop dividing into subthreads; if the number of individuals
     * to evaluate is less than this number, do the evaluation directly.
     */
    private int threshold;
    
    /**
     * Construct a new EvaluateThread object to evaluate a portion of the
     * population, using a threshold of 10.
     * 
     * @param prob Problem used to evaluate individuals.
     * @param pop Population to evaluate.
     * @param start Index of first individual in the population to evaluate.
     * @param end Index of last individual in the population to evaluate.
     */
    public EvaluateThread(Problem prob, Individual[] pop,
            int start, int end) {
        this.prob = prob;
        this.pop = pop;
        this.start = start;
        this.end = end;
        setThreshold(10);
    }
    
    @Override
    /**
     * Evaluate the population: in-place if section is small enough, or with
     * two subthreads.
     */
    protected void compute() {
        // if the population section to evaluate is small enough, evaluate
        // the individuals directly without spawning subthreads
        if((end - start) < threshold) {
            for(int i = start; i <= end; i++) {
                prob.evaluateIndividual(pop[i]);
            } // for i
        } else {
            // if threshold hasn't been reached, divide section in half
            // and spawn subthreads
            int mid = (start + end) / 2;
            invokeAll(new EvaluateThread(prob, pop, start, mid),
                    new EvaluateThread(prob, pop, mid + 1, end));
        }
    }

    /**
     * Get the threshold value for dividing into threads. If the number of individuals
     * to evaluate is less than this number, do the evaluation directly.
     * 
     * @return the threshold
     */
    public final int getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold value for dividing into threads. If the number of individuals
     * to evaluate is less than this number, do the evaluation directly.
     * 
     * @param threshold the threshold to set
     */
    public final void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    
}
