package psl.com.aptitudetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psl.com.util.NetworkHelper;

public class HtmlFetcher extends Service {
    private static String TAG = HtmlFetcher.class.getCanonicalName();
    File htmlFile;
    String content;
    List<String> list;
    NetworkHelper netobj;
    Map<String, String> htmlFiles;
    public HtmlFetcher()
    {
        htmlFiles= new HashMap<>();

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Aptitude Test service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netobj= new NetworkHelper(cm);
        String[] topics = getApplicationContext().getResources().getStringArray(R.array.topics);
      /*  for(int i=0;i<topics.length;i++) for re assuaring all htmls are downloaded!
        {
            String temp=topics[i];
            temp= temp.toLowerCase();
            temp= temp.replace(" ","_");
            htmlFiles.put(temp,"notFound");
            htmlFiles.put(temp+"_solved","notFound");
        }
        Iterator itr= htmlFiles.entrySet().iterator();
        while(itr.hasNext())
        {
            final Map.Entry pair = (Map.Entry)itr.next();
            File file= new File(getExternalFilesDir(null), pair.getKey()+".html");
            if(file.length()==0)
            {   new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        download(pair.getKey().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();// need to be dowloaded
                Log.d(TAG,"notFound "+pair.getKey());
            }
            else
            {
                htmlFiles.put(pair.getKey().toString(),"found");
            }
        }*/
       Thread service= new Thread(new Runnable() {
           @Override
           public void run() {
               while(!netobj.isOnline())
               {
                   try {
                       Thread.sleep(10000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }


               final String []solutions={"14","15","16"};
               final String[] topics = getApplicationContext().getResources().getStringArray(R.array.topics);
               File dir[] = getExternalFilesDirs(null);
               list = new ArrayList<String>();
               for (int i = 0; i < dir.length; i++) {
                   File listFile[] = dir[i].listFiles();
                   if (listFile != null) {
                       for (int j = 0; j < listFile.length; j++) {
                           list.add(listFile[j].getName());
                           // Toast.makeText(getApplicationContext(),listFile[j].getName(),Toast.LENGTH_SHORT).show();
                       }
                   }
               }
               Boolean needToCopy = true;
               Boolean needToDownload = true;
               for (String filename : list) {
                   if (filename.equalsIgnoreCase("ajit.css")) {   // Toast.makeText(getApplicationContext(),"Files already present in storage",Toast.LENGTH_SHORT).show();
                       needToCopy = false;
                   }
               }
               if (needToCopy) {//Toast.makeText(getApplicationContext(),"Copying Assets !",Toast.LENGTH_SHORT).show();
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           copyAssets();
                       }
                   }).start();
               }

               AppController.getInstance(getApplicationContext());
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           for(int i=0;i<topics.length;i++)
                           {
                               if(list.contains(topics[i]+".html"))
                               {
                                   //Already file is present
                               }
                               else
                               {
                                   // download(topics[i],"http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/html/"); // TODO download formulae as well
                                   // Thread.sleep(10000); // Instead of this, we should check if the fileis completely downloaded or not
                               }
                               if(!list.contains(topics[i]+"_solved.html"))
                               {
                                   download(topics[i]+"_solved","http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/html/");
                                   Thread.sleep(10000);
                               }
                           }
                           for(int i=0;i<solutions.length;i++)
                           {
                               if(!list.contains(solutions[i]+".html"))
                               {
                                   download(solutions[i],"http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/html/solution/");
                                   Thread.sleep(10000);
                               }
                           }
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }).start();
           }
       });
        service.start();
    }

 public void download(String fname, String mainURL){

     fname=fname.toLowerCase();
     fname= fname.replace(" ","_");
         String  tag_json_req = "json_req";
        // String url = "http://aptitude.southeastasia.cloudapp.azure.com:8080/test/services/html/"+fname;
           String url = mainURL+fname;
         if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
             Toast.makeText(getApplicationContext(),"Error in accessing external storage",Toast.LENGTH_SHORT).show();
         }
         else {
             //    Toast.makeText(getApplicationContext(),"Storage available!",Toast.LENGTH_SHORT).show();
         }
           htmlFile = new File(getExternalFilesDir(null), fname+".html");
         if(!htmlFile.exists())
         {
             // Toast.makeText(getApplicationContext(),"Created TEMP.html",Toast.LENGTH_SHORT).show();
             try {
                 htmlFile.createNewFile();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         else
         {
             // Toast.makeText(getApplicationContext(),"temp present ",Toast.LENGTH_SHORT).show();
         }

         JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         try {
                             content=response.get("content").toString();

                       /*     FileOutputStream outputStream;  // works well for internal storage
                            try {
                                outputStream = openFileOutput(htmlFile.getName(), MODE_PRIVATE);
                                outputStream.write(content.getBytes());
                                outputStream.close();*/
                             try {
                                 FileOutputStream fos = new FileOutputStream(htmlFile);
                                 fos.write(content.getBytes());
                                 fos.close();

                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {

             @Override
             public void onErrorResponse(VolleyError error) {
             }
         });
         // Adding request to request queue
         AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);
     }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        FileOutputStream fos;
        for(String filename : files) {
            if(filename.contains(".js")||filename.contains(".css"))
            {
                InputStream in = null;
                try {
                    in = assetManager.open(filename);
                    String outpath= getExternalFilesDir(null).getAbsolutePath()  ; //TODO change it to external storage

                    File outFile = new File(outpath, filename);
                    if(!outFile.exists())
                    {
                        outFile.createNewFile();
                    }
                    fos= new FileOutputStream(outFile);
                    fos.write(readFromFile(in).getBytes());
                    fos.close();
                } catch(IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                }
            }
        }
    }

    private String readFromFile(InputStream in)
    {   String myData = "";
        try {
            BufferedReader br =new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myData;
    }

}

