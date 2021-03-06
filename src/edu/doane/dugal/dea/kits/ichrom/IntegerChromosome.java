package edu.doane.dugal.dea.kits.ichrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;

/**
 * Class representing a fixed-length chromosome of integers.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class IntegerChromosome extends Individual {

    /**
     * Array of integers representing the chromosome for this individual.
     */
    int[] chromosome;

    /**
     * Low end of legal range of values for a gene in the chromosome.
     */
    int lo;

    /**
     * High end of legal range of values for a game in the chromosome.
     */
    int hi;

    /**
     * Reference to the random number generator used by this individual.
     */
    private PRNG prng;

    /**
     * Default constructor. Create a shell of an IntegerChromosome.
     */
    public IntegerChromosome() {
        super();
        prng = PRNG.getInstance();
    }

    /**
     * Initializing constructor. Create a new IntegerChromosome of the specified
     * length, with each gene randomly initialized to be in the range [lo, hi].
     *
     * @param length Number of integers in the chromosome.
     * @param lo Low end of legal range of gene values in the chromosome.
     * @param hi High end of legal range of gene values in the chromosome.
     */
    public IntegerChromosome(int length, int lo, int hi) {
        super();

        this.lo = lo;
        this.hi = hi;

        prng = PRNG.getInstance();

        chromosome = new int[length];

        for (int i = 0; i < chromosome.length; i++) {
            randomizeGene(i);
        }
    }
    
    /**
     * Copy constructor. Make this IntegerChromosome deep-copy identical to the
     * parameter.
     * 
     * @param ind IntegerChromosome object to mimic
     */
    public IntegerChromosome(IntegerChromosome ind) {
        super();
        chromosome = new int[ind.chromosome.length];
        System.arraycopy(ind.chromosome, 0, chromosome, 0, chromosome.length);
        hi = ind.hi;
        lo = ind.lo;
        prng = ind.prng;
        setFitness(ind.getFitness());
    }

    /**
     * Randomly initialize one of the genes in the chromosome to be in the range
     * [lo, hi].
     *
     * @param gene Which gene in the chromosome to randomize.
     */
    final public void randomizeGene(int gene) {
        chromosome[gene] = prng.nextInt(lo, hi);
    }

    /**
     * Get the value of one gene in the chromosome.
     *
     * @param gene Which gene to get.
     * @return value of the specified gene.
     */
    public int getGene(int gene) {
        return chromosome[gene];
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");

        for (int i = 0; i < chromosome.length - 1; i++) {
            s.append(chromosome[i]).append(", ");
        }

        s.append(chromosome[chromosome.length - 1]).append("]");

        return s.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    @Override
    public int compareTo(Object o) {
        IntegerChromosome io = (IntegerChromosome)o;
        
        if(io.chromosome.length != chromosome.length) {
            return chromosome.length - io.chromosome.length;
        }
        
        for(int i = 0; i < chromosome.length; i++) {
            if(chromosome[i] < io.chromosome[i]) {
                return -1;
            } else if(chromosome[i] > io.chromosome[i]) {
                return 1; 
            }
        }
        
        return 0;
    }
}
