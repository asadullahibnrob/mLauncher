package mlauncher.asad.com.mlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by machine7 on 8/7/2015.
 */
public class AppInfo {
    private CharSequence title;
    private Intent intent;
    private Drawable icon;
    private boolean hasBanner;
    private String packageName;
    private boolean isCustomIcon;
    private ActivityInfo activityInfo;

    public AppInfo(){       };



    public AppInfo(String packageName, Drawable icon, Intent intent, CharSequence title,Boolean isCustomIcon, ActivityInfo info) {
        super();
        this.packageName = packageName;
        this.icon = icon;
        this.intent = intent;
        this.title = title;
        this.isCustomIcon = isCustomIcon;
        this.activityInfo = info;

    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean getHasBanner() {
        return hasBanner;
    }

    public void setHasBanner(boolean hasBanner) {
        this.hasBanner = hasBanner;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isCustomIcon() {
        return isCustomIcon;
    }

    public void setIsCustomIcon(boolean isCustomIcon) {
        this.isCustomIcon = isCustomIcon;
    }

    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

}
