package edu.doane.dugal.samples.jj;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.general.DiversityThresholdStats;
import edu.doane.dugal.dea.kits.ichrom.PointCrossover;
import edu.doane.dugal.dea.kits.ichrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.ichrom.IntegerChromosome;
import java.util.Set;
import java.util.TreeSet;

/**
 * DEA program to evolve Jolly Jumpers sequences.
 * 
 * @author Mark M. Meysenburg
 * @version 02/09/2018
 */
public class JJ implements Problem {
    
    private static final int LENGTH = 3000;

    @Override
    public Individual createRandomIndividual() {
        return new IntegerChromosome(LENGTH, -10000, 10000);
    }

    @Override
    public void evaluateIndividual(Individual ind) {
        IntegerChromosome ic = (IntegerChromosome)ind;
        Set<Integer> set = new TreeSet<>();
        
        for(int i = 1; i < LENGTH; i++) {
            set.add(i);
        }
        
        for(int i = 0; i < LENGTH - 1; i++) {
            int a = ic.getGene(i);
            int b = ic.getGene(i + 1);
            int d = a - b;
            if(d < 0) d = -d;
            set.remove(d);
        }
        ic.setFitness(-set.size());
    }
    
    @Override
    public String toString() {
        return "JJ (Jolly Jumpers)";
    }
    
    /**
     * Application entry point. 
     * 
     * @param args Command line arguments: n gen chi mu k thresh keep, where n 
     * is population size, gen is maximum number of generations, chi is 
     * probability of crossover, mu is probability of mutation, k is tournament 
     * size, thresh is diversity threshold, and keep is percentage of population
     * to fill with best ever individual after re-seeding.
     */
    public static void main(String[] args) {
        if(args.length != 7) {
            System.err.println("Usage: java -cp dist/DEA.jar edu.doane.dugal.samples.jj.JJ n gen chi mu k thresh keep");
            System.exit(-1);
        }
        int n = Integer.parseInt(args[0]);
        int gen = Integer.parseInt(args[1]);
        double chi = Double.parseDouble(args[2]);
        double mu = Double.parseDouble(args[3]);
        int k = Integer.parseInt(args[4]);
        double threshold = Double.parseDouble(args[5]);
        double keep = Double.parseDouble(args[6]);
        
        JJ jj = new JJ();
        DEA dea = new DEA(jj, n, gen);
        dea.addOperator(new PointCrossover(chi));
        dea.addOperator(new PointMutation(mu));
        dea.addOperator(new Evaluate(jj, 10));
        dea.addOperator(new ElitistTournamentSelection(k));
        DiversityThresholdStats stats = 
                new DiversityThresholdStats(2, threshold, keep, jj);
        dea.addOperator(stats);
        System.out.println(dea.getTableau());
        
        dea.start();
        
        try {
            dea.join();
        } catch (InterruptedException ex) {
            System.err.println("DEA interrupted");
        } finally {
            System.out.println(stats.getBestEverIndividual().toString());
        }
    }
    
}
