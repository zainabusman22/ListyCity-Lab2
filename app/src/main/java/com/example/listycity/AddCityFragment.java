package com.example.listycity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
//import android.app.Dialog;
//import android.view.LayoutInflater;
//import android.view.View;


public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
    }
    private AddCityDialogListener listener;

    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        Bundle args = getArguments();
        if (args != null && args.containsKey("city")) {
            City cityToEdit = (City) args.getSerializable("city");
            if (cityToEdit != null) {
                editCityName.setText(cityToEdit.getName());
                editProvinceName.setText(cityToEdit.getProvince());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/edit city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    Bundle argus = getArguments();
                    if (argus != null && argus.containsKey("city")) {
                        // --- EDIT MODE ---
                        City cityToEdit = (City) argus.getSerializable("city");
                        // Update the existing object using setters
                        cityToEdit.setCityName(cityName);
                        cityToEdit.setProvinceName(provinceName);
                        // Notify the listener to refresh the list
                        listener.addCity(cityToEdit);
                    } else {
                        // --- ADD MODE ---
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
