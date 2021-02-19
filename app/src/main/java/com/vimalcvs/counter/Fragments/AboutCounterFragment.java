package com.vimalcvs.counter.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.vimalcvs.counter.R;
import com.vimalcvs.counter.Utility;
import com.vimalcvs.counter.ViewModels.AboutCounterViewModel;
import com.vimalcvs.counter.ViewModels.Factories.AboutCounterViewModelFactory;


public class AboutCounterFragment extends Fragment {
    private MaterialTextView mCounterName;
    private MaterialTextView mCreateData;
    // TODO: 2/13/2021 reseted заменить другим словом
    private MaterialTextView mLastResetedValue;
    private MaterialTextView mCounterValue;
    private MaterialTextView mCounterStep;
    private MaterialTextView mCounterGroup;
    private MaterialTextView mCounterMinValue;
    private MaterialTextView mCounterMaxValue;
    private MaterialTextView mLastResetData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_counter, container, false);
        long counterId = AboutCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        AboutCounterViewModel viewModel = new ViewModelProvider(this, new AboutCounterViewModelFactory(requireActivity().getApplication(),
                counterId)).get(AboutCounterViewModel.class);
        mCounterName = view.findViewById(R.id.counterName);
        mCreateData = view.findViewById(R.id.createData);
        mLastResetedValue = view.findViewById(R.id.lastResetedValue);
        mCounterValue = view.findViewById(R.id.value);
        mCounterStep = view.findViewById(R.id.step);
        mCounterGroup = view.findViewById(R.id.group);
        mCounterMinValue = view.findViewById(R.id.minValue);
        mCounterMaxValue = view.findViewById(R.id.maxValue);
        mLastResetData = view.findViewById(R.id.lastReset);

        Toolbar mToolbar = view.findViewById(R.id.toolbar_aboutCounter);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        viewModel.counter.observe(getViewLifecycleOwner(), counter -> {
            mCounterName.setText(counter.title);
            mCreateData.setText(getString(R.string.created, Utility.formatDateToString(counter.createDate)));
            mLastResetedValue.setText(String.valueOf(counter.lastResetValue));
            mCounterValue.setText(String.valueOf(counter.value));
            mCounterStep.setText(String.valueOf(counter.step));
            if (counter.grope!=null)
            mCounterGroup.setText(counter.grope);
            mCounterMinValue.setText(String.valueOf(counter.counterMinValue));
            mCounterMaxValue.setText(String.valueOf(counter.counterMaxValue));
            if (counter.lastResetDate!=null)
            mLastResetData.setText(Utility.formatDateToString(counter.lastResetDate));
        });

        return view;
    }
}