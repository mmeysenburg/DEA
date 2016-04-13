package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.general.TournamentSelection;

/**
 * Sample DEA application to optimize Ackley's function, 
 * 
 * f_5 = - \left( 20 + e - 20^{-0.2 \sqrt{ \frac{1}{30} \sum_{i = i}^{30} x_i^2 }} -
 *          e^{ \frac{1}{30} \sum_{i = 1}^{30} \cos \left( 2 \pi x_i \right) } \right)
 * 
 * where -30.0 \leq x_i \leq 30.0.
 * 
 * (This is a negation of the standard Ackley function, since the DEA framework
 * maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class Ackley implements Problem {

    /**
     * Get a random DoubleChromosome individual that represents a potential
     * solution to Ackley's function.
     * 
     * @return 30-gene DoubleChromosome Individual with each gene in the range
     * [-30, 30].
     */
    @Override
    public Individual createRandomIndividual() {
        // 30 genes each in [-30, 30]
        return new DoubleChromosome(30, -30.0, 30.0, 3);
    }

    /**
     * Evaluate the fitness of a DoubleChromosome Individual representing a 
     * potential solution to Ackley's function
     * 
     * @param ind #0-gene DoubleChromosome Individual, with each gene in the
     * range [-30, 30]
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double f = 0.0;
        
        double t1 = 0.0;
        double t3 = 0.0;
        for(int i = 0; i < 30; i++) {
            t1 += dc.getGene(i) * dc.getGene(i);
            t3 += Math.cos(2.0 * Math.PI * dc.getGene(i));
        }
        double t2 = 20.0 * Math.pow(Math.E, -0.2 * Math.sqrt(t1 / 30.0));
        double t4 = Math.pow(Math.E, t3 / 30.0);
        
        f = 20 + Math.E - t2 - t4;
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "Ackley, Ackley's fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of Ackley.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem ack = new Ackley();
        DEA alg = new DEA(ack, 200000, 1000); // 200000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation(0.1));

        // ... then evaluation ...
        alg.addOperator(new Evaluate(ack, 100));

        // ... then selection ...
        alg.addOperator(new TournamentSelection());

        // ... then statistics
        StandardStats stats = new StandardStats(3);
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
