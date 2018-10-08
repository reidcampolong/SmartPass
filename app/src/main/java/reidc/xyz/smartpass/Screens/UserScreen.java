package reidc.xyz.smartpass.Screens;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.Handlers.HallPassHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.Frame;

public class UserScreen extends Fragment {

    private User user;
    private TextView userNameID;
    private Button viewPassesButton, issuePassButton;
    private ImageView userQRCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = UserHandler.i().getTargetUser();
        userNameID = (TextView) getActivity().findViewById(R.id.userUserIDText);
        userQRCode = (ImageView) getActivity().findViewById(R.id.userQRCode);
        viewPassesButton = (Button) getActivity().findViewById(R.id.userButtonPasses);

        Log.i("MYAPP", user.getEmail());
        userQRCode.setImageBitmap(Frame.drawQR(user.getEmail()));

        userNameID.setText(user.getEmail());

        viewPassesButton = (Button) getActivity().findViewById(R.id.userButtonPasses);
        viewPassesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HallPassHandler.i().getPasses(user.getEmail(), getFragmentManager());
            }
        });

        issuePassButton = (Button) getActivity().findViewById(R.id.userIssuePass);
        issuePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frame.switchFrame(getFragmentManager(), new IssueScreen());
            }
        });

        //Bitmap bm;
        //if((bm = user.getProfilePicture()) != null)
          //  profileImage.setImageBitmap(bm);

    }

    public void setUser(User user) {
        this.user = user;
    }


}
