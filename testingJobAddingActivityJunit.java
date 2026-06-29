package csci3130.fall_2021.group1.project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testingJobAddingActivityJunit {
    static jobAddingActivity jobAddingActivity;
    @BeforeClass
    public static void setup(){
        jobAddingActivity = new jobAddingActivity();
    }

    @Test
    public void checkIfBlank(){
        assertTrue(jobAddingActivity.isEmptyInfo(" ", " ", " ", " ", " "));
        assertFalse(jobAddingActivity.isEmptyInfo("talking", "backseat of my car", "2020-04-19", "20", "20"));
    }

    @Test
    public void checkIfVaildPay(){
        assertFalse(jobAddingActivity.checkPay("LOL"));
        assertTrue(jobAddingActivity.checkPay("20"));
    }

    @Test
    public void checkIfVaildDuration(){
        assertFalse(jobAddingActivity.checkDuration("LOL"));
        assertTrue(jobAddingActivity.checkDuration("20"));
    }

    @Test
    public void checkIfDateFormatCorrect(){
        assertFalse(jobAddingActivity.checkDate("LOL"));
        assertFalse(jobAddingActivity.checkDate("20/0/1"));
        assertTrue(jobAddingActivity.checkDate("11-01-2022"));
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }
}
