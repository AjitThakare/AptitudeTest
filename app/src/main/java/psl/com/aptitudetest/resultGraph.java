package psl.com.aptitudetest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class resultGraph extends ActionBarActivity {
    PieChart pieChart;
    ImageButton close;
    ArrayList <Integer>colorArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras=getIntent().getExtras();

        setContentView(R.layout.activity_result_graph);
        pieChart=(PieChart)findViewById(R.id.chart);
        close=(ImageButton)findViewById(R.id.closeButton);
        close.setImageResource(R.drawable.close_button);
        List entries = new ArrayList<>();
        colorArray= new ArrayList(); // HARD coded for 4 colors right now
        if(extras.get("Correct").toString().equalsIgnoreCase("0"))
        {

        }
        else {
                entries.add(new PieEntry(Float.parseFloat(extras.get("Correct").toString()), "Correct"));
                colorArray.add(R.color.green);
             }
        if(extras.get("Incorrect").toString().equalsIgnoreCase("0"))
        {

        }
        else
        {
            entries.add(new PieEntry(Float.parseFloat(extras.get("Incorrect").toString()), "Incorrect"));
            colorArray.add(R.color.red);
        }
        if(extras.get("NotAttempted").toString().equalsIgnoreCase("0"))
        {

        }
        else {
            entries.add(new PieEntry(Float.parseFloat(extras.get("NotAttempted").toString()), "Skipped"));
            colorArray.add(R.color.yellow1);
        }
//       entries.add(new PieEntry(30.8f, "Skipped"));

        int arr[]=new int[]{R.color.green,R.color.red,R.color.yellow1,R.color.blue,R.color.blue2};
        for(int i=0;i<colorArray.size();i++)
        {
            arr[i]=colorArray.get(i);
        }


        PieDataSet set = new PieDataSet(entries, "Election Results");
        set.setColors(arr,this); // new int[]{R.color.green,R.color.red,R.color.yellow1,R.color.blue,R.color.blue2} also can be used
        PieData data = new PieData(set);

        pieChart.setCenterText("TEST Results");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.blue25));
        pieChart.setDescriptionColor(getResources().getColor(R.color.blue2));
        if(Integer.parseInt(extras.get("Correct").toString())>(Integer.parseInt(extras.get("Incorrect").toString())+Integer.parseInt(extras.get("NotAttempted").toString())))
        pieChart.setDescription("Congratulations! It's a Good Result"); // Change description according to result
        else
            pieChart.setDescription("Best wishes for your studies"); // Change description according to result
        pieChart.setDescriptionTextSize(18f);


        pieChart.setData(data);
        pieChart.setTouchEnabled(false);
        pieChart.invalidate(); // refresh

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
