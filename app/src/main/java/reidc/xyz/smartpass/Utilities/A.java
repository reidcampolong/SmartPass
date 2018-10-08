package reidc.xyz.smartpass.Utilities;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Reid on 3/19/17.
 */
public class A {

    public static String stripAlpha(String text) {
        return text.replaceAll("[^A-Za-z0-9]", "");
    }

    public static String getUID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String formatTime(Timestamp time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
        return dateFormat.format(time);
    }
}
