package edu.doane.dugal.samples.bugbomb;

/**
 * Class to use a brute-force approach to determine the best solution for the
 * bug bomb problem.
 *
 * @author Mark M. Meysenburg
 * @version 04/14/2016
 */
public class BugBombMaximizer {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {
        // make an array to keep track of which nests are dead
        boolean[] isDead = new boolean[BugBomb.waspPops.length];
        for (int i = 0; i < isDead.length; i++) {
            isDead[i] = false;
        }

        double bestFitness = Double.MIN_VALUE;

        for (int bx1 = 1; bx1 <= 100; bx1++) {
            for (int bx2 = 1; bx2 <= 100; bx2++) {
                for (int bx3 = 1; bx3 <= 100; bx3++) {
                    for (int by1 = 1; by1 <= 100; by1++) {
                        for (int by2 = 1; by2 <= 100; by2++) {
                            for (int by3 = 1; by3 <= 100; by3++) {
                                for(int i = 0; i < isDead.length; i++) {
                                    isDead[i] = false;
                                }
                                
                                double fitness = 0.0;
                                // ... and see how many wasps are within 15 units of the bomb
                                for (int j = 0; j < BugBomb.waspLocs.length; j++) {
                                    // only bother if the nest hasn't been killed yet
                                    if (!isDead[j]) {
                                        int wx = BugBomb.waspLocs[j][0];
                                        int wy = BugBomb.waspLocs[j][1];

                                        // if the distance is close enough, that many wasps are
                                        // dead; make sure we don't double count
                                        double d = Math.sqrt((bx1 - wx) * (bx1 - wx) + (by1 - wy) * (by1 - wy));
                                        if (d <= 15) {
                                            fitness += BugBomb.waspPops[j];
                                            // mark the nest as dead
                                            isDead[j] = true;
                                        } // if close enough
                                        
                                        d = Math.sqrt((bx2 - wx) * (bx2 - wx) + (by2 - wy) * (by2 - wy));
                                        if(d <= 15 && !isDead[j]) {
                                            fitness += BugBomb.waspPops[j];
                                            isDead[j] = true;
                                        }

                                        
                                        d = Math.sqrt((bx3 - wx) * (bx3 - wx) + (by3 - wy) * (by3 - wy));
                                        if(d <= 15 && !isDead[j]) {
                                            fitness += BugBomb.waspPops[j];
                                            isDead[j] = true;
                                        }
                                    } // if not dead
                                } // for j
                                if(fitness > bestFitness) {
                                    bestFitness = fitness;
                                    System.out.printf("[(%d, %d), (%d, %d), (%d, %d)]: %f\n",
                                            bx1, by1, bx2, by2, bx3, by3, bestFitness);
                                }
                            } // for by3
                        } // for by2
                    } // for by1
                } // for bx3
            } // for bx2
        } // for bx1
        
        System.out.println("Best fitness: " + bestFitness);
    }
}
