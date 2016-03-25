package mlauncher.asad.com.mlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class GundamAdapter extends ArrayAdapter<AppInfo> {
    private ImageView img;
    Context mContext;
    ArrayList<AppInfo> list;
    private LruCache<String, Bitmap> mMemoryCache;
    int cacheSize;
    int mResource;
    LayoutInflater inflater;


    public GundamAdapter(Context context, int resource, ArrayList<AppInfo> objects, int availableMemory) {
        super(context, resource, objects);
        mContext =context;
        list = objects;
        cacheSize = availableMemory;
        mResource = resource;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() /1024;
            }
        };
             inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppInfo info = list.get(position);

       // Drawable icon = info.icon;

        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
            img= (ImageView) convertView.findViewById(R.id.thumb);

            if (info.getHasBanner() == true){

                img.setImageDrawable(info.getIcon());
            }
            if (info.getHasBanner() == false){
                ImageView img2 = (ImageView) convertView.findViewById(R.id.thumbty);
                TextView title = (TextView) convertView.findViewById(R.id.title);

                new GridViewBitmapAsyncTask(img2).execute(info.getPackageName());
                 title.setText(info.getTitle()
                 );
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



    public class GridViewBitmapAsyncTask extends AsyncTask<String,Void,Bitmap> {
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

 static class ViewHolder{
     CharSequence title;
     Intent intent;
     Drawable icon;
     boolean hasBanner;
     String packageName;
     boolean isCustomIcon;
 }

}
