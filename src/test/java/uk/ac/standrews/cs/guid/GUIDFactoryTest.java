package uk.ac.standrews.cs.guid;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.guid.exceptions.GUIDGenerationException;
import uk.ac.standrews.cs.guid.impl.keys.InvalidID;
import uk.ac.standrews.cs.guid.utils.StreamsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashSet;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 *
 * NIST tests: http://csrc.nist.gov/groups/ST/toolkit/examples.html
 * Other tests have been reproduced using http://www.sha1-online.com/
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class GUIDFactoryTest {

    public static final String TEST_STRING = "TEST";
    public static final String TEST_EMPTY_STRING = "";
    public static final String HELLO_STRING = "hello";

    public static final String TEST_STRING_HASHED = "984816fd329622876e14907634264e6f332e9fb3";
    public static final String TEST_EMPTY_STRING_HASHED = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    public static final String HELLO_STRING_HASHED_SHA1 = "aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d";
    public static final String HELLO_STRING_HASHED_SHA256 = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824";
    public static final String HELLO_STRING_HASHED_SHA384 = "59e1748777448c69de6b800d7a33bbfb9ff1b463e44354c3553bcdb9c666fa90125a3c79f90397bdf5f6a13de828684f";
    public static final String HELLO_STRING_HASHED_SHA512 = "9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca72323c3d99ba5c11d7c7acc6e14b8c5da0c4663475c2e5c3adef46f73bcdec043";

    @Test
    public void generateGUIDFromStreamTest() throws Exception {
        InputStream inputStreamFake = StreamsUtils.StringToInputStream(TEST_STRING);
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, inputStreamFake);
        assertEquals(TEST_STRING_HASHED, guid.toString());
    }

    @Test
    public void generateGUIDFromEmptyStreamTest() throws Exception {
        InputStream inputStreamFake = StreamsUtils.StringToInputStream(TEST_EMPTY_STRING);
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, inputStreamFake);
        assertNotNull(guid);
        assertEquals(TEST_EMPTY_STRING_HASHED, guid.toString());
    }

    @Test (expectedExceptions = GUIDGenerationException.class)
    public void nullStreamTest() throws Exception {
        InputStream stream = null;
        GUIDFactory.generateGUID(ALGORITHM.SHA1, stream);
    }

    @Test (expectedExceptions = GUIDGenerationException.class)
    public void generateGUIDNullStringTest() throws Exception {
        String string = null;
        GUIDFactory.generateGUID(ALGORITHM.SHA1, string);
    }

    @Test (expectedExceptions = GUIDGenerationException.class)
    public void generateGUIDNullStreamTest() throws Exception {
        InputStream stream = null;
        GUIDFactory.generateGUID(ALGORITHM.SHA1, stream);
    }

    @Test (expectedExceptions = GUIDGenerationException.class)
    public void generateGUIDEmptyStringTest() throws Exception {
        GUIDFactory.generateGUID(ALGORITHM.SHA1, TEST_EMPTY_STRING);
    }

    @Test
    public void randomGUIDTest() throws Exception {
        IGUID guid = GUIDFactory.generateRandomGUID(ALGORITHM.SHA1);
        assertNotNull(guid);
        assertNotEquals(guid, new InvalidID());
    }

    @Test
    public void manyRandomGUIDTest() throws Exception {
        LinkedHashSet<IGUID> guids = new LinkedHashSet<>();
        for(int i = 0; i < 100; i++) {
            guids.add(GUIDFactory.generateRandomGUID(ALGORITHM.SHA1));
        }

        assertEquals(100, guids.size());
    }

    @Test
    public void generateGUID_abc_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, "abc");
        assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", guid.toString());
    }

    @Test
    public void generateGUID_long_string_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
        assertEquals("84983e441c3bd26ebaae4aa1f95129e5e54670f1", guid.toString());
    }

    @Test
    public void generateGUID_256_abc_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA256, "abc");
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", guid.toString());
    }

    @Test
    public void generateGUID_256_long_string_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA256, "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq");
        assertEquals("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1", guid.toString());
    }

    @Test
    public void generateGUID_384_abc_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA384, "abc");
        assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed8086072ba1e7cc2358baeca134c825a7", guid.toString());
    }

    @Test
    public void generateGUID_384_long_string_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA384, "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu");
        assertEquals("09330c33f71147e83d192fc782cd1b4753111b173b3b05d22fa08086e3b0f712fcc7c71a557e2db966c3e9fa91746039", guid.toString());
    }

    @Test
    public void generateGUID_512_abc_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA512, "abc");
        assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f", guid.toString());
    }

    @Test
    public void generateGUID_512_long_string_NIST_Test() throws Exception {
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA512, "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu");
        assertEquals("8e959b75dae313da8cf4f72814fc143f8f7779c6eb9f7fa17299aeadb6889018501d289e4900f7e4331b99dec4b5433ac7d329eeb6dd26545e96e55b874be909", guid.toString());
    }

    @Test
    public void shortStringTest() throws Exception {
        InputStream inputStreamFake = StreamsUtils.StringToInputStream(TEST_STRING);
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, inputStreamFake);

        assertTrue(TEST_STRING_HASHED.startsWith(guid.toShortString()));
    }

    @Test
    public void multiHashTest() throws Exception {
        InputStream inputStreamFake = StreamsUtils.StringToInputStream(TEST_STRING);
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, inputStreamFake);

        assertEquals(guid.toMultiHash(BASE.HEX), "SHA1_16_" + TEST_STRING_HASHED);
    }

    @Test
    public void fileSHA1Hash() throws GUIDGenerationException, FileNotFoundException {

        File file = new File("src/test/resources/hello.txt");
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, file);

        assertEquals(guid.toMultiHash(), "SHA1_16_" + HELLO_STRING_HASHED_SHA1);
    }

    @Test
    public void fileSHA256Hash() throws GUIDGenerationException, FileNotFoundException {

        File file = new File("src/test/resources/hello.txt");
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA256, file);

        assertEquals(guid.toMultiHash(), "SHA256_16_" + HELLO_STRING_HASHED_SHA256);
    }

    @Test
    public void fileSHA384Hash() throws GUIDGenerationException, FileNotFoundException {

        File file = new File("src/test/resources/hello.txt");
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA384, file);

        assertEquals(guid.toMultiHash(), "SHA384_16_" + HELLO_STRING_HASHED_SHA384);
    }

    @Test
    public void fileSHA512Hash() throws GUIDGenerationException, FileNotFoundException {

        File file = new File("src/test/resources/hello.txt");
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA512, file);

        assertEquals(guid.toMultiHash(), "SHA512_16_" + HELLO_STRING_HASHED_SHA512);
    }

    @Test
    public void emptyFileSHA1Hash() throws GUIDGenerationException, FileNotFoundException {

        File file = new File("src/test/resources/empty.txt");
        IGUID guid = GUIDFactory.generateGUID(ALGORITHM.SHA1, file);

        assertEquals(guid.toMultiHash(), "SHA1_16_" + TEST_EMPTY_STRING_HASHED);
    }
}
