package mlauncher.asad.com.mlauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by machine7 on 9/3/2015.
 */
public class GridAdapter extends BaseAdapter {

    private ImageView img;
    Context mContext;
    ArrayList<AppInfo> list;
    private LruCache<String, Bitmap> mMemoryCache;
    int cacheSize;


    public GridAdapter(Activity a, Context context, ArrayList<AppInfo> apps, int cacheSizer){
        list = apps;
        mContext = context;
        cacheSize = cacheSizer;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSizer){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }



    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return img;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppInfo info = list.get(position);
        Drawable icon = info.getIcon();

        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
            img= (ImageView) convertView.findViewById(R.id.thumb);

            if (info.getHasBanner() == true){

            img.setImageDrawable(icon);
            }




            if (info.getHasBanner() == false){

                ImageView img2 = (ImageView) convertView.findViewById(R.id.thumbty);
                TextView title = (TextView) convertView.findViewById(R.id.title);

               // try {

                   // Drawable icon2 = mContext.getPackageManager().getApplicationIcon(info.packageName);
                    new GridViewBitmapAsyncTask(img2).execute(info.getPackageName());
                  //  img2.setImageDrawable(icon2);
                    //GridViewBitmapAsyncTask(img2).execute;
              //  } catch (PackageManager.NameNotFoundException e) {
               //     e.printStackTrace();

              //  }
                title.setText(info.getTitle());
               // img2.setImageDrawable(icon2);
                title.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Log.d("get bitmap", "got it");
        return mMemoryCache.get(key);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth){
            final int halfHeight = height /2;
            final int halfWidth = height /2;
            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }

    public static Bitmap decodeSampledBitmapFromString(String string, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(string,options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(string, options);


    }


    public class GridViewBitmapAsyncTask extends AsyncTask<String,Void,Bitmap>{
        ImageView tempImage;
       public GridViewBitmapAsyncTask(ImageView imageView){
           tempImage = imageView;
       }

       @Override
       protected Bitmap doInBackground(String... params) {
           String packageName = params[0];
           Drawable bm = null;
           Bitmap myLogo = getBitmapFromMemCache(packageName);
           if (myLogo == null){
               try {
                   bm = mContext.getPackageManager().getApplicationIcon(packageName);
                    myLogo = ((BitmapDrawable) bm).getBitmap();
                    addBitmapToMemoryCache(packageName, myLogo);
                   Log.d("hello", "added to bitmapcache");
               } catch (PackageManager.NameNotFoundException e) {
                   e.printStackTrace();
               }
           }

              // Drawable icon3 =mContext.getPackageManager().getApplicationIcon(packageName);

           return myLogo;
       }

       @Override
       protected void onPostExecute(Bitmap drawable) {
           tempImage.setImageBitmap(drawable);
       }
   }

}
