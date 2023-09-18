package com.example.weatherapidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private Button search;
    private Spinner location_spinner,element_spinner,time_spinner;
    private String selected_location,selected_element,selected_time;
    private  ApiClient apiClient;
    private GetApi getApi;
    private String[] location_data,element_data,time_data,tw_element;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        search = findViewById(R.id.search);
        location_spinner = findViewById(R.id.locationName);
        element_spinner = findViewById(R.id.elementName);
        time_spinner = findViewById(R.id.time);
        //綁定物件id
        apiClient = new ApiClient();
        getApi = apiClient.myWeatherApi().create(GetApi.class);
        //初始化apiClient以及綁定
        location_data = getResources().getStringArray(R.array.location_data);
        element_data = getResources().getStringArray(R.array.element_data);
        time_data = getResources().getStringArray(R.array.time_data);
        tw_element = getResources().getStringArray(R.array.tw_element);
        //將spinner的資料從strings抓過來

        setSpinner();
        search.setOnClickListener(view -> getWeather(selected_location,selected_element,selected_time));
    }

    private void setSpinner() {
        ArrayAdapter location_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                location_data
        );
        ArrayAdapter element_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                element_data
        );
        ArrayAdapter time_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                time_data
        );

        location_spinner.setAdapter(location_adapter);
        element_spinner.setAdapter(element_adapter);
        time_spinner.setAdapter(time_adapter);//分別設定spinner的資料

        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_location = location_spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//location的spinner點擊事件

        element_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_element = element_spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//element的spinner點擊事件

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_time = time_spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//time的spinner點擊事件
    }
    private void getWeather(String selectedLocation, String selectedElement, String selectedTime) {
        String authorization = "CWB-2F70211E-8C2F-4A7F-8841-292FDCE00BEB";
        if (selectedElement.equals("All")) selectedElement = "";
        String finalSelectedElement = selectedElement;
        getApi.getWeatherApi(authorization,selectedLocation,selectedElement)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherResponse>() {
                    @Override
                    public void onNext(@NonNull WeatherResponse weatherResponse) {
                        result.setText("");
                        List time_list = Arrays.asList(time_data);
                        List element_list = Arrays.asList(element_data);
                        if(weatherResponse.getElementSize() != 1){
                            for (int i = 0; i < weatherResponse.getElementSize(); i++) {
                                result.append(tw_element[i] + weatherResponse.getDataByTime(i,time_list.indexOf(selectedTime))+"\n");
                            }
                        }
                        else {
                            result.setText(tw_element[element_list.indexOf(finalSelectedElement)] + weatherResponse.getDataByTime(0,time_list.indexOf(selectedTime))+"\n");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("test", "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("test", "onComplete: ");
                    }
                });
    }
}