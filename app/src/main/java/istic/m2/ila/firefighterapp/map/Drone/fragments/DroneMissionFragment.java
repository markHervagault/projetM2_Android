package istic.m2.ila.firefighterapp.map.Drone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by markh on 25/04/2018.
 */

public class DroneMissionFragment extends Fragment
{
    //region Members

    private String TAG = "DroneMissionFragment";
    private View _view;
    public Context context;

    private List<FloatingActionButton> floatingActionButtonList;

    //endregion

    //region Properties

    //Buttons
    public FloatingActionButton _removeSelectedMarkerButton;
    public FloatingActionButton _openCloseClosePathButton;
    public FloatingActionButton _addModeButton;
    public FloatingActionButton _zoneButton;
    public FloatingActionButton _sendMissionButton;
    public FloatingActionButton _photoButton;

    //Path Closed
    private boolean _isPathClosed;
    public boolean isPathClosed() { return _isPathClosed; }
    public void RefreshOpenPathButton(int markerCounts)
    {
        Log.i(TAG, "Refreshing OpenPath button with marker count : " + markerCounts);
        if (markerCounts < 3)
            _openCloseClosePathButton.setEnabled(false);

        else
            _openCloseClosePathButton.setEnabled(true);
    }
    public void ClosePath()
    {
        _isPathClosed = true;
        _openCloseClosePathButton.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
        _openCloseClosePathButton.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
        _openCloseClosePathButton.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
        _openCloseClosePathButton.setImageResource(R.drawable.closedloop);
    }
    public void OpenPath()
    {
        _isPathClosed = false;

        _openCloseClosePathButton.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
        _openCloseClosePathButton.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
        _openCloseClosePathButton.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
        _openCloseClosePathButton.setImageResource(R.drawable.openloop);
    }

    //Add Mode
    private boolean _addMode;
    public boolean isAddMode() { return _addMode; }
    public void SetAddMode()
    {
        _addMode = true;

        _addModeButton.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
        _addModeButton.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
        _addModeButton.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
    }
    public void UnSetAddMode()
    {
        _addMode = false;

        _addModeButton.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
        _addModeButton.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
        _addModeButton.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
    }

    //Send Mission
    private boolean _canSendMission;
    public boolean canSendMission() { return _canSendMission; }
    public void setCanSendMission()
    {
        _canSendMission = true;
        _sendMissionButton.setEnabled(true);
    }
    public void unSetCanSendButton()
    {
        _canSendMission = false;
        _sendMissionButton.setEnabled(false);
    }

    //TakePhoto
    private boolean _isTakingPhoto;
    public boolean isTakingPhoto() { return _isTakingPhoto; }
    public void setTakePhoto()
    {
        _isTakingPhoto = true;

        _photoButton.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
        _photoButton.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
        _photoButton.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
    }
    public void unsetTakePhoto()
    {
        _isTakingPhoto = false;

        _photoButton.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
        _photoButton.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
        _photoButton.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
    }

    //endregion

    //region Constructor

    public DroneMissionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drone_edit_mode, container, false);
        this._view = view;
        this.context = view.getContext();

        InitMenu();

        return view;
    }

    private void InitMenu()
    {
        _isPathClosed = false;
        _addMode = false;
        _isTakingPhoto = false;

        _removeSelectedMarkerButton = _view.findViewById(R.id.fabMenu_removeSelectedMarker);
        _openCloseClosePathButton = _view.findViewById(R.id.fabMenu_openClosePath);
        _addModeButton = _view.findViewById(R.id.fabMenu_addMarker);
        _zoneButton = _view.findViewById(R.id.fabMenu_zone);
        _sendMissionButton = _view.findViewById(R.id.fab_menu2_send);
        _photoButton = _view.findViewById(R.id.fabMenu_photo);
    }

    //endregion

    //region Methods

    public void Reset()
    {
        RefreshOpenPathButton(0);
        UnSetAddMode();
        OpenPath();
    }

    public void EnableActionButtons()
    {
        _photoButton.setEnabled(true);
        _removeSelectedMarkerButton.setEnabled(true);
    }

    public void DisableActionButtons()
    {
        _photoButton.setEnabled(false);
        _removeSelectedMarkerButton.setEnabled(false);
    }

    //endregion
}