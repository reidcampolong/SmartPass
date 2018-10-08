package reidc.xyz.smartpass.Screens;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import reidc.xyz.smartpass.HallPasses.HallPass;
import reidc.xyz.smartpass.Handlers.HallPassHandler;
import reidc.xyz.smartpass.Handlers.UserHandler;
import reidc.xyz.smartpass.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassesScreen extends Fragment {

    private ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passes_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        List<String> items = new ArrayList<String>();

        if(HallPassHandler.currentPasses.containsKey(UserHandler.i().getTargetUser().getEmail())) {
            for (HallPass p : HallPassHandler.currentPasses.get(UserHandler.i().getTargetUser().getEmail())) {
                items.add("From " + p.getIssuedBy() + " at " + p.getIssued());
            }
        } else {
            items.add("No recorded passes");
        }

        ListAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.hallpass_item, items);

        lv = (ListView) getActivity().findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

}
