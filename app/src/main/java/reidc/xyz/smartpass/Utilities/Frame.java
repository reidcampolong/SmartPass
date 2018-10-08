package reidc.xyz.smartpass.Utilities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.Handlers.DataHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Screens.ProfileScreen;
import reidc.xyz.smartpass.Screens.UserScreen;


/**
 * Created by Reid on 3/19/17.
 */
public class Frame {

    public static void switchFrame(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    public static void openUserFrame(FragmentManager manager, User user) {
        UserScreen screen = new UserScreen();
        screen.setUser(user);
        manager.beginTransaction().replace(R.id.content_frame, screen);
    }


    public static Bitmap drawQR(String code) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void convertEmailToID(final String email) {
        Log.i("MYAPP", "Abegisn");

        DataHandler.i().getRef().child("ids").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("MYAPP", "startA");
                UserHandler.i().emailID.put(email, dataSnapshot.child(A.stripAlpha(email)).getValue(String.class));
                Log.i("MYAPP", "comete:"+UserHandler.i().emailID.get(email));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static String returnID(DataSnapshot dataSnapshot, String email) {
        String id = "";
        Log.i("MYAPP", "B");
        for(DataSnapshot ss : dataSnapshot.getChildren()) {
            Log.i("MYAPP", "C");
            id = ss.child("id").child(email.replaceAll("[^A-Za-z0-9]", "")).getValue(String.class);
        }
        return id;
    }
}
