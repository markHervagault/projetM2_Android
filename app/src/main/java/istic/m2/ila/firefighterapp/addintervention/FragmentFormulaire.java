package istic.m2.ila.firefighterapp.addintervention;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.InterventionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;
import retrofit2.Response;

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
    private EditText editTextNom;
    /*Champs du formulaire à enregistrer et vérfier*/


    private OnFragmentInteractionListener mListener;

    public FragmentFormulaire() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("FragmentFormulaire", "OnCreate");
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        editTextNom = (EditText) getView().findViewById(R.id.editText_nom);
        /*TODO
        searchPlace.setAdapter(searchPlaceAdapter);
        */

        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);

        try {
            //Appel de l'api pour les codes sinistre
            Response<List<CodeSinistreDTO>> responseCodeSinistreDTOS = interventionConsumer.getAllCodeSinistre(token).execute();

            if(responseCodeSinistreDTOS != null && responseCodeSinistreDTOS.code() == HttpURLConnection.HTTP_OK) {
               /*Adapter de l'autocomplete code sinistre*/
                CodeSinistreAdapter codeSinistreAdapter = new CodeSinistreAdapter(this.getContext(), R.layout.item_autocomplete, responseCodeSinistreDTOS.body());
                searchCodeSinistre.setAdapter(codeSinistreAdapter);
                /*Adapter de l'autocomplete code sinistre*/
            }
            else{
                Log.e("CodeSinitre", "Error From Server : " + responseCodeSinistreDTOS.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    public Boolean isFormulaireValide() {
        if (this.searchCodeSinistre.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextVille.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextCp.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextNRue.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextRue.getText().toString().isEmpty()) {
            return false;
        }
        if (this.editTextRue.getText().toString().isEmpty()) {
            return false;
        }

        //TODO Plus de verif necessaire
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
        if (isFormulaireValide()) {
            String codeSinistreString = this.searchCodeSinistre.getText().toString().split(" ")[0];
            Long codeSinistreId = ((CodeSinistreAdapter)this.searchCodeSinistre.getAdapter()).getCodeSinistresId(codeSinistreString);

            bundle.putString("nom", this.editTextNom.getText().toString());
            bundle.putString("ville", this.editTextVille.getText().toString());
            bundle.putString("cp", this.editTextCp.getText().toString());
            bundle.putString("rue", this.editTextRue.getText().toString());
            bundle.putLong("numero", Long.valueOf(this.editTextNRue.getText().toString()));
            bundle.putLong("codeSinistreId", codeSinistreId);

            bundle.putDouble("latitude", Double.valueOf(this.editTextLat.getText().toString()));
            bundle.putDouble("longitude", Double.valueOf(this.editTextLng.getText().toString()));

            bundle.putString("adresse", this.searchPlace.getText().toString());
            return bundle;
        }
        else {
            return null;
        }
    }
}
