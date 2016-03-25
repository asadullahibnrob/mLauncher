package mlauncher.asad.com.mlauncher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by machine7 on 9/13/2015.
 */
public class WallpaperAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> filepath;
    private LruCache<String, Bitmap> mMemoryCache;
    private static LayoutInflater inflater = null;
    int cacheSize;


    public WallpaperAdapter(Activity a, ArrayList<String> fpath, int cachesize) {
        activity = a;
        filepath = fpath;
        cacheSize = cachesize;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoryCache = new LruCache<String,Bitmap>(cachesize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public int getCount() {
        return filepath.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.wallitems, null);

        ImageView image = (ImageView) vi.findViewById(R.id.imageView);

        // Bitmap bmp = decodeSampledBitmapFromString((filepath[position]), 200, 200);
        //image.setImageBitmap(bmp);

        new BitmapAsyncTask(image).execute(filepath.get(position));
        return vi;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
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

    private class BitmapAsyncTask extends AsyncTask<String,Void,Bitmap>{
        ImageView bmImage;

        public BitmapAsyncTask(ImageView bmImage){
            this.bmImage = bmImage;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            String pathName = params[0];
            final String imageKey = String.valueOf(params[0]);
            Bitmap mIcon11 = getBitmapFromMemCache(imageKey);
            if(mIcon11 == null) {

                mIcon11 = decodeSampledBitmapFromString(pathName, 200, 200);
                if(mIcon11 != null)
                    addBitmapToMemoryCache(imageKey,mIcon11);
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmImage.setImageBitmap(bitmap);
        }
    }

}
