package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        // Start with main menu fragment
        Fragment GeneralMenu = new GeneralMenuWelcomeScreenFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.WelcomeScreenContainer, GeneralMenu);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
