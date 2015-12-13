package com.example.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ShareActionProvider;

/**
 * 获取经纬度的service
 * 
 * @author zhaimeng
 * 
 */
public class LocationService extends Service {
	private LocationManager lm;
	MyLocationListener listener;
	SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		listener = new MyLocationListener();
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria, true);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			mPref.edit().putString(
					"location","j:" + location.getLongitude() 
							+ "; w:" + location.getLatitude()).commit();
			stopSelf();
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
	}
}
