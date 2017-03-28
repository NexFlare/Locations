package com.nexflare.locations;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Per";
    TextView lati,longi;
    Button btn;

    @Override
    protected void onStart() {
        if(!isLocationAvailable()){
            AlertDialog.Builder build=new AlertDialog.Builder(this);
            build.setMessage("CLICK OK ENABLE LOCATIONS ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            build.show();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lati= (TextView) findViewById(R.id.Lati);
        longi= (TextView) findViewById(R.id.Logi);
        btn= (Button) findViewById(R.id.button);
        if(!isLocationAvailable()){
            AlertDialog.Builder build=new AlertDialog.Builder(this);
            build.setMessage("CLICK OK ENABLE LOCATIONS ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            build.show();
        }
        if(longi.getText().toString()==""){
            btn.setEnabled(false);
        }
        else{
            btn.setEnabled(true);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,MapsActivity.class);
                i.putExtra("longitude",longi.getText().toString());
                i.putExtra("latitide",lati.getText().toString());
                startActivity(i);
            }
        });
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longi.setText(String.valueOf(location.getLongitude()));
                lati.setText(String.valueOf(location.getLatitude()));
                if(String.valueOf(location.getLongitude())==""){
                    btn.setEnabled(false);
                }
                else{
                    btn.setEnabled(true);
                }
                Log.d(TAG, "onLocationChanged: latitude "+location.getLatitude());
                Log.d(TAG, "onLocationChanged: longitude "+location.getLongitude());
                Log.d(TAG, "onLocationChanged: Angle from North "+location.getBearing());
                Log.d(TAG, "onLocationChanged: Altitide"+location.getAltitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1230);
            return;
        }

            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    10,
                    locationListener
            );
            lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    10,
                    locationListener
            );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1230){
            Log.d(TAG, "onRequestPermissionsResult: "+"Requseted for permission");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    boolean isLocationAvailable(){
        LocationManager location= (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        boolean gpsenabled=false;
        if(location.isProviderEnabled(location.GPS_PROVIDER)){
            gpsenabled=true;
        }

        return gpsenabled;
    }
}
