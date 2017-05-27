package ca.ualberta_ecocar.ecocar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import ca.ualberta_ecocar.ecocar.util.CarDataPacket;
import ca.ualberta_ecocar.ecocar.util.EcoCarDatabase;
import ca.ualberta_ecocar.ecocar.util.EcoCarGPS;
import ca.ualberta_ecocar.ecocar.util.TimerDataPacket;
import ca.ualberta_ecocar.ecocar.util.WebDataPacket;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

// Used https://stackoverflow.com/questions/13450406/how-to-receive-serial-data-using-android-bluetooth as reference
public class DriversActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BluetoothAdapter currentBTAdapter;
    private BluetoothDevice currentCarDevice;
    private BluetoothSocket CarSocket;
    private OutputStream CarOutputStream;
    private InputStream CarInputStream;

    private ActiveDriverInfoFragment ActiveInfoFragment;

    Thread workerThread;
    final Handler handler = new Handler();
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    EcoCarDatabase carDatabase;
    EcoCarGPS CarGPS;
    Socket socket;
    Gson gson;

    // TODO: Change for race!
    private boolean IS_TEST_BENCH = true;
    private boolean USE_GPS_SPEED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);
        carDatabase = new EcoCarDatabase(this, false);
        socket = carDatabase.getActiveSocket();
        gson = carDatabase.getGson();
        setupTimerSocket();
        CarGPS = new EcoCarGPS(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        tryToConnectToCar();
    }

    public void tryToConnectToCar(){
        if(getCarConnection()){
            try {
                openConnectionToCar();
            } catch (IOException e) {
                // TODO: Throw error and restart
                e.printStackTrace();
                showBluetoothErrorFragment();
                return;
            }
            showDriversDisplayFragment();
            startListeningForData();
        }
        // Could not connect to car
        else{
            showBluetoothErrorFragment();
        }
    }

    public void showBluetoothErrorFragment(){
        Fragment BTErrorFragment = new BluetoothErrorFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.DriversCurrentFragment, BTErrorFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showDriversDisplayFragment(){
        ActiveInfoFragment = new ActiveDriverInfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.DriversCurrentFragment, ActiveInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupTimerSocket(){
        socket.on("time_update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new GsonBuilder().create();
                JSONObject obj = (JSONObject)args[0];
                TimerDataPacket newTimerPacket = gson.fromJson(obj.toString(), TimerDataPacket.class);
                ActiveInfoFragment.updateTimeData(newTimerPacket);
            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drivers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean getCarConnection() {
        currentBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(currentBTAdapter == null)
        {
            // Should toast error
            return false;
        }

        if(!currentBTAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        String btDeviceName = getSavedCarName();
        if (btDeviceName.equals("failed")){
            // TODO: Show toast error
            return false;
        }
        Set<BluetoothDevice> pairedDevices = currentBTAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(btDeviceName))
                {
                    currentCarDevice = device;
                    return true;
                }
            }
        }
        return false;
    }

    public String getSavedCarName(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String carName = sharedPref.getString(getString(R.string.current_bluetooth_device_name_save), "failed");
        return carName;
    }

    void openConnectionToCar() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        CarSocket = currentCarDevice.createRfcommSocketToServiceRecord(uuid);
        CarSocket.connect();
        CarOutputStream = CarSocket.getOutputStream();
        CarInputStream = CarSocket.getInputStream();
    }

    void startListeningForData()
    {
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = CarInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            CarInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;



                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //Pass clean JSON string along
                                            formatData(data);
                                        }
                                    });

                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    void formatData(String data){
        try {
            Gson gson = new GsonBuilder().create();
            CarDataPacket newPacket = gson.fromJson(data, CarDataPacket.class);
            // Override for using GPS Speed. Not recommended.
            if(USE_GPS_SPEED){
                if(CarGPS != null){
                    newPacket.setSpeed(CarGPS.getCurrentGPSSpeed());
                }
            }
            ActiveInfoFragment.updateBluetoothOnScreenData(newPacket);
            Double lat = 0.0;
            Double lng = 0.0;
            if (CarGPS != null && CarGPS.getLocation() != null){
                lat = CarGPS.getLocation().getLatitude();
                lng = CarGPS.getLocation().getLongitude();
                Log.v("CarGPS", "Lat: " + lat + " Lng: " + lng);
            }
            if (IS_TEST_BENCH){
                // Disable GPS for testing (Keep privacy)
                WebDataPacket web = new WebDataPacket(newPacket,0,0);
                carDatabase.sendWebDataPacketToServer(web);
                return;
            }
            WebDataPacket web = new WebDataPacket(newPacket,lat,lng);
            carDatabase.sendWebDataPacketToServer(web);
        }
        catch (JsonParseException e) {
            // TODO: Log malformed json error
            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
            Log.wtf("JSON Error", "Bad Json");
        }
    }
}
