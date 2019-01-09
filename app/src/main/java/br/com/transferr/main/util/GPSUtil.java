package br.com.transferr.main.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Andressa on 09/10/2017.
 * Edited by Lunider on 01/11/2017.
 */


public class GPSUtil implements LocationListener {

    public static final int MULTIPLE_PERMISSION_REQUEST_CODE = 123;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minuto

    Context context;
    public GPSUtil(Context context){
        this.context = context;
    }

    private LocationManager locationManager;

    private Location bestLocation;

    public boolean gpsIsEnabled() {
        //checkPermissionToUseLocation();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


/*
    private void checkPermissionToUseLocation() {
        boolean isLocationPermitted = PermissionUtil.hasAllPermissions((Activity) context,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

    }
*/
    @SuppressLint("MissingPermission")
    public LatLng getLocation() {
        if (!gpsIsEnabled()) {
            showGPSSettings();
            return null;
        } else {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            List<String> providers = null;
            if (locationManager != null) {
                providers = locationManager.getProviders(true);
            }
            bestLocation = null;
            if (providers != null) {
                for (String provider : providers) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location == null) {
                        continue;
                    }
                    if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                        bestLocation = location;
                    }
                }
            }
            LatLng bestLatLngLocation = null;
            if (bestLocation != null) {
                bestLatLngLocation = new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
            }
            return bestLatLngLocation;
        }
    }

    public void showGPSSettings() {

        new AlertDialog.Builder(context)
                .setTitle("Ativar GPS")
                .setMessage("Seu GPS está desativado, deseja ativá-lo?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
