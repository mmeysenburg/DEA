package edu.doane.dugal.samples.functions;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.DiversityThresholdStats;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;

/**
 * Sample DEA application to optimize Skekel's Foxholes function, 
 * 
 * f_9 = \sum_{i = 1}^m \left( \sum_{j = 1}^4 \left( x_j - C_{ij} \right)^2 + \Beta_i \right)^{-1},
 * where m = 10, and C and \Beta are as shown in the code. Each x_i is in [0, 10].
 * 
 * (This is a negation of the standard Shekel function, since the DEA framework
 * maximizes instead of minimizes.)
 * 
 * @author Mark M. Meysenburg
 * @version 03/27/2016
 */
public class ShekelsFoxHoles implements Problem {    

    private static final double[] BETA = {0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0,5};
    
    private static final double[][] C = {
        {4, 1, 8, 6, 3, 2, 5, 8, 6, 7},
        {4, 1, 8, 6, 7, 9, 3, 1, 2, 3},
        {4, 1, 8, 6, 3, 2, 5, 8, 6, 7},
        {4, 1, 8, 6, 7, 9, 3, 1, 2, 3}};
   
    /**
     * Get a random DoubleChromosome Individual representing a potential 
     * solution to Shekel's Foxholes function.
     * 
     * @return A random DoubleChromosome, with four genes, each in [0, 10].
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(4, 0, 10, 3);
    }

    /**
     * Evaluate a DoubleChromosome Individual representing a potential solution
     * to Shekel's Foxholes function. 
     * 
     * @param ind A DoubleChromosome, with four genes, each in [0, 10].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome dc = (DoubleChromosome)ind;
        double f = 0.0;
        
        for(int i = 0; i < 10; i++) {
            double s = 0.0;
            for(int j = 0; j < 4; j++) {
                double x = dc.getGene(j);
                s += (x - C[j][i]) * (x - C[j][i]);
            } // for j
            s += BETA[i];
            f += 1.0 / s;
        } // for i
        dc.setFitness(-f);
    }
    
    @Override
    public String toString() {
        return "ShekelsFoxHoles, Shekel's Foxholes fucntion with double chromosomes";
    }
    
    /**
     * Application entry point for console-based run of ShekelsFoxHoles.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // create problem and algorithm
        Problem sfh = new ShekelsFoxHoles();
        DEA alg = new DEA(sfh, 1000000, 1000); // 1000000 population, 1000 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(sfh, 1000));

        // ... then selection ...
        alg.addOperator(new ElitistTournamentSelection());

        // ... then statistics
        //StandardStats stats = new StandardStats(3);
        DiversityThresholdStats stats = new DiversityThresholdStats(3, 0.1, 0.024, sfh);
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
