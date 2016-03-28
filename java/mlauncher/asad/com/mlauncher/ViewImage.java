package mlauncher.asad.com.mlauncher;


        import android.app.Activity;
        import android.app.WallpaperManager;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.util.ArrayList;

public class ViewImage extends Activity {
    // Declare Variable

    ImageView imageview;
    Button setwallpaperbutton;
    WallpaperManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewimage);

        manager = WallpaperManager.getInstance(this);
        Intent i = getIntent();
        int position = i.getExtras().getInt("position");


        ArrayList<String> filepath = i.getStringArrayListExtra("filepath");

        imageview = (ImageView) findViewById(R.id.full_image_view);
        setwallpaperbutton = (Button) findViewById(R.id.setwallpaper);

        final Bitmap bmp = BitmapFactory.decodeFile(filepath.get(position));



        imageview.setImageBitmap(bmp);
        setwallpaperbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SceneChanger().execute(bmp);
            }
        });

    }

    class SceneChanger extends AsyncTask<Bitmap,Void,Void>{



        @Override
        protected Void doInBackground(Bitmap... params) {

            try {
                manager.setBitmap(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ViewImage.this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
            finish();
            super.onPostExecute(aVoid);
        }
    }

}



