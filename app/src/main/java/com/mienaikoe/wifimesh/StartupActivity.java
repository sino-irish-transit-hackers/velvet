package com.mienaikoe.wifimesh;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.mienaikoe.wifimesh.mesh.BluetoothMeshField;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshListeningMember;
import com.mienaikoe.wifimesh.mesh.BluetoothMeshParticipatingMember;

import java.util.ArrayList;


public class StartupActivity extends Activity {

    private TextView stateView;
    private TextView advertisersView;
    private ImageView mapView;


/*
    private BroadcastReceiver wifiMeshServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<String> peers = intent.getStringArrayListExtra(WifiMeshService.FIELD_PEERS);
            String status = intent.getStringExtra(WifiMeshService.FIELD_STATUS);
            Log.i(this.getClass().getName(), "Peers: " + peers.toString());
            Log.i(this.getClass().getName(), "Status: " + status);
            if( peers.size() > 0 ) {
                texties.setText("Peer Found: " + peers.get(0));
            } else {
                texties.setText("Finding Peers");
            }
        }
    };
*/

    private BroadcastReceiver bluetoothMeshServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        ArrayList<String> advertisers = intent.getStringArrayListExtra(BluetoothMeshField.ADVERTISERS.getLabel());
        String state = intent.getStringExtra(BluetoothMeshField.STATE.getLabel());
        Log.i(this.getClass().getName(), "Peers: " + advertisers.toString());
        Log.i(this.getClass().getName(), "State: " + state);

        stateView.setText(state);
        advertisersView.setText(advertisers.size() + " Advertisers");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, VelvetService.class));

        loadMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(bluetoothMeshServiceReceiver, new IntentFilter(BluetoothMeshField.INTENT.getLabel()));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothMeshServiceReceiver);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_test_mesh:
                openMeshTest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void openMeshTest(){

    }

    private void openLine(){
        Intent go = new Intent(  );
        startActivity(go);
    }


    private void loadMap(){
        setContentView(R.layout.activity_startup_activity);

        this.stateView = (TextView) findViewById(R.id.stateView);
        this.advertisersView = (TextView) findViewById(R.id.advertiserView);
        this.mapView = (ImageView) findViewById(R.id.mapView);

        // Map Stuff?
        Log.i(this.getClass().getSimpleName(), "Map Loaded");
    }

}
