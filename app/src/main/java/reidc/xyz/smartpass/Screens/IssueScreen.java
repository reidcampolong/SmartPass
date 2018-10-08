package reidc.xyz.smartpass.Screens;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.util.Calendar;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.Handlers.HallPassHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.A;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * A simple {@link Fragment} subclass.
 */
public class IssueScreen extends Fragment {

    private TextView issuedTo, issuedBy, issuedAt, expiresAt;
    private Button submitButton, add5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_issue_screen, container, false);
    }

    private Timestamp currentTime;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        issuedTo = (TextView) getActivity().findViewById(R.id.issueIssuedTo);
        issuedBy = (TextView) getActivity().findViewById(R.id.issueIssuedBy);
        issuedAt = (TextView) getActivity().findViewById(R.id.issueIssuedOn);

        submitButton = (Button) getActivity().findViewById(R.id.buttonIssueSubmit);
        add5 = (Button) getActivity().findViewById(R.id.add5Min);

        final User to = UserHandler.i().getTargetUser();
        issuedTo.setText("Issued To: " + to.getEmail());

        final User by = UserHandler.i().getCurrentUser();
        issuedBy.setText("Issued By: " + by.getEmail());


        currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
        issuedAt.setText("Issued On " + A.formatTime(currentTime));

        expiresAt = (TextView) getActivity().findViewById(R.id.issueExpiresAt);
        update();

        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime.setTime(currentTime.getTime() + 300000);
                update();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MYAPP", "click submit");
                UserHandler.i().setTargetUser(by.getEmail(), by.getPassword());
                Frame.switchFrame(getFragmentManager(), new ProfileScreen());
                HallPassHandler.i().createPassFor(to.getEmail(), by.getEmail(), currentTime);
            }
        });

    }

    public void update() {
        expiresAt
                .setText("Expires " + A.formatTime(currentTime));
    }

}
