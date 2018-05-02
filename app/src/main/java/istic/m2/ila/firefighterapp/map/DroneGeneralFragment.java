package istic.m2.ila.firefighterapp.map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.eventbus.drone.UnSelectPathPointMessage;
import istic.m2.ila.firefighterapp.map.Drone.Drawings.PathPointDrawing;
import istic.m2.ila.firefighterapp.map.Drone.fragments.DroneListPictureFragment;
import istic.m2.ila.firefighterapp.map.Drone.fragments.DroneListViewFragment;
import istic.m2.ila.firefighterapp.map.Drone.fragments.DroneMapFragment;

/**
 * Created by markh on 02/05/2018.
 */

public class DroneGeneralFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private static final String TAG = "DroneGeneralFragment";

    /**
     * Contexte
     */
    public Context _context;

    private View _view;


    private DroneListPictureFragment _listPictureFrag;
    private DroneMapFragment _droneMapFrag;
    private DroneListViewFragment _droneListFrag;

    public DroneGeneralFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(TAG, "OnCreateView");
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_drone_general, container, false);

        InitUI();

        return _view;
    }

    private void InitUI()
    {
        _listPictureFrag = new DroneListPictureFragment();
        _droneMapFrag = new DroneMapFragment();
        _droneListFrag = new DroneListViewFragment();

        getFragmentManager().beginTransaction().replace(R.id.mapDroneContainer, _droneMapFrag).commit();
        getFragmentManager().beginTransaction().replace(R.id.photoListFragmentLayout, _listPictureFrag).commit();
        getFragmentManager().beginTransaction().replace(R.id.listViewDroneContainer, _droneListFrag).commit();

        getFragmentManager().beginTransaction().hide(_listPictureFrag).commit();
        getFragmentManager().beginTransaction().show(_droneMapFrag).commit();
        getFragmentManager().beginTransaction().show(_droneListFrag).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //region events
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onClickPassagePoint(PathPointDrawing pathPoint){
        Log.i(TAG, "Sélection d'un point de passage");
        _listPictureFrag.onClickOnPathPointDrawing(pathPoint);
        getFragmentManager().beginTransaction().show(_listPictureFrag).commit();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onUnClickPassagePoint(UnSelectPathPointMessage m){
        Log.i(TAG, "Deselection d'un point de passage");
        getFragmentManager().beginTransaction().hide(_listPictureFrag).commit();
    }
    //endregion
}
