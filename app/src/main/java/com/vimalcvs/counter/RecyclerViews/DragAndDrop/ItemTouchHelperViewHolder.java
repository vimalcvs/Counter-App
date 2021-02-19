package com.vimalcvs.counter.RecyclerViews.DragAndDrop;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperViewHolder {
     void clearView();
     void onDragging(RecyclerView.ViewHolder viewHolder);
}
