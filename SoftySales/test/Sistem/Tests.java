package Sistem;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class Tests {

    public Tests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of sha256 method, of class SistemProdaje.
     */
    @Test
    public void testSha256() {
        System.out.println("sha256");
        String password = "ETFBLsoftversko";
        String expResult = "e29a794d30e454697bf77930e56446d8ab2dd9ea8afd288036d9d27077beb67e";
        String result = SistemProdaje.sha256(password);
        assertEquals(expResult, result);
    }

    /**
     * Test of loginCheck method, of class SistemProdaje.
     */
    @Test
    public void testLoginCheck() {
        System.out.println("loginCheck");
        String username = "kebab";
        String password = "pljeskavica";
        HashMap<String, String> listaKorisnika = new HashMap();
        listaKorisnika.put(username, SistemProdaje.sha256(password));
        Integer expResult = 1;
        Integer result = SistemProdaje.loginCheck(username, password, listaKorisnika);
        assertEquals(expResult, result);
    }

    /**
     * Test of readRadnici method, of class SistemProdaje.
     */
    @Test
    public void testReadRadnici() {
        System.out.println("readRadnici");
        HashMap<String, String> radnici = SistemProdaje.readRadnici();
        boolean expResult = true;
        boolean result = radnici.containsKey("admin");
        assertEquals(expResult, result);
    }

}
