package psl.com.aptitudetest;

/**
 * Created by ajit_thakare on 10/18/2016.*/

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import psl.com.util.LruBitmapCache;

public class AppController {
    public static final String TAG = AppController.class
            .getSimpleName();
    private static Context mCtx;
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private AppController (Context context)
    {
        mCtx=context;
        mRequestQueue=getRequestQueue();
    }

    public static synchronized AppController getInstance(Context context) {
        if (null == mInstance)
            mInstance = new AppController(context);
        return mInstance;
    }
    public static synchronized AppController getInstance() {
        if (null == mInstance)
            Log.d(TAG,"No Context yet passed from main app/ Appcontroller is null ");
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}