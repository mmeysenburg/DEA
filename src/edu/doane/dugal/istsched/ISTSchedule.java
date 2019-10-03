package edu.doane.dugal.istsched;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.ichrom.IntegerChromosome;
import edu.doane.dugal.dea.kits.ichrom.PointCrossover;
import edu.doane.dugal.dea.kits.ichrom.PointMutation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mark.meysenburg
 */
public class ISTSchedule implements Problem {
    
    private static final int NUMBER_OF_COURSES = 16;
    private static final int EVEN_FALL_COURSES = 4;
    private static final int ODD_SPRING_COURSES = 6;
    private static final int ODD_FALL_COURSES = 4;
    private static final int EVEN_SPRING_COURSES = 6;
    private static final int EF_START = 0;
    private static final int EF_END = EF_START + EVEN_FALL_COURSES;
    private static final int OS_START = EF_END;
    private static final int OS_END = OS_START + ODD_SPRING_COURSES;
    private static final int OF_START = OS_END;
    private static final int OF_END = OF_START + ODD_FALL_COURSES;
    private static final int ES_START = OF_END;
    private static final int ES_END = ES_START + EVEN_SPRING_COURSES;
    private static final int IST140 = 0, 
            IST145 = 1, 
            IST146 = 2, 
            IST217 = 3,
            IST246 = 4,
            IST252 = 5,
            IST371A = 6,
            IST371M = 7,
            IST310 = 8,
            IST314 = 9,
            IST315 = 10,
            IST322 = 11,
            IST3UU = 12,
            IST4UU = 13,
            IST3WW = 14,
            IST4WW = 15;
    
    private Map<Integer, String> courses = new HashMap<>();
    
    public ISTSchedule() {
        courses.put(IST140, "IST 140");
        courses.put(IST145, "IST 145");
        courses.put(IST146, "IST 146");
        courses.put(IST217, "IST 217");
        courses.put(IST246, "IST 246");
        courses.put(IST252, "IST 252");
        courses.put(IST371A, "IST 371A");
        courses.put(IST371M, "IST 371M");
        courses.put(IST310, "IST 310");
        courses.put(IST314, "IST 314");
        courses.put(IST315, "IST 315");
        courses.put(IST322, "IST 322");
        courses.put(IST3UU, "IST 3UU");
        courses.put(IST4UU, "IST 4UU");
        courses.put(IST3WW, "IST 3WW");
        courses.put(IST4WW, "IST 4WW");
    }

    @Override
    public Individual createRandomIndividual() {
        return new IntegerChromosome(
                EVEN_FALL_COURSES + ODD_SPRING_COURSES + 
                        ODD_FALL_COURSES + EVEN_SPRING_COURSES, 
                0, NUMBER_OF_COURSES - 1);
    }
    
    private int ci(int[] schedule, int i0, int i1, int k) {
        int count = 0;
        
        for(int i = i0; i < i1; i++) {
            if(schedule[i] == k) {
                count++;
            }
        }
        
        return count;
    }

    @Override
    public void evaluateIndividual(Individual ind) {
        double fitness = 0.0;
        IntegerChromosome icInd = (IntegerChromosome)ind;
        
        int[] schedule = new int[EVEN_FALL_COURSES + ODD_SPRING_COURSES + 
                        ODD_FALL_COURSES + EVEN_SPRING_COURSES];        
        
        for(int i = 0; i < schedule.length; i++) {
            schedule[i] = icInd.getGene(i);
        }
        
        // ist 140 every fall?
        if(ci(schedule,EF_START, EF_END, IST140) == 1 &&
                ci(schedule,OF_START, OF_END, IST140) == 1 && 
                ci(schedule,OS_START, OS_END, IST140) == 0 &&
                ci(schedule,ES_START, ES_END, IST140) == 0) {
            fitness += 1.0;
        }
        
        // ist 145 every semester?
        if(ci(schedule,EF_START, EF_END, IST145) == 1 &&
                ci(schedule,OF_START, OF_END, IST145) == 1 && 
                ci(schedule,OS_START, OS_END, IST145) == 1 &&
                ci(schedule,ES_START, ES_END, IST145) == 1) {
            fitness += 1.0;
        }
        
        // ist 146 every spring?
        if(ci(schedule,EF_START, EF_END, IST146) == 0 &&
                ci(schedule,OF_START, OF_END, IST146) == 0 && 
                ci(schedule,OS_START, OS_END, IST146) == 1 &&
                ci(schedule,ES_START, ES_END, IST146) == 1) {
            fitness += 1.0;
        }
        
        // ist 246 every other fall?
        if((ci(schedule,EF_START, EF_END, IST246) == 1 &&
                ci(schedule,OF_START, OF_END, IST246) == 0 && 
                ci(schedule,OS_START, OS_END, IST246) == 0 &&
                ci(schedule,ES_START, ES_END, IST246) == 0) ||
                (ci(schedule,EF_START, EF_END, IST246) == 0 &&
                ci(schedule,OF_START, OF_END, IST246) == 1 && 
                ci(schedule,OS_START, OS_END, IST246) == 0 &&
                ci(schedule,ES_START, ES_END, IST246) == 0)) {
            fitness += 1.0;
        }
        
        // ist 252 every fall?
        if(ci(schedule,EF_START, EF_END, IST252) == 1 &&
                ci(schedule,OF_START, OF_END, IST252) == 1 && 
                ci(schedule,OS_START, OS_END, IST252) == 0 &&
                ci(schedule,ES_START, ES_END, IST252) == 0) {
            fitness += 1.0;
        }
        
        // special topics each spring?
        if(ci(schedule, 0, schedule.length, IST371A) == 1 &&
                ci(schedule, 0, schedule.length, IST371M) == 1) {
            if(ci(schedule, OS_START, OS_END, IST371A) == 1 ||
                    ci(schedule, OS_START, OS_END, IST371M) == 1){
                if(ci(schedule, ES_START, ES_END, IST371A) == 1 ||
                        ci(schedule, ES_START, ES_END, IST371M) == 1) {
                    fitness += 1.0;
                }
            }
            
        }
        
        // ist 322 in alternating fall?
        if((ci(schedule,EF_START, EF_END, IST322) == 1 &&
                ci(schedule,OF_START, OF_END, IST322) == 0 && 
                ci(schedule,OS_START, OS_END, IST322) == 0 &&
                ci(schedule,ES_START, ES_END, IST322) == 0) ||
                (ci(schedule,EF_START, EF_END, IST322) == 0 &&
                ci(schedule,OF_START, OF_END, IST322) == 1 && 
                ci(schedule,OS_START, OS_END, IST322) == 0 &&
                ci(schedule,ES_START, ES_END, IST322) == 0)) {
            fitness += 1.0;
        }
        
        // every course taught? 
        boolean[] taught = new boolean[NUMBER_OF_COURSES];
        for(int i = 0; i < schedule.length; i++) {
            taught[schedule[i]] = true;
        }
        for(int i = 0; i < taught.length; i++) {
            if(taught[i] == false) {
                fitness -= 10.0;
            }
        }
                
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
//        DiversityThresholdStats stats = 
//                new DiversityThresholdStats(2, threshold, keep, ist_sched);
        StandardStats stats = new StandardStats(2);
        dea.addOperator(stats);
        System.out.println(dea.getTableau());
        
        dea.start();
        
        try {
            dea.join();
        } catch (InterruptedException ex) {
            System.err.println("DEA interrupted");
        } finally {
            IntegerChromosome ic = (IntegerChromosome)stats.getBestEverIndividual();

            String[] evenFall = new String[EVEN_FALL_COURSES];
            evenFall[0] = ist_sched.courses.get(ic.getGene(0));
            evenFall[1] = ist_sched.courses.get(ic.getGene(1));
            evenFall[2] = ist_sched.courses.get(ic.getGene(2));
            evenFall[3] = ist_sched.courses.get(ic.getGene(3));
            Arrays.sort(evenFall);
            
            String[] oddSpring = new String[ODD_SPRING_COURSES];
            oddSpring[0] = ist_sched.courses.get(ic.getGene(4));
            oddSpring[1] = ist_sched.courses.get(ic.getGene(5));
            oddSpring[2] = ist_sched.courses.get(ic.getGene(6));
            oddSpring[3] = ist_sched.courses.get(ic.getGene(7));
            oddSpring[4] = ist_sched.courses.get(ic.getGene(8));
            oddSpring[5] = ist_sched.courses.get(ic.getGene(9));
            Arrays.sort(oddSpring);
            
            String[] oddFall = new String[ODD_FALL_COURSES];
            oddFall[0] = ist_sched.courses.get(ic.getGene(10));
            oddFall[1] = ist_sched.courses.get(ic.getGene(11));
            oddFall[2] = ist_sched.courses.get(ic.getGene(12));
            oddFall[3] = ist_sched.courses.get(ic.getGene(13));
            Arrays.sort(oddFall);
            
            String[] evenSpring = new String[EVEN_SPRING_COURSES];
            evenSpring[0] = ist_sched.courses.get(ic.getGene(14));
            evenSpring[1] = ist_sched.courses.get(ic.getGene(15));
            evenSpring[2] = ist_sched.courses.get(ic.getGene(16));
            evenSpring[3] = ist_sched.courses.get(ic.getGene(17));
            evenSpring[4] = ist_sched.courses.get(ic.getGene(18));
            evenSpring[5] = ist_sched.courses.get(ic.getGene(19));
            Arrays.sort(evenSpring);
            
            System.out.println("Even fall:\t\t" + Arrays.toString(evenFall));
            System.out.println("Odd spring:\t\t" + Arrays.toString(oddSpring));
            System.out.println("Odd fall:\t\t" + Arrays.toString(oddFall));
            System.out.println("Even spring:\t\t" + Arrays.toString(evenSpring));
        }
    }
    
}
