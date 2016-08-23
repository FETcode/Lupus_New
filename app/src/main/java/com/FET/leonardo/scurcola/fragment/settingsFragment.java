package com.FET.leonardo.scurcola.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ImageButton;
import android.widget.Toast;

import com.FET.leonardo.scurcola.DataProvider;
import com.FET.leonardo.scurcola.R;



public class SettingsFragment extends DialogFragment {
    private DataProvider provider;
    private ImageButton imageButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        provider = (DataProvider) getActivity();
        imageButton = (ImageButton) getActivity().findViewById(R.id.restartButton); //TODO Change the right ID

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings)
                .setItems(R.array.settingsOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch(which){
                            case 0:
                                //Restart game
                                SharedPreferences preferences = getActivity().getSharedPreferences("X", Context.MODE_PRIVATE);
                                preferences.edit().clear().apply();
                                provider.getFragmentSwitcher().main();
                                provider.hideSoftKeyBoard(imageButton);
                                break;
                            case 1:
                                //Credits
                                Toast.makeText(getActivity(), "App created by FET", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
