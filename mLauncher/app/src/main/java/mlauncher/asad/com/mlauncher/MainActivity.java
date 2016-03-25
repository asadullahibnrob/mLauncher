package mlauncher.asad.com.mlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity {
    ArrayList<AppInfo> mApplications;
    private final BroadcastReceiver applicationsInstalledReceiver = new ApplicationsInstalledReceiver();
    Intent quickSettingsIntent = new Intent();
    GridView mGridView;
    Boolean upOrDownPressed;
    Context mContext;
    int cacheSize;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 5;
        mContext = getBaseContext();
        registerMyReceiver();
        loadApplications(true);
        gridStuff();



    }

    private void gridStuff() {

        mGridView = (GridView)findViewById(R.id.gridview);
        mGridView.setAdapter(new GundamAdapter(MainActivity.this, R.layout.row, mApplications, cacheSize));
       // mGridView.setAdapter(new GridAdapter(MainActivity.this, this, mApplications,cacheSize));
        mGridView.setSelection(0);
        mGridView.setOnItemClickListener(new ApplicationLauncher());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(applicationsInstalledReceiver);
    }

    private void registerMyReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.setPriority(999);
        filter.addDataScheme("package");
        registerReceiver(applicationsInstalledReceiver, filter);

           }

    public static List<ApplicationInfo> getInstalledApplication(Context context) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<ApplicationInfo> appInfoList = new ArrayList();


        for (ApplicationInfo info : apps) {

            if ((packageManager.getLeanbackLaunchIntentForPackage(info.packageName)) != null ) {
                appInfoList.add(info);}

            if (appInfoList.contains(info)== false && (packageManager.getLaunchIntentForPackage(info.packageName)) != null) {
                appInfoList.add(info);
            }
            if((info.packageName).contains("mlauncher.asad.com.mlauncher")){
                appInfoList.remove(info);
            }


            if((info.loadLabel(packageManager)).toString().contains("Google Play Games")){
                appInfoList.remove(info);
            }

        }

        Collections.sort(appInfoList, new ApplicationInfo.DisplayNameComparator(packageManager));
        return appInfoList;
    }



    //http://forum.xda-developers.com/showpost.php?p=59832260&postcount=8 for the keycodes
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        quickSettingsIntent.setClassName("mlauncher.asad.com.mlauncher", "mlauncher.asad.com.mlauncher.QuickSettings");

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {

                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    try{
                        this.startActivity(quickSettingsIntent);} catch ( ActivityNotFoundException e) {
                        e.printStackTrace();}
                        return true;


                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

                    try{
                    this.startActivity(quickSettingsIntent);} catch ( ActivityNotFoundException e) {
                    e.printStackTrace();}

                   return true;




                case KeyEvent.KEYCODE_BUTTON_Y:
                    try{
                        this.startActivity(quickSettingsIntent);} catch ( ActivityNotFoundException e) {
                        e.printStackTrace();}

                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    upOrDownPressed = true;
                    Log.d("dpad dap", "dpad");

                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    upOrDownPressed = false;
                    break;

            }
        }

        return super.dispatchKeyEvent(event);
    }

    private List<ResolveInfo> getResolvedApps(Context context, PackageManager manager) {

        Intent leanIntent = new Intent(Intent.ACTION_MAIN);
        leanIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);

        Intent regularIntent = new Intent(Intent.ACTION_MAIN);
        regularIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> rapps = manager.queryIntentActivities(leanIntent, PackageManager.GET_META_DATA);
        List<ResolveInfo> reggies = manager.queryIntentActivities(regularIntent, PackageManager.GET_META_DATA);

        ArrayList<ResolveInfo> allApps = new ArrayList<>();

        for(ResolveInfo rInfo: rapps){
            final String lbPackageName = rInfo.activityInfo.packageName;
            allApps.add(rInfo);
            for (ResolveInfo regApps: reggies){
                final String regPackageName = regApps.activityInfo.packageName;
                if ((lbPackageName.equals(regPackageName) == false) && (allApps.contains(regApps) ==false) && (manager.getLeanbackLaunchIntentForPackage(regPackageName)== null) ){
                    allApps.add(regApps);
                }


            }
           Collections.sort(allApps, new ResolveInfo.DisplayNameComparator(manager));
        }
        return allApps;
    }
    public void loadApplications(boolean isLaunching) {
        if (isLaunching && mApplications != null) {
            return;
        }
        PackageManager manager = getPackageManager();

        //List<ApplicationInfo> apps = getInstalledApplication(this);
        List<ResolveInfo> rApps = getResolvedApps(this,manager);

        if (rApps != null) {

            if (mApplications == null) {
                mApplications = new ArrayList<AppInfo>();
            }
            mApplications.clear();
           // for (ApplicationInfo packageInfo : apps) {
                for (ResolveInfo packageInfo : rApps) {
                AppInfo application = new AppInfo();

                application.setTitle(packageInfo.loadLabel(manager));
               // application.packageName = packageInfo.packageName;
                    application.setPackageName(packageInfo.activityInfo.packageName)  ;
                if (application.getIcon() == null) {
                    application.setHasBanner(true);
                   // application.icon = packageInfo.loadBanner(manager);
                    application.setIcon(packageInfo.activityInfo.loadBanner(manager));
                }

                if (application.getIcon() == null) {
                    application.setHasBanner(true);
                   // application.icon = packageInfo.loadLogo(manager);
                    application.setIcon(packageInfo.activityInfo.loadLogo(manager));
                }


                if (application.getIcon()== null) {
                    application.setHasBanner(false);
                   // application.icon = packageInfo.activityInfo.loadIcon(manager);


                }

                mApplications.add(application);
                if (application.getTitle().equals("Google Play services")) {
                    mApplications.remove(application);
                }

            }
        }
    }




    public class ApplicationsInstalledReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
          
            Log.d("apps have ", "hello");

            mApplications.clear();
            loadApplications(false);

        }
    }


    private class ApplicationLauncher implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            final AppInfo info = mApplications.get(position);
            Intent mnIntent = mContext.getPackageManager().getLeanbackLaunchIntentForPackage(info.getPackageName());
            if(mnIntent == null){

                mnIntent = mContext.getPackageManager().getLaunchIntentForPackage(info.getPackageName());
            }


            mContext.startActivity(mnIntent);
        }
    }

    }





