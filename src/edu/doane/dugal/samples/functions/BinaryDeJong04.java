package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.bchrom.BinaryChromosome;
import edu.doane.dugal.dea.kits.bchrom.PointCrossover;
import edu.doane.dugal.dea.kits.bchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.general.TournamentSelection;
import java.util.Random;

/**
 * Sample DEA application to optimize DeJong's fourth function, using binary
 * chromosomes. The function is:
 * 
 * f_4 = \sum_{i = 1}^{30} \left( ix_i^4 + Gauss\left( 0, 1 \right) \right),
 * -1.28 \leq x_i \leq 1.28
 * 
 * (This is the negation of the standard DeJong's fourth function, since the 
 * DEA maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class BinaryDeJong04 implements Problem {
    
    /**
     * Java.util.Random PRNG to provide the Gauss(0, 1) portion of the 
     * function.
     */
    private Random prng;
    
    /**    
     * Create and instance of the problem.
     */
    public BinaryDeJong04() {
        PRNG masterRNG = PRNG.getInstance();
        prng = new Random(masterRNG.nextInt());
    }

    /**
     * Return a random BinaryChromosome individual representing a potential
     * solution to DeJong's fourth function.
     * 
     * @return 240-bit BinaryChromosome individual; 30 genes at 8 bits per
     * gene
     */
    @Override
    public Individual createRandomIndividual() {
        // 30 genes, 8 bits per gene
        return new BinaryChromosome(240);
    }

    /**
     * See how good a solution an individual is
     * 
     * @param ind 30-bit BinaryChromosome Individual representing a potential
     * solution to DeJong's fourth function.
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        BinaryChromosome bc = (BinaryChromosome)ind;
        double f = 0.0;
        for(int i = 0; i < 30; i++) {
            double v = bc.getBitsAsInt(i * 8, i * 8 + 7) / 100.0 - 1.28;
            f += (i + 1) * Math.pow(v, 4) + prng.nextDouble();
        }
        bc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "BinaryDeJong04, DeJong's fourth fucntion with binary chromosomes";
    }

    /**
     * Application entry point for console-based run of BinaryDeJong04.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem dj04 = new BinaryDeJong04();
        DEA alg = new DEA(dj04, 100000, 1000); // 100000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation(0.1));

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj04));

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
            System.out.printf("Best ever fitness: %.3f\n", stats.getBestEverIndividual().getFitness());
        }
    }    
}
