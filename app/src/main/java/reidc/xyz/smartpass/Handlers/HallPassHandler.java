package reidc.xyz.smartpass.Handlers;

import android.app.FragmentManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.HallPasses.HallPass;
import reidc.xyz.smartpass.Screens.PassesScreen;
import reidc.xyz.smartpass.Screens.ProfileScreen;
import reidc.xyz.smartpass.Utilities.A;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * Created by Reid on 3/19/17.
 */
public class HallPassHandler {
    public static boolean loading = false;
    public static HashMap<String, List<HallPass>> currentPasses;
    private static HallPassHandler instance;

    public HallPassHandler() {
        instance = this;
        currentPasses = new HashMap<>();
    }
  /*  public void queryDataForPasses(User user) {

        String userID = "";
        if(!UserHandler.i().emailID.containsKey(email)) {
            Frame.convertEmailToID(email);

            int i = 0;
            while(!UserHandler.i().emailID.containsKey(email) && i < 10000) {
                i++;
            }
        }

    }
*/

    public void getPasses(final String email, final FragmentManager fm) {
        Log.i("MYAPP", "BA");

        DataHandler.i().getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("MYAPP", "CA");

                Log.i("MYAPP", "going to check " + email);
                Log.i("MYAPP", "value is" + UserHandler.i().getID(email));
                //Log.i("MYAPP", data);
                for(DataSnapshot ss : dataSnapshot.child("users").child(UserHandler.i().getID(email)).child("passes").getChildren()) {
                    Log.i("MYAPP" , "DA");
                    HallPass pass = ss.getValue(HallPass.class);

                    List<HallPass> passes = new ArrayList<HallPass>();
                    if(currentPasses.containsKey(email))
                        passes = currentPasses.get(email);
                    passes.add(pass);
                    currentPasses.put(email, passes);
                    Log.i("MYAPP", currentPasses.get(email).size()+"");
                }
                Log.i("MYAPP" , "EA");
                Frame.switchFrame(fm, new PassesScreen());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createPassFor(String issuedTo, String issuedBy, Timestamp expires) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        HallPass hallPass = new HallPass();
        hallPass.setIssuedTo(issuedTo);
        hallPass.setIssuedBy(issuedBy);
        hallPass.setIssued(dateFormat.format(Calendar.getInstance().getTimeInMillis()));
        hallPass.setExpires(dateFormat.format(expires));
        DataHandler.i().getRef().child("users").child(UserHandler.i().getID(issuedTo)).child("passes").push().setValue(hallPass);
    }

    public static HallPassHandler i() {
        return instance;
    }
}
