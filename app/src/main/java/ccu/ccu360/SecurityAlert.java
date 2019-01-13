package ccu.ccu360;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.SEND_SMS;

public class SecurityAlert extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private Activity activity;
    private static final int REQ_PERMISSION = 10;
    private static final String TAG = "MainActivity";
    LocationManager locationManager;
    private Location location;

    public SecurityAlert(Activity _activity) {
        this.activity = _activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, list)){
            new AppSettingsDialog.Builder(activity).build().show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Log.d(TAG, "onActivityResult: ");
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        //code here
    }
    private class PhoneCallListener extends PhoneStateListener{
        private boolean isPhonecalling = false;
        public void onCallStateChanged(int state){
            if(TelephonyManager.CALL_STATE_OFFHOOK == state)
            {
                isPhonecalling = true;
            }
            else if(TelephonyManager.CALL_STATE_IDLE == state)
            {
                if(isPhonecalling){
                    //reboot App, back to activity
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    isPhonecalling = false;
                }
            }
        }
    }

    public String calling(){
        String PhoneNumber,message;
        ItemDAO itemDAO = new ItemDAO(activity.getApplicationContext());
        Map<String,String> user_data = itemDAO.getBaseData();
        if(user_data.get("Contact") .equals("-1")){
            Toast toast = Toast.makeText(activity,"請設定聯絡人",Toast.LENGTH_SHORT);
            toast.show();
            return "請設定聯絡人";
        }
        PhoneNumber = itemDAO.getContact_Phone(user_data.get("Contact"));


        WhereAmI whereAmI = new WhereAmI();


        message = user_data.get("Student_ID") + " " + user_data.get("Name") + " " + user_data.get("Department") + "發出求救，地點位於：";
        if(location != null)
            message += whereAmI.whereami(location.getLongitude(), location.getLatitude()) + " " + String.valueOf(location.getLongitude()) + " " + String.valueOf(location.getLatitude());
        else
            message += "系統無法取得使用者位置";
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

        String [] perms = {CALL_PHONE,READ_PHONE_STATE,SEND_SMS};
        if (EasyPermissions.hasPermissions(activity,perms)){
        }else {
            EasyPermissions.requestPermissions(activity,
                    "未允許「" + activity.getString(R.string.app_name) + "」打電話及管理通話權限，將使「" + activity.getString(R.string.app_name) + "」無法正常運作，是否重新設定權限？",
                    REQ_PERMISSION,
                    perms);
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(PhoneNumber,null,message,null,null);
        Toast toast = Toast.makeText(activity,"訊息已送出至 "+PhoneNumber,Toast.LENGTH_SHORT);
        toast.show();
        Intent intentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+PhoneNumber));
        activity.startActivity(intentDial);
        return message;
    }

    public void check_perrmission(){
        String [] perms = {CALL_PHONE,READ_PHONE_STATE,SEND_SMS,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(activity,perms)){
        }else {
            EasyPermissions.requestPermissions(activity,
                    "未允許「" + activity.getString(R.string.app_name) + "」打電話及管理通話權限，將使「" + activity.getString(R.string.app_name) + "」無法正常運作，是否重新設定權限？",
                    REQ_PERMISSION,
                    perms);
        }
    }



    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location_temp) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            location = location_temp;
            Log.d("AAAAAAA","AAAAAAAA");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

// Register the listener with the Location Manager to receive location updates



}
