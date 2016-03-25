package edu.doane.dugal.dea.kits.bchrom;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.PRNG;
import java.util.BitSet;

/**
 * Class representing a binary chromosome individual.
 *
 * @author Mark M. Meysenburg
 * @version 12/30/2014
 */
public class BinaryChromosome extends Individual {

    /**
     * Number of bits in the chromosome.
     */
    private int length;

    /**
     * Set of bits representing this individual.
     */
    BitSet chromosome;

    /**
     * Random number generator used by this individual.
     */
    private PRNG prng;

    /**
     * Default constructor. Create a shell of a BinaryChromosome individual.
     */
    public BinaryChromosome() {
        prng = PRNG.getInstance();
    }

    /**
     * Initializing constructor. Create a BinaryChromosome individual of the
     * specified size, with all bits initialized randomly.
     *
     * @param length Number of bits in the individual.
     */
    public BinaryChromosome(int length) {

        this.length = length;

        prng = PRNG.getInstance();

        chromosome = new BitSet(length);
        for (int i = 0; i < length; i++) {
            chromosome.set(i, prng.nextDouble() < 0.5);
        }
    }

    /**
     * Get the value of a range of bits in the chromosome, as an int.
     *
     * @param lo index of starting bit in the range.
     * @param hi index of ending bit in the range.
     * @return Integer value of the bits in the range [lo, hi]
     * @throws IllegalArgumentException if hi is less than or equal to lo.
     */
    public int getBitsAsInt(int lo, int hi) throws IllegalArgumentException {
        if (hi <= lo) {
            throw new IllegalArgumentException("Bad range to getBitsAsInt: " + lo + ", " + hi);
        }

        String sBits = toString(lo, hi);
        return Integer.parseInt(sBits, 2);
    }

    /**
     * The the value of a specified bit.
     *
     * @param bit Bit to get
     * @return Value of the bit
     */
    public boolean getBit(int bit) {
        return chromosome.get(bit);
    }

    /**
     * Set the specified bit to a specified value.
     *
     * @param bit Which bit to set.
     * @param value Value to set it to.
     */
    public void setBit(int bit, boolean value) {
        chromosome.set(bit, value);
    }

    /**
     * Get the length of this individual's chromosome.
     *
     * @return Number of bits in the chromosome.
     */
    public int getLength() {
        return length;
    }

    /**
     * Helper method to get some subset of the bits as a string.
     *
     * @param lo lo bit of range to fetch
     * @param hi high bit of range to fetch
     * @return String representation of bits in the range [lo, hi]
     */
    public String toString(int lo, int hi) {
        StringBuilder sb = new StringBuilder();
        for (int i = lo; i <= hi; i++) {
            if (chromosome.get(i)) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(0, length - 1);
    }

    /**
     * Return a deep copy of this BinaryChromosome, as an Individual reference.
     *
     * @return Reference to a new BinaryChromosome just like this one.
     */
    @Override
    public Individual copy() {
        BinaryChromosome b = new BinaryChromosome();
        b.setFitness(getFitness());
        b.length = length;
        b.prng = prng;
        
        b.chromosome = new BitSet(b.length);

        for (int i = 0; i < length; i++) {
            b.chromosome.set(i, chromosome.get(i));
        }

        return b;
    }
}
