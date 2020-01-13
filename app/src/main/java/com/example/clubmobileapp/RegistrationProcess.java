package com.example.clubmobileapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationProcess extends Fragment {
    private View view;
    private Button becomeMember;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.registration_process_fragment, container, false);
        becomeMember = view.findViewById(R.id.becomeMemberBtn);

        becomeMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.become_a_member_layout);
                dialog.show();
                dialog.getWindow().setLayout(700, 850);

                Button confirmBtn = dialog.findViewById(R.id.confirmBtn);
                confirmBtn.setFocusable(true);

                Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
                cancelBtn.setFocusable(true);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Your membership request was successful. The club will get back to you short", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}
