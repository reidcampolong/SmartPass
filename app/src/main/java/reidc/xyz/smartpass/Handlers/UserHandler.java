package reidc.xyz.smartpass.Handlers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.HallPasses.HallPass;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.A;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * Created by Reid on 3/19/17.
 */
public class UserHandler {

    public boolean loadingUser = false;
    private User viewedUser;
    private User currentUser;
    private List<User> users;
    public HashMap<String, String> emailID;
    public HashMap<String, List<HallPass>> passes;

    private static UserHandler instance;

    public UserHandler() {
        instance = this;
        currentUser = null;
        viewedUser = null;
        users = new ArrayList<User>();
        emailID = new HashMap<>();
        passes = new HashMap<>();
    }

    public void registerUser(String email, String password, Resources resources) {
        String userID = A.getUID();

        currentUser = new User();

        currentUser.setEmail(email);
        currentUser.setPassword(password);
        //currentUser.setProfilePicture(BitmapFactory.decodeResource(resources, R.drawable.profile));
        DataHandler.i().getRef().child("users").child(userID).setValue(currentUser);

        // Can call this later to retrieve id from email
        DataHandler.i().getRef().child("ids").child(A.stripAlpha(email)).setValue(userID);
    }

    public String getID(String email) {
        return emailID.get(A.stripAlpha(email));
    }

    public void loadProfile(final FragmentManager fm, final Fragment toLoadWhenDone) {
        final String userID = A.getUID();

        DataHandler.i().getRef().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setLoadUser(userID, dataSnapshot, fm, toLoadWhenDone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void loadOtherProfile(final String email, final FragmentManager fm, final Fragment toLoadWhenDone) {
        //Frame.convertEmailToID(email);
        final String id = UserHandler.i().getID(email);
        DataHandler.i().getRef().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setLoadUser(id, dataSnapshot, fm, toLoadWhenDone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadOtherUserIds() {
        Log.i("MYAPP", " Beggining to log other user IDS");
        DataHandler.i().getRef().child("ids").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("MYAPP", " Starting..");
                for(DataSnapshot account : dataSnapshot.getChildren()) {
                    Log.i("MYAPP", "loaded accout " + account.getKey() + " and " +account.getValue(String.class));
                    UserHandler.i().emailID.put(account.getKey(), account.getValue(String.class));
                    //Frame.convertEmailToID(account.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


        private void setLoadUser(String userID, DataSnapshot ds, FragmentManager fm, Fragment toLoadWhenDone) {
            User user = new User();
            user.setEmail(ds.child(userID).getValue(User.class).getEmail());
            user.setPassword(ds.child(userID).getValue(User.class).getPassword());
            currentUser = user;
            Frame.convertEmailToID(user.getEmail());
            Frame.switchFrame(fm, toLoadWhenDone);
    }

    public User loadUser(String email, DataSnapshot snapshot) {
        User user = null;
        for(DataSnapshot ss : snapshot.getChildren()) {
            user = new User();
            user.setEmail(ss.child(email).getValue(User.class).getEmail());
            user.setPassword(ss.child(email).getValue(User.class).getPassword());
        }
        users.add(user);
        return user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getTargetUser() {return viewedUser;}

    public void setTargetUser(String email, String password) {
        viewedUser = new User();
        viewedUser.setEmail(email);
        viewedUser.setPassword(password);
    }

    public void setCurrentUser(String email, String password) {
        currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPassword(password);
    }

    public User retreiveUser(String QRCode) {
        User user = new User();
        user.setEmail(QRCode);
        user.setPassword(null);
        return user;
    }


    public static UserHandler i() {
        return instance;
    }

}
