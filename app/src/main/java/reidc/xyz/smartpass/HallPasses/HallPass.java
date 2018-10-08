package reidc.xyz.smartpass.HallPasses;

import java.sql.Timestamp;

import reidc.xyz.smartpass.Accounts.User;

/**
 * Created by Reid on 3/19/17.
 */
public class HallPass {

    private String issuedBy;
    private String issuedTo;

    private String issued;
    private String expires;

    public HallPass() {

    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String t) {
       issued = t;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String t) {
        expires = t;
    }

}
