package istic.m2.ila.firefighterapp.fragment.map.intervention;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.ITraitTopo;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;

/**
 * Created by bob on 25/04/18.
 */

public class ButtonFactory {

    private static final String DeleteTitle = "Supprimer";
    private static final String MoveTitle = "Deplacer";
    private static final String CreateTitle = "Creer";

    public static List<Button> getButton(Activity activity, ITraitTopo traitTopo){
        List<Button> buttons = new ArrayList<>();
        if(traitTopo instanceof TraitTopoDTO){
            if(traitTopo.getId() != null){
                //Object existant (BDD) button de Supression et de déplacement
                buttons.add(createSimplebutton(activity,DeleteTitle));
                buttons.add(createSimplebutton(activity,MoveTitle));
            } else {
                //Button de creation
                buttons.add(createSimplebutton(activity,CreateTitle));
            }
        } else if (traitTopo instanceof TraitTopographiqueBouchonDTO){

        }
        return buttons;
    }

    public static List<Button> getButton(Activity activity, SinistreDTO sinistre){
        List<Button> buttons = new ArrayList<>();
        if(sinistre.getId() != null){
            //Object existant (BDD) button de Supression et de déplacement
            buttons.add(createSimplebutton(activity,DeleteTitle));
            buttons.add(createSimplebutton(activity,MoveTitle));
        } else {
            //Button de creation
            buttons.add(createSimplebutton(activity,CreateTitle));
        }
        return buttons;
    }

    private static Button createSimplebutton(Activity activity, String title) {
        Button btn = new Button(activity);
        btn.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        btn.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        btn.setText(title);
        return btn;
    }
}
