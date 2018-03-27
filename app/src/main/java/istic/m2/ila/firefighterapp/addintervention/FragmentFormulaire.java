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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import istic.m2.ila.firefighterapp.R;

public class FragmentFormulaire extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView searchPlace;
    private AutoCompleteTextView searchCodeSinistre;
    private EditText editTextLat;
    private EditText editTextLng;



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
        editTextLat = (EditText) getView().findViewById(R.id.editText_lat);
        editTextLng = (EditText) getView().findViewById(R.id.editText_lng);

        /*TODO
        searchPlace.setAdapter(searchPlaceAdapter);
        */

        /*Temporaire en attendant les dto*/
        CodeSinistre codeSinistre = new CodeSinistre((long) 1, "Code Sinistre");
        CodeSinistre codeSinistre1 = new CodeSinistre((long) 2, "Code Sinistre");
        ArrayList<CodeSinistre> autocompleteFields = new ArrayList<>();
        autocompleteFields.add(codeSinistre);
        autocompleteFields.add(codeSinistre1);
        /*Temporaire en attendant les dto*/

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

    /*
      Spinner code Incident
    */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("adresse", this.searchPlace.getText().toString());
        //((CodeSinistreAdapter)this.searchCodeSinistre.getAdapter()).getCodeSinistres();
        //bundle.putLong("codeSinistreId", this.searchCodeSinistre.getAdapter().getCode);
        bundle.putLong("lattitude", Long.parseLong(this.editTextLat.getText().toString()));
        bundle.putLong("longitude", Long.parseLong(this.editTextLng.getText().toString()));
        return bundle;
    }
}
