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
 * Sample DEA application to optimize Griewangk's function, 
 * 
 * f_6 = -1 - \sum_{i = 1}^{10} \left( \frac{x_i^2}{4000} \right) +
 *      \prod_{i = 1}^{10} \left( \cos \left( \frac{x_i}{\sqrt{i}} \right) \right)
 * where -600 \leq x_i \leq 600.
 * 
 * (This is a negation of the standard Griewangk function, since the DEA 
 * framework maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class Griewangk implements Problem {

    /**
     * Return a new DoubleChromosome Individual representing a possible solution
     * to the problem. 
     * 
     * @return 10-gene chromosome with each value in [-600, 600].
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(10, -600.0, 600.0, 3);
    }

    /**
     * Evaluate the fitness of a DoubleChromosome individual representing a 
     * possible solution to the problem. 
     * 
     * @param ind 10-gene chromosome with each value in [-600, 600].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double sum = 0.0, product = 1.0, x;
        
        for(int i = 0; i < 10; i++) {
            x = dc.getGene(i);
            sum += (x * x) / 4000.0;
            product *= Math.cos(x / Math.sqrt(i + 1));
        }
        
        double f = 1 + sum - product;
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "Griewangk, Griewangk's fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of Griewangk.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem gr = new Griewangk();
        DEA alg = new DEA(gr, 100000, 1000); // 100000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation(0.02));

        // ... then evaluation ...
        alg.addOperator(new Evaluate(gr));

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
