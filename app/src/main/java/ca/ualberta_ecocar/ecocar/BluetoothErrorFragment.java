package ca.ualberta_ecocar.ecocar;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BluetoothErrorFragment extends Fragment {


    public BluetoothErrorFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_bluetooth_error, container, false);

        Button selectNewBTDeviceButton = (Button) currentView.findViewById(R.id.SelectNewBluetooth);
        selectNewBTDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch select bluetooth fragment
                Fragment BTSetUp = new BluetoothSetupFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.DriversCurrentFragment, BTSetUp);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button retryButton = (Button) currentView.findViewById(R.id.RetryBlueToothConnection);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get activity and retry connecting.
                ((DriversActivity)getActivity()).tryToConnectToCar();
            }
        });

        return currentView;
    }

}
