package reidc.xyz.smartpass.Screens;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.HallPasses.HallPass;
import reidc.xyz.smartpass.Handlers.HallPassHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private Button buttonLogin;
    private Button buttonRegister;

    private EditText emailText;
    private EditText passwordText;

    private ProgressDialog progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buttonLogin = (Button) getActivity().findViewById(R.id.loginButton);
        buttonRegister = (Button) getActivity().findViewById(R.id.registerButton);

        emailText = (EditText) getActivity().findViewById(R.id.emailText);
        passwordText = (EditText) getActivity().findViewById(R.id.passwordText);

        progressBar = new ProgressDialog(getActivity());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }

        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }

        });

    }

    public void registerUser() {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Invalid email/password!", Toast.LENGTH_LONG).show();
            return;

        }

        // An email and password are entered:
        progressBar.setMessage("Registering account...");
        progressBar.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    // Upload them to the database
                    UserHandler.i().registerUser(email, password, getResources());

                    Toast.makeText(getActivity(), "Registered!", Toast.LENGTH_SHORT).show();
                    progressBar.hide();

                    // Sign them in
                    signIn();
                } else {
                    Toast.makeText(getActivity(), "Could not register", Toast.LENGTH_SHORT).show();
                    progressBar.hide();
                }
            }
        });

    }

    public void signIn() {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Invalid email/password!", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setMessage("Logging in...");
        progressBar.show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Logged in


                    UserHandler.i().loadOtherUserIds();
                    UserHandler.i().loadProfile(getFragmentManager(), new ProfileScreen());

                    int i = 0;
                    /*while(UserHandler.i().getCurrentUser() == null && i < Integer.MAX_VALUE) {
                        // Wait for data to load up to 10000
                        i++;
                    }*/

                    if(i == Integer.MAX_VALUE) {
                        Toast.makeText(getActivity(), "Couldn't log in in time", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getActivity(), "Logged in!", Toast.LENGTH_SHORT).show();
                    progressBar.hide();

                    // Switch to user's home screen
                    //Frame.switchFrame(getFragmentManager(), new ProfileScreen());
                } else {
                    Toast.makeText(getActivity(), "Could not log in", Toast.LENGTH_SHORT).show();
                    progressBar.hide();
                }
            }
        });
    }

}
