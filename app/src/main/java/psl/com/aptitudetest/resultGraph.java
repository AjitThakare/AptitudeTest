package psl.com.aptitudetest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class resultGraph extends ActionBarActivity {

    PieChart pieChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_graph);
        pieChart=(PieChart)findViewById(R.id.chart);
        List entries = new ArrayList<>();


        entries.add(new PieEntry(5.0f, "Correct"));
        entries.add(new PieEntry(5.0f, "Incorrect"));
        entries.add(new PieEntry(10.0f, "Not Attended"));
      //  entries.add(new PieEntry(30.8f, "Skipped"));



        PieDataSet set = new PieDataSet(entries, "Election Results");
        set.setColors(new int[]{R.color.green,R.color.red,R.color.yellow1,R.color.blue,R.color.blue2},this);
        PieData data = new PieData(set);

        pieChart.setCenterText("TEST Results");

        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.invalidate(); // refresh

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
