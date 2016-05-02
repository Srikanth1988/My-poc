package com.example.sreez.searchresultsapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.example.sreez.searchresultsapplication.R;

/**
 * Created by sreez on 4/27/2016.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.opps_sorry_text)
                .setMessage(R.string.error_msg_text)
                .setPositiveButton(R.string.ok_text, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
