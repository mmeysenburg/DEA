package edu.doane.dugal.dea.kits.dchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;

/**
 * Class representing a fixed-length chromosome of doubles.
 *
 * @author Mark M. Meysenburg
 * @version 12/10/2014
 */
public class DoubleChromosome extends Individual {

    /**
     * Array of doubles holding the genes for this Individual's chromosome.
     */
    private double[] chromosome;

    /**
     * Lowest value possible for a gene in the chromosome.
     */
    private double lo;

    /**
     * Highest value possible for a gene in the chromosome.
     */
    private double hi;

    /**
     * Number of fraction digits to display when converting the chromosome to a
     * string.
     */
    private int fracDigits;

    /**
     * Reference to the random number generator.
     */
    private PRNG prng;

    /**
     * Default constructor. Create a shell of a DoubleChromosome.
     */
    public DoubleChromosome() {
        super();
        prng = PRNG.getInstance();
    }

    /**
     * Create a new DoubleChromosome with the specified length, range for each
     * gene, and desired number of digits to output when the individual is
     * turned into a string.
     *
     * @param length Number of genes in the chromosome
     * @param lo Low value for each gene in the chromosome
     * @param hi High value for each gene in the chromosome
     * @param fracDigits Number of fraction digits to output
     */
    public DoubleChromosome(int length, double lo, double hi, int fracDigits) {
        // initialize fitness and do other base class stuff
        super();

        prng = PRNG.getInstance();

        // remember parameters
        this.lo = lo;
        this.hi = hi;
        this.fracDigits = fracDigits;

        // create and fill chromosome
        chromosome = new double[length];
        for (int i = 0; i < chromosome.length; i++) {
            randomizeGene(i);
        }
    }

    /**
    * Copy constructor. Make this DoubleChromosome object deep-copy identical 
    * to the parameter.
    * 
    * @param other DoubleChromosome to mimic
    */    
    public DoubleChromosome(DoubleChromosome other) {
        super();
        chromosome = new double[other.chromosome.length];
        System.arraycopy(other.chromosome, 0, chromosome, 0, chromosome.length);
        fracDigits = other.fracDigits;
        hi = other.hi;
        lo = other.lo;
        prng = other.prng;
        setFitness(other.getFitness());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");

        for (int i = 0; i < chromosome.length - 1; i++) {
            s.append(String.format("%." + fracDigits + "f", chromosome[i]));
            s.append(", ");
        }

        s.append(String.format("%." + fracDigits + "f", chromosome[chromosome.length - 1]));
        s.append("]");

        return s.toString();
    }

    /**
     * Randomize one of the genes in the chromosome, to a new value in the range
     * specified by the individual's lo and hi values.
     *
     * @param gene Which gene in the chromosome to randomize.
     */
    final public void randomizeGene(int gene) {
        chromosome[gene] = prng.nextDouble(lo, hi);
    }

    /**
     * Get the length of this individual's chromosome.
     *
     * @return Number of doubles in this DoubleChromosome's array.
     */
    public int getLength() {
        return chromosome.length;
    }

    /**
     * Get the value of a certain gene in the chromosome.
     *
     * @param gene Which gene to get
     * @return Value of the specified gene
     */
    public double getGene(int gene) {
        return chromosome[gene];
    }

    /**
     * Change the value of one of the genes in the chromosome.
     *
     * @param gene Which gene to change
     * @param value New value for the gene, in [lo, hi]
     * @throws IllegalArgumentException If value is not in [lo, hi]
     */
    public void setGene(int gene, double value) throws IllegalArgumentException {
        if (value < lo || value > hi) {
            throw new IllegalArgumentException("Illegal value: " + value);
        }

        chromosome[gene] = value;
    }
    
    @Override
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }
    
    @Override
    public int compareTo(Object o) {
        DoubleChromosome io = (DoubleChromosome)o;
        
        if(io.chromosome.length != chromosome.length) {
            return chromosome.length - io.chromosome.length;
        }
        
        for(int i = 0; i < chromosome.length; i++) {
            if(chromosome[i] < io.chromosome[i]) {
                return -1;
            } else {
                if(chromosome[i] > io.chromosome[i]) {
                    return 1; 
                }
            }
        }
        
        return 0;
    }
}
