package mlauncher.asad.com.mlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by machine7 on 9/8/2015.
 */
public class QuickSettings extends Activity implements  AdapterView.OnItemClickListener {
    String[] shortCuts = {"Reboot", "Recovery", "Wallpaper", "Settings", "Sleep", "Overscan", "Power Off", "Apps"};
    LinearLayout linearLayout;
    ListView quickSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        setContentView(linearLayout);
    }

    private void initialize() {
        linearLayout = new LinearLayout(this);
        quickSettings = new ListView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (int) (200 * getResources().getDisplayMetrics().scaledDensity);
        quickSettings.setLayoutParams(params);
        quickSettings.setSelector(new ColorDrawable(Color.parseColor("#FFFFFF")));
        quickSettings.setBackgroundColor(Color.parseColor("#009688"));
        quickSettings.setDividerHeight(0);
        quickSettings.setSoundEffectsEnabled(false);
        linearLayout.addView(quickSettings);
        quickSettings.setAdapter(new ArrayAdapter<String>(QuickSettings.this, R.layout.listitem, shortCuts));
        quickSettings.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //reboot
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{"su", "-c", "reboot "});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case 1:

                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{"su", "-c", "reboot recovery"});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

               break;
            case 2:
                //wallpaper
                Intent wallpaperIntent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(wallpaperIntent);

                break;
            case 3:
                //settings
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));

                break;
            case 4:
                //sleep
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "input keyevent 26"});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case 5:
                //overscan
                Intent overscanIntent = new Intent();
                try {
                    overscanIntent.setClassName("com.google.android.tungsten.overscan", "com.google.android.tungsten.overscan.CalibratorActivity");
                    startActivity(overscanIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                //poweroff
                try {
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
            case 7:
                Intent appsIntent = new Intent();
                appsIntent.setClassName("com.android.tv.settings", "com.android.tv.settings.device.apps.AppsActivity");
                startActivity(appsIntent);
                break;
            case 8:

                break;


        }
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                finish();

            case  KeyEvent.KEYCODE_BACK:
                finish();
            case KeyEvent.KEYCODE_DPAD_LEFT:
                finish();
            case KeyEvent.KEYCODE_MEDIA_PLAY:

                return true;


            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:


                return true;
        }

        return super.dispatchKeyEvent(event);

    }
 /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("keypress", "keynumber is " + keyCode);
        Log.d("keypress", "keyCode is " + event.getKeyCode());

        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                finish();

            case  KeyEvent.KEYCODE_BACK:
                finish();
            case KeyEvent.KEYCODE_DPAD_LEFT:
                finish();
        }
        return true;
    }*/
}


