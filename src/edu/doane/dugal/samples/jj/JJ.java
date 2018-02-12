package edu.doane.dugal.samples.jj;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.ichrom.PointCrossover;
import edu.doane.dugal.dea.kits.ichrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.Scrambler;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.general.TournamentSelection;
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
        return new IntegerChromosome(LENGTH, -3000, 3000);
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
     * @param args Command line arguments: n gen chi mu k, where n is population 
     * size, gen is maximum number of generations, chi is probability of 
     * crossover, mu is probability of mutation, and k is tournament size.
     */
    public static void main(String[] args) {
        if(args.length != 5) {
            System.err.println("Usage: java -cp dist/DEA.jar edu.doane.dugal.samples.jj.JJ n gen chi mu k");
            System.exit(-1);
        }
        int n = Integer.parseInt(args[0]);
        int gen = Integer.parseInt(args[1]);
        double chi = Double.parseDouble(args[2]);
        double mu = Double.parseDouble(args[3]);
        int k = Integer.parseInt(args[4]);
        
        JJ jj = new JJ();
        DEA dea = new DEA(jj, n, gen);
        dea.addOperator(new PointCrossover(chi));
        dea.addOperator(new PointMutation(mu));
        StandardStats stats = new StandardStats(2);
        dea.addOperator(new Scrambler(0.05, jj, stats, 0.05));
        dea.addOperator(new Evaluate(jj, 10));
        dea.addOperator(new TournamentSelection(k));
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