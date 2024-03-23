package br.edu.ufersa.entities;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long accID;
    private String password;

    public User(long accID, String password) {
        this.accID = accID;
        this.password = password;
    }
    public long getAccID() {
        return accID;
    }
    public String getPassword() {
        return password;
    }
}
