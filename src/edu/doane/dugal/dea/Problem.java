package edu.doane.dugal.dea;

/**
 * Interface representing a problem to be solved by the evolutionary algorithm.
 * Classes that implement Problem should know how to create random individuals
 * that represent potential solutions to the problem, and how to evaluate
 * individuals to see how good a solution they really are. In addition, the
 * constructor for the Problem is a good place to make the first call to the
 * PRNG getInstance() method, to get the random number generator up and running.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
public interface Problem {

    /**
     * Create a random individual representing a potential solution to the
     * problem.
     *
     * @return Reference to a new Individual, representing a solution to the
     * problem.
     */
    public Individual createRandomIndividual();

    /**
     * Evaluate an individual to see how good a solution to the problem it is.
     * This method should come up with a double representing the individual's
     * fitness (higher means better), and set the individual's fitness field.
     *
     * @param ind Individual to evaluate.
     */
    public void evaluateIndividual(Individual ind);
}
