package com.getcinderella.app.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;


public class Permissions {

    public static boolean hasPermission(Context context, String permission) {

        int res = context.checkCallingOrSelfPermission(permission);

        return res == PackageManager.PERMISSION_GRANTED;

    }
    public static boolean hasAllPermissions(Context context) {

        boolean hasAllPermissions = true;

        for(String permission : PERMISSIONS) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (! hasPermission(context, permission)) {hasAllPermissions = false; }
        }

        return hasAllPermissions;

    }

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

}