package psl.com.aptitudetest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;


public class topicList extends ListActivity {
public static String TAG=topicList.class.getCanonicalName();
    //ListView list;
    String [] topics;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        button = (ImageButton)findViewById(R.id.closeButton);
        button.setImageResource(R.drawable.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle extras=getIntent().getExtras();
                if(extras!=null)
                {
            Log.d(TAG,"data in intent");
                }
        else{
                    Log.d(TAG,"no data in intent");
                }

       // list=(ListView)findViewById(R.id.list);
        topics=getApplicationContext().getResources().getStringArray(R.array.topics);
        int [] imgeForTopic={R.drawable.age,R.drawable.general,R.drawable.area,R.drawable.general,R.drawable.general,
                R.drawable.general,R.drawable.clock,R.drawable.general,R.drawable.general,R.drawable.general,
                R.drawable.percentage,R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.profit,
                R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.work,
                R.drawable.train,R.drawable.general,R.drawable.general};
        TopicListArrayAdapter adapter= new TopicListArrayAdapter(topics,this,imgeForTopic);
        setListAdapter(adapter);
      }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {  // After clicking on item, open formulae for that Topic
        super.onListItemClick(l, v, position, id);
        String topic= (String) l.getAdapter().getItem(position);
        //Toast.makeText(this,topic,Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(getApplicationContext(),FormulaeDisplay.class);
        intent.putExtra("topic",topic); // Show formulae for this Topic
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_list, menu);
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
