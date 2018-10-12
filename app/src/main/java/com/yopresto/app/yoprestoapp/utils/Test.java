package com.yopresto.app.yoprestoapp.utils;

import android.content.Context;

import com.yopresto.app.yoprestoapp.R;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Test {

    public void viewDialog(Context context){
        final PrettyDialog  dialog = new PrettyDialog(context)
                .setTitle("PrettyDialog Title")
                .setMessage("PrettyDialog Message");

                dialog.addButton(
                        "OK",					// button text
                        R.color.pdlg_color_white,		// button text color
                        R.color.pdlg_color_green,		// button background color
                        new PrettyDialogCallback() {		// button OnClick listener
                            @Override
                            public void onClick() {
                                // Do what you gotta do
                                dialog.dismiss();
                            }
                        }
                );
                dialog.show();

    }
}
