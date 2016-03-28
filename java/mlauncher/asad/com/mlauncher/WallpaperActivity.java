package mlauncher.asad.com.mlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by machine7 on 8/30/2015.
 */
public class WallpaperActivity extends Activity {
    private File[] listFile;
    File file;
    GridView gridView;
    WallpaperAdapter adapter;
    int cacheSize;
    ArrayList<String> fileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_layout);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 5;
        getFileNamesOfPictures();


        initializeGrid();



    }

    private void getFileNamesOfPictures() {
        file = new File(Environment.getExternalStorageDirectory().toString()+ "/Pictures" +File.separator);
        file.mkdirs();
        if (file.isDirectory()) {
            listFile = file.listFiles();
            fileList = new ArrayList<>();
            for(File mFile: listFile){
                if (mFile.isDirectory() == true){

                }else{
                    fileList.add(mFile.getAbsolutePath()); }
            }
        }
    }

    private void initializeGrid() {
        gridView = (GridView)findViewById(R.id.gridview);
        adapter = new WallpaperAdapter(this, fileList, cacheSize);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent i = new Intent(WallpaperActivity.this, ViewImage.class);

                i.putExtra("filepath", fileList);

                i.putExtra("position", position);
                startActivity(i);
            }

        });

    }




}