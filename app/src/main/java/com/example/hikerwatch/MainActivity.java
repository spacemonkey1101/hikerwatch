package com.example.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationListener locationListener;
    LocationManager locationManager;
    TextView latitude,longitude,accuracy,altitude,address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            }

    }

    public  void updateLocation(Location location){
        latitude.setText("Latitude : " + location.getLatitude());
        longitude.setText("Longitude : " + location.getLongitude());
        accuracy.setText("Accuracy : "+ location.getAccuracy());
        altitude.setText("Altitude : "+location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),10);
            String string="";
            if(addressList.get(0).getSubThoroughfare() != null &&addressList.size()>0)
            {
                string += addressList.get(0).getSubThoroughfare();
                string += "\n";
            }
            if(addressList.get(0).getAdminArea() != null &&addressList.size()>0)
            {
                string += addressList.get(0).getAdminArea();
                string += "\n";
            }if(addressList.get(0).getLocality()!= null &&addressList.size()>0)
            {
                string += addressList.get(0).getLocality();
                string += "\n";
            }
            if(addressList.get(0).getCountryName() != null &&addressList.size()>0)
            {
                string += addressList.get(0).getCountryName();
                string += "\n";
            }
            if(addressList.get(0).getPostalCode() != null &&addressList.size()>0)
            {
                string += addressList.get(0).getPostalCode();
                string += "\n";
            }
            if(string.equals("")){
                string="Unknown";
            }
            address.setText("Address;"+string);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = findViewById(R.id.latitudeTextView);
        longitude = findViewById(R.id.longitudeTextView);
        accuracy=findViewById(R.id.accuracyTextView);
        altitude=findViewById(R.id.altitudeTextView);
        address = findViewById(R.id.addressTextView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               updateLocation(location);
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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc!= null)
            updateLocation(loc);

        }
    }
}
