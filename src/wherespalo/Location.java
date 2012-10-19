
package wherespalo;

//represents a physical location as determined by a decimal latitude and longitude
public class Location {
    private double latitude, longitude, radianLat, radianLong;
    
    //the earth's radius in both kilometers and miles
    private final double RADIUS_KILOS = 6371.01, RADIUS_MILES = 3958.762079;
    
    //constructor is passed latitude and longitude coorindates in decimal form as parameters
    public Location(double givenLat, double givenLong) {
        latitude = givenLat;
        longitude = givenLong;
        
        radianLat = Math.toRadians(latitude);
        radianLong = Math.toRadians(longitude);
    }
    
    //returns location's latitude
    public double getLat() {
        return latitude;
    }
    
    //returns location's longitude
    public double getLong() {
        return longitude;
    }
    
    //sets location's latitude to given parameter
    public void setLat(double newLat) {
        latitude = newLat;
        radianLat = Math.toRadians(latitude);
    }
    
    //sets location's longitude to given paramter
    public void setLong(double newLong) {
        longitude = newLong;
        radianLong = Math.toRadians(longitude);
    }
    
    /* When passed another location object as a paramter, calculates the distance
     * between the two in either miles or kilometers based on second paramter.
     * Haversine formula is used for actual calculation. 
     */
    public double calculateDistance(Location location, boolean imperial) {
        double distance;
        double lat2 = Math.toRadians(location.getLat()); 
        double long2 = Math.toRadians(location.getLong());
        
        double dLat = lat2 -radianLat;
        double dLong = long2 -radianLong;
        
        double sindLat = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double sindLong = Math.sin(dLong /2) * Math.sin(dLong / 2);
        
        double toSquare = sindLat + (Math.cos(radianLat) * 
                Math.cos(lat2) * sindLong);
        double centralAngle = 2 * Math.asin(Math.sqrt(toSquare));
        
        if (imperial) {
            distance = RADIUS_MILES * centralAngle;
        } else {
            distance = RADIUS_KILOS  * centralAngle;
        }
        
        return distance;
    }
    
    //calculates and returns the users bearing towards the Palo
    public double calculateBearing(Location location, double currentBearing) {
        double lat2 = Math.toRadians(location.getLat());
        
        double dLong = Math.toRadians(location.getLong() - longitude);
        
        double y = Math.sin(dLong) * Math.cos(lat2);
        double x = (Math.cos(radianLat) * Math.sin(lat2)) - (Math.sin(radianLat) * Math.cos(lat2) * Math.cos(dLong));
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (bearing + 360.0) % 360.0;
        
        if (currentBearing == 0)
            return bearing;
        else
            return Math.abs((bearing - (currentBearing - 360)) % 360);
    }
        
}