package design.chat.template.model;

import java.io.Serializable;

/**
 * Created by balachowis on 16/07/2017.
 */

public class Coordinates implements Serializable {

    private String latitude;
    private String longitude;


    public Coordinates(){

    }

    public Coordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAltitude() {
        return latitude;
    }

    public void setAltitude(String altitude) {
        this.latitude = altitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
