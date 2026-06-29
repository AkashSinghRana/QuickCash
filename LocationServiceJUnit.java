package csci3130.fall_2021.group1.project;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationServiceJUnit {

    static LocationService LocationService;

    @BeforeClass
    public static void setup(){
        LocationService = new LocationService();
    }

    @Test
    public void distanceTest(){
        LatLng loc1 = new LatLng(44.670420,-63.671310);
        LatLng loc2 = new LatLng(44.667850,-63.669628);
        LatLng loc3 = new LatLng(44.670420,-63.671310);
        LatLng loc4 = new LatLng(44.8838072,-76.0953412);
        assertTrue(LocationService.distance(loc1,loc2));
        assertFalse(LocationService.distance(loc3,loc4));

    }

}
