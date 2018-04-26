package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.app.Activity;

/**
 * Created by bob on 25/04/18.
 */

public interface IManipulableFragment {
    void create();
    void update();
    void move();
    void delete();
    Activity getMeActivity();
}
