package psl.com.aptitudetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ajit_thakare on 8/23/2016.
 */
public class HomescreenGridAdapter extends BaseAdapter {
    private static String TAG = HomescreenGridAdapter.class.getCanonicalName();
    private final String [] topics;
    private final int []imageId;
    private Context mContext;

    public HomescreenGridAdapter(Context mContext, String[] topics, int[] imageId ) {
        this.topics = topics;
        this.imageId = imageId;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
       return topics.length;
    }

    @Override
    public Object getItem(int position) {
        return topics[position];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    } // Logic NOT implemented yet

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
          //  grid= new View(mContext);
            grid=inflater.inflate(R.layout.grid_single,null);
        }
        else
        {
            grid=convertView;
        }
        TextView txtview=(TextView)grid.findViewById(R.id.grid_text);
        ImageView imgView=(ImageView)   grid.findViewById(R.id.grid_image);
        txtview.setText(topics[position]);
        imgView.setImageResource(imageId[position]);
        return grid;
    }




}
