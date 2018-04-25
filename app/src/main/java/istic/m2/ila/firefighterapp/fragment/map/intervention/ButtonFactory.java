package istic.m2.ila.firefighterapp.fragment.map.intervention;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.ITraitTopo;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.IManipulableFragment;

/**
 * Created by bob on 25/04/18.
 */

public class ButtonFactory {

    private static final String DeleteTitle = "Supprimer";
    private static final String MoveTitle = "Deplacer";
    private static final String CreateTitle = "Creer";
    private static final String UpdateTitle = "Mettre a jour";

    public static List<Button> getButton(final IManipulableFragment fragment, ITraitTopo traitTopo){
        List<Button> buttons = new ArrayList<>();
        if(traitTopo instanceof TraitTopoDTO){
            if(traitTopo.getId() != null){
                //Object existant (BDD) button de Supression et de déplacement
                buttons.add(createDeleteButton(fragment));
                buttons.add(createMoveButton(fragment));
            } else {
                //Button de creation
                buttons.add(createCreateButton(fragment));
            }
        } else if (traitTopo instanceof TraitTopographiqueBouchonDTO){

        }
        return buttons;
    }

    public static List<Button> getButton(final IManipulableFragment fragment, final SinistreDTO sinistre){
        List<Button> buttons = new ArrayList<>();
        if(sinistre.getId() != null) {
            //Object existant (BDD) button de Supression et de déplacement
            buttons.add(createDeleteButton(fragment));
            buttons.add(createMoveButton(fragment));
        } else {
            //Button de creation
            buttons.add(createCreateButton(fragment));
        }
        return buttons;
    }

    private static Button createDeleteButton(final IManipulableFragment fragment) {
        Button btn = createSimplebutton(fragment.getMeActivity(),DeleteTitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.delete();
            }
        });
        return btn;
    }

    private static Button createUpdateButton(final IManipulableFragment fragment) {
        Button btn = createSimplebutton(fragment.getMeActivity(),UpdateTitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.update();
            }
        });
        return btn;
    }

    private static Button createMoveButton(final IManipulableFragment fragment) {
        Button btn = createSimplebutton(fragment.getMeActivity(),MoveTitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.move();
            }
        });
        return btn;
    }

    private static Button createCreateButton(final IManipulableFragment fragment) {
        Button btn = createSimplebutton(fragment.getMeActivity(),CreateTitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.create();
            }
        });
        return btn;
    }

    private static Button createSimplebutton(final Activity activity, String title) {
        Button btn = new Button(activity);
        btn.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        btn.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        btn.setText(title);
        return btn;
    }
}