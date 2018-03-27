package istic.m2.ila.firefighterapp.addintervention;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amendes on 26/03/18.
 */

public class CodeSinistreAdapter extends ArrayAdapter<CodeSinistre> {

    private ArrayList<CodeSinistre> codeSinistres = new ArrayList<>();

    public CodeSinistreAdapter(@NonNull Context context, int resource, @NonNull List<CodeSinistre> objects) {
        super(context, resource, objects);
        codeSinistres = (ArrayList<CodeSinistre>) objects;
    }

    public ArrayList<CodeSinistre> getCodeSinistres(Long code) {
        return codeSinistres;
    }
}
