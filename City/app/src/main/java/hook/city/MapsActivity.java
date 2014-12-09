package hook.city;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.res.AssetManager;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.view.View;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    String line=null;
    //String [] lat=new String[500];
    //String [] lon=new String[500];
    ArrayList<String> lat= new ArrayList<String>();
    ArrayList<String> lon= new ArrayList<String>();
    ArrayList<ArrayList<ArrayList<String>>> latok= new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<ArrayList<String>>> lonok= new ArrayList<ArrayList<ArrayList<String>>>();
    ArrayList<ArrayList<String>>waylatok=new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>>waylonok=new ArrayList<ArrayList<String>>();

    String [] busz_szama=new String[200];
    //String [] bus_way_coords_lon = new String[30000];
    ArrayList<String> busz = new ArrayList<String>();
    //ArrayList<String> busz_szama= new ArrayList<String>();
    int hanyas = 1;
    int i=0;
    String egy = "47.54747390";
    String ketto = "21.62453120";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        int iter=0;
        try
        {
            InputStream in = getAssets().open("bus_way_coords.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            int j=0;


            while ((line = br.readLine ()) != null)
            {

                String[]st = line.split ("\\s+");

                if (st[1].equals ("busz") || st[1].equals ("(Airport)")
                        || st[1].equals ("György"))

                {


                    busz_szama[iter] = st[0];

                    iter++;

                    latok.add (new ArrayList <ArrayList< String >> (waylatok));


                    waylatok.clear ();

                    lonok.add (new ArrayList <ArrayList< String >> (waylonok));

                    waylonok.clear ();


                }

                else if(st[0].equals("way"))
                {
                    waylatok.add (new ArrayList < String > (lat));


                    lat.clear ();

                    waylonok.add (new ArrayList < String > (lon));

                    lon.clear ();
                }

                else if (st.length > 0)
                {


                    lat.add (st[0]);

                    lon.add (st[1]);

                }


            }


        }
        catch (IOException e)
        {

            e.printStackTrace ();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(47.54747390, 21.62453120), 16));


        //String egy = "47.54747390";
        //String ketto = "21.62453120";
        /*ArrayList<String> tmp1 = new ArrayList<String>();
        tmp1=lonok.get(0);
        String y = tmp1.get(hanyas);*/

        /*mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(egy), Double.parseDouble(ketto))).title("Marker"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.52971580, 21.61245630)).title("Marker"));
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(47.54747390, 21.62453120))  // Sydney
                .add(new LatLng(47.53058000, 21.61894500))  // Fiji
                .add(new LatLng(47.52971580, 21.61245630))  // Hawaii
        );*/

    }

    public void chooseBus(View view) {
        //String wat=busz_szama.get(hanyas);
        //String miez = bus_way_coords_lon[i-2];
        mMap.clear();

        String kezdo_lat = latok.get(hanyas).get(0).get(0);
        String veg_lat = latok.get(hanyas).get(latok.get(hanyas).size()-1).get(latok.get(hanyas).get(latok.get(hanyas).size()-1).size()-1);
        //ArrayList<String> tmp1 = new ArrayList<String>();
        //tmp1=lonok.get(hanyas).get(0);
        String kezdo_lon = lonok.get(hanyas).get(0).get(0);
        String veg_lon = lonok.get(hanyas).get(lonok.get(hanyas).size()-1).get(lonok.get(hanyas).get(lonok.get(hanyas).size()-1).size()-1);

        ((android.widget.Button)findViewById(R.id.chooseBus)).setText(busz_szama[hanyas]);
        hanyas++;
        if(hanyas==latok.size()){
            hanyas=0;
        }


        Marker kezdopont = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(kezdo_lat), Double.parseDouble(kezdo_lon))));
        kezdopont.setVisible(true);
        LatLng pont = new LatLng(Double.valueOf(kezdo_lat), Double.valueOf(kezdo_lon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pont));
        Marker vegpont = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(veg_lat), Double.parseDouble(veg_lon))));
        kezdopont.setVisible(true);

        for(int k=0;k<latok.get(hanyas).size();k++){
            ArrayList<String> tmp = new ArrayList<String>(latok.get(hanyas).get(k));
            //tmp=latok.get(hanyas).get(k);
            ArrayList<String> tmp1 = new ArrayList<String>(lonok.get(hanyas).get(k));



            //tmp1=lonok.get(hanyas).get(k);
            if(k<latok.get(hanyas).size()-2){
            String kovetkezo_eleje1=latok.get(hanyas).get(k+1).get(0);
            String kovetkezo_eleje2=lonok.get(hanyas).get(k+1).get(0);
            String kovetkezo_vege1=latok.get(hanyas).get(k+1).get(latok.get(hanyas).get(k+1).size()-1);
            String kovetkezo_vege2=lonok.get(hanyas).get(k+1).get(lonok.get(hanyas).get(k+1).size()-1);
            double tav1,tav2;
                String hozzafuz1,hozzafuz2,hozzafuz3,hozzafuz4;
                if(tavolsag(tmp.get(tmp.size()-1),tmp1.get(tmp.size()-1),kovetkezo_vege1,kovetkezo_vege2)<=tavolsag(tmp.get(tmp.size()-1),tmp1.get(tmp.size()-1),kovetkezo_eleje1,kovetkezo_eleje2))
                {   //vége-eleje,vége-vége
                    hozzafuz1=kovetkezo_vege1;
                    hozzafuz2=kovetkezo_vege2;
                    tav1=tavolsag(tmp.get(tmp.size()-1),tmp1.get(tmp.size()-1),kovetkezo_vege1,kovetkezo_vege2);}
                else
                {
                    hozzafuz1=kovetkezo_eleje1;
                    hozzafuz2=kovetkezo_eleje2;
                    tav1=tavolsag(tmp.get(tmp.size()-1),tmp1.get(tmp.size()-1),kovetkezo_eleje1,kovetkezo_eleje2);
                }
                if(tavolsag(tmp.get(0),tmp1.get(0),kovetkezo_vege1,kovetkezo_vege2)<=tavolsag(tmp.get(0),tmp1.get(0),kovetkezo_eleje1,kovetkezo_eleje2))
                {//eleje-eleje,eleje-vége
                    hozzafuz3=kovetkezo_vege1;
                    hozzafuz4=kovetkezo_vege2;
                    tav2=tavolsag(tmp.get(0),tmp1.get(0),kovetkezo_vege1,kovetkezo_vege2);}
                else
                {
                    hozzafuz3=kovetkezo_eleje1;
                    hozzafuz4=kovetkezo_eleje2;
                    tav2=tavolsag(tmp.get(0),tmp1.get(0),kovetkezo_eleje1,kovetkezo_eleje2);
                }
                if(tav1<=tav2)
                {
                    tmp.add(hozzafuz1);
                    tmp1.add(hozzafuz2);
                }
                else
                {
                    ArrayList<String> tmp_masolat = new ArrayList<String>();
                    ArrayList<String> tmp1_masolat = new ArrayList<String>();
                    tmp_masolat.add(hozzafuz3);
                    tmp1_masolat.add(hozzafuz4);
                    tmp_masolat.addAll(tmp);
                    tmp1_masolat.addAll(tmp1);
                    tmp=tmp_masolat;
                    tmp1=tmp1_masolat;
                }

            }

        Polyline utvonal = mMap.addPolyline(new PolylineOptions());
        List<LatLng> points = new ArrayList<LatLng>();
        for(int it=0;it<tmp.size();it++){
            LatLng temp=new LatLng(Double.parseDouble(tmp.get(it)),Double.parseDouble(tmp1.get(it)));
            points.add(temp);
        }
        utvonal.setPoints(points);
        utvonal.setColor(-65281);
        utvonal.setVisible(true);}


                // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        // Kabloey
    }
    public static double tavolsag(String elso, String masodik, String melso, String mmasodik)
    {
        //System.out.println(masodik.getLatitude()+" "+elso.getLatitude());
        //System.out.println(Math.pow(masodik.getLatitude()-elso.getLatitude(),2)+Math.pow(masodik.getLongitude()-elso.getLongitude(),2));
        return Math.sqrt(Math.pow(Double.parseDouble(melso)- Double.parseDouble(elso),2)+Math.pow(Double.parseDouble(mmasodik)-Double.parseDouble(masodik),2));
    }

}
