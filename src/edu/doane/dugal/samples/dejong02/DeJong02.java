package edu.doane.dugal.samples.dejong02;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.samples.dejong01.DeJong01;

/**
 * Sample DEA application: DeJong's second function, using double chromosomes.
 * Maximize the function
 *
 * f_2 = 100 \left( x_1^2 - x_2 \right)^2 + \left( 1 - x_1 \right)^2
 *
 * @author Mark M. Meysenburg
 * @version 03/25/2016
 */
public class DeJong02 implements Problem {

    /**
     * Create a randomized potential solution to the problem, consisting
     * of two doubles in the range [-2.048, 2.048].
     * 
     * @return A new DoubleChromosome individual with two values in the
     * range [-2.048, 2.048].
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(2, -2.048, 2.048, 3);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind A DoubleChromosome Individual, with two values in the
     * range [-2.048, 2.048].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome d = (DoubleChromosome)ind;
        d.setFitness(deJong02(d.getGene(0), d.getGene(1)));        
    }
    
    /**
     * Calculate the value of DeJong's second function for a 
     * particular set of values.
     * 
     * @param x1
     * @param x2
     * 
     * @return Value of DeJong's second function at (x1, x2).
     */
    public static double deJong02(double x1, double x2) {
        return 100 * Math.pow(x1 * x1 - x2, 2.0) + 
                Math.pow(1 - x1, 2.0);
    }
    
    @Override
    public String toString() {
        return "DeJong's second function (DeJong02)";
    }
    
    /**
     * Application entry point for console-based run of DeJong02.
     * 
     * @param args Command-line arguments; ignored by this app. 
     */
    public static void main(String[] args) {
        // code to brute force the max value, 3905.9262268415996
//        double max = Double.NEGATIVE_INFINITY;
//        for(double x1 = -2.048; x1 <= 2.048; x1 += 0.0005) {
//            for(double x2 = -2.048; x2 <= 2.048; x2 += 0.0005) {
//                double d = DeJong02.deJong02(x1, x2);
//                if(d > max) {
//                    max = d;
//                }
//            }
//        }
//        System.out.println(max);
//        System.exit(0);
        
        // to run with a specific seed, set it before doing anything else;
        // otherwise, the first access to the PRNG object will cause it to
        // be seeded based on the system time.
        // PRNG p = PRNG.getInstance();
        // p.setSeed(1209432115);
        // create problem and algorithm
        Problem dj02 = new DeJong02();
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
