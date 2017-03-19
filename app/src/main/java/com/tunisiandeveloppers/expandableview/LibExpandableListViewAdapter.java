package com.tunisiandeveloppers.expandableview;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tunisiandeveloppers.expandableview.R;
import com.tunisiandeveloppers.expandableview.R2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MED on 17/03/2017.
 */
public class LibExpandableListViewAdapter extends RecyclerView.Adapter<LibExpandableListViewAdapter.ViewHolder> {

    private String[] data;

    private List<Boolean> itemsState;

    @Nullable
    private ViewHolder lastSelected;
    private int lastExpanded = -1;

    public LibExpandableListViewAdapter(String[] stringArray) {
        this.data = stringArray;
        this.itemsState = new ArrayList<>(Collections.nCopies(data.length, false));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.expandableView.setContentText(data[position]);
        //holder.expandableView.setExpanded(false);
        holder.expandableView.setHeaderText(position + " " + holder.getAdapterPosition() + " " + holder.getLayoutPosition() + " " + holder.toString());

        holder.expandableView.setOnExpandableViewChangeStatusListener(new OnExpandableViewChangeStatusListener() {
            @Override
            public void onCollapse() {
                int position = holder.getLayoutPosition();
                holder.expandableView.setExpanded(false);
                itemsState.set(position, false);
            }

            @Override
            public void onExpand() {
                int position = holder.getLayoutPosition();
                itemsState.set(position, true);
                if (lastSelected != null && lastExpanded != position && lastExpanded != -1) {
                    lastSelected.expandableView.collapse();
                    itemsState.set(lastExpanded, false);
                }
                holder.expandableView.setExpanded(true);
                lastExpanded = position;
                lastSelected = holder;
            }
        });


        if (itemsState.get(position)) {
            holder.expandableView.expand();
        } else {
            holder.expandableView.collapse();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.lib_recycle_item;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.expanded_item)
        protected ExpandableView expandableView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
