package reidc.xyz.smartpass.Screens;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import reidc.xyz.smartpass.Accounts.User;
import reidc.xyz.smartpass.Handlers.HallPassHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileScreen extends Fragment {

    private final int IMAGE_CODE = 412;
    private User user;
    private TextView userNameID;
    private ImageButton profileImage;
    private ImageView profileQRCode;
    private Button viewPassesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = UserHandler.i().getCurrentUser();
        return inflater.inflate(R.layout.fragment_profile_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        userNameID = (TextView) getActivity().findViewById(R.id.userIDText);
        profileImage = (ImageButton) getActivity().findViewById(R.id.userImage);
        profileQRCode = (ImageView) getActivity().findViewById(R.id.profileQRCode);
        viewPassesButton = (Button) getActivity().findViewById(R.id.profileButtonPasses);

        //if(user != null) {
            //if (user.getProfilePicture() != null)
              //  profileImage.setImageBitmap(user.getProfilePicture());

            profileQRCode.setImageBitmap(Frame.drawQR(user.getEmail()));
            userNameID.setText(user.getEmail());


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Change users profile image
                //showFileChooser();
                //UserHandler.i().setTargetUser("a@gmail.com", null);
                //Frame.switchFrame(getFragmentManager(), new UserScreen());
                //UserHandler.i().loadOtherProfile("a@gmail.com", getFragmentManager(), new UserScreen());
            }

        });

        viewPassesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = UserHandler.i().getCurrentUser().getEmail();
                UserHandler.i().setTargetUser(email, UserHandler.i().getCurrentUser().getPassword());
                HallPassHandler.i().getPasses(email, getFragmentManager());
                //Frame.switchFrame(getFragmentManager(), new PassesScreen());
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture"), IMAGE_CODE);
    }

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == IMAGE_CODE && resultCode == getActivity().RESULT_OK && data.getData() != null) {
        Uri filePath = data.getData();
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            profileImage.setImageBitmap(bm);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataStream = baos.toByteArray();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference mountainsRef = storageRef.child("test.jpg");

            UploadTask uploadTask = mountainsRef.putBytes(dataStream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
}