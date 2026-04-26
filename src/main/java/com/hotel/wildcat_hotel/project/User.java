package com.hotel.wildcat_hotel.project;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    //Default Constructor
    public User() {}

    public User(String username, String password, boolean isAdmin){
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    //Getter methods
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public boolean isAdmin(){return isAdmin;}

    //Setter methods
    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    //Authentication Methods
    public static boolean isUserValid(User user){
        List<User> users = Database.getUser(); //calls the database and roll each column of user
        for(User u: users){
            if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }

    public static boolean isUserAdmin(User user){
        List<User> users = Database.getUser();
        for(User u: users){
            if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())){
                return u.isAdmin();
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "User{username=" + username + ", isAdmin=" + isAdmin + "}";
    }

}
