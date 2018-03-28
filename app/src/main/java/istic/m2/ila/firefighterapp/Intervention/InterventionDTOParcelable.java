package istic.m2.ila.firefighterapp.Intervention;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import istic.m2.ila.firefighterapp.dto.InterventionDTO;

/**
 * Created by adou on 02/02/18.
 */

public class InterventionDTOParcelable implements Parcelable, Serializable {

    InterventionDTO interventionDTO;

    public InterventionDTOParcelable(InterventionDTO interventionDTO) {
        this.interventionDTO = interventionDTO;
    }

    // Parcelling part
    public InterventionDTOParcelable(Parcel in){
        interventionDTO = (InterventionDTO) in.readValue(InterventionDTO.class.getClassLoader());
    }

    public static final Creator<InterventionDTOParcelable> CREATOR = new Creator<InterventionDTOParcelable>() {
        @Override
        public InterventionDTOParcelable createFromParcel(Parcel in) {
            return new InterventionDTOParcelable(in);
        }

        @Override
        public InterventionDTOParcelable[] newArray(int size) {
            return new InterventionDTOParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(interventionDTO);
    }

    public InterventionDTO getInterventionDTO() {
        return interventionDTO;
    }
}
