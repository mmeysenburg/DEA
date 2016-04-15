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
 * Sample DEA application: DeJong's third function, using double chromosomes.
 * Maximize the function
 *
 * f_3 = \sum_{i = 1}^5 int\left( x_i \right)
 * 
 * Maximum value is 25.
 *
 * @author Mark M. Meysenburg
 * @version 03/25/2016
 */
public class DeJong03 implements Problem {

    /**
     * Create a double chromosome with 5 doubles in the range [-5.12, 5.12]
     * 
     * @return Random individual with the proper characteristics
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(5, -5.12, 5.12, 2);
    }

    /**
     * Evaluate the fitness of an individual.
     * 
     * @param ind 5 double chromosome that is a potential solution to the 
     * problem
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome d = (DoubleChromosome)ind;
        
        d.setFitness(deJong03(d.getGene(0), 
                d.getGene(1), 
                d.getGene(2), 
                d.getGene(3), 
                d.getGene(4)));
    }
    
    /**
     * Compute the value of DeJong's third function. 
     * 
     * @param x1
     * @param x2
     * @param x3
     * @param x4
     * @param x5
     * 
     * @return Value of the function at (x1, x2, x3, x4, x5).
     */
    public static double deJong03(double x1, double x2, double x3, double x4, double x5) {
        return (int)x1 + (int)x2 + (int)x3 + (int)x4 + (int)x5;
    }
    
    @Override
    public String toString() {
        return "DeJong03, DeJong's thrid fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of DeJong03.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        Problem dj03 = new DeJong03();
        DEA alg = new DEA(dj03, 10000, 1000); // 10000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj03, 100));

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
