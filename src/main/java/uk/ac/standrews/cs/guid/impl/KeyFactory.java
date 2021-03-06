/*
 * Created on 26-Oct-2004
 */
package uk.ac.standrews.cs.guid.impl;

import org.apache.commons.codec.digest.DigestUtils;
import uk.ac.standrews.cs.guid.ALGORITHM;
import uk.ac.standrews.cs.guid.IKey;
import uk.ac.standrews.cs.guid.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.guid.impl.keys.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;

import static uk.ac.standrews.cs.guid.ALGORITHM.SHA256;

/**
 * Provides various ways to generate uk.ac.standrews.cs.impl.
 *
 * SHA256 is the default algorithm
 *
 * @author stuart, graham, sic2
 */
public class KeyFactory {

    /**
     * Creates a key with an arbitrary value.
     *
     * @return a key with an arbitrary value
     */
    public static IKey generateKey() throws GUIDGenerationException {
        return generateKey(SHA256, "null");
    }

    public static IKey generateKey(String string) throws GUIDGenerationException {
        return generateKey(SHA256, string);
    }

    /**
     * Creates a key with a value generated from the given string.
     *
     * @param string the string from which to generate the key's value
     * @return a key with a value generated from s
     */
    public static IKey generateKey(ALGORITHM algorithm, String string) throws GUIDGenerationException {
        if (string == null || string.isEmpty()) {
            throw new GUIDGenerationException();
        }

        return generateKey(algorithm, string.getBytes(Charset.forName("UTF-8")));
    }

    public static IKey generateKey(InputStream source) throws GUIDGenerationException {
        return generateKey(SHA256, source);
    }

    /**
     * Creates a key with a value generated from the given input stream.
     *
     * @param source
     * @return
     * @throws GUIDGenerationException
     */
    public static IKey generateKey(ALGORITHM algorithm, InputStream source) throws GUIDGenerationException {
        if (source == null) {
            throw new GUIDGenerationException();
        }

        try {
            return hash(algorithm, source);
        } catch (IOException e) {
            throw new GUIDGenerationException("IOException while generating GUID");
        }
    }

    public static IKey generateKey(byte[] bytes) throws GUIDGenerationException {
        return generateKey(SHA256, bytes);
    }

    public static IKey generateKey(ALGORITHM algorithm, byte[] bytes) throws GUIDGenerationException {
        if (bytes == null || bytes.length == 0) {
            throw new GUIDGenerationException();
        }

        return hash(algorithm, bytes);
    }

    public static IKey recreateKey(byte[] string) throws GUIDGenerationException {
        return recreateKey(SHA256, string);
    }

    public static IKey recreateKey(ALGORITHM algorithm, byte[] string) throws GUIDGenerationException {

        switch(algorithm) {
            case SHA1:
                return new SHA1Key(string);
            case SHA256:
                return new SHA256Key(string);
            case SHA384:
                return new SHA384Key(string);
            case SHA512:
                return new SHA512Key(string);
            case MD5:
                return new MD5Key(string);
            default:
                throw new GUIDGenerationException("Unsupported sha algorithm: " + algorithm);
        }
    }

    public static IKey generateRandomKey() throws GUIDGenerationException {
        Random rand = new SecureRandom();
        long seed = rand.nextLong();
        return generateKey(SHA256, String.valueOf(seed));
    }

    /**
     * Creates a key with a pseudo-random value.
     *
     * @return a key with a pseudo-random value
     */
    public static IKey generateRandomKey(ALGORITHM algorithm) throws GUIDGenerationException {
        Random rand = new SecureRandom();
        long seed = rand.nextLong();
        return generateKey(algorithm, String.valueOf(seed));
    }

    private static IKey hash(ALGORITHM algorithm, byte[] source) throws GUIDGenerationException {

        byte[] bytes;
        switch(algorithm) {
            case SHA1:
                bytes = DigestUtils.sha1(source);
                return new SHA1Key(bytes);
            case SHA256:
                bytes = DigestUtils.sha256(source);
                return new SHA256Key(bytes);
            case SHA384:
                bytes = DigestUtils.sha384(source);
                return new SHA384Key(bytes);
            case SHA512:
                bytes = DigestUtils.sha512(source);
                return new SHA512Key(bytes);
            case MD5:
                bytes = DigestUtils.md5(source);
                return new MD5Key(bytes);
            default:
                throw new GUIDGenerationException("Unsupported algorithm: " + algorithm);
        }

    }

    private static IKey hash(ALGORITHM algorithm, InputStream source) throws GUIDGenerationException, IOException {

        byte[] bytes;
        switch(algorithm) {
            case SHA1:
                bytes = DigestUtils.sha1(source);
                return new SHA1Key(bytes);
            case SHA256:
                bytes = DigestUtils.sha256(source);
                return new SHA256Key(bytes);
            case SHA384:
                bytes = DigestUtils.sha384(source);
                return new SHA384Key(bytes);
            case SHA512:
                bytes = DigestUtils.sha512(source);
                return new SHA512Key(bytes);
            case MD5:
                bytes = DigestUtils.md5(source);
                return new MD5Key(bytes);
            default:
                throw new GUIDGenerationException("Unsupported algorithm: " + algorithm);
        }

    }

}
