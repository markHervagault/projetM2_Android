package istic.m2.ila.firefighterapp.Intervention.model;

import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 27.11.2017.
 */

public class CellModel implements ISortableModel {
    private String mId;
    private Object mData;
    private String backgroundColor;
    private String textColor;

    public CellModel(String pId, Object mData) {
        this.mId = pId;
        this.mData = mData;
        this.backgroundColor = null;
        this.textColor = null;
    }

    public Object getData() {
        return mData;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public Object getContent() {
        return mData;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTextColor() {
        return textColor;
    }
}
