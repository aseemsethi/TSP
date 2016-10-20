package in.netstack.tsp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class tsp extends AppCompatActivity {
    private static final String TAG = "TSP Activity";
    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Start with the start fragment
        Bundle bundle = new Bundle();
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        startTSP startOne = new startTSP();
        startOne.setArguments(bundle);
        ft2.replace(R.id.fragment_container, startOne, "Start TSP");
        ft2.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tsp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Settings menu selected");
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                settingsTSP settingsOne = new settingsTSP();
                ft1.replace(R.id.fragment_container, settingsOne, "Settings");
                ft1.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}