package istic.m2.ila.firefighterapp.Intervention.holder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import istic.m2.ila.firefighterapp.Intervention.model.*;
import istic.m2.ila.firefighterapp.R;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.recyclerview.CellRecyclerView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;



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
        if (backgroundColor != null) {
            cell_textview.setBackgroundColor(Color.parseColor(backgroundColor));
        }

        // Adapter le text color de la cellule à la composante
        String textColor = p_jModel.getTextColor();
        if (textColor != null) {
            cell_textview.setTextColor(Color.parseColor(textColor));
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

        if (p_nSelectionState == SelectionState.SELECTED) {
            if (cellRow != null) {
                // Sélectionner la ligne
                cellRow.setSelected(p_nSelectionState);
//                cellRow.row_header_textview.performClick();

            } else {
                cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
                    .selected_text_color));
            }
        } else {
            cell_textview.setTextColor(ContextCompat.getColor(cell_textview.getContext(), R.color
                    .selected_text_color));
        }
    }

    private RowHeaderViewHolder cellRow;
    public void setRowHeaderViewHolder(RowHeaderViewHolder r){
        cellRow = r;
    }
}
