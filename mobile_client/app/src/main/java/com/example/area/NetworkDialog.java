package com.example.area;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The type Network dialog.
 */
public class NetworkDialog extends AppCompatDialogFragment {
    private EditText editTextNetwork;
    private NetworkDialogListener listener;
    private tokenManager manageToken = new tokenManager();

    /**
     * Create your own custom Dialog object
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.network_dialog, null);

        String restoredNetwork = null;
        restoredNetwork = manageToken.retrieveSharedPref("userToken", "networkLocation", getActivity());
        if (restoredNetwork != null) {
            EditText ipText = view.findViewById(R.id.editText_network);
            ipText.setText(restoredNetwork, TextView.BufferType.EDITABLE);
        }
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String network = editTextNetwork.getText().toString();
                        listener.getNetwork(network);
                    }
                });
        editTextNetwork = view.findViewById(R.id.editText_network);
        return builder.create();
    }

    /**
     * Called once the fragment is associated with its activity
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NetworkDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    /**
     * The interface Network dialog listener.
     */
    public interface NetworkDialogListener {
        /**
         * Gets network.
         *
         * @param network the network
         */
        void getNetwork(String network);
    }
}
