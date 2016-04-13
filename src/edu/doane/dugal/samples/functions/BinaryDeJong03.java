package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.bchrom.BinaryChromosome;
import edu.doane.dugal.dea.kits.bchrom.PointCrossover;
import edu.doane.dugal.dea.kits.bchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;

/**
 * Sample DEA application: DeJong's third function, using binary chromosomes.
 * Maximize the function
 *
 * f_3 = \sum_{i = 1}^5 int\left( x_i \right)
 *
 * @author Mark M. Meysenburg
 * @version 03/25/2016
 */
public class BinaryDeJong03 implements Problem {

   /**
    * Create a new 50-bit chromosome (5, 10-bit genes) representing a 
    * potential solution to the problem. 
    * 
    * @return New individual with the specified characteristics
    */
    @Override
    public Individual createRandomIndividual() {
        return new BinaryChromosome(50);
    }

    /**
     * Evaluate an individual.
     * 
     * @param ind 50-bit chromosome representing a potential solution to 
     * the problem
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        BinaryChromosome b = (BinaryChromosome)ind;
        double x1 = (int)(b.getBitsAsInt(0, 9) / 100.0 - 5.12);
        double x2 = (int)(b.getBitsAsInt(10, 19) / 100.0 - 5.12);
        double x3 = (int)(b.getBitsAsInt(20, 29) / 100.0 - 5.12);
        double x4 = (int)(b.getBitsAsInt(30, 39) / 100.0 - 5.12);
        double x5 = (int)(b.getBitsAsInt(40, 49) / 100.0 - 5.12);
        
        b.setFitness(x1 + x2 + x3 + x4 + x5);
    }
    
    @Override
    public String toString() {
        return "BinaryDeJong03, DeJong's thrid fucntion with binary chromosomes";
    }
    
    /**
     * Application entry point for the console-based BinaryDeJong03 application.
     * 
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {
        Problem dj03 = new BinaryDeJong03();
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
