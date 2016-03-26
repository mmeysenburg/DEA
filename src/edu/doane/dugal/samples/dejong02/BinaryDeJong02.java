package edu.doane.dugal.samples.dejong02;

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
 *
 * @author Mark
 */
/**
 * Sample DEA application: DeJong's second function, using binary chromosomes.
 * Maximize the function
 *
 * f_2 = 100 \left( x_1^2 - x_2 \right)^2 + \left( 1 - x_1 \right)^2
 *
 * @author Mark M. Meysenburg
 * @version 03/25/2016
 */
public class BinaryDeJong02 implements Problem {

    /**
     * Create a randomized potential solution to the problem, consisting
     * of two 12-bit chromosomes.
     * 
     * @return A new BinaryChromosome individual with two 12-bit chromosomes.
     */
    @Override
    public Individual createRandomIndividual() {
        // binary chromosome with 2, 12-bit chromosomes
        return new BinaryChromosome(24);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind A BinaryChromosome Individual, with two 12-bit chromosomes.
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        BinaryChromosome b = (BinaryChromosome)ind;
        
        double x1 = b.getBitsAsInt(0, 11) / 1000.0 - 2.048;
        double x2 = b.getBitsAsInt(12, 23) / 1000.0 - 2.048;
        
        // relies on the static fitness evaluation in DeJong02 class
        b.setFitness(DeJong02.deJong02(x1, x2));
    }
    
    /**
     * Application entry point for console-based run of BinaryDeJong02.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        Problem dj02 = new BinaryDeJong02();
        DEA alg = new DEA(dj02, 10000, 1000); // 10000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj02));

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
