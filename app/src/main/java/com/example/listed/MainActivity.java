package com.example.listed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Declaration
    private RecyclerView recyclerViewLinks;
    private ArrayList<LinkModel> arrayListTopLinks;
    private LinkAdapter linkAdapter;

    private ArrayList<LinkModel> arrayListRecentLinks;

    private GraphView graphView;
    private Chip chipTopLink, chipRecentLink;
    private TextView textViewTodayCLick, textViewTopLocation, textViewTopSource, textViewGreeting;

    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recyclerViewLinks = findViewById(R.id.recyclerview_links);
        recyclerViewLinks.setNestedScrollingEnabled(false);
        arrayListTopLinks = new ArrayList<>();

        arrayListRecentLinks = new ArrayList<>();

        textViewTodayCLick = findViewById(R.id.tv_today_clicks);
        textViewTopLocation = findViewById(R.id.tv_top_location);
        textViewTopSource = findViewById(R.id.tv_top_source);
        textViewGreeting = findViewById(R.id.tv_greeting);

        graphView = findViewById(R.id.line_graph);

        chipTopLink = findViewById(R.id.chip_top_links);
        chipRecentLink = findViewById(R.id.chip_recent_links);

        LocalDateTime currentDateTime = LocalDateTime.now();
        int hour = currentDateTime.getHour();
        if ( hour < 10 ) {
            textViewGreeting.setText("Good Morning");
        } else if ( hour < 16 ) {
            textViewGreeting.setText("Good Afternoon");
        } else {
            textViewGreeting.setText("Good Evening");
        }

        getData();

        chipTopLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkAdapter.setArrayListTopLinks(arrayListTopLinks);
                linkAdapter.notifyDataSetChanged();
            }
        });

        chipRecentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkAdapter.setArrayListTopLinks(arrayListRecentLinks);
                linkAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getData() {

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://api.inopenapp.com/api/v1/dashboardNew", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            Log.e("onResponse", response.toString());

                            JSONArray jsonArray = response.getJSONObject("data").getJSONArray("top_links");
//                            Log.e("-->>", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject info = jsonArray.getJSONObject(i);
                                arrayListTopLinks.add(new LinkModel(
                                        info.getString("original_image"),
                                        info.getString("title"),
                                        info.getString("times_ago"),
                                        info.getString("total_clicks"),
                                        info.getString("smart_link")
                                ));
                            }
                            if(jsonArray.length()>0){
                                linkAdapter = new LinkAdapter(arrayListTopLinks, MainActivity.this);
                                recyclerViewLinks.setHasFixedSize(true);
                                recyclerViewLinks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerViewLinks.setAdapter(linkAdapter);
                                linkAdapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            }

                            // for Recent link
                            JSONArray jsonArray2 = response.getJSONObject("data").getJSONArray("recent_links");
//                            Log.e("-->>", jsonArray2.toString());
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject info2 = jsonArray2.getJSONObject(i);
                                arrayListRecentLinks.add(new LinkModel(
                                        info2.getString("original_image"),
                                        info2.getString("title"),
                                        info2.getString("times_ago"),
                                        info2.getString("total_clicks"),
                                        info2.getString("smart_link")
                                ));
                            }

                            // for the chart
//                            Log.e("->", response.getJSONObject("data").getJSONObject("overall_url_chart").toString());
                            JSONObject info = response.getJSONObject("data").getJSONObject("overall_url_chart");
//                            Log.e("--->", "onResponse:" + String.valueOf(info.getInt("2023-05-24")));
                            graphView.setTitleColor(R.color.blue_listed);
                            graphView.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
                            // change x-axis data type to string
                            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
                            staticLabelsFormatter.setHorizontalLabels(new String[] {"26 Apr", "9 May","16 May", "22 May"});
                            graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                            series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                    // on below line we are adding
                                    // each point on our x and y axis.
                                    new DataPoint(0, info.getInt("2023-04-26")),
                                    new DataPoint(1, info.getInt("2023-04-28")),
                                    new DataPoint(2, info.getInt("2023-05-02")),
                                    new DataPoint(3, info.getInt("2023-05-05")),
                                    new DataPoint(4, info.getInt("2023-05-09")),
                                    new DataPoint(5, info.getInt("2023-05-12")),
                                    new DataPoint(6, info.getInt("2023-05-16")),
                                    new DataPoint(7, info.getInt("2023-05-19")),
                                    new DataPoint(8, info.getInt("2023-05-20")),
                                    new DataPoint(9, info.getInt("2023-05-22")),
                                    new DataPoint(10, info.getInt("2023-05-24"))
                            });
                            graphView.addSeries(series);

                            //for the cards
//                            Log.e("-->", "onResponse:" + response.getInt("today_clicks"));
                            textViewTodayCLick.setText(String.valueOf(response.getInt("today_clicks")));
                            textViewTopLocation.setText(String.valueOf(response.getInt("top_location")));
                            textViewTopSource.setText(String.valueOf(response.getInt("top_source")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI");
                return headers;
            }
        };
        mQueue.add(request);
    }
}