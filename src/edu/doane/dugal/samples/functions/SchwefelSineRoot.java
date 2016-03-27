package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;

/**
 * Sample DEA application to optimize Schwefel's Sine Root function, 
 * 
 * f_8 = -\left( 4189.829 + \sum_{i = 1}^{10} \left( -x_i \sin \left( \sqrt{ | x_i | } \right) \right) \right)
 * where -500 \leq x_i \leq 500.
 * 
 * (This is a negation of the standard Schwefel function, since the DEA 
 * framework maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class SchwefelSineRoot implements Problem {

    /**
     * Get a random DoubleChromosome Individual representing a potential 
     * solution to Schwefel's Sine Root function. 
     * 
     * @return A random DoubleChromosome with 10 genes, each in the range 
     * [-500, 500].
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(10, -500, 500, 3);
    }

    /**
     * Evaluate the fitness of a DoubleChromosome Individual representing a 
     * potential solution to Schwefel's Sine Root function.
     * 
     * @param ind A DoubleChromosome with 10 genes, each in the range 
     * [-500, 500].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double f = 4189.829, x = 0.0;
        for(int i = 0; i < 10; i++) {
            x = dc.getGene(i);
            f += x * Math.sin(Math.sqrt(Math.abs(x)));
        }
        
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "SchwefelSineRoot, Schwefel's Sine Root fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of SchwefelSineRoot.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem sch = new SchwefelSineRoot();
        DEA alg = new DEA(sch, 100000, 1000); // 100000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(sch));

        // ... then selection ...
        alg.addOperator(new ElitistTournamentSelection());

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
