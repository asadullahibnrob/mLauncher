package mlauncher.asad.com.mlauncher;

import android.content.Context;
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
    Context mContext;
    ArrayList<AppInfo> list;
    private LruCache<String, Bitmap> mMemoryCache;
    int cacheSize;
    int mResource;
    LayoutInflater inflater;
    ViewHolder holder;


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
         View v = convertView;
        if(v==null){
            holder = new ViewHolder();
            v = inflater.inflate(mResource,null);
            holder.name = (TextView)v.findViewById(R.id.title);
            holder.imageView=(ImageView)v.findViewById(R.id.thumb);
            holder.imageViewSmall = (ImageView)v.findViewById(R.id.thumbty);
            v.setTag(holder);
        }else{
            holder = (ViewHolder)v.getTag();
        }

            if (info.getHasBanner() == true){

                holder.imageView.setImageDrawable(info.getIcon());
               // new GridViewBitmapAsyncTask(holder.imageView).execute(info.getPackageName());
            }else
            {

                new GridViewBitmapAsyncTask(holder.imageViewSmall).execute(info.getPackageName());
                 holder.name.setText(info.getTitle());
                holder.name.setVisibility(View.VISIBLE);
               // holder.imageViewSmall.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
            }

        return v;
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
     TextView name;
     ImageView imageView;
     ImageView imageViewSmall;

 }

}
