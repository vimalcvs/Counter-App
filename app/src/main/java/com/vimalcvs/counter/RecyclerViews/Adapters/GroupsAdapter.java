package com.vimalcvs.counter.RecyclerViews.Adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.vimalcvs.counter.R;
import com.vimalcvs.counter.RecyclerViews.DrawerMenuItemClickHelper;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Vh>{

    private final DrawerMenuItemClickHelper mDrawerMenuItemClickHelper;
    private List<String> mData = new ArrayList<>();

    public GroupsAdapter() {
        mDrawerMenuItemClickHelper = new DrawerMenuItemClickHelper();
    }

    public void setGroups (List<String> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public LiveData<String> getSelectedItem() {
        return mDrawerMenuItemClickHelper.getSelectedItem();
    }

    public void allCountersItemSelected(View view) {
        mDrawerMenuItemClickHelper.allCountersItemSelected(view);
        notifyDataSetChanged();
    }
    public void restoreSelectedItem(String string) {
        mDrawerMenuItemClickHelper.restoreSelectedItem(string);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class Vh extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private LinearLayout mItem;
        private TextView mTitle;

        public Vh(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_i, parent, false));
            mItem = itemView.findViewById(R.id.item_group_i);
            mTitle = itemView.findViewById(R.id.group_title_i);
            itemView.setOnTouchListener(this);

        }

        public void bind(String s) {
            mTitle.setText(s);
            mDrawerMenuItemClickHelper.bindBackground(s,this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    itemView.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                    itemView.performClick();
                    mDrawerMenuItemClickHelper.selectRvItem(mData.get(getBindingAdapterPosition()), this);
                    notifyDataSetChanged();
                    //no break
                case MotionEvent.ACTION_CANCEL:
                    itemView.setPressed(false);
                    break;
            }
            return true;
        }
    }
}
