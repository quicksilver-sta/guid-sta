package uk.ac.standrews.cs.impl.keys;

import uk.ac.standrews.cs.IGUID;
import uk.ac.standrews.cs.IKey;
import uk.ac.standrews.cs.IPID;
import uk.ac.standrews.cs.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.impl.RadixMethods;

import java.math.BigInteger;

/**
 * Implementation of key.
 * 
 * @author stuart, al, graham, sja7
 * @author sic2 - removed p2p dependencies
 */
public class KeyImpl implements IGUID, IPID {

    private static final int KEYLENGTH = 160;
    private static final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);

    private static final int DEFAULT_TO_STRING_RADIX = 16; // The radix used in converting the key's value to a string.
    private static final int DEFAULT_TO_STRING_LENGTH = 40; // The length of the key's value in digits.

    public static BigInteger KEYSPACE_SIZE;
    private BigInteger key_value;

    /**
     * Default constructor - initialises the keyspace
     */
    private KeyImpl () {
        KEYSPACE_SIZE = TWO.pow(getKeylength());
    }

    /**
     * Creates a new key using the given value modulo the key space size.
     * 
     * @param key_value the value of the key
     */
    public KeyImpl(BigInteger key_value) throws GUIDGenerationException {
        this();

        if (key_value == null) {
            throw new GUIDGenerationException();
        }

        try {
            this.key_value = key_value.remainder(KEYSPACE_SIZE);

            // Allow for negative key value.
            if (this.key_value.compareTo(BigInteger.ZERO) < 0)
                this.key_value = this.key_value.add(KEYSPACE_SIZE);

        } catch (NumberFormatException e) {
            throw new GUIDGenerationException();
        }
    }

    /**
     * Creates a new key using a string representation of a BigInteger to base DEFAULT_TO_STRING_RADIX.
     *
     * @param string the string value of the key
     * @see #DEFAULT_TO_STRING_RADIX
     */
    public KeyImpl(String string) throws GUIDGenerationException {
        this(new BigInteger(string, DEFAULT_TO_STRING_RADIX));
    }

    /**
     * Returns the representation of this key.
     *
     * @return the representation of this key
     */
    public BigInteger bigIntegerRepresentation() {
        return key_value;
    }

    /**
     * Returns a string representation of the key value.
     *
     * @return a string representation of the key value using the default radix and length
     */
    public String toString() {
        return toString(DEFAULT_TO_STRING_RADIX, getStringLength());
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    /**
     * Returns a string representation of the key value.
     *
     * @param radix the radix
     * @return a string representation of the key value using the given radix
     */
    public String toString(int radix) {
        int bits_per_digit = RadixMethods.bitsNeededTORepresent(radix);
        int toStringLength = getKeylength() / bits_per_digit;

        return toString(radix, toStringLength);
    }

    /**
     * Returns a string representation of the key value.
     *
     * @param radix the radix
     * @param stringLength the length to which the key representation should be padded
     * @return a string representation of the key value using the given radix
     */
    public String toString(int radix, int stringLength) {
        StringBuffer result = new StringBuffer(key_value.toString(radix));
        while (result.length() < stringLength) result.insert(0, '0');
        return result.toString();
    }

    /**
     * Compares this key with another.
     *
     * @param o the key to compare
     * @return -1, 0, or 1 if the argument key is greater, equal to, or less
     *         than this node, respectively
     */
    public int compareTo(Object o) {
        try {
            IKey k = (IKey) o;
            return key_value.compareTo(k.bigIntegerRepresentation());
        } catch (ClassCastException e) {
            return 0;
        }
    }

    /**
     * Compares this key with another.
     *
     * @param o the key to compare
     * @return true if the argument key's representation is equal to that of this node
     */
    public boolean equals(Object o) {
        try {
            IKey k = (IKey) o;
            return key_value.equals(k.bigIntegerRepresentation());
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode(){
        return toString().hashCode();
    }

    protected int getKeylength() {
        return KEYLENGTH;
    }

    protected int getStringLength() {
        return DEFAULT_TO_STRING_LENGTH;
    }
}