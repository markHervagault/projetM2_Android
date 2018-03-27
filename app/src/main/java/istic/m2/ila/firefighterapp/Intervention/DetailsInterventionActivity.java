package istic.m2.ila.firefighterapp.Intervention;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by markh on 20/03/2018.
 */

public class DetailsInterventionActivity extends AppCompatActivity {

    //Todo : return POJO details
    public String getData(){
        return "LOL";
    }

    public List<String> getDatas(){

        List<String> datas = new ArrayList<>();
        datas.add("MDR");
        datas.add("LOL");
        datas.add("YOLO");
        return datas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_details);
    }

}
