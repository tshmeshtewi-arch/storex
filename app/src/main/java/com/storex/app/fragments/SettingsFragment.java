package com.storex.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.storex.app.MainActivity;
import com.storex.app.R;
import com.storex.app.utils.SessionManager;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = new SessionManager(requireContext());

        TextView settingsName = view.findViewById(R.id.settingsName);
        TextView settingsEmail = view.findViewById(R.id.settingsEmail);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        settingsName.setText(session.getLoggedInName());
        settingsEmail.setText(session.getLoggedInEmail());

        btnLogout.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logoutAndRestart();
            }
        });
    }
}
