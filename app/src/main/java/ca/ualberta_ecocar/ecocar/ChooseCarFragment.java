package ca.ualberta_ecocar.ecocar;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ualberta_ecocar.ecocar.util.StartupLogic;


public class ChooseCarFragment extends Fragment {


    public ChooseCarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_choose_car, container, false);

        Button AliceButton = (Button) currentView.findViewById(R.id.AliceButton);
        AliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save data and move on
                saveCarChoiceToDevice(0);
                launchNextFragment();
            }
        });

        Button PrototypeButton = (Button) currentView.findViewById(R.id.PrototypeButton);
        PrototypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save data and move on
                saveCarChoiceToDevice(1);
                launchNextFragment();
            }
        });

        return currentView;
    }

    private void saveCarChoiceToDevice(int carID){
        SharedPreferences.Editor sharedPrefEdit = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        sharedPrefEdit.putInt(getString(R.string.current_driving_car_save), carID);
        sharedPrefEdit.commit();
    }

    private void launchNextFragment() {
        Fragment nextSetupScreen = StartupLogic.getSetupFragment(getActivity());
        if(nextSetupScreen == null){
            Intent launchDriversScreen = new Intent(getActivity(), DriversActivity.class);
            startActivity(launchDriversScreen);
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.WelcomeScreenContainer, nextSetupScreen);
        transaction.setCustomAnimations(R.animator.slide_in_left,
                R.animator.slide_out_right, 0, 0);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
