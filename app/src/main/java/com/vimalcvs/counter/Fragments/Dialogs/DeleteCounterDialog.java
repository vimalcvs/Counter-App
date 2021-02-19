package com.vimalcvs.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.vimalcvs.counter.R;


public class DeleteCounterDialog extends AppCompatDialogFragment {
  private final DeleteDialogListener mListener;
  private final int mCountersCount;

    public interface DeleteDialogListener {
        void onDialogDeleteClick();
    }

   public DeleteCounterDialog(DeleteDialogListener listener, int count){
        mListener = listener;
        mCountersCount = count;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title;

        if (mCountersCount > 1){
            title = getResources().getString(R.string.deleteCountersDeleteDialog);
        }else {
            title = getResources().getString(R.string.deleteCounterDeleteDialog);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(R.string.deleteCounterDialogText)
                .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which) -> {
                    mListener.onDialogDeleteClick();
                })
                .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null);

        return builder.create();
    }
}
