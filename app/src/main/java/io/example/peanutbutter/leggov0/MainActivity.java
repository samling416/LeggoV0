package io.example.peanutbutter.leggov0;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static io.example.peanutbutter.leggov0.RecyclerAdapter.ScaleFactor;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.CancelableCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    // Constants
    public static final String TAG = "MainActivity";
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    public static final LatLngBounds NEWZEALAND = new LatLngBounds(new LatLng(-47.25, 163.95),
            new LatLng(-33.967, 179.73));
    public static final LatLng CENTER = new LatLng(-41.291, 173.45);
    public static final int PLACE_PICKER_REQUEST = 1;
    public static final int ZOOM_IN_ANIMATION = 0;
    public static final int ZOOM_OUT_ANIMATION = 1;
    public static final int PAN_TO_DESTINATION_ANIMATION = 2;
    public static final int vertical = 0, horizontal = 1;
    public static final int EXPANDED = 0, COLLAPSED = 1;

    // UI elements
    private ImageButton mMyLocation;
    private TextView mSearchText;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mRecyclerView;
    private CustomLayoutManager mLinearLayoutManager;
    private ViewGroup.LayoutParams mRecyclerViewParams;
    private ViewGroup.LayoutParams mMapParams;
    private SupportMapFragment mapFragment;
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private View mBottomSheet;

    private boolean permissionflag;
    private GoogleMap mMap;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mCameraAnimation;
    private Boolean locationFetched = false;
    private LatLng currentLoc;
    private ArrayList<DiscoverTile> mDiscoverTiles;
    private RecyclerAdapter mAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private SnapHelper helper;
    private int mRecyclerviewState = COLLAPSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        //Create Location Services Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get dimensions of screen for UI interactions.
        getScreenDimensions();

        // Load data
        mDiscoverTiles = new ArrayList<DiscoverTile>();
        ArrayList<Integer> item1activities = new ArrayList<>();
        ArrayList<Integer> item2activities = new ArrayList<>();
        ArrayList<Integer> item3activities = new ArrayList<>();
        item1activities.add(R.drawable.ic_kayaking);
        item1activities.add(R.drawable.ic_swimming);
        item1activities.add(R.drawable.ic_tent);
        item2activities.add(R.drawable.ic_hiking);
        item2activities.add(R.drawable.ic_running);
        item3activities.add(R.drawable.ic_running);
        item3activities.add(R.drawable.ic_kayaking);
        mDiscoverTiles.add(new DiscoverTile("Wanaka", (R.drawable.wanaka), item1activities, -44.71, 169.13));
        mDiscoverTiles.add(new DiscoverTile("Auckland", (R.drawable.discover), item2activities, CENTER.latitude, CENTER.longitude));
        mDiscoverTiles.add(new DiscoverTile("Taupo", (R.drawable.group), item3activities, -38.67, 176.075));
        mDiscoverTiles.add(new DiscoverTile("Mt.Eden", R.drawable.profile, item1activities, -36.877, 174.764));


        // Set UI elements
        mMyLocation = (ImageButton) findViewById(R.id.location_button);
        mMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.isMyLocationEnabled()) {
                    fetchLastLocation();
                }
            }
        });
        mSearchText = (TextView) findViewById(R.id.search_text);
        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Construct Intent to launch place picker activity.
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(MainActivity.this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, PLACE_PICKER_REQUEST); // Result should be placename and coordinates
                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    //showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), MainActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(MainActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        checkPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        checkPermissions();
    }

    // When expecting a result from calling another activity. Note that fragments cannot be replaced
    // in here.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Provide results based on location picked in placepicker.
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                /*String toastPlaceNameMsg = String.format("Place selected: %s", place.getName());
                Toast.makeText(this, toastPlaceNameMsg, Toast.LENGTH_LONG).show();*/

                // Update map
                searchDestination((String) place.getName(), place.getLatLng());
            }
        }
    }


    /* Map Code*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");

        mMap = googleMap;

        // Initialize map
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);
        mMap.setLatLngBoundsForCameraTarget(NEWZEALAND);
        mMap.setMinZoomPreference(5);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER, 5));
        mMap.getUiSettings().setCompassEnabled(false);

        if (permissionflag) {
            mMap.setMyLocationEnabled(true);
            fetchLastLocation();

            /*for (int i = 0; i < mDiscoverTiles.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mDiscoverTiles.get(i).getLat(), mDiscoverTiles.get(i).getLng())).title(mDiscoverTiles.get(i).getName()));
            }*/
        }

        initializeRecyclerView();
        initializeBottomSheet();
    }

    public void searchDestination(String text, LatLng destination) {
        Log.d(TAG, "updateSearchBar: ");

        panToDestination(destination);

        // Update TextView showing location
        mSearchText.setText(text);
    }

    public void panToDestination(LatLng destination) {
        // Pan map to destination

        if (mMap.getCameraPosition().zoom <= 8) {
            //If initial position is zoomed out, move to new destination and zoom in.
            mCameraAnimation = ZOOM_IN_ANIMATION;
            mMap.animateCamera(CameraUpdateFactory.newLatLng(destination), this);
        } else {
            // If initial position is zoomed in, move to new destination but don't zoom in.
            mMap.animateCamera(CameraUpdateFactory.newLatLng(destination), 1500, null);
        }
    }

    @Override
    public void onFinish() {
        switch (mCameraAnimation) {
            case ZOOM_IN_ANIMATION:
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);
                break;
            case ZOOM_OUT_ANIMATION:
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5), 1500, null);
                break;
        }
    }

    @Override
    public void onCancel() {

    }

    /*Location Services*/
    @SuppressWarnings("MissingPermission")
    public void fetchLastLocation() {
        Log.d(TAG, "getLastLocation: Start");
        // Retrieve users current location and show on map initialisation
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    Log.d(TAG, "onSuccess: Failed.");
                    currentLoc = CENTER;
                }
                locationFetched = true;
                panToDestination(currentLoc);
                locationFetched = false;
            }
        });
    }

    /*UI and Animations*/
    public void initializeRecyclerView() {

        mRecyclerViewParams = mRecyclerView.getLayoutParams();
        mRecyclerViewParams.width = 2 * mScreenWidth;
        mRecyclerView.setLayoutParams(mRecyclerViewParams);
        mAdapter = new RecyclerAdapter(mDiscoverTiles, MainActivity.this);
        mLinearLayoutManager = new CustomLayoutManager(this, LinearLayoutManager.HORIZONTAL, false, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(0, horizontal, mScreenWidth, mScreenHeight));
        mRecyclerView.setAdapter(mAdapter);


        //ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void hideRecyclerView() {
        //Log.d(TAG, "hideRecyclerView: HidingRecyclerView");
        enableRecyclerViewScroll(false);
    }

    public void showRecyclerView() {
        //Log.d(TAG, "showRecyclerView: Showing RecyclerView");
        enableRecyclerViewScroll(true);
        mRecyclerView.setOnFlingListener(null);
        helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    public void enableRecyclerViewScroll(boolean enable){
        mLinearLayoutManager.setScrollEnabled(enable);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    public void initializeBottomSheet(){
        // Initialize bottomsheet
        mBottomSheetBehavior = (CustomBottomSheetBehavior) CustomBottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setActivity(MainActivity.this);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(mScreenHeight/11);
        mBottomSheetBehavior.setHideable(false);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:{
                        hideRecyclerView();
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED:{
                        showRecyclerView();
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void getScreenDimensions() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }


    /*Permissions*/
    public void checkPermissions() {
        Log.d(TAG, "checkPermissions: ");
        //Check Runtime Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionflag = false;
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                // PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permissions have been allowed
            permissionflag = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    permissionflag = true;


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissionflag = false;
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.title_location_permission)
                            .setMessage(R.string.text_location_permission)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                }
                            })
                            .create()
                            .show();
                    /*this.finish();
                    System.exit(0);*/

                }
            }
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getRecyclerviewState() {
        return mRecyclerviewState;
    }

    public void setRecyclerviewState(int recyclerviewState) {
        mRecyclerviewState = recyclerviewState;
    }
}
