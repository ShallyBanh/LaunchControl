package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ObserverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        // Start with Time Keeper Fragment
        Fragment TimeKeeperFragment = new TimeKeeperFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.ObserverScreenContainer, TimeKeeperFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
