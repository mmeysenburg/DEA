package edu.doane.dugal.dea.kits.general;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Operator;
import edu.doane.dugal.dea.Problem;
import java.util.ArrayList;

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
     * Initializing constructor. Create an Evaluate operator using the specified
     * problem to evaluate individuals.
     *
     * @param prob Problem used to evaluate individuals.
     */
    public Evaluate(Problem prob) {
        this.prob = prob;
    }

    /**
     * Evaluate all the individuals in the specified population.
     *
     * @param population Population to evaluate.
     */
    @Override
    public void operate(ArrayList<Individual> population) {

        for (Individual p : population) {
            prob.evaluateIndividual(p);
        }
    }

    @Override
    public String toString() {
        return "Evaluate, using Problem: " + prob;
    }

}
