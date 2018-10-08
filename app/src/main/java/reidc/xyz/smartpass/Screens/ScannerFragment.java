package reidc.xyz.smartpass.Screens;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;
import reidc.xyz.smartpass.Utilities.Frame;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        IntentIntegrator integrator = IntentIntegrator.forFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Content has been scanned, interpret data
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {

            String email = UserHandler.i().getCurrentUser().getEmail();
            if(result.getContents() == null) {
                Log.d("ScannerActivity", "cancelled scan");
                Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_LONG).show();
                UserHandler.i().setTargetUser(email, "");
                Frame.switchFrame(getFragmentManager(), new ProfileScreen());
            } else {

                // Scan was successful, determine what to do with the data
                Log.d("MainActivity", "scanned");
                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    if(email.equals(result.getContents())) {
                        UserHandler.i().setTargetUser(email, "");
                        Frame.switchFrame(getFragmentManager(), new ProfileScreen());
                    } else {
                        UserHandler.i().setTargetUser(result.getContents(), "");
                        Frame.switchFrame(getFragmentManager(), new UserScreen());
                    }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
