package com.goodeva.testapi;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    //initiate Data and Service
    Button btn_cityID, btn_weatherID, btn_weatherName ;
    EditText input_Data;
    ListView lv_weather_report;

    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btn_cityID = findViewById(R.id.btn_getCityID);
//        btn_weatherID = findViewById(R.id.btn_getWeatherByID);
        btn_weatherName = findViewById(R.id.btn_getWeatherByName);

        input_Data = findViewById(R.id.input_Data);
        lv_weather_report = findViewById(R.id.lv_weatherReport);

        //add validation
        awesomeValidation.addValidation(this,R.id.input_Data, RegexTemplate.NOT_EMPTY,R.string.invalid_data);


        //Button Search City by ID
//        btn_cityID.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (awesomeValidation.validate()) {
//                    // Here, we are sure that form is successfully validated. So, do your stuffs now...
//
//                    weatherDataService.getCityID(input_Data.getText().toString(), new WeatherDataService.VolleyResponseListener() {
//                        @Override
//                        public void onError(String massage) {
//                            Toast.makeText(MainActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onResponse(String cityID) {
//                            Toast.makeText(MainActivity.this, "City ID : " + cityID, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });

//        btn_weatherID.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    weatherDataService.getCityForecastByID(input_Data.getText().toString(), new WeatherDataService.ForCastByIDResponse() {
//
//                        @Override
//                        public void onError(String massage) {
//                            if (awesomeValidation.validate()) {
//                                Toast.makeText(MainActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onResponse(List<WeatherReportModel> weatherReportModels) {
//
//                            //put entire list into the listview control
//                            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModels);
//                            lv_weather_report.setAdapter(arrayAdapter);
//                        }
//                    });
//            }
//        });

        btn_weatherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    weatherDataService.getCityForecastByName(input_Data.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                        @Override
                        public void onError(String massage) {
                            Toast.makeText(MainActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(List<WeatherReportModel> weatherReportModels) {
                            //put entire list into the listview control
                            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,
                                    weatherReportModels);
                            lv_weather_report.setAdapter(arrayAdapter);
                        }
                    });
                }

            }
        });
    }
}