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
 * Sample DEA application to optimize Rastrigin's function, 
 * 
 * f_7 = -\left( 200 + \sum_{i = 1}^{20} \left( x_i^2 - 10 \cos \left( 2 \pi x_i \right) \right) \right),
 * where -5.12 \leq x_i \leq 5.12.
 * 
 * (This is a negation of the standard Rastrigin function, since the DEA 
 * framework maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class Rastrigin implements Problem {

    /**
     * Get a DoubleChromosome Individual representing a potential solution to 
     * Rastrigin's function. 
     * 
     * @return A random DoubleChromosome Individual, with 20 genes in the 
     * range [-5.12, 5.12].
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(20, -5.12, 5.12, 3);
    }

    /**
     * Evaluate the fitness of a DoubleChromosome Individual representing a 
     * potential solution to Rastrigin's function. 
     * 
     * @param ind A DoubleChromosome Individual, with 20 genes in the 
     * range [-5.12, 5.12].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double f = 200.0, x = 0.0;
        
        for(int i = 0; i < 20; i++) {
            x = dc.getGene(i);
            f += x * x - 10.0 * Math.cos(2.0 * Math.PI * x);
        }
        
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "Rastrigin, Rastrigin's fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of Rastrigin.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem ras = new Rastrigin();
        DEA alg = new DEA(ras, 100000, 1000); // 100000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(ras));

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
