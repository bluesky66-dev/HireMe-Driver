// ILocationRecord.aidl
package  com.driver.hire_me;
import android.location.Location;
// Declare any non-default types here with import statements

interface ILocationRecord {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


     int getPid();



     double coverDistance();
  Location  getCurrentLocation();

}
