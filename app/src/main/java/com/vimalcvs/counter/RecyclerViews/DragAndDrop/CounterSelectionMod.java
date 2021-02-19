package com.vimalcvs.counter.RecyclerViews.DragAndDrop;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.vimalcvs.counter.Database.Models.Counter;
import com.vimalcvs.counter.Database.Repo;
import com.vimalcvs.counter.R;
import com.vimalcvs.counter.RecyclerViews.Adapters.CountersAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CounterSelectionMod {

    private final Repo mRepo;
    private  Drawable mDefaultBackground = null;
    private final MutableLiveData<Boolean> mSelectionMod = new MutableLiveData<>(false);
    private List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private RecyclerView.ViewHolder mDraggingHolder;
    private List<Counter> mCopyBeforeReset;
    private final MutableLiveData<Integer> mCountSelected = new MutableLiveData<>(mSelectedCounters.size());

    public LiveData<Boolean> selectionMod = mSelectionMod;


    public CounterSelectionMod(Application application) {
        mRepo = new Repo(application);
    }

    public void selectCounter(Counter newCounter, RecyclerView.ViewHolder viewHolder) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                unSelectCounter(oldCounter, viewHolder);
                isAlreadySelected = true;
                break;
            }
        }
        if (!isAlreadySelected) {
            if (!mSelectionMod.getValue())
                mSelectionMod.setValue(true);

            mSelectedCounters.add(newCounter);
            mSelectedVhs.add(viewHolder);
            setItemSelectedBackground(viewHolder);
        }
        mCountSelected.setValue(mSelectedCounters.size());
    }

    public void clearAllSelectedCounters() {
        mSelectionMod.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
        mCountSelected.setValue(0);
    }

    public void selectAllCounters(List<Counter> counters) {
        mSelectedCounters.clear();
        mSelectedCounters.addAll(counters);
        mSelectionMod.setValue(true);
        mCountSelected.setValue(counters.size());
    }

    public void dragHolder(RecyclerView.ViewHolder viewHolder) {
        clearAllSelectedCounters();
        setItemDraggingBackground(viewHolder);
    }

    public void clearDragHolderBackground() {
        if (mDraggingHolder != null) {
            setDefaultBackground(mDraggingHolder);
            mDraggingHolder = null;
        }
    }

    public void bindVhBackground(Counter newCounter, CountersAdapter.Vh vh) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                setItemSelectedBackground(vh);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) {
            if (mDraggingHolder == null) {
                setDefaultBackground(vh);
            }
        }
    }

    public void incSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            counter.value += counter.step;
            if (counter.value > counter.maxValue){
                counter.value = counter.maxValue;
            }
            if (counter.value > counter.counterMaxValue)
                counter.counterMaxValue = counter.value;
            if (counter.value < counter.counterMinValue)
                counter.counterMaxValue = counter.value;
            mRepo.updateCounter(counter);
        }
    }

    public void decSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            counter.value -= counter.step;
            if (counter.value < counter.minValue){
                counter.value = counter.minValue;
            }
            if (counter.value > counter.counterMaxValue)
                counter.counterMaxValue = counter.value;
            if (counter.value < counter.counterMinValue)
                counter.counterMinValue = counter.value;
            mRepo.updateCounter(counter);
        }
    }

    public void resetSelectedCounters() {
        mCopyBeforeReset = new ArrayList<>();
        for (Counter counter : mSelectedCounters) {
            Counter copy = new Counter(counter.title, counter.value,
                    counter.maxValue, counter.minValue, counter.step,
                    counter.grope, counter.createDate, counter.createDateSort,
                    counter.lastResetDate, counter.lastResetValue,
                    counter.counterMaxValue, counter.counterMinValue);
            copy.setId(counter.id);
            mCopyBeforeReset.add(copy);
            counter.lastResetValue = counter.value;
            counter.lastResetDate = new Date();

            if (counter.minValue > 0){
                counter.value = counter.minValue;
            }else {
                counter.value = 0;
            }
            mRepo.updateCounter(counter);
        }
        mCountSelected.setValue(mSelectedCounters.size());
    }

    public void undoReset() {
        for (Counter counter : mCopyBeforeReset) {
            mRepo.updateCounter(counter);
        }
        mSelectedCounters = mCopyBeforeReset;
        mSelectionMod.setValue(mSelectedCounters != null);
        mCountSelected.setValue(mSelectedCounters.size());
    }

    public void deleteSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            mRepo.deleteCounter(counter);
        }
        mSelectionMod.setValue(false);
        mCountSelected.setValue(0);
        mSelectedCounters.clear();
    }

    public LiveData<Integer> getSelectedCountersCount() {
        return mCountSelected;
    }

    public Counter getSelectedCounter() {
        return mSelectedCounters.get(0);
    }

    public void setDefaultBackground(Drawable background) {
        if (mDefaultBackground == null)
            mDefaultBackground = background;
    }


    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.setBackground(mDefaultBackground);
        vh.itemView.findViewById(R.id.counter_item).setBackgroundResource(0);
        vh.itemView.setElevation(7F);
    }

    private void setItemSelectedBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.findViewById(R.id.counter_item).setBackgroundResource(R.drawable.item_selected);
        vh.itemView.setElevation(8f);
    }



    private void setItemDraggingBackground(RecyclerView.ViewHolder viewHolder) {
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackground(mDefaultBackground);
        mDraggingHolder.itemView.setElevation(25F);
    }

    private void unSelectCounter(Counter counter, RecyclerView.ViewHolder viewHolder) {
        setDefaultBackground(viewHolder);
        if (mSelectedCounters.size() == 1)
            mSelectionMod.setValue(false);
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
    }

}
