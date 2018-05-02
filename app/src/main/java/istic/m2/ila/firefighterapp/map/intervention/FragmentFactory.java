package istic.m2.ila.firefighterapp.map.intervention;


import android.support.v4.app.Fragment;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.map.intervention.fragments.CreationSinistre;
import istic.m2.ila.firefighterapp.map.intervention.fragments.CreationTraitTopo;
import istic.m2.ila.firefighterapp.map.intervention.fragments.DemandeMoyenFragement;
import istic.m2.ila.firefighterapp.map.intervention.fragments.DetailDeployFragment;
import istic.m2.ila.firefighterapp.map.intervention.fragments.DetailSinistreFragment;
import istic.m2.ila.firefighterapp.map.intervention.fragments.DetailTraitTopoFragment;

/**
 * Created by amendes on 25/04/18.
 */

public class FragmentFactory{
    public static Fragment getFragment(TraitTopoDTO dto){
        if (dto.getId() != null) {
            return DetailTraitTopoFragment.newInstance(dto);
        } else {
            return CreationTraitTopo.newInstance();
        }
    }
    public static Fragment getFragment(SinistreDTO dto){
        if (dto.getId() != null) {
            return DetailSinistreFragment.newInstance(dto);
        } else {
            return CreationSinistre.newInstance();
        }
    }

    public static Fragment getFragment(DeploiementDTO dto){
        if (dto.getId() != null) {
            return DetailDeployFragment.newInstance(dto);
        } else {
            return new DemandeMoyenFragement();
        }
    }
    public static Fragment getFragment(TraitTopographiqueBouchonDTO dto){
        if (dto.getId() != null) {
            return DetailTraitTopoFragment.newInstance(dto);
        } else {
            return CreationTraitTopo.newInstance();
        }
    }

}
