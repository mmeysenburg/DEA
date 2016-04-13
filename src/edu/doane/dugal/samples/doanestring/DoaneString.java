package edu.doane.dugal.samples.doanestring;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.bchrom.BinaryChromosome;
import edu.doane.dugal.dea.kits.bchrom.PointCrossover;
import edu.doane.dugal.dea.kits.bchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.general.TournamentSelection;

/**
 * DEA application that uses binary chromosomes to evolve the string "DOANE".
 * Each five bit gene represents a character in a five character string; 00000
 * is A, 11001 is Z.
 *
 * @author Mark M. Meysenburg
 * @version 12/31/2014
 */
public class DoaneString implements Problem {

    /**
     * Return a new, randomly initialized BinaryChromosome individual
     * representing a potential solution to the problem.
     *
     * @return The new random BinaryChromosome individual.
     */
    @Override
    public Individual createRandomIndividual() {
        // chromosome has five genes, five bits each
        return new BinaryChromosome(25);
    }

    /**
     * Evaluate a BinaryChromosome individual. Fitness is the negative of the
     * sum of the differences between the encoded characters and the target
     * characters in "DOANE".
     *
     * @param ind BinaryChromosome representing a potential solution to the
     * problem.
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        BinaryChromosome bc = (BinaryChromosome) ind;

        int c1 = 'A' + bc.getBitsAsInt(0, 4);
        int c2 = 'A' + bc.getBitsAsInt(5, 9);
        int c3 = 'A' + bc.getBitsAsInt(10, 14);
        int c4 = 'A' + bc.getBitsAsInt(15, 19);
        int c5 = 'A' + bc.getBitsAsInt(20, 24);

        double fitness = Math.abs('D' - c1) + Math.abs('O' - c2) + Math.abs('A' - c3) + Math.abs('N' - c4)
                + Math.abs('E' - c5);

        bc.setFitness(-fitness);
    }

    /**
     * Decode a BinaryChromosome from 32 bits into an ASCII string.
     *
     * @param bc BinaryChromosome individual to convert.
     *
     * @return String equivalent of the individual.
     */
    public String toString(BinaryChromosome bc) {
        char c1 = (char) ('A' + bc.getBitsAsInt(0, 4));
        char c2 = (char) ('A' + bc.getBitsAsInt(5, 9));
        char c3 = (char) ('A' + bc.getBitsAsInt(10, 14));
        char c4 = (char) ('A' + bc.getBitsAsInt(15, 19));
        char c5 = (char) ('A' + bc.getBitsAsInt(20, 24));

        StringBuilder sb = new StringBuilder();
        sb.append(c1);
        sb.append(c2);
        sb.append(c3);
        sb.append(c4);
        sb.append(c5);

        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "DoaneString";
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
        DoaneString ds = new DoaneString();
        DEA alg = new DEA(ds, 100, 100); // 100 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(ds, 10));

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

            System.out
                    .println("Best ever individual: " + ds.toString((BinaryChromosome) stats.getBestEverIndividual()));
            System.out.printf("Best ever fitness: %.2f\n", stats.getBestEverIndividual().getFitness());
        }
    } // main

}
