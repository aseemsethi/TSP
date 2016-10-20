package in.netstack.tsp;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by aseem on 18-10-2016.
 */

public class settingsTSP extends Fragment {
    private static final String TAG = "TSP Settings";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_fragment, container, false);

        /*
        Button addStart = (Button) v.findViewById(R.id.add_start);
        addStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "starting TSP");
                Toast.makeText(v.getContext(), "Start TSP", Toast.LENGTH_SHORT).show();
            }
        });
        */
        return v;
    }
}