package reidc.xyz.smartpass.Accounts;

import android.graphics.Bitmap;

import java.util.List;

import reidc.xyz.smartpass.HallPasses.HallPass;

/**
 * Created by Reid on 3/19/17.
 */
public class User {

    private String email;
    private String password;
    //private List<HallPass> passes;

    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //public List<HallPass> getPasses() {
    //    return passes;
    //}

    //public void setPasses(List<HallPass> passes) {
    //    this.passes = passes;
    //}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
