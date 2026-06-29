package csci3130.fall_2021.group1.project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class testingRegistrationJunit {
    static registrationActivity registrationActivity;

    @BeforeClass
    public static void setup(){
        registrationActivity = new registrationActivity();
    }

    @Test
    public void checkIfBlank(){
        assertTrue(registrationActivity.isEmptyInfo(" ", " ", " ", " ", " ", " ") );
        assertFalse(registrationActivity.isEmptyInfo("sdd", "sdd", "sdd", "sdd", "sdd", "sdd"));
    }

    @Test
    public void checkValidPassword(){
        assertTrue(registrationActivity.isValidPassWord("bluebear1!"));
        assertFalse(registrationActivity.isValidPassWord("dj!"));
    }

    @Test
    public void checkValidUsername(){
        assertTrue(registrationActivity.isValidUserName("iteration1!"));
        assertFalse(registrationActivity.isValidUserName("it"));
    }

    @Test
    public void checkConfirmedPassword(){
        assertTrue(registrationActivity.isConfirmedPassword("hey123", "hey123"));

    }

    @Test
    public void checkValidCreditCard(){
        assertTrue(registrationActivity.isValidCreditCard("1234567890123456"));
    }

    @Test
    public void checkValidCVV(){
        assertTrue(registrationActivity.isValidCVV("300"));
    }

    @Test
    public void checkValidCreditDate(){
        assertTrue(registrationActivity.isValidCreditDate("1123"));
    }
    @AfterClass
    public static void tearDown() {
        System.gc();
    }
}