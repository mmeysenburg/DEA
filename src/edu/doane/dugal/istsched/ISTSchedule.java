package edu.doane.dugal.istsched;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.general.DiversityThresholdStats;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.ichrom.IntegerChromosome;
import edu.doane.dugal.dea.kits.ichrom.PointCrossover;
import edu.doane.dugal.dea.kits.ichrom.PointMutation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author mark.meysenburg
 */
public class ISTSchedule implements Problem {

    private static final int NUM_TERMS = 10;
    private static final int COURSES_PER_TERM = 4;
        
    private Map<Integer, String> courseNames = new HashMap<>();

    private Map<String, Integer> courseIndices = new HashMap<>();

    private String[] classNames = {
        "IST 140", 
        "IST 145",
        "IST 146",
        "IST 217",
        "IST 246", 
        "IST 252",
        "IST 322", 
        "IST 3uu",
        "IST 3ww", 
        "IST 371", 
        "IST 4uu",
        "IST 4ww"
    };
    
    public ISTSchedule() {
        for (int i = 0; i < classNames.length; i++) {
            courseNames.put(i, classNames[i]);
            courseIndices.put(classNames[i], i);
        }
    }

    @Override
    public Individual createRandomIndividual() {
        return new IntegerChromosome(NUM_TERMS * COURSES_PER_TERM, 0, courseNames.size() - 1);
    }

    private boolean cir(int[] sched, int courseIndex, int start, int end) {
        for(int i = start; i < end; i++) {
            if(sched[i] == courseIndex) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void evaluateIndividual(Individual ind) {
        double fitness = 0.0;

        // place the schedule in an array for future use
        int[] sched = new int[NUM_TERMS * COURSES_PER_TERM];
        for (int i = 0; i < sched.length; i++) {
            sched[i] = ((IntegerChromosome)ind).getGene(i);
        }

        // special topics once a year?
        int x = 0, y = 0, z = courseIndices.get("IST 371");
        for (int i = 0; i < sched.length / 2; i++) {
            if (sched[i] == z) {
                x++;
            }
            if(sched[i + sched.length / 2] == z) {
                y++;
            }
        }
        if (x == 1 && y == 1) {
            fitness += 1.0;
        }

        // no duplicates in the same term?
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < COURSES_PER_TERM; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM; i < COURSES_PER_TERM * 2; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 2; i < COURSES_PER_TERM * 3; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 3; i < COURSES_PER_TERM * 4; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 4; i < COURSES_PER_TERM * 5; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 5; i < COURSES_PER_TERM * 6; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 6; i < COURSES_PER_TERM * 7; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 7; i < COURSES_PER_TERM * 8; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();
        for (int i = COURSES_PER_TERM * 8; i < sched.length; i++) {
            set.add(sched[i]);
        }
        if(set.size() == 4) {
            fitness += 1.0;
        }
        set.clear();

        // can start major in autumn or winter 2 each year?
        x = courseIndices.get("IST 140");
        y = courseIndices.get("IST 145");
        z = courseIndices.get("IST 217");
        if(cir(sched, x, 0, COURSES_PER_TERM) || cir(sched, y, 0, COURSES_PER_TERM) || cir(sched, z, 0, COURSES_PER_TERM)) {
            if(cir(sched, x, COURSES_PER_TERM * 2, COURSES_PER_TERM * 3) || cir(sched, y, COURSES_PER_TERM * 2, COURSES_PER_TERM * 3) || cir(sched, z, COURSES_PER_TERM * 2, COURSES_PER_TERM * 3)) {
                if(cir(sched, x, COURSES_PER_TERM * 5, COURSES_PER_TERM * 6) || cir(sched, y, COURSES_PER_TERM * 5, COURSES_PER_TERM * 6) || cir(sched, z, COURSES_PER_TERM * 5, COURSES_PER_TERM * 6)) {
                    if(cir(sched, x, COURSES_PER_TERM * 7, COURSES_PER_TERM * 8) || cir(sched, y, COURSES_PER_TERM * 7, COURSES_PER_TERM * 8) || cir(sched, z, COURSES_PER_TERM * 7, COURSES_PER_TERM * 8)) {
                        fitness += 1.0;
                    }
                }
            }
        }

        // 
                
        ind.setFitness(fitness);
    }
    
    public static void main(String[] args) {
        if(args.length != 7) {
            System.err.println("Usage: java -cp dist/DEA.jar edu.doane.dugal.istsched.ISTSchedule n gen chi mu k thresh keep");
            System.exit(-1);
        }
        int n = Integer.parseInt(args[0]);
        int gen = Integer.parseInt(args[1]);
        double chi = Double.parseDouble(args[2]);
        double mu = Double.parseDouble(args[3]);
        int k = Integer.parseInt(args[4]);
        double threshold = Double.parseDouble(args[5]);
        double keep = Double.parseDouble(args[6]);
        
        ISTSchedule ist_sched = new ISTSchedule();
        DEA dea = new DEA(ist_sched, n, gen);
        dea.addOperator(new PointCrossover(chi));
        dea.addOperator(new PointMutation(mu));
        dea.addOperator(new Evaluate(ist_sched, 10));
        dea.addOperator(new ElitistTournamentSelection(k));
        DiversityThresholdStats stats = 
                new DiversityThresholdStats(2, threshold, keep, ist_sched);
        dea.addOperator(stats);
        System.out.println(dea.getTableau());
        
        dea.start();
        
        try {
            dea.join();
        } catch (InterruptedException ex) {
            System.err.println("DEA interrupted");
        } finally {
            IntegerChromosome ic = (IntegerChromosome)stats.getBestEverIndividual();
            int[] sched = new int[NUM_TERMS * COURSES_PER_TERM];
            for (int i = 0; i < sched.length; i++) {
                sched[i] = ic.getGene(i);
            }

            // sort ascending each term
            Arrays.sort(sched, 0, COURSES_PER_TERM);
            Arrays.sort(sched, COURSES_PER_TERM, COURSES_PER_TERM * 2);
            Arrays.sort(sched, COURSES_PER_TERM * 2, COURSES_PER_TERM * 3);
            Arrays.sort(sched, COURSES_PER_TERM * 3, COURSES_PER_TERM * 4);
            Arrays.sort(sched, COURSES_PER_TERM * 4, COURSES_PER_TERM * 5);
            Arrays.sort(sched, COURSES_PER_TERM * 5, COURSES_PER_TERM * 6);
            Arrays.sort(sched, COURSES_PER_TERM * 6, COURSES_PER_TERM * 7);
            Arrays.sort(sched, COURSES_PER_TERM * 7, COURSES_PER_TERM * 8);
            Arrays.sort(sched, COURSES_PER_TERM * 8, sched.length);

            System.out.println("*** EVEN AUTUMN ***");
            for (int i = 0; i < COURSES_PER_TERM; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** EVEN WINTER 1 ***");
            for (int i = COURSES_PER_TERM; i < COURSES_PER_TERM * 2; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** ODD WINTER 2 ***");
            for (int i = COURSES_PER_TERM * 2; i < COURSES_PER_TERM * 3; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** ODD SPRING ***");
            for (int i = COURSES_PER_TERM * 3; i < COURSES_PER_TERM * 4; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** ODD SUMMER ***");
            for (int i = COURSES_PER_TERM * 3; i < COURSES_PER_TERM * 4; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** ODD AUTUMN ***");
            for (int i = COURSES_PER_TERM * 4; i < COURSES_PER_TERM * 5; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** ODD WINTER 1 ***");
            for (int i = COURSES_PER_TERM * 5; i < COURSES_PER_TERM * 6; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** EVEN WINTER 2 ***");
            for (int i = COURSES_PER_TERM * 6; i < COURSES_PER_TERM * 7; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** EVEN SPRING ***");
            for (int i = COURSES_PER_TERM * 7; i < COURSES_PER_TERM * 8; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
            System.out.println("\n*** EVEN SUMMER ***");
            for (int i = COURSES_PER_TERM * 8; i < COURSES_PER_TERM * 9; i++) {
                System.out.println(ist_sched.courseNames.get(sched[i]));
            }
        }
    }
    
}
