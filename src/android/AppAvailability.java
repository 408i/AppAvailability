package com.ohh2ahh.appavailability;

import java.util.List;
import java.lang.Object;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.database.Cursor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageItemInfo;
import android.content.pm.ApplicationInfo;


public class AppAvailability extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("checkAvailability")) {
            String uri = args.getString(0);
            this.checkAvailability(uri, callbackContext);
            return true;
        }
        else if(action.equals("getApps")) {
            this.getApps(callbackContext);
            return true;
        }
        else if(action.equals("getId")) {
            this.getId(callbackContext);
            return true;
        }
        return false;
    }
    
    // Thanks to http://floresosvaldo.com/android-cordova-plugin-checking-if-an-app-exists
    public boolean appInstalled(String uri) {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch(PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public String appsAvailable() {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        

        // build stringified JSON array of apps
        String packageNames = "[";
        for (ApplicationInfo packageInfo : packages) {
            packageNames += "{\"package\":\"" + packageInfo.packageName + 
                         "\", \"name\":\""+ packageInfo.loadLabel(pm).toString() + "\"}, ";
        }
        packageNames  = packageNames.substring(0, packageNames.length() - 2) + "]";
        return packageNames;


        // http://stackoverflow.com/questions/2695746/how-to-get-a-list-of-installed-android-applications-and-pick-one-to-run
    }


    public static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    public static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";

    public String getAttributionId() {
        // https://developers.facebook.com/docs/marketing-api/mobile-conversions-endpoint/v2.3
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final ContentResolver contentResolver = ctx.getContentResolver();
        String [] projection = {ATTRIBUTION_ID_COLUMN_NAME};
        Cursor c = contentResolver.query(ATTRIBUTION_ID_CONTENT_URI, projection, null, null, null);
        if (c == null || !c.moveToFirst()) {
            return null;
        }
        String attributionId = c.getString(c.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME));
        c.close();
        return attributionId;
     }
    
    private void checkAvailability(String uri, CallbackContext callbackContext) {
        if(appInstalled(uri)) {
            callbackContext.success();
        }
        else {
            callbackContext.error("");
        }
    }
    private void getApps(CallbackContext callbackContext) {
        String packages = appsAvailable();
        callbackContext.success(packages);
        // callbackContext.error("");
    }
    private void getId(CallbackContext callbackContext) {
        String attributionId = getAttributionId();
        callbackContext.success( attributionId );
        // callbackContext.error("");
    }
}
