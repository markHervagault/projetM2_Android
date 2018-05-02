package istic.m2.ila.firefighterapp.Intervention.holder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import istic.m2.ila.firefighterapp.R;


/**
 * Created by evrencoskun on 1.12.2017.
 */

public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textview;

    public RowHeaderViewHolder(View p_jItemView) {
        super(p_jItemView);
        row_header_textview = p_jItemView.findViewById(R.id.row_header_textview);
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

        int nBackgroundColorId;
        int nForegroundColorId;


            nBackgroundColorId = Color.parseColor("#28415f");
            nForegroundColorId = Color.WHITE;


        itemView.setBackgroundColor(nBackgroundColorId);
        row_header_textview.setTextColor(nForegroundColorId);
    }
}
