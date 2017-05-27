package ca.ualberta_ecocar.ecocar;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.ualberta_ecocar.ecocar.util.PermsUtil;
import ca.ualberta_ecocar.ecocar.util.StartupLogic;


public class CollectPermissionsFragment extends Fragment {


    public CollectPermissionsFragment() {
    }

    private String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.INTERNET};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_collect_permissions, container, false);

        Button ImACarButton = (Button) thisView.findViewById(R.id.AskForPerms);
        ImACarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PermsUtil.getPermissions(getActivity());
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
        });
        return thisView;
    }

}
