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
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
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

    private boolean isReduce = false;

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

    public void setDataDTO(List<DeploiementDTO> listDeploiment) {
        this.listDeploiment = listDeploiment;
    }

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
        if(this.context instanceof MapActivity) {
            this.listDeploiment = new ArrayList<>();
        } else {
            this.listDeploiment = getDeploiments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        mTableView = view.findViewById(R.id.moyenTableView);
        // Create TableView Adapter
        mTableAdapter = new MoyenTableAdapter(getContext());
        mTableView.setAdapter(mTableAdapter);
        mTableView.setTableViewListener(new MoyenTableViewListener(mTableView, getContext(), mTableAdapter, this));

        // Call InitTable pour avoir les données au chargement de la table
        initTable();

        return view;
    }

    public void initTable() {
        // UserInfo data will be getting from a web server.
        populatedTableViewAll(this.listDeploiment, false, -1);
    }

    /**
     * Initialisation du tableau
     * Création de la ligne d'entête, des numéros de colonne et des données de chaque cellule
     * @param depDto Liste des données, un item de la liste représentant une ligne
     */
    public void populatedTableViewAll(List<DeploiementDTO> depDto, boolean selected, int index) {
        isReduce = false;
        // create Models
        mColumnHeaderList = createColumnHeaderModelListAll();
        mCellList = loadCellModelListAll(depDto, selected, index);
        mRowHeaderList = createRowHeaderList();

        // Set all items to the TableView
        mTableAdapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
    }

    public void populatedTableViewReduce(List<DeploiementDTO> depDto, boolean selected, int index) {
        // create Models
        isReduce = true;
        mColumnHeaderList = createColumnHeaderModelListReduce();
        mCellList = loadCellModelListReduce(depDto, selected, index);
        mRowHeaderList = createRowHeaderList();

        // Set all items to the TableView
        mTableAdapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
    }

    /**
     * Crée la ligne d'entête des colonnes du tableau
     * @return cette liste
     */
    private List<ColumnHeaderModel> createColumnHeaderModelListAll() {
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

    private List<ColumnHeaderModel> createColumnHeaderModelListReduce() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("Id"));
        list.add(new ColumnHeaderModel("Type"));
        list.add(new ColumnHeaderModel("Nom"));
        list.add(new ColumnHeaderModel("Etat"));
        list.add(new ColumnHeaderModel("CRM"));

        return list;
    }

    /**
     * Crée les lignes de données pour les déploiements dans le tableau
     * @param depDto Liste de déploiement à créer
     * @param selec
     * @return Liste de lignes créées
     */
    private List<List<CellModel>> loadCellModelListAll(List<DeploiementDTO> depDto, boolean selec, int index) {
        List<List<CellModel>> lists = new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        // Creating cell model list from UserInfo list for Cell Items
        // In this example, UserInfo list is populated from web service
        boolean selected = false;
        for (int i = 0; i < depDto.size(); i++) {

            selected = selec && i == index;
            DeploiementDTO depInfo = depDto.get(i);
            TypeComposanteDTO composante = depInfo.getComposante();

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, depInfo.getId(), selected));

            String labelToDisplay = null;
            if(depInfo.getTypeDemande() != null){
                labelToDisplay = depInfo.getTypeDemande().getLabel();
            } else {
                labelToDisplay = depInfo.getVehicule().getType().getLabel();
            }

            CellModel cellWithComposante = changeBackgroundAndText("2-" + i, labelToDisplay,
                    composante.getCouleur(), "#ffffff" /* default color*/, selected);
            list.add(cellWithComposante);

            if(depInfo.getVehicule()!=null) {
                list.add(new CellModel("3-" + i, depInfo.getVehicule().getLabel(), selected));
            } else {
                list.add(new CellModel("3-" + i, "...", selected));
            }
            CellModel cellEtat;
            if (depInfo.getState() == EEtatDeploiement.REFUSE) {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), "#000000", "#ffffff", selected);
            } else if (depInfo.getState() == EEtatDeploiement.DEMANDE) {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), "#747474", "#ffffff", selected);
            } else {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), null, null, selected);
            }
            list.add(cellEtat);

            if( depInfo.isPresenceCRM()){
                list.add(new CellModel("5-" + i, "CRM", selected));
            } else {
                list.add(new CellModel("5-" + i, "...", selected));
            }

            if(depInfo.getDateHeureDemande()!=null) {
                list.add(new CellModel("6-" + i, formater.format(depInfo.getDateHeureDemande()), selected));
            } else {
                list.add(new CellModel("6-" + i, "...", selected));
            }

            if(depInfo.getDateHeureValidation()!=null) {
                list.add(new CellModel("7-" + i, formater.format(depInfo.getDateHeureValidation()), selected));
            } else {
                list.add(new CellModel("7-" + i, "...", selected));
            }

            if(depInfo.getDateHeureEngagement()!=null) {
                list.add(new CellModel("8-" + i, formater.format(depInfo.getDateHeureEngagement()), selected));
            } else {
                list.add(new CellModel("8-" + i, "...", selected));
            }

            if(depInfo.getDateHeureDesengagement()!=null) {
                list.add(new CellModel("9-" + i, formater.format(depInfo.getDateHeureDesengagement()), selected));
            } else {
                list.add(new CellModel("9-" + i, "...", selected));
            }

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<List<CellModel>> loadCellModelListReduce(List<DeploiementDTO> depDto, boolean selec, int index) {
        List<List<CellModel>> lists = new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        // Creating cell model list from UserInfo list for Cell Items
        // In this example, UserInfo list is populated from web service
        boolean selected = false;
        for (int i = 0; i < depDto.size(); i++) {
            selected = selec && i == index;
            DeploiementDTO depInfo = depDto.get(i);
            TypeComposanteDTO composante = depInfo.getComposante();

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, depInfo.getId(), selected));

            CellModel cellWithComposante = changeBackgroundAndText("2-" + i, depInfo.getTypeDemande().getLabel(),
                    composante.getCouleur(), "#ffffff", selected);
            list.add(cellWithComposante);

            if (depInfo.getVehicule() != null) {
                list.add(new CellModel("3-" + i, depInfo.getVehicule().getLabel(), selected));
            } else {
                list.add(new CellModel("3-" + i, "...", selected));
            }
            CellModel cellEtat;
            if (depInfo.getState() == EEtatDeploiement.REFUSE) {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), "#000000", "#ffffff", selected);
            } else if (depInfo.getState() == EEtatDeploiement.DEMANDE) {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), "#747474", "#ffffff", selected);
            } else {
                cellEtat = changeBackgroundAndText("4-" + i, depInfo.getState(), null, null, selected);
            }
            list.add(cellEtat);

            if (depInfo.isPresenceCRM()) {
                list.add(new CellModel("5-" + i, "CRM", selected));
            } else {
                list.add(new CellModel("5-" + i, "...", selected));
            }

            // Add
            lists.add(list);
        }

        return lists;
    }


    public CellModel changeBackgroundAndText(String idTV, Object cellData, String backgroundColor, String textColor, boolean selected) {
        CellModel cellWithColors = new CellModel(idTV, cellData, selected);
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


    public List<DeploiementDTO> getListDeploiment() {
        return listDeploiment;
    }

    public boolean isReduce() {
        return isReduce;
    }

    public void synchroDeployUpdate(DeploiementDTO deploiementDTO) {

        for (int i = 0; i < listDeploiment.size(); i++) {
            DeploiementDTO deploy = listDeploiment.get(i);
            if((deploy.getId() != null) && (deploy.getId().equals(deploiementDTO.getId()))){

                changeDiffValue(deploy,deploiementDTO);
                // On sort - break
                break;
            }
        }

        if (isReduce()) {
            populatedTableViewReduce(listDeploiment, false, -1);
        } else {
            populatedTableViewAll(listDeploiment, false, -1);
        }
    }

    private void changeDiffValue(DeploiementDTO reelDep, DeploiementDTO momDep) {

        if(reelDep.getComposante() != null
                && momDep.getComposante() != null) {
            if (!reelDep.getComposante().equals(momDep.getComposante())) {
                reelDep.setComposante(momDep.getComposante());
            }
        }

        if(reelDep.getDateHeureDesengagement() != null
                && momDep.getDateHeureDesengagement() != null) {
            if (!reelDep.getDateHeureDesengagement().equals(momDep.getDateHeureDesengagement())) {
                reelDep.setDateHeureDesengagement(momDep.getDateHeureDesengagement());
            }
        }

        if(reelDep.getDateHeureEngagement() != null
                && momDep.getDateHeureEngagement() != null) {
            if (!reelDep.getDateHeureEngagement().equals(momDep.getDateHeureEngagement())) {
                reelDep.setDateHeureEngagement(momDep.getDateHeureEngagement());
            }
        }

        if(reelDep.getDateHeureValidation() != null
                && momDep.getDateHeureValidation() != null) {
            if (!reelDep.getDateHeureValidation().equals(momDep.getDateHeureValidation())) {
                reelDep.setDateHeureValidation(momDep.getDateHeureValidation());
            }
        }

        if(reelDep.getDateHeureDemande() != null
                && momDep.getDateHeureDemande() != null) {
            if (!reelDep.getDateHeureDemande().equals(momDep.getDateHeureDemande())) {
                reelDep.setDateHeureDemande(momDep.getDateHeureDemande());
            }
        }
        if(reelDep.getState() != null
                && momDep.getState() != null) {
            if (!reelDep.getState().equals(momDep.getState())) {
                reelDep.setState(momDep.getState());
            }
        }

        if(reelDep.getPresenceCRM() != null
                && momDep.getPresenceCRM() != null) {
            if (!reelDep.getPresenceCRM().equals(momDep.getPresenceCRM())) {
                reelDep.setPresenceCRM(momDep.getPresenceCRM());
            }
        }

        if(reelDep.getGeoPosition() != null
                && momDep.getGeoPosition() != null) {
            if (!reelDep.getGeoPosition().equals(momDep.getGeoPosition())) {
                reelDep.setGeoPosition(momDep.getGeoPosition());
            }
        }
    }


    public void synchroDeployCreate(DeploiementDTO deploiementDTO) {

        Log.println(Log.INFO, "synchroDeployCreate", "synchroDeployCreate");
        listDeploiment.add(deploiementDTO);

        if (isReduce()) {
            populatedTableViewReduce(listDeploiment, false, -1);
        } else {
            populatedTableViewAll(listDeploiment, false, -1);
        }
    }

    public boolean containsId(DeploiementDTO message) {

        boolean exist = false;
            for (int i = 0; i < listDeploiment.size(); i++) {
                DeploiementDTO deploy = listDeploiment.get(i);
                if((deploy.getId() != null) && (deploy.getId().equals(message.getId()))){
                    exist = true;
                    // On sort - break
                    break;
                }
            }
        return exist;
    }
}
