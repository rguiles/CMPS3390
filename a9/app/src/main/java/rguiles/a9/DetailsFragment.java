package rguiles.a9;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    Coin coin;
    TextView chartLabel;
    private LineData data;
    LineChart lineChart;
    int color = 0;
    private final Context context;

    public DetailsFragment(Context context, Coin coin) {
        this.coin = coin;
        this.context = context;

        if(coin.getName().equals("bitcoin"))
            color = ContextCompat.getColor(context, R.color.bitcoin);
        else if (coin.getName().equals("ethereum"))
            color = ContextCompat.getColor(context, R.color.ethereum);

        getHistoricalData();
    }

    private void getHistoricalData() {
        String url = String.format("https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=90&interval=daily",
                coin.getName());
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray prices = response.getJSONArray("prices");
                    ArrayList<Entry> yValues = new ArrayList<>();

                    for(int loop = 0; loop < prices.length(); loop++){
                        float val = (float)prices.getJSONArray(loop).getDouble(1);
                        yValues.add(new Entry(loop, val));
                        // Log.d("HIST", String.valueOf(prices));
                    }

                    LineDataSet dataSet = new LineDataSet(yValues, "Daily Price");
                    dataSet.setColor(color);
                    dataSet.setLineWidth(3);
                    dataSet.setDrawCircles(false);
                    dataSet.setDrawValues(false);
                    data = new LineData(dataSet);
                    updateChart();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateChart() {
        if(lineChart != null){
            lineChart.setData(data);
            lineChart.invalidate();
            lineChart.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        chartLabel = getView().findViewById(R.id.txtChartLabel);
        chartLabel.setText(coin.getName().toUpperCase());
        lineChart = getView().findViewById(R.id.lcPrice);
        lineChart.animateY(500);
        lineChart.animateX(2000);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        updateChart();
    }
}