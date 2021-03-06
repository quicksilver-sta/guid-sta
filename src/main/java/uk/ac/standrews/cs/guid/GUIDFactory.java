package uk.ac.standrews.cs.guid;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import uk.ac.standrews.cs.guid.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.guid.impl.KeyFactory;
import uk.ac.standrews.cs.guid.impl.keys.InvalidID;
import uk.ac.standrews.cs.guid.impl.keys.KeyImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

import static uk.ac.standrews.cs.guid.IKey.MULTI_HASH_DELIMITER;

/*
 * Created originally on 19-Aug-2005
 */
public class GUIDFactory {

    public static IGUID generateRandomGUID() {
        return generateRandomGUID(ALGORITHM.SHA256);
    }

    public static IGUID generateRandomGUID(ALGORITHM algorithm) {

        try {
            return (KeyImpl) KeyFactory.generateRandomKey(algorithm);
        } catch (GUIDGenerationException e) {
            return new InvalidID();
        }
    }

    public static IGUID generateGUID(String string) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(string);
    }

    public static IGUID generateGUID(ALGORITHM algorithm, String string) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(algorithm, string);
    }

    public static IGUID generateGUID(InputStream inputStream) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(inputStream);
    }

    public static IGUID generateGUID(ALGORITHM algorithm, InputStream inputStream) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(algorithm, inputStream);
    }

    public static IGUID generateGUID(File file) throws GUIDGenerationException, FileNotFoundException {
        return (KeyImpl) KeyFactory.generateKey(new FileInputStream(file));
    }

    public static IGUID generateGUID(ALGORITHM algorithm, File file) throws GUIDGenerationException, FileNotFoundException {
        return (KeyImpl) KeyFactory.generateKey(algorithm, new FileInputStream(file));
    }

    public static IGUID generateGUID(byte[] bytes) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(bytes);
    }

    public static IGUID generateGUID(ALGORITHM algorithm, byte[] bytes) throws GUIDGenerationException {
        return (KeyImpl) KeyFactory.generateKey(algorithm, bytes);
    }

    /**
     * Recreate a GUID from its multihash string format.
     *
     * @param multihash ALGORITHM:BASE:KEY (e.g. SHA1_16_a9993e364706816aba3e25717850c26c9cd0d89d)
     * @return GUID object
     * @throws GUIDGenerationException if the GUID could not be recreated
     */
    public static IGUID recreateGUID(String multihash) throws GUIDGenerationException {

        if (multihash == null || multihash.isEmpty()) throw new GUIDGenerationException();

        String[] multihashComponents = multihash.split(MULTI_HASH_DELIMITER);
        if (multihashComponents.length != 3) throw new GUIDGenerationException();

        try {
            ALGORITHM algorithm = ALGORITHM.get(multihashComponents[0]);
            BASE base = BASE.get(Integer.parseInt(multihashComponents[1]));

            switch (base) {
                case HEX:
                {
                    byte[] input = Hex.decodeHex(multihashComponents[2].toCharArray());
                    return (KeyImpl) KeyFactory.recreateKey(algorithm, input);
                }
                case CANON:
                {
                    String cleanedKey = multihashComponents[2].replace("-", "");
                    byte[] input = Hex.decodeHex(cleanedKey.toCharArray());
                    return (KeyImpl) KeyFactory.recreateKey(algorithm, input);
                }
                case BASE_64:
                {
                    byte[] input = Base64.getDecoder().decode(multihashComponents[2]);
                    return (KeyImpl) KeyFactory.recreateKey(algorithm, input);
                }
                case INVALID:
                default:
                    return new InvalidID();
            }
        } catch (DecoderException | IllegalArgumentException e) {
            throw new GUIDGenerationException();
        }

    }

}
