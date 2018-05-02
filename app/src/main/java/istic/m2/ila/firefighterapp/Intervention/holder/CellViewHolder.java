package istic.m2.ila.firefighterapp.Intervention.holder;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import istic.m2.ila.firefighterapp.Intervention.model.CellModel;
import istic.m2.ila.firefighterapp.R;



/**
 * Created by evrencoskun on 1.12.2017.
 */

public class CellViewHolder extends AbstractViewHolder {
    public final TextView cell_textview;
    public final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCellModel(CellModel p_jModel, int pColumnPosition) {

        // Adapter le background de la cellule à la composante
        String backgroundColor = p_jModel.getBackgroundColor();
        if (p_jModel.isSelected()) {
            cell_textview.setBackgroundColor(Color.parseColor("#99FFFF"));
            cell_textview.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) cell_textview.getLayoutParams();
            param.setMargins(0, 0, 0, 0);
            cell_textview.setLayoutParams(param);


        } else if (backgroundColor != null) {
            cell_textview.setBackgroundColor(Color.parseColor(backgroundColor));
            cell_textview.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) cell_textview.getLayoutParams();
            param.setMargins(0, 0, 0, 0);
            cell_textview.setLayoutParams(param);

        } else {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) cell_textview.getLayoutParams();
            param.setMargins(10, 0, 10, 0);
            cell_textview.setLayoutParams(param);

        }

        // Adapter le text color de la cellule à la composante
        String textColor = p_jModel.getTextColor();
        if (p_jModel.isSelected()) {
            cell_textview.setTextColor(Color.BLACK);
        } else if (textColor != null) {
            cell_textview.setTextColor(Color.parseColor(textColor));

        } else {
            cell_textview.setTextColor(Color.BLACK);
        }

        // Change textView align by column
        cell_textview.setGravity(ColumnHeaderViewHolder.COLUMN_TEXT_ALIGNS[pColumnPosition] |
                Gravity.CENTER_VERTICAL);

        // Set text
        cell_textview.setText(String.valueOf(p_jModel.getData()));

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

    }

    private RowHeaderViewHolder cellRow;
    public void setRowHeaderViewHolder(RowHeaderViewHolder r){
        cellRow = r;
    }
}
