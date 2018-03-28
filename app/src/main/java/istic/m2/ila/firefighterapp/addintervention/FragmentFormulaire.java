package istic.m2.ila.firefighterapp.addintervention;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;

public class FragmentFormulaire extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    /*Champs du formulaire à enregistrer et vérfier*/
    private AutoCompleteTextView searchPlace;
    private AutoCompleteTextView searchCodeSinistre;

    //TODO faire une liste de champs pour éviter la verbosité de la vérification
    private EditText editTextVille;
    private EditText editTextRue;
    private EditText editTextCp;
    private EditText editTextNRue;
    private EditText editTextLat;
    private EditText editTextLng;
    /*Champs du formulaire à enregistrer et vérfier*/


    private OnFragmentInteractionListener mListener;

    public FragmentFormulaire() {
        // Required empty public constructor
    }

    public static FragmentFormulaire newInstance(String param1, String param2) {
        FragmentFormulaire fragment = new FragmentFormulaire();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("FragmentFormulaire", "OnCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_formulaire, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("FragmentFormulaire", "OnViewCreated");

        searchPlace = (AutoCompleteTextView) getView().findViewById(R.id.search_place);
        searchCodeSinistre = (AutoCompleteTextView) getView().findViewById(R.id.searchCodeSinistre);

        editTextVille = (EditText) getView().findViewById(R.id.editText_ville);
        editTextRue = (EditText) getView().findViewById(R.id.editText_rue);
        editTextCp = (EditText) getView().findViewById(R.id.editText_cp);
        editTextNRue = (EditText) getView().findViewById(R.id.editText_nrue);
        editTextLat = (EditText) getView().findViewById(R.id.editText_lat);
        editTextLng = (EditText) getView().findViewById(R.id.editText_lng);

        /*TODO
        searchPlace.setAdapter(searchPlaceAdapter);
        */

        /*Temporaire en attendant le Rest consumer*/
        CodeSinistreDTO codeSinistre = new CodeSinistreDTO();
        CodeSinistreDTO codeSinistre1 = new CodeSinistreDTO();

        codeSinistre.setId((long) 1);
        codeSinistre1.setId((long) 2);

        codeSinistre.setCode("1110");
        codeSinistre1.setCode("1320");

        codeSinistre.setIntitule("AVP léger");
        codeSinistre1.setIntitule("Accident PL avec désincarcération");

        ArrayList<CodeSinistreDTO> autocompleteFields = new ArrayList<>();
        autocompleteFields.add(codeSinistre);
        autocompleteFields.add(codeSinistre1);
        /*Temporaire en attendant les Rest consumer*/

        /*Adapter de l'autocomplete code sinistre*/
        CodeSinistreAdapter codeSinistreAdapter = new CodeSinistreAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line, autocompleteFields);
        searchCodeSinistre.setAdapter(codeSinistreAdapter);
        /*Adapter de l'autocomplete code sinistre*/


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public Boolean isValidFormulaire() {
        if (this.searchPlace.getText().toString().isEmpty()) {
            return false;
        }
        if (this.searchCodeSinistre.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextLat.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextLng.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        if (isValidFormulaire()) {
            String codeSinistreString = this.searchCodeSinistre.getText().toString();
            bundle.putLong("codeSinistreId", Long.valueOf(codeSinistreString.substring(0, codeSinistreString.indexOf(" "))));
            bundle.putString("adresse", this.searchPlace.getText().toString());
            bundle.putLong("lattitude", Long.valueOf(this.editTextLat.getText().toString()));
            bundle.putLong("longitude", Long.valueOf(this.editTextLng.getText().toString()));
            return bundle;
        }
        else {
            return null;
        }
    }
}
