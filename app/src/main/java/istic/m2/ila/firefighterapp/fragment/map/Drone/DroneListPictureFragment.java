package istic.m2.ila.firefighterapp.fragment.map.Drone;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.ItemListPictureAdapter;
import istic.m2.ila.firefighterapp.dto.PhotoDTO;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Drawings.PathPointDrawing;

import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by markh on 30/04/2018.
 */

public class DroneListPictureFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<PhotoDTO> photos;

    /**
     * Contexte
     */
    public Context _context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_pictures, container, false);
        _context = view.getContext();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.pictureRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(_context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ItemListPictureAdapter(photos);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onClickOnPathPointDrawing (PathPointDrawing point) {
        // TODO : Récupérer en base la collection des photos lié à ce point
        // TODO : instancié le la liste<PhotoDTO> avec la collection récupérée en BDD
    }
}
