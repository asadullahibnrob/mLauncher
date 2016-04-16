package mlauncher.asad.com.mlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        mContext = context;
        list = objects;
        cacheSize = availableMemory;
        mResource = resource;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppInfo info = list.get(position);
        final String pName = info.getPackageName();
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(mResource, null);
            holder.name = (TextView) v.findViewById(R.id.title);
            holder.imageView = (CustomImageView) v.findViewById(R.id.thumb);
            holder.imageViewSmall = (CustomImageView) v.findViewById(R.id.thumbty);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (info.getHasBanner() == true) {

             new GridViewBitmapAsyncTask(holder.imageView,info).execute(pName);
        } else {
            holder.imageView.setVisibility(View.GONE);

             new GridViewBitmapAsyncTask(holder.imageViewSmall, info).execute(pName);
            holder.name.setText(info.getTitle());

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


    public class GridViewBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
        CustomImageView tempImage;
        AppInfo appInfo;

        public GridViewBitmapAsyncTask(CustomImageView imageView, AppInfo appyInfo) {
            tempImage = imageView;
            appInfo = appyInfo;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final String packageName = params[0];
            Drawable bm = null;
            Bitmap myLogo = getBitmapFromMemCache(packageName);
            if (myLogo == null) {
                bm = appInfo.getIcon();
                myLogo = ((BitmapDrawable) bm).getBitmap();
                addBitmapToMemoryCache(packageName, myLogo);
                Log.d("hello", "added to bitmapcache");
            }



            return myLogo;
        }

        @Override
        protected void onPostExecute(Bitmap drawable) {
            tempImage.setImageBitmap(drawable);
        }
    }

    static class ViewHolder {
        TextView name;
        CustomImageView imageView;
        CustomImageView imageViewSmall;

    }

}
