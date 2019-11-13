package halla.icsw.gpsapi;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private ArrayList<bus> buses;
    private ArrayList<MarkerOptions> markers;
    private GoogleMap mMap;
    private Marker oldMarker=null;
    String strHtml="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Handler h=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                HTMLParse();
            }
        };
        new wokerThread(h).start();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        for (bus b: buses) {
            MarkerOptions marker = new MarkerOptions();
            LatLng pos = new LatLng(b.getX(),b.getY());
            marker.position(pos);
            marker.title(b.getName());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
            mMap.addMarker(marker);
            markers.add(marker);
        }
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

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
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }catch(SecurityException e){

        }
        // Add a marker in Sydney and move the camera
    }
    class wokerThread extends Thread{
        Handler h;

        String strLine;
        wokerThread(Handler h){this.h=h;}
        @Override
        public void run() {
            try {
                URL url = new URL("http://its.wonju.go.kr/map/RoutePosition.do?route_id=251000154");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while((strLine=in.readLine())!=null){
                    strHtml+=strLine;
                }
                in.close();

            } catch (Exception e) {
                System.out.println("네트워크 에러");
            }
        }
    }
    void HTMLParse(){

        try {
            int xstart =0;
            int xend=0;
            for(int i=0; i<=2; i++) {
                xstart = strHtml.indexOf("_X\":", xend);
                xend = strHtml.indexOf(",", xstart);
                int yStart=strHtml.indexOf("_Y\":",xend);
                int yEnd=strHtml.indexOf(",",yStart);
                int nStart=strHtml.indexOf("_NO\":\"",yEnd);
                int nEnd=strHtml.indexOf("\"",nStart);
                bus bs=new bus(Float.valueOf(strHtml.substring(xstart + 4, xend)),Float.valueOf(strHtml.substring(yStart+4,yEnd)),strHtml.substring(nStart+7,nEnd));
                buses.add(bs);
                System.out.println(buses.get(i-1).toString());
            }

        }catch (Exception e){
            System.out.println("파싱에러");
        }
    }
    class bus{
        private float x;
        private float y;
        private String name;

        public bus(float x, float y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

