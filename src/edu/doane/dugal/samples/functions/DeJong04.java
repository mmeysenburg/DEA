package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.general.TournamentSelection;
import java.util.Random;

/**
 * Sample DEA application to optimize DeJong's fourth function, 
 * 
 * f_4 = -\left( \sum_{i = 1}^{30} \left( ix_i^4 + Gauss\left( 0, 1 \right) \right) \right),
 * -1.28 \leq x_i \leq 1.28
 * 
 * (This is the negation of the standard DeJong's fourth function, since the 
 * DEA maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class DeJong04 implements Problem {
    
    /**
     * Java.util.Random PRNG to provide the Gauss(0, 1) portion of the 
     * function.
     */
    private Random prng;

    /**    
     * Create and instance of the problem.
     */
    public DeJong04() {
        PRNG masterRNG = PRNG.getInstance();
        
        prng = new Random(masterRNG.nextInt());   
    }

    /**
     * Return a new, random individual representing a potential solution to
     * this problem.
     * 
     * @return 30 element double chromosome, with each element in the range
     * [-1.28, 1.28]
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(30, -1.28, 1.28, 2);
    }

    /**
     * Evaluate an individual representing a solution to DeJong's fourth
     * function.
     * 
     * @param ind 30 element DoubleChromosome, with each in [-1.28, 1.28].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double f = 0.0;
        
        for(int i = 0; i < 30; i++) {
            f += (i + 1) * Math.pow(dc.getGene(i), 4.0) + prng.nextDouble();
        }
        
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "DeJong04, DeJong's fourth fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of DeJong04.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem dj04 = new DeJong04();
        DEA alg = new DEA(dj04, 100000, 1000); // 100000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation(0.1));

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj04, 100));

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
