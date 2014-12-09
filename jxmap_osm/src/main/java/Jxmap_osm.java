package justine.robocar;


import java.awt.BorderLayout;

import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;

import java.io.File;

import java.lang.Math.*;

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashSet;

import java.util.List;

import java.util.Set;

import java.util.Collections;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.event.MouseInputListener;


import org.jxmapviewer.JXMapViewer;

import org.jxmapviewer.OSMTileFactoryInfo;

import org.jxmapviewer.input.CenterMapListener;

import org.jxmapviewer.input.PanKeyListener;

import org.jxmapviewer.input.PanMouseInputListener;

import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;

import org.jxmapviewer.viewer.DefaultTileFactory;

import org.jxmapviewer.viewer.GeoPosition;

import org.jxmapviewer.viewer.LocalResponseCache;

import org.jxmapviewer.viewer.TileFactoryInfo;

import org.jxmapviewer.viewer.DefaultWaypoint;

import org.jxmapviewer.viewer.Waypoint;

import org.jxmapviewer.viewer.WaypointPainter;

import org.jxmapviewer.painter.CompoundPainter;

import org.jxmapviewer.painter.Painter;



public class Jxmap_osm
{


    public static void main (String[]args)
    {

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo ();

        DefaultTileFactory tileFactory = new DefaultTileFactory (info);

        tileFactory.setThreadPoolSize (8);


        // Setup local file cache
        File cacheDir =
            new File (System.getProperty ("user.home") + File.separator +
                      ".jxmapviewer2");

        LocalResponseCache.installResponseCache (info.getBaseURL (), cacheDir,
                false);


        // Setup JXMapViewer
        final JXMapViewer mapViewer = new JXMapViewer ();

        mapViewer.setTileFactory (tileFactory);

        String line;

        String szam = null;

        ArrayList < Double > lat = new ArrayList < Double > ();

        ArrayList < Double > lon = new ArrayList < Double > ();
        ArrayList < ArrayList  <Double>> waylatok = new ArrayList < ArrayList <Double> > ();
        ArrayList < ArrayList<Double> > waylonok = new ArrayList < ArrayList <Double>> ();

        ArrayList < ArrayList <ArrayList<Double>> > latok = new ArrayList < ArrayList <ArrayList<Double>> > ();

        ArrayList < ArrayList<ArrayList<Double>> > lonok = new ArrayList < ArrayList<ArrayList<Double>> > ();

        ArrayList < GeoPosition > ut = new ArrayList < GeoPosition > ();

        String[]busz_szama = new String[200];


        File file = new File ("bus_way_coords.txt");

        int iter = 0;
        int wayiter=0;



        try
        {

            FileReader fr = new FileReader (file);

            BufferedReader br = new BufferedReader (fr);


            while ((line = br.readLine ()) != null)
            {

                String[]st = line.split ("\\s+");

                if (st[1].equals ("busz") || st[1].equals ("(Airport)")
                        || st[1].equals ("György"))

                {


                    busz_szama[iter] = st[0];

                    iter++;

                    latok.add (new ArrayList <ArrayList< Double >> (waylatok));


                    waylatok.clear ();

                    lonok.add (new ArrayList <ArrayList< Double >> (waylonok));

                    waylonok.clear ();


                }

                else if(st[0].equals("way"))
                {
                    waylatok.add (new ArrayList < Double > (lat));


                    lat.clear ();

                    waylonok.add (new ArrayList < Double > (lon));

                    lon.clear ();
                }

                else if (st.length > 0)
                {


                    lat.add (Double.valueOf (st[0]));

                    lon.add (Double.valueOf (st[1]));

                }


            }


        }
        catch (IOException e)
        {

            e.printStackTrace ();

        }


        int meddig = 0;

        for (int i = 0; i < iter; i++)

        {

            if (args[0].compareTo (busz_szama[i]) == 0)

            {

                meddig = i;

            }

        }

        ArrayList<RoutePainter> routepainters=new ArrayList<RoutePainter>();
        ArrayList<GeoPosition> geok=new ArrayList<GeoPosition>();
        ArrayList<GeoPosition> geok2=new ArrayList<GeoPosition>();
        GeoPosition temp= new GeoPosition(0,0);
        double temptav=0;
        GeoPosition temp2= new GeoPosition(0,0);
        double temptav2=0;
        for(int j=0; j<latok.get(meddig).size(); j++) {
            for(int k=0; k<latok.get(meddig).get(j).size(); k++) {
                ut.add (new GeoPosition (latok.get(meddig).get(j).get(k), lonok.get(meddig).get(j).get(k)));
                geok.add(new GeoPosition (latok.get(meddig).get(j).get(k), lonok.get(meddig).get(j).get(k)));
            }
            if(j!=latok.get(meddig).size()-1) {
                for(int k=0; k<latok.get(meddig).get(j+1).size(); k++)
                    geok2.add(new GeoPosition (latok.get(meddig).get(j+1).get(k), lonok.get(meddig).get(j+1).get(k)));
                if(tavolsag(geok.get(geok.size()-1),geok2.get(geok2.size()-1))<=tavolsag(geok.get(geok.size()-1),geok2.get(0))) {


                    temp=geok2.get(geok2.size()-1);
                    temptav=tavolsag(geok.get(geok.size()-1),geok2.get(geok2.size()-1));
                }
                else {

                    temp=geok2.get(0);
                    temptav=tavolsag(geok.get(geok.size()-1),geok2.get(0));
                }

                if(tavolsag(geok.get(0),geok2.get(geok2.size()-1))<=tavolsag(geok.get(0),geok2.get(0)))
                {
                    temp2=geok2.get(geok2.size()-1);
                    temptav2=tavolsag(geok.get(0),geok2.get(geok2.size()-1));
                }
                else
                {
                    temp2=geok2.get(0);
                    temptav2=tavolsag(geok.get(0),geok2.get(0));
                }
                if (temptav<=temptav2) {
                    geok.add(temp);
                }
                else {
                    ArrayList<GeoPosition>geok_copy=new ArrayList<GeoPosition>();
                    geok_copy.add(temp2);
                    geok_copy.addAll(geok);
                    geok=geok_copy;
                }
            }
            routepainters.add(new RoutePainter(new ArrayList<GeoPosition>(geok)));
            geok.clear();
            geok2.clear();
        }

        // Set the focus
        mapViewer.setZoom (7);

        mapViewer.setAddressLocation (ut.get (0));


        // Add interactions
        MouseInputListener mia = new PanMouseInputListener (mapViewer);

        mapViewer.addMouseListener (mia);

        mapViewer.addMouseMotionListener (mia);


        mapViewer.addMouseListener (new CenterMapListener (mapViewer));


        mapViewer.
        addMouseWheelListener (new ZoomMouseWheelListenerCursor (mapViewer));


        mapViewer.addKeyListener (new PanKeyListener (mapViewer));


        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter (mapViewer);

        SelectionPainter sp = new SelectionPainter (sa);

        mapViewer.addMouseListener (sa);

        mapViewer.addMouseMotionListener (sa);

        mapViewer.setOverlayPainter (sp);


        // Display the viewer in a JFrame
        final JFrame frame = new JFrame ();

        frame.setLayout (new BorderLayout ());

        frame.
        add (new
             JLabel (busz_szama[meddig] +
                     " busz útvonala. (Bal egérgomb: navigálás a térképen | Görgő: zoomolás)"),
             BorderLayout.NORTH);

        frame.add (mapViewer);

        frame.setSize (800, 600);

        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        frame.setVisible (true);


        mapViewer.addPropertyChangeListener ("zoom",
                                             new PropertyChangeListener ()
        {

            @Override
            public void
            propertyChange
            (PropertyChangeEvent evt)
            {

                updateWindowTitle (frame,
                                   mapViewer);

            }
        });


        mapViewer.addPropertyChangeListener ("center",
                                             new PropertyChangeListener ()
        {

            @Override
            public void
            propertyChange
            (PropertyChangeEvent evt)
            {

                updateWindowTitle (frame,
                                   mapViewer);

            }
        });

        // Set the focus
        mapViewer.zoomToBestFit (new HashSet < GeoPosition > (ut), 0.7);


        // Create waypoints from the geo-positions
        Set < Waypoint > waypoints =
            new HashSet < Waypoint >
        (Arrays.
         asList (
             new DefaultWaypoint (ut.get (0)),
             new DefaultWaypoint (ut.get (ut.size () - 1))));

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter < Waypoint > waypointPainter =
            new WaypointPainter < Waypoint > ();

        waypointPainter.setWaypoints (waypoints);


        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List < Painter < JXMapViewer >> painters =
            new ArrayList < Painter < JXMapViewer >> ();

        for(RoutePainter i:routepainters)
            painters.add (i);

        painters.add (waypointPainter);


        CompoundPainter < JXMapViewer > painter =
            new CompoundPainter < JXMapViewer > (painters);

        mapViewer.setOverlayPainter (painter);


        updateWindowTitle (frame, mapViewer);

    }

    protected static void updateWindowTitle (JFrame frame,
            JXMapViewer mapViewer)
    {

        double lat = mapViewer.getCenterPosition ().getLatitude ();

        double lon = mapViewer.getCenterPosition ().getLongitude ();

        int zoom = mapViewer.getZoom ();


        frame.setTitle (String.
                        format ("Busz útvonala (%.2f / %.2f) - Zoom: %d", lat,
                                lon, zoom));

    }
    public static double tavolsag(GeoPosition elso, GeoPosition masodik)
    {
        return Math.sqrt(Math.pow(masodik.getLatitude()-elso.getLatitude(),2)+Math.pow(masodik.getLongitude()-elso.getLongitude(),2));
    }

}
