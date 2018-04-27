package istic.m2.ila.firefighterapp.fragment.map.intervention;


import android.support.v4.app.Fragment;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.fragment.map.DemandeMoyenFragement;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.CreationSinistre;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.CreationTraitTopo;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailDeployFragment;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailSinistreFragment;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailTraitTopoFragment;

/**
 * Created by amendes on 25/04/18.
 */

public class FragmentFactory{
    public static Fragment getFragment(TraitTopoDTO dto){
        if (dto.getId() != null) {
            return DetailTraitTopoFragment.newInstance((TraitTopoDTO)dto);
        } else {
            return CreationTraitTopo.newInstance();
        }
    }
    public static Fragment getFragment(SinistreDTO dto){
        if (dto.getId() != null) {
            return DetailSinistreFragment.newInstance((SinistreDTO)dto);
        } else {
            return CreationSinistre.newInstance();
        }
    }

    public static Fragment getFragment(DeploiementDTO dto){
        if (dto.getId() != null) {
            return DetailDeployFragment.newInstance((DeploiementDTO)dto);
        } else {
            return new DemandeMoyenFragement();
        }
    }
    public static Fragment getFragment(TraitTopographiqueBouchonDTO dto){
        if (dto.getId() != null) {
            return DetailTraitTopoFragment.newInstance((TraitTopographiqueBouchonDTO)dto);
        } else {
            return CreationTraitTopo.newInstance();
        }
    }

}
