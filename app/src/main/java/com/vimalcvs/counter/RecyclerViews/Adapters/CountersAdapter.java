package com.vimalcvs.counter.RecyclerViews.Adapters;

import android.app.Application;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.FastCountButton;
import com.vimalcvs.counter.R;
import com.vimalcvs.counter.RecyclerViews.DragAndDrop.ItemTouchHelperAdapter;
import com.vimalcvs.counter.RecyclerViews.DragAndDrop.ItemTouchHelperViewHolder;
import com.vimalcvs.counter.RecyclerViews.DragAndDrop.MyItemTouchHelper;
import com.vimalcvs.counter.RecyclerViews.DragAndDrop.CounterSelectionMod;

import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements ItemTouchHelperAdapter {


    public interface CounterItemListeners {
        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
    }

        private final CounterSelectionMod mCounterSelectionMod;

        private List<Counter> mData = new ArrayList<>();
        private final CounterItemListeners mCounterItemListeners;
        private final ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        public final boolean mLeftHandMod;

        public CountersAdapter(CounterItemListeners counterItemListeners, Application application) {
            setHasStableIds(true);
            mCounterSelectionMod = new CounterSelectionMod(application);
            mCounterItemListeners = counterItemListeners;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
            mLeftHandMod = sharedPreferences.getBoolean("leftHandMod", false);
        }

        public void setData(List<Counter> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public void selectAllCounters(){
            mCounterSelectionMod.selectAllCounters(mData);
            notifyDataSetChanged();
        }

         public void clearSelectedCounters(){
            mCounterSelectionMod.clearAllSelectedCounters();
            notifyDataSetChanged();
         }

         public void decSelectedCounters() {
            mCounterSelectionMod.decSelectedCounters();
         }

         public void incSelectedCounters() {
            mCounterSelectionMod.incSelectedCounters();
         }

         public void resetSelectedCounters() {
            mCounterSelectionMod.resetSelectedCounters();
         }

         public void undoReset() {
            mCounterSelectionMod.undoReset();
         }

         public void deleteSelectedCounters() {
            mCounterSelectionMod.deleteSelectedCounters();
         }

         public LiveData<Integer> getSelectedCountersCount(){return mCounterSelectionMod.getSelectedCountersCount();}

         public Counter getSelectedCounter() {
            return mCounterSelectionMod.getSelectedCounter();
          }

         public LiveData<Boolean> getSelectionMod(){
            return mCounterSelectionMod.selectionMod;
         }

        @NonNull
        @Override
        public CountersAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mLeftHandMod){
                return new CountersAdapter.Vh(parent, R.layout.counter_left_hend_i);
            }else {
                return new CountersAdapter.Vh(parent, R.layout.counter_i);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull CountersAdapter.Vh holder, int position) {
            holder.bind(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public long getItemId(int position) {
            return mData.get(position).id;
        }

        @Override
        public void onMoved(int fromPos, int toPos) {
            if (fromPos != -1 && toPos != -1){
                Counter deleted = mData.remove(fromPos);
                mData.add(toPos, deleted);
                mCounterItemListeners.onMoved(mData.get(fromPos), mData.get(toPos));
                notifyItemMoved(fromPos, toPos);
            }
        }


        public class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
                View.OnTouchListener, GestureDetector.OnGestureListener {
            private final GestureDetector mGestureDetector;
            private final TextView mTitle;
            private final TextView mValue;
            private final TextView mPlus;
            private final TextView mMinus;

            public Vh(@NonNull ViewGroup parent, int idLayout) {
                super(LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false));
                mTitle = itemView.findViewById(R.id.title_i);
                mValue = itemView.findViewById(R.id.value_i);
                mPlus = itemView.findViewById(R.id.plus_i);
                mMinus = itemView.findViewById(R.id.minus_i);
                mGestureDetector = new GestureDetector(parent.getContext(),this);
                itemView.setOnTouchListener(this);

                 new FastCountButton(mPlus, () -> {
                    if (getBindingAdapterPosition()!=-1)
                        mCounterItemListeners.onPlusClick(mData.get(getBindingAdapterPosition()));
                });

                new FastCountButton(mMinus, () ->{
                    if (getBindingAdapterPosition()!=-1)
                        mCounterItemListeners.onMinusClick(mData.get(getBindingAdapterPosition()));
                });


            }

            private void bind(Counter counter) {
                mTitle.setText(counter.title);
                mValue.setText(String.valueOf(counter.value));
                mCounterSelectionMod.setDefaultBackground(itemView.getBackground());
                mCounterSelectionMod.bindVhBackground(counter,this);

                /*if selection mod is active we need to disable buttons plus and minos*/
                mCounterSelectionMod.selectionMod.observe((LifecycleOwner) itemView.getContext(), aBoolean -> {
                    if (aBoolean){
                        mMinus.setClickable(false);
                        mPlus.setClickable(false);
                        mPlus.setEnabled(false);
                        mMinus.setEnabled(false);
                    }else {
                        mMinus.setClickable(true);
                        mPlus.setClickable(true);
                        mPlus.setEnabled(true);
                        mMinus.setEnabled(true);
                    }
                });

            }


            @Override
            public void clearView() {
                mCounterSelectionMod.clearDragHolderBackground();
            }

            @Override
            public void onDragging(RecyclerView.ViewHolder holder) {
                mCounterSelectionMod.dragHolder(holder);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mCounterSelectionMod.selectionMod.getValue()){
                    mCounterSelectionMod.selectCounter(mData.get(getBindingAdapterPosition()),this);
                }else {
                    mCounterItemListeners.onOpen(mData.get(getBindingAdapterPosition()));
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if(!mCounterSelectionMod.selectionMod.getValue()){
                    itemView.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
                    itemTouchHelper.startDrag(this);
                }
                if (getBindingAdapterPosition()!=-1)
                mCounterSelectionMod.selectCounter(mData.get(getBindingAdapterPosition()),this);
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        itemView.setPressed(!mCounterSelectionMod.selectionMod.getValue());
                        break;
                    case MotionEvent.ACTION_UP:
                        itemView.performClick();
                        //no break
                    case MotionEvent.ACTION_CANCEL:
                        itemView.setPressed(false);
                        break;
                }
                return true;
            }
        }
}
