package edu.doane.dugal.samples.bugbomb;

import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.kits.ichrom.IntegerChromosome;
import edu.doane.dugal.dea.kits.ichrom.PointCrossover;
import edu.doane.dugal.dea.kits.ichrom.PointMutation;

/**
 * This class represents the bug bomb problem based on New Light Industry's
 * Generator GA software demo spreadsheet. There are 12 wasp nests in an attic,
 * and we have three bug bombs to kill 'em; the GA chooses where to place the
 * bombs for maximum carnage! Each bomb has a radius of 15; every wasp within
 * that radius is killed.
 * 
 * Maximum number of wasps killed: 4241, with three bombs at 
 * [(76, 1), (22, 14), (53, 82)], and [(69, 4), (22, 14), (53, 82)], 
 * [(92, 3), (74, 86), (35, 10)], and others.
 *
 * @author Mark M. Meysenburg
 * @version 03/06/2004
 */
public class BugBomb implements Problem {

    /**
     * Populations of the 12 wasp nests.
     */
    public static final int[] waspPops = {100, 200, 327, 440, 450, 639, 650, 678, 750, 801, 945, 967};

    /**
     * (x, y) coordinates of the 12 wasp nests.
     */
    public static final int[][] waspLocs = {{25, 65}, {23, 8}, {7, 13}, {95, 53}, {3, 3}, {54, 56},
    {67, 78}, {32, 4}, {24, 76}, {66, 89}, {84, 4}, {34, 23}};

    /**
     * Create a random potential solution to the problem.
     *
     * @return An IntegerChromosome Individual, with six genes, each in [1,
     * 100].
     *
     */
    @Override
    public Individual createRandomIndividual() {
        return new IntegerChromosome(6, 1, 100);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind An IntegerChromosome Individual, with six genes, each in [1,
     * 100].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        // make an array to keep track of which nests are dead
        boolean[] isDead = new boolean[waspPops.length];
        for (int i = 0; i < isDead.length; i++) {
            isDead[i] = false;
        }

        IntegerChromosome iInd = (IntegerChromosome) ind;
        double fitness = 0.0;

        // look at each bomb...
        for (int i = 0; i < 3; i++) {
            int bx = iInd.getGene(i * 2);
            int by = iInd.getGene(i * 2 + 1);

            // ... and see how many wasps are within 15 units of the bomb
            for (int j = 0; j < waspLocs.length; j++) {
                // only bother if the nest hasn't been killed yet
                if (!isDead[j]) {
                    int wx = waspLocs[j][0];
                    int wy = waspLocs[j][1];

                    // if the distance is close enough, that many wasps are
                    // dead; make sure we don't double count
                    double d = Math.sqrt((bx - wx) * (bx - wx) + (by - wy) * (by - wy));
                    if (d <= 15) {
                        fitness += waspPops[j];
                        // mark the nest as dead
                        isDead[j] = true;
                    }

                }
            }
        }

        ind.setFitness(fitness);
    }

    @Override
    public String toString() {
        return "Bug Bomb";
    }

    /**
     * Application entry point for console-based execution.
     *
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {

        // to run with a specific seed, set it before doing anything else;
        // otherwise, the first access to the PRNG object will cause it to
        // be seeded based on the system time.
        // PRNG p = PRNG.getInstance();
        // p.setSeed(1209432115);
        // create problem and algorithm
        Problem prob = new BugBomb();
        DEA alg = new DEA(prob, 100, 100); // 100 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(prob, 10));

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
            System.out.printf("Best ever fitness: %.2f\n", stats.getBestEverIndividual().getFitness());
        }

    }

}
