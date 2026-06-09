package ams.cms.utility;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomStringGenerator 
{
	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUM = UPPER + LOWER + DIGITS;

    private final char[] symbols;
    private final char[] buf;
	
    private final Random random;
    
    public String nextString() 
    {
        for (int idx = 0; idx < buf.length; ++idx) 
        {
        	buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }   

    public RandomStringGenerator(int length, Random random, String symbols) 
    {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomStringGenerator(int length, Random random) {
        this(length, random, ALPHANUM);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomStringGenerator(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomStringGenerator() {
        this(21);
    }

}
