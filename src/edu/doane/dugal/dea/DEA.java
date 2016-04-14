package edu.doane.dugal.dea;

import java.util.ArrayList;

/**
 * Main class in the Doane Evolutionary Algorithm. Create a DEA object with your
 * problem, population size, and number of generations, add operators, and start
 * it up!
 *
 * @author Mark M. Meysenburg
 * @version 12/28/2014
 */
public class DEA extends Thread {

    /**
     * Problem to be solved by this algorithm.
     */
    private final Problem problem;

    /**
     * Population of Individuals representing potential solutions to the
     * problem.
     */
    private final Individual[] population;

    /**
     * List of operators to apply to the population each generation.
     */
    private final ArrayList<Operator> operators;

    /**
     * Number of generations to run the algorithm.
     */
    private final int numGens;

    /**
     * Create a new DEA object.
     *
     * @param problem Problem to solve.
     * @param popSize Population size.
     * @param numGens Number of generations to execute.
     */
    public DEA(Problem problem, int popSize, int numGens) {
        this.problem = problem;

        // create initial random population
        population = new Individual[popSize];
        for (int i = 0; i < population.length; i++) {
            population[i] = this.problem.createRandomIndividual();
        }

        this.numGens = numGens;

        operators = new ArrayList<>();
    }

    /**
     * Add an operator to the list of operations performed on each generation.
     * These are applied in order, so make sure you add them in the order you
     * want them to be executed.
     *
     * @param op Operator to add to the list of operations performed on each
     * generation.
     */
    public void addOperator(Operator op) {
        operators.add(op);
    }

    /**
     * Get the tableau for a run of the DEA, as a String. The tableau includes
     * information on population size, number of generations, characteristics of
     * the operators, and the random number generator seed value used for the
     * run.
     *
     * @return A String object with the tableau for a run of the DEA.
     */
    public String getTableau() {
        StringBuilder s = new StringBuilder();

        s.append("Problem: ").append(problem.toString()).append("\n");
        s.append("Operators:\n");
        for (Operator op : operators) {
            s.append("\t").append(op.toString()).append("\n");
        }
        s.append("Population size: ").append(population.length).append("\n");
        s.append("Generations: ").append(numGens).append("\n");
        PRNG p = PRNG.getInstance();
        s.append("PRNG seed: ").append(p.getSeed()).append("\n");

        return s.toString();
    }

    /**
     * Execute the algorithm. For each generation, apply each of the operators
     * in turn to the population.
     */
    @Override
    public void run() {
        for (int gen = 0; gen < numGens; gen++) {
            for (Operator op : operators) {
                op.operate(population);
            } // for operators
        } // for gens
    }

}
