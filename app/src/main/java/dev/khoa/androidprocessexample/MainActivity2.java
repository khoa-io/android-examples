package dev.khoa.androidprocessexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import dev.khoa.androidprocessexample.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);

    SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, key) -> {
        if (Objects.equals(key, "MY_KEY")) {
            // Do some with with key "MY_KEY"
        }
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this.listener);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("MY_KEY", true);
        editor.apply();
    }
}
