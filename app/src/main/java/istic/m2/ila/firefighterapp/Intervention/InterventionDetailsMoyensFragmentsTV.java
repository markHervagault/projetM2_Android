package istic.m2.ila.firefighterapp.Intervention;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.TableView;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.Intervention.model.CellModel;
import istic.m2.ila.firefighterapp.Intervention.model.ColumnHeaderModel;
import istic.m2.ila.firefighterapp.Intervention.model.RowHeaderModel;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.DeploimentConsumer;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsMoyensFragmentsTV extends Fragment  {

    private static String TAG = "FragmentMoyens";

    private TableView mTableView;
    private MoyenTableAdapter mTableAdapter;

    // For TableView
    private List<List<CellModel>> mCellList;

    public List<ColumnHeaderModel> getColumnHeaderList() {
        return mColumnHeaderList;
    }

    private List<ColumnHeaderModel> mColumnHeaderList;
    private List<RowHeaderModel> mRowHeaderList;

    private Long idIntervention;
    private List<DeploiementDTO> listDeploiment;
    private Context context;

    public InterventionDetailsMoyensFragmentsTV() {
        // Required empty public constructor
    }

    /**
     * Récupère la liste des déploiements depuis le serveur
     * @return la liste des déploiements
     */
    private List<DeploiementDTO> getDeploiments() {
        Log.i(TAG, "getDeploimentsTri Begin");
        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        String id = this.idIntervention.toString();

        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer deploimentConsumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<List<DeploiementDTO>> response = null;

        List<DeploiementDTO>  listDeploimentTmp = null;


        try {
            response = deploimentConsumer.getListDeploimentById(token, id).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                listDeploimentTmp = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.i(TAG, "getDeploimentsTri End");
        return listDeploimentTmp;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init pojo data
        this.context = context;
        this.idIntervention = ((ActivityMoyens) this.getActivity()).getIdIntervention();
        this.listDeploiment = getDeploiments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        mTableView = (TableView) view.findViewById(R.id.moyenTableView);

        // Create TableView Adapter
        mTableAdapter = new MoyenTableAdapter(getContext());
        mTableView.setAdapter(mTableAdapter);


        // UserInfo data will be getting from a web server.
        populatedTableView(this.listDeploiment);
        mTableView.setTableViewListener(new MoyenTableViewListener(mTableView, getContext(), listDeploiment));
        //mTableView.hideColumn(1);
        return view;
    }

    /**
     * Initialisation du tableau
     * Création de la ligne d'entête, des numéros de colonne et des données de chaque cellule
     * @param depDto Liste des données, un item de la liste représentant une ligne
     */
    public void populatedTableView(List<DeploiementDTO> depDto) {
        // create Models
        mColumnHeaderList = createColumnHeaderModelList();
        mCellList = loadCellModelList(depDto);
        mRowHeaderList = createRowHeaderList();

        // Set all items to the TableView
        mTableAdapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
    }

    /**
     * Crée la ligne d'entête des colonnes du tableau
     * @return cette liste
     */
    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("Id"));
        list.add(new ColumnHeaderModel("Type"));
        list.add(new ColumnHeaderModel("Nom"));
        list.add(new ColumnHeaderModel("Etat"));
        list.add(new ColumnHeaderModel("CRM"));

        list.add(new ColumnHeaderModel("Heure de demande"));
        list.add(new ColumnHeaderModel("Heure validation/refus"));
        list.add(new ColumnHeaderModel("Heure engagement"));
        list.add(new ColumnHeaderModel("Heure libération"));

        return list;
    }

    /**
     * Crée les lignes de données pour les déploiements dans le tableau
     * @param depDto Liste de déploiement à créer
     * @return Liste de lignes créées
     */
    private List<List<CellModel>> loadCellModelList(List<DeploiementDTO> depDto) {
        List<List<CellModel>> lists = new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        // Creating cell model list from UserInfo list for Cell Items
        // In this example, UserInfo list is populated from web service

        for (int i = 0; i < depDto.size(); i++) {
            DeploiementDTO depInfo = depDto.get(i);
            TypeComposanteDTO composante = depInfo.getComposante();

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, depInfo.getId()));

            CellModel cellWithComposante = changeBackgroundAndText("2-" + i, depInfo.getTypeDemande().getLabel(),
                    composante.getCouleur(), null /* default color*/);
            list.add(cellWithComposante);

            if(depInfo.getVehicule()!=null) {
                list.add(new CellModel("3-" + i, depInfo.getVehicule().getLabel()));
            } else {
                list.add(new CellModel("3-" + i, "..."));
            }
            CellModel cellEtat;
            if (depInfo.getState() == EEtatDeploiement.REFUSE) {
                cellEtat= changeBackgroundAndText("4-" + i, depInfo.getState(),"#9D98AB", "#ffffff");
            } else if (depInfo.getState() == EEtatDeploiement.DEMANDE) {
                cellEtat= changeBackgroundAndText("4-" + i, depInfo.getState(),"#cdcdc2", "#ffffff");
            }else {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(),null, "#ffffff");
            }
            list.add(cellEtat);

            if( depInfo.isPresenceCRM()){
                list.add(new CellModel("5-" + i, "CRM"));
            } else {
                list.add(new CellModel("5-" + i, "..."));
            }

            if(depInfo.getDateHeureDemande()!=null) {
                list.add(new CellModel("6-" + i, formater.format(depInfo.getDateHeureDemande())));
            } else {
                list.add(new CellModel("6-" + i, "..."));
            }

            if(depInfo.getDateHeureValidation()!=null) {
                list.add(new CellModel("7-" + i, formater.format(depInfo.getDateHeureValidation())));
            } else {
                list.add(new CellModel("7-" + i, "..."));
            }

            if(depInfo.getDateHeureEngagement()!=null) {
                list.add(new CellModel("8-" + i, formater.format(depInfo.getDateHeureEngagement())));
            } else {
                list.add(new CellModel("8-" + i, "..."));
            }

            if(depInfo.getDateHeureDesengagement()!=null) {
                list.add(new CellModel("9-" + i, formater.format(depInfo.getDateHeureDesengagement())));
            } else {
                list.add(new CellModel("9-" + i, "..."));
            }

            // Add
            lists.add(list);
        }

        return lists;
    }

    public CellModel changeBackgroundAndText(String idTV, Object cellData, String backgroundColor, String textColor) {
        CellModel cellWithColors = new CellModel(idTV, cellData);
        cellWithColors.setBackgroundColor(backgroundColor);
        cellWithColors.setTextColor(textColor);
        return cellWithColors;
    }

    /**
     * Crée les Id de ligne
     * @return la liste des indexs de ligne
     */
    private List<RowHeaderModel> createRowHeaderList() {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < mCellList.size(); i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();

        //update fields
        // On masque la colonne 1
        //mTableView.hideColumn(0);
    }

}
