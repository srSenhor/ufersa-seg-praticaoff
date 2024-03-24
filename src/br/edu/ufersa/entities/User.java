package br.edu.ufersa.entities;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long accID;
    private String password;
    private boolean isLogged;

    public User(long accID, String password) {
        this.accID = accID;
        this.password = password;
        this.isLogged = false;
    }
    public long getAccID() {
        return accID;
    }
    public String getPassword() {
        return password;
    }
    public boolean isLogged(){
        return this.isLogged;
    }
    public void userLoggedIn(){
        this.isLogged = true;
    }
    public void userLoggedOut(){
        this.isLogged = false;
    }

}
