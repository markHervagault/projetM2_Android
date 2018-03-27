package istic.m2.ila.firefighterapp.Intervention;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.util.List;

import istic.m2.ila.firefighterapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsMoyensFragments extends Fragment {

    private List<String> datas;
    private Context context;

    public InterventionDetailsMoyensFragments() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //init pojo data
        this.context = context;
        this.datas = ((DetailsInterventionActivity)this.getActivity()).getDatas();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.interventionDetailsMoyenRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleAdapter(recyclerView,datas));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //update fields
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;
        private List<String> dataset;

        public SimpleAdapter(RecyclerView recyclerView,List<String> dataset) {
            this.recyclerView = recyclerView;
            this.dataset = dataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_intervention_recycler_item_type_moyen, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private ExpandableLayout expandableLayout;
            private LinearLayout expandedLinearLayout;
            private TextView expandButton;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = itemView.findViewById(R.id.expand_button);
                expandButton.setOnClickListener(this);
                expandedLinearLayout = itemView.findViewById(R.id.list_layout_moyen);
            }

            public void bind() {
                int position = getAdapterPosition();
                String content = dataset.get(position);

                boolean isSelected = position == selectedItem;
                expandButton.setText(content);
                expandButton.setSelected(isSelected);
                expandableLayout.setExpanded(isSelected, false);

                TextView tmpText1 = new TextView(itemView.getContext());
                TextView tmpText2 = new TextView(itemView.getContext());
                TextView tmpText3 = new TextView(itemView.getContext());
                tmpText1.setText("Item 1");
                tmpText2.setText("Item 2");
                tmpText3.setText("Item 3");

                //for each item add to layout
                expandedLinearLayout.addView(tmpText1);
                expandedLinearLayout.addView(tmpText2);
                expandedLinearLayout.addView(tmpText3);
            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                int position = getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }

            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
                if (state == ExpandableLayout.State.EXPANDING) {
                    recyclerView.smoothScrollToPosition(getAdapterPosition());
                }
            }
        }
    }
}
