package psl.com.aptitudetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TopicListArrayAdapter extends BaseAdapter {
    private final String [] topics;
    private final int []imageId;
    private Context mContext;

    public TopicListArrayAdapter(String[] topics, Context mContext, int[] imageId) {
        this.topics = topics;
        this.mContext = mContext;
        this.imageId = imageId;
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
    public long getItemId(int i) { // not implemented
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            //  grid= new View(mContext);
            grid=inflater.inflate(R.layout.item_row,null);
        }
        else
        {
            grid=convertView;
        }
        TextView txtview=(TextView)grid.findViewById(R.id.rowText);
        ImageView imgView=(ImageView)   grid.findViewById(R.id.rowIcon);
        txtview.setText(topics[position]);
        imgView.setImageResource(imageId[position]);
        return grid;
    }
}
