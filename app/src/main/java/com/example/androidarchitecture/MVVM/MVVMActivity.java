package com.example.androidarchitecture.MVVM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidarchitecture.MVP.CountriesPresenter;
import com.example.androidarchitecture.MVP.MVPActivity;
import com.example.androidarchitecture.R;

import java.util.ArrayList;
import java.util.List;

public class MVVMActivity extends AppCompatActivity {

    private List<String> listValues = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView list;
    private CountriesViewModel mViewModel;
    private Button retryButton;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);
        setTitle("MVVM Activity");

        mViewModel = ViewModelProviders.of(this).get(CountriesViewModel.class);

        list = findViewById(R.id.list);
        retryButton = findViewById(R.id.retryButton);
        progress = findViewById(R.id.progress);

        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.listText, listValues);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MVVMActivity.this, "You clicked "+ listValues.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        observeViewModel();
    }

    private void observeViewModel(){
        mViewModel.getCountries().observe(this, countries -> {
            if(countries != null){
                listValues.clear();
                listValues.addAll(countries);
                list.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
            else{
                list.setVisibility(View.GONE);
            }
        });

        mViewModel.getCountryError().observe(this, error -> {
            progress.setVisibility(View.GONE);
            if(error){
                Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
                retryButton.setVisibility(View.VISIBLE);
            }else{
                retryButton.setVisibility(View.GONE);
            }
        });
    }

    public static Intent getIntent(Context context){
        return new Intent(context, MVVMActivity.class);
    }

    public void onRetry(View view){
        mViewModel.onRefresh();
        list.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }
}