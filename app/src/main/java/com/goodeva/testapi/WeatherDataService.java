package com.goodeva.testapi;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_ID = "https://www.metaweather.com/api/location/";

    String cityID;
    Context context;

    public WeatherDataService(Context context) {
        this.context = context ;
    }

    public interface VolleyResponseListener{
        void onError(String massage);

        void onResponse(String cityID);
    }

    public void getCityID(String cityName,final VolleyResponseListener volleyResponseListener){
        // Instantiate the RequestQueue.
        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null ,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                cityID = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(context, "City ID = " + cityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Error 404", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Error 404");
            }
        });
        //Function Callback
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ForCastByIDResponse{

        void onError(String massage);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByID(String cityID, ForCastByIDResponse forCastByIDResponse){
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        String url = QUERY_FOR_CITY_WEATHER_ID + cityID;

        //get the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");
                    // get first item in array

                    for (int i = 0; i<consolidated_weather_list.length();i++) {

                        JSONObject first_day_api = (JSONObject) consolidated_weather_list.get(i);
                        WeatherReportModel one_day_weather = new WeatherReportModel();

                        one_day_weather.setId(first_day_api.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_api.getString("weather_state_name"));
                        one_day_weather.setApplicable_date(first_day_api.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_api.getLong("min_temp"));
                        one_day_weather.setMax_temp(first_day_api.getLong("max_temp"));
                        one_day_weather.setThe_temp(first_day_api.getLong("the_temp"));

                        weatherReportModels.add(one_day_weather);
                    }

                    forCastByIDResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forCastByIDResponse.onError("Error");

            }
        });

        //get each item in the array and assign it to a new WeatherReportModel object
        MySingleton.getInstance(context).addToRequestQueue(request);MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallback{

        void onError(String massage);
        void onResponse(List<WeatherReportModel> weatherReportModel );
    }

    public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback){
        // fetch the city given the city name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String massage) {
                Toast.makeText(context, "Invalid Data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String cityID) {
                // now we have the city id
                getCityForecastByID(cityID, new ForCastByIDResponse() {
                    @Override
                    public void onError(String massage) {
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        //now we have the weather report
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }

                });
            }
        });

    }
}
