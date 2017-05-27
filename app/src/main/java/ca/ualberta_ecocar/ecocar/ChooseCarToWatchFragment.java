package ca.ualberta_ecocar.ecocar;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ChooseCarToWatchFragment extends Fragment {


    public ChooseCarToWatchFragment() {
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
                LaunchObserverActivity();
            }
        });

        Button PrototypeButton = (Button) currentView.findViewById(R.id.PrototypeButton);
        PrototypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save data and move on
                saveCarChoiceToDevice(1);
                LaunchObserverActivity();
            }
        });

        return currentView;
    }


    private void saveCarChoiceToDevice(int carID){
        SharedPreferences.Editor sharedPrefEdit = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        sharedPrefEdit.putInt(getString(R.string.current_watching_car_save), carID);
        sharedPrefEdit.commit();
    }

    public void LaunchObserverActivity(){
        Intent launchObserverActivity = new Intent(getActivity(), ObserverActivity.class);
        startActivity(launchObserverActivity);
    }

}
