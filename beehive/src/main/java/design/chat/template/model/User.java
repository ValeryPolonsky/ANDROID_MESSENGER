package design.chat.template.model;

import java.util.LinkedList;

import design.chat.template.model.Coordinates;

/**
 * Created by Asi on 7/8/2017.
 */

public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String userID;
    private String avatar;
    private Coordinates coordinates;

    public User(){

    }

    public User(String firstName, String lastName, String userName, String userID, String avatar, Coordinates coordinates) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userID = userID;
        this.avatar = avatar;
        this.coordinates = coordinates;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
