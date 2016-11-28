package psl.com.aptitudetest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;


public class HomeScreen extends ActionBarActivity {
    private static String TAG = HomeScreen.class.getCanonicalName();
DBManager dbm;

    ImageButton headerimage = null;
    //String navDrwerMENU[] = {"Formulae","Solved Questions"};
    int ICONS[] = {R.drawable.formulae,R.drawable.formulae,R.drawable.guess,R.drawable.test,R.drawable.fire};
    //String [] topics={"Age","Area","Clock","Percentage","Profit","Train","Work"};
    String[] topics;
    String navDrwerMENU[];
        int [] imgeForTopic={R.drawable.age,R.drawable.general,R.drawable.area,R.drawable.general,R.drawable.general,
                R.drawable.general,R.drawable.clock,R.drawable.general,R.drawable.general,R.drawable.general,
                R.drawable.percentage,R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.profit,
                R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.general,R.drawable.work,
                R.drawable.train,R.drawable.general,R.drawable.general};
    GridView grid;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
private   android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Context context=getApplicationContext();
        topics = context.getResources().getStringArray(R.array.topics);
        navDrwerMENU =context.getResources().getStringArray(R.array.drawer_menu);
        dbm= new DBManager(this);
        sharedpref= getSharedPreferences(getString(R.string.spf_file_key), Context.MODE_PRIVATE);
        editor = sharedpref.edit();
        startService(new Intent(getBaseContext(),HtmlFetcher.class));
        try {
            dbm.open();
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
           //   dbm.insertDB();                   //insets DB from DBManager class
         //   dbm.deleteAllQuestions();        //to Reset the DB
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(navDrwerMENU,ICONS,this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final GestureDetector mGestureDetector = new GestureDetector(HomeScreen.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    int choice= recyclerView.getChildPosition(child);
                    drawerClick(choice);
                    Log.d(TAG,"choice is "+choice);
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
                TextView header= (TextView)findViewById(R.id.HEADER);
                String defaultValue = "";
                String username = sharedpref.getString(getString(R.string.saved_username), defaultValue);
                header.setText(username);

                //headerimage= (ImageButton) Drawer.findViewById(R.id.loginButton);
                headerimage= (ImageButton) findViewById(R.id.loginButton);
                headerimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        HomescreenGridAdapter gridAdapter= new HomescreenGridAdapter(HomeScreen.this,topics,imgeForTopic);
        grid=(GridView)findViewById(R.id.gridView);
        grid.setAdapter(gridAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               // Toast.makeText(HomeScreen.this,"Clicked on"+topics[position],Toast.LENGTH_SHORT).show();
                // Open new activity from here
                Intent intent= new Intent(getApplicationContext(),Test.class);  // StartTest
                intent.putExtra("topicName",topics[position]);
                startActivity(intent);
            }
        });
    }

    private void drawerClick(int position) { // Drawer click will open new Activty using this method
        Intent intent;
        switch (position)
        {
            case 0:
                intent= new Intent(this,Login.class);
                startActivity(intent);

                break;
            case 1: //Intent intent= new Intent(this,FormulaeDisplay.class);    // Displays webview directly
                intent= new Intent(this,topicList.class);
                intent.putExtra("goal","formulae");
                    startActivity(intent);
                break;
            case 2: //Intent intent= new Intent(this,FormulaeDisplay.class);    // Displays webview directly
                intent= new Intent(this,topicList.class);
                intent.putExtra("goal","solved_examples");
                startActivity(intent);
                break;

            case 3:intent= new Intent(this,Test.class);
                intent.putExtra("testmode","Guess");
                startActivity(intent);
                break;
            case 4:intent= new Intent(this,Test.class);
                intent.putExtra("testmode","Test");
                startActivity(intent);
                break;

            default:
                break;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
           startService(new Intent(getBaseContext(),HtmlFetcher.class));
            return true;
        }
        else if(id==R.id.view_questions){
            Intent intent= new Intent(this,Test.class);
            startActivity(intent);
        }
        else if(id==R.id.updateQuestions){
            Intent intent= new Intent(this,UpdateQuestions.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }




}
