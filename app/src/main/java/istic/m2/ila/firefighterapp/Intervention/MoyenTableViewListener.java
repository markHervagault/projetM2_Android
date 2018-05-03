package istic.m2.ila.firefighterapp.Intervention;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.listener.ITableViewListener;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;

/**
 * Created by evrencoskun on 2.12.2017.
 */

public class MoyenTableViewListener implements ITableViewListener {

    private ITableView mTableView;
    private Context mContext;
    private MoyenTableAdapter mTableAdapter;
    private InterventionDetailsMoyensFragmentsTV interTabMoyensFrag;

    public MoyenTableViewListener(ITableView pTableView) {
        this.mTableView = pTableView;
        this.mContext = null;
    }

    public MoyenTableViewListener(ITableView pTableView, Context mContext, MoyenTableAdapter mTableAdapter, InterventionDetailsMoyensFragmentsTV InterTabMoyensFrag) {
        this.mTableView = pTableView;
        this.mContext = mContext;
        this.mTableAdapter = mTableAdapter;
        this.interTabMoyensFrag = InterTabMoyensFrag;
    }

    @Override
        public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int
            p_nYPosition) {
        onRowHeaderClicked(p_jCellView, p_nYPosition);
        if (mContext != null && mContext instanceof MapActivity) {

                ((MapActivity) mContext).displayFragmentHolder(interTabMoyensFrag.getListDeploiment().get(p_nYPosition));
            // Le fragment existe
            if (interTabMoyensFrag.isReduce()) {
                interTabMoyensFrag.populatedTableViewReduce(interTabMoyensFrag.getListDeploiment(), true, p_nYPosition);
            } else {
                interTabMoyensFrag.populatedTableViewAll(interTabMoyensFrag.getListDeploiment(), true, p_nYPosition);
            }

        } else {
            // La istic.m2.ila.firefighterapp.map (et le fragment) n'existe pas
            // On ne fait rien
        }
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int
            p_nXPosition) {

        if (interTabMoyensFrag.isReduce()) {
            interTabMoyensFrag.populatedTableViewReduce(interTabMoyensFrag.getListDeploiment(), false, -1);
        } else {
            interTabMoyensFrag.populatedTableViewAll(interTabMoyensFrag.getListDeploiment(), false, -1);
        }
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int p_nXPosition) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {
        Log.e(getClass().getSimpleName(), "onRowHeaderClicked a été appelé");

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int
            p_nYPosition) {

    }
}
