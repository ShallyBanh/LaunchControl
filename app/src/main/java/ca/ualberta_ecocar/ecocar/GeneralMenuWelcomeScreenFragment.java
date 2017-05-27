package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ualberta_ecocar.ecocar.util.StartupLogic;

public class GeneralMenuWelcomeScreenFragment extends Fragment {

    public GeneralMenuWelcomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.general_menu_welcome_screen, container, false);
        Button ImACarButton = (Button) currentView.findViewById(R.id.ImACar);
        ImACarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Fragment nextSetupScreen = StartupLogic.getSetupFragment(getActivity());
                if(nextSetupScreen == null){
                    Intent launchDriversScreen = new Intent(getActivity(), DriversActivity.class);
                    startActivity(launchDriversScreen);
                    return;
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right, 0, 0);
                transaction.replace(R.id.WelcomeScreenContainer, nextSetupScreen);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Button ImATeamMember = (Button) currentView.findViewById(R.id.ImATeamMember);
        ImATeamMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Change the fragment to data view
                Fragment ChooseCarToWatchFragment = new ChooseCarToWatchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right, 0, 0);
                transaction.replace(R.id.WelcomeScreenContainer, ChooseCarToWatchFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return currentView;
    }


}
