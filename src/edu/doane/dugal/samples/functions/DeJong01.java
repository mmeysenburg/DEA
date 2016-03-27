package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;

/**
 * Sample DEA application: DeJong's first function, using double chromosomes.
 * Maximize the function
 *
 * f_1 = -\sum_{i = 1}^{3} x_{i}^{2}, -5.12 \leq x_{i} 5.12
 * 
 * (This is the negation of the standard DeJong's first function, since the 
 * DEA framework maximizes instead of minimizes.)
 *
 * @author Mark M. Meysenburg
 * @version 12/26/2014
 */
public class DeJong01 implements Problem {

    /**
     * Create a random potential solution to the problem.
     *
     * @return A DoubleChromosome Individual, with three genes, each in [-5.12,
     * 5.12].
     */
    @Override
    public Individual createRandomIndividual() {
        // create a new Individual with three double chromosomes in the
        // range [-5.12, 5.12]; use 2 decimal points when printing 
        // fitness of the individual
        return new DoubleChromosome(3, -5.12, 5.12, 2);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind A DoubleChromosome Individual, with three genes, each in
     * [-5.12, 5.12].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome d = (DoubleChromosome) ind;
        double f = 0.0;
        for (int i = 0; i < 3; i++) {
            f += d.getGene(i) * d.getGene(i);
        }

        d.setFitness(-f);
    }

    @Override
    public String toString() {
        return "DeJong's first fucntion (DeJong01)";
    }

    /**
     * Application entry point for console-based execution.
     *
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {

        // to run with a specific seed, set it before doing anything else;
        // otherwise, the first access to the PRNG object will cause it to
        // be seeded based on the system time.
        // PRNG p = PRNG.getInstance();
        // p.setSeed(1209432115);
        
        // create problem and algorithm
        Problem dj01 = new DeJong01();
        DEA alg = new DEA(dj01, 1000, 100); // 1000 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover(0.65));
        alg.addOperator(new PointMutation(0.02));   // ... then mutation ...
        alg.addOperator(new Evaluate(dj01));    // ... then evaluation ...
        alg.addOperator(new ElitistTournamentSelection()); // ... then selection ...
        StandardStats stats = new StandardStats(2); // ... then statistics
        alg.addOperator(stats);

        // dump run parameters to standard output, so a successful run could be duplicated
        System.out.println(alg.getTableau());

        // start the DEA thread
        alg.start();

        // wait for the DEA thread to complete before reporting final
        // statistics
        try {
            alg.join();
        } catch (InterruptedException ex) {
            System.err.println("DEA thread interrupted!");
        } finally {
            System.out.println("Best ever individual: " + stats.getBestEverIndividual());
            System.out.printf("Best ever fitness: %.2f\n", stats.getBestEverIndividual().getFitness());
        }
    } // main
}
