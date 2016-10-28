package psl.com.aptitudetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ajit_thakare on 8/9/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    Context context;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
    MyAdapter(String Titles[],int Icons[], Context passedContext){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        this.context=passedContext;
    }

    @Override    // from here we can change header view TEXT/ add Image to it.
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout

           // ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view
            return  (new ViewHolder(v, viewType, context));
           // return vhItem; // Returning the created object
        }
            //inflate your layout and pass it to view holder
        else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType,context); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list +1 , because 1 header is before item list
    }

    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int Holderid;
        TextView textView;
        ImageView imageView;
        ImageButton loginButton;
        Context contxt;
        public ViewHolder(View itemView,int ViewType, final Context c) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            contxt=c;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else { // it is header
                textView = (TextView) itemView.findViewById(R.id.HEADER);
                loginButton=(ImageButton)itemView.findViewById(R.id.loginButton); // login button
            }
        }

        @Override
        public void onClick(View v) {
          // Toast.makeText(contxt,"Position is"+getPosition(),Toast.LENGTH_SHORT).show();
                       // Based on POsition invoke different intents NOW
        }
    }

}
