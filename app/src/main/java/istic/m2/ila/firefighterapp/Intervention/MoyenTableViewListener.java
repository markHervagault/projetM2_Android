package istic.m2.ila.firefighterapp.Intervention;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;

import java.util.List;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;

/**
 * Created by evrencoskun on 2.12.2017.
 */

public class MoyenTableViewListener implements ITableViewListener {

    private ITableView mTableView;
    private Context mContext;
    private List<DeploiementDTO> deploiements;
    private MoyenTableAdapter mTableAdapter;
    private InterventionDetailsMoyensFragmentsTV interventionDetailsMoyensFragmentsTV;

    public MoyenTableViewListener(ITableView pTableView) {
        this.mTableView = pTableView;
        this.mContext = null;
    }

    public MoyenTableViewListener(ITableView pTableView, Context mContext, List<DeploiementDTO> deploiements, MoyenTableAdapter mTableAdapter, InterventionDetailsMoyensFragmentsTV interventionDetailsMoyensFragmentsTV) {
        this.mTableView = pTableView;
        this.mContext = mContext;
        this.deploiements = deploiements;
        this.mTableAdapter = mTableAdapter;
        this.interventionDetailsMoyensFragmentsTV = interventionDetailsMoyensFragmentsTV;
    }

    @Override
        public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition) {
        onRowHeaderClicked(p_jCellView, p_nYPosition);
        if (mContext != null && mContext instanceof MapActivity) {
            // Le fragment existe
            if(interventionDetailsMoyensFragmentsTV.isReduce()){
                interventionDetailsMoyensFragmentsTV.populatedTableViewReduce(interventionDetailsMoyensFragmentsTV.getListDeploiment(), true,p_nYPosition);
            } else {
                interventionDetailsMoyensFragmentsTV.populatedTableViewAll(interventionDetailsMoyensFragmentsTV.getListDeploiment(), true,p_nYPosition);
            }

        } else {
            // La map (et le fragment) n'existe pas
            // On ne fait rien
        }
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
            p_nXPosition) {

        if(interventionDetailsMoyensFragmentsTV.isReduce()){
            interventionDetailsMoyensFragmentsTV.populatedTableViewReduce(interventionDetailsMoyensFragmentsTV.getListDeploiment(), false,-1);
        } else {
            interventionDetailsMoyensFragmentsTV.populatedTableViewAll(interventionDetailsMoyensFragmentsTV.getListDeploiment(), false,-1);
        }
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int p_nXPosition) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {
        Log.e(getClass().getSimpleName(), "onRowHeaderClicked a été appelé");

        if(interventionDetailsMoyensFragmentsTV.isReduce()){
            interventionDetailsMoyensFragmentsTV.populatedTableViewReduce(interventionDetailsMoyensFragmentsTV.getListDeploiment(), true,p_nYPosition);
        } else {
            interventionDetailsMoyensFragmentsTV.populatedTableViewAll(interventionDetailsMoyensFragmentsTV.getListDeploiment(), true,p_nYPosition);
        }
        if (mContext != null && mContext instanceof MapActivity) {

            ((MapActivity) mContext).displayFragmentHolder(deploiements.get(p_nYPosition));
        }
    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {

    }
}
