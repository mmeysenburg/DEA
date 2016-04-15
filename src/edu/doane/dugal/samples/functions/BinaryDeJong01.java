package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.bchrom.BinaryChromosome;
import edu.doane.dugal.dea.kits.bchrom.PointCrossover;
import edu.doane.dugal.dea.kits.bchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.kits.general.TournamentSelection;

/**
 * Sample DEA application: DeJong's first function, using binary chromosomes.
 * Maximize the function
 *
 * -\sum_{i = 1}^{3} x_{i}^{2}, -5.12 \leq x_{i} 5.12
 * 
 * (This is a negation of the standard DeJong's first function, since the DEA
 * framework maximizes instead of minimizes.)
* 
 * Maximum is 0, at (0, 0, 0).
  *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class BinaryDeJong01 implements Problem {

    /**
     * Create a random potential solution to the problem.
     *
     * @return A DoubleChromosome Individual, with three genes, each in [-5.12,
     * 5.12], making 10 bits per gene.
     *
     */
    @Override
    public Individual createRandomIndividual() {
        return new BinaryChromosome(30);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind A 30-bit BinaryChromosome individual.
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        BinaryChromosome d = (BinaryChromosome) ind;

        double x1 = -5.12 + (d.getBitsAsInt(0, 9) * 0.01);
        double x2 = -5.12 + (d.getBitsAsInt(10, 19) * 0.01);
        double x3 = -5.12 + (d.getBitsAsInt(20, 29) * 0.01);

        d.setFitness(-(x1 * x1 + x2 * x2 + x3 * x3));
    }

    @Override
    public String toString() {
        return "DeJong's first fucntion (BinaryDeJong01)";
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
        Problem dj01 = new BinaryDeJong01();
        DEA alg = new DEA(dj01, 1000, 100); // 1000 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj01, 10));

        // ... then selection ...
        alg.addOperator(new TournamentSelection());

        // ... then statistics
        StandardStats stats = new StandardStats(2);
        alg.addOperator(stats);

        // dump run parameters to standard output, so a successful run
        // could be duplicated
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
