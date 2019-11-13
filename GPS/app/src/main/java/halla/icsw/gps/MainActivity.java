package halla.icsw.gps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView status;
    float latitude;
    float longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status=findViewById(R.id.status);
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //새로운 위치가 발견되면 위치 제공자에 의하여 호출
                status.setText("위도: "+location.getLatitude()+"\n경도: "+location.getLongitude()+"\n고도: "+location.getAltitude());
                latitude=(float)location.getLatitude();//위도
                longitude=(float)location.getLongitude();//경도
                Uri uri=Uri.parse(String.format("geo:%f,%f?z=14",latitude,longitude));
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
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
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }catch (SecurityException e){
        }

    }
}
