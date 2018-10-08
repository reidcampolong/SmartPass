package reidc.xyz.smartpass.Handlers;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Reid on 3/19/17.
 */
public class DataHandler {

    private static DataHandler instance;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public DataHandler() {
        instance = this;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
    }

    public DatabaseReference getRef() {
        return ref;
    }

    public static DataHandler i() {
        return instance;
    }


}
