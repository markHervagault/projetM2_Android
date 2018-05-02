package istic.m2.ila.firefighterapp.map.intervention;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.dto.IDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.map.intervention.fragments.IManipulableDeployFragment;
import istic.m2.ila.firefighterapp.map.intervention.fragments.IManipulableFragment;

/**
 * Created by bob on 25/04/18.
 */

public class ButtonFactory {

    private static final String DeleteTitle = "Supprimer";
    private static final String MoveTitle = "Deplacer";
    private static final String CreateTitle = "Creer";
    private static final String UpdateTitle = "Mettre a jour";
    private static final String CRMTitle = "CRM";
    private static final String PlacerTitle = "Placer";
    private static final String ModifierTitle = "Modifier";
    private static final String DesengegerTitle = "Desengager";
    private static final String ActionTitle = "Est en Action";
    private static final String EngageTitle = "Est Engagé";

    public static void populate(final IManipulableFragment fragment, final IDTO dto, final LinearLayout buttonLayout) {
        if(dto instanceof TraitTopographiqueBouchonDTO){

        } else {
            if(dto.getId() != null) {
                //Object existant (BDD) button de Supression et de déplacement
                Button updateBtn = createUpdateButton(fragment);
                updateBtn.setVisibility(View.GONE);
                buttonLayout.addView(updateBtn);
                buttonLayout.addView(createMoveButton(fragment,updateBtn));
                buttonLayout.addView(createDeleteButton(fragment));
            } else {
                //Button de creation
                buttonLayout.addView(createCreateButton(fragment));
            }
        }
    }

    public static void populate(final IManipulableDeployFragment fragment, final DeploiementDTO deploiement, final  LinearLayout buttonLayout){
        if(deploiement.getId() == null){
            //not use for moment
        } else {
            if(deploiement.getState() == EEtatDeploiement.DEMANDE){

                Button updateBtn = createUpdateButton(fragment);
                updateBtn.setVisibility(View.GONE);

                if(deploiement.getGeoPosition()!=null || !deploiement.isPresenceCRM()){
                    buttonLayout.addView(createMoveButton(fragment, updateBtn));
                } else {
                    buttonLayout.addView(createPlacerButton(fragment, updateBtn));
                }

                buttonLayout.addView(updateBtn);

            } else if(deploiement.getState() == EEtatDeploiement.VALIDE
                    || deploiement.getState() == EEtatDeploiement.ENGAGE) {

                Button updateBtn = createUpdateButton(fragment);
                updateBtn.setVisibility(View.GONE);

                if(deploiement.getState() == EEtatDeploiement.VALIDE) {
                    buttonLayout.addView(createEngageButton(fragment));
                } else {
                    buttonLayout.addView(createActionButton(fragment));
                }

                if(deploiement.isPresenceCRM()) {
                    buttonLayout.addView(createCRMButton(fragment));
                }
                if(deploiement.getGeoPosition()!=null || !deploiement.isPresenceCRM()){
                    buttonLayout.addView(createMoveButton(fragment, updateBtn));
                } else {
                    buttonLayout.addView(createPlacerButton(fragment, updateBtn));
                }
                buttonLayout.addView(createModifierButton(fragment, updateBtn));
                buttonLayout.addView(updateBtn);

            } else if(deploiement.getState() == EEtatDeploiement.EN_ACTION) {

                Button updateBtn = createUpdateButton(fragment);
                updateBtn.setVisibility(View.GONE);

                buttonLayout.addView(createDesengegerButton(fragment));
                buttonLayout.addView(createCRMButton(fragment));
                buttonLayout.addView(createMoveButton(fragment,updateBtn));
                buttonLayout.addView(createModifierButton(fragment, updateBtn));
                buttonLayout.addView(updateBtn);

            } else {
                //no Button
            }
        }
    }

    private static Button createEngageButton(final IManipulableDeployFragment fragment) {
        final Button btn = createSimplebutton(fragment.getMeActivity(), EngageTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fragment.engage();
            }
        });
        return btn;
    }

    private static Button createActionButton(final IManipulableDeployFragment fragment) {
        final Button btn = createSimplebutton(fragment.getMeActivity(), ActionTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fragment.action();
            }
        });
        return btn;
    }

    private static Button createPlacerButton(final IManipulableDeployFragment fragment, final Button updateBtn){
        final Button btn = createSimplebutton(fragment.getMeActivity(), PlacerTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn.setVisibility(View.GONE);
                fragment.move();
                updateBtn.setVisibility(View.VISIBLE);
            }
        });
        return btn;
    }

    private static Button createCRMButton(final IManipulableDeployFragment fragment){
        final Button btn = createSimplebutton(fragment.getMeActivity(), CRMTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fragment.toCrm();
            }
        });
        return btn;
    }

    private static Button createModifierButton(final IManipulableDeployFragment fragment, final Button updateBtn){
        final Button btn = createSimplebutton(fragment.getMeActivity(), ModifierTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn.setVisibility(View.GONE);
                fragment.modif();
                updateBtn.setVisibility(View.VISIBLE);
            }
        });
        return btn;
    }

    private static Button createDesengegerButton(final IManipulableDeployFragment fragment){
        final Button btn = createSimplebutton(fragment.getMeActivity(), DesengegerTitle);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fragment.desengage();
            }
        });
        return btn;
    }

    private static Button createMoveButton(final IManipulableFragment fragment, final Button updateBtn){
        final Button btn = createSimplebutton(fragment.getMeActivity(),MoveTitle);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.setVisibility(View.GONE);
                fragment.move();
                updateBtn.setVisibility(View.VISIBLE);
            }
        });
        return btn;
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
        btn.setText(title);
        btn.setSingleLine(true);
        btn.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        return btn;
    }
}
