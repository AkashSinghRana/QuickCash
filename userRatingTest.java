package csci3130.fall_2021.group1.project;

import junit.framework.TestCase;

import org.junit.Test;

public class userRatingTest extends TestCase {

    @Test
    public void testQuickMaths() {
        userRating userRating = new userRating();
        userRating.setRatingCalculation(69);
        assertEquals(userRating.getRatingCalculation(), Float.parseFloat("69.0"));
    }

    @Test
    public void testC() {
        userRating userRating = new userRating();
        userRating.setCountVariable(1);
        assertEquals(userRating.getCountVariable(), 1);
    }

    @Test
    public void testOverallRating() {
        userRating userRating = new userRating();
        userRating.setOverallRating(200);
        assertEquals(userRating.getOverallRating(), Float.parseFloat("200.0"));
    }

    @Test
    public void testTotalValue() {
        userRating userRating = new userRating();
        userRating.setTotalValue(100);
        assertEquals(userRating.getTotalValue(), Float.parseFloat("100.0"));
    }
}