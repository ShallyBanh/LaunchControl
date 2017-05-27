package ca.ualberta_ecocar.ecocar;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

// Used this blog for reference: https://wingoodharry.wordpress.com/2014/03/15/simple-android-to-arduino-via-bluetooth-serial-part-2/
public class BluetoothSetupFragment extends Fragment {

    private BluetoothAdapter localBluetoothAdapter;
    private ArrayAdapter<String> pairedDevicesArray;

    public BluetoothSetupFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_setup, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        pairedDevicesArray = new ArrayAdapter<>(getActivity(), R.layout.paired_bluetooth_devices);

        ListView pairedListView = (ListView) getActivity().findViewById(R.id.bluetooth_device_list);
        pairedListView.setAdapter(pairedDevicesArray);

        getBlueToothAdapter();
        pairedDevicesArray.clear();
        if(localBluetoothAdapter != null){
            Set<BluetoothDevice> pairedDevices = localBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesArray.add(device.getName() + "\n" + device.getAddress());
                }
            } else {
                pairedDevicesArray.add("No Devices Found!");
            }
        }
        else {
            pairedDevicesArray.add("Bluetooth not supported on this phone!");
        }

        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String info = ((TextView) view).getText().toString();
                if(info.equals("No Devices Found!") || info.equals("Bluetooth not supported on this phone!")){
                    Toast.makeText(getActivity(), "Please pair a car!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Get Mac Address and name
                String address = info.substring(info.length() - 17);
                String name = info.substring(0, info.length() - 17).trim();
                // Save and move off to drivers activity
                saveBluetoothDeviceToDevice(address, name);
                Intent launchDriversScreen = new Intent(getActivity(), DriversActivity.class);
                startActivity(launchDriversScreen);
            }
        });
    }



    private void getBlueToothAdapter(){
        localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if( localBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "This device does not have bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!localBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        }
    }

    private void saveBluetoothDeviceToDevice(String address, String name){
        SharedPreferences.Editor sharedPrefEdit = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
        sharedPrefEdit.putString(getString(R.string.current_bluetooth_device_address_save), address);
        sharedPrefEdit.putString(getString(R.string.current_bluetooth_device_name_save), name);
        sharedPrefEdit.commit();
    }

}
