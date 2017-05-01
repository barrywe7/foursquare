package com.barryirvine.foursquare.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.sharedprefs.SharedPrefs;

public class RuntimePermissionUtils {

    public static final int REQUEST_LOCATION = 0;

    public static boolean verify(@NonNull final int[] grantResults) {
        boolean granted = false;
        if (grantResults.length > 0) {
            for (final int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                    break;
                }
            }
        }
        return granted;
    }

    public static boolean hasPermission(@NonNull final Context context, @NonNull final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkForPermission(@NonNull final Activity activity, @NonNull final String permission, @NonNull final CheckPermissionListener checkPermissionListener) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            checkPermissionListener.onPermissionAlreadyGranted();
        } else if (SharedPrefs.isPermissionRequested(activity, permission) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            checkPermissionListener.onPermissionAlreadyDeniedWithDoNotAskAgain();
        } else {
            checkPermissionListener.onPermissionPendingRequest();
        }
    }

    public static void onRequestPermissionsResult(@NonNull final int[] grantResults, @NonNull final Activity activity, @NonNull final String permission, @NonNull final OnResultPermissionListener onResultPermissionListener) {
        if (RuntimePermissionUtils.verify(grantResults)) {
            onResultPermissionListener.onPermissionGranted();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            onResultPermissionListener.onPermissionDeniedButShowRequestPermissionRationale();
        } else {
            onResultPermissionListener.onPermissionDeniedWithDoNotAskAgain();
        }
    }

    public static void requestPermissionFromFragment(@NonNull final Fragment fragment, @NonNull final String permission, final int requestCode) {
        fragment.requestPermissions(new String[]{permission}, requestCode);
        SharedPrefs.setPermissionRequested(fragment.getContext(), permission, true);
    }

    public static void requestPermissionFromActivity(@NonNull final Activity activity, @NonNull final String permission, final int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        SharedPrefs.setPermissionRequested(activity, permission, true);
    }


    public static void missingPermissionAlertDialog(@NonNull final Context context, @StringRes final int titleResId, @StringRes final int bodyResId, final DialogInterface.OnCancelListener onCancelListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(titleResId)
                .setMessage(bodyResId)
                .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onCancelListener != null) {
                            onCancelListener.onCancel(dialog);
                        }
                    }
                });
        alertDialogBuilder.create().show();
    }

    public interface CheckPermissionListener {
        void onPermissionAlreadyGranted();

        void onPermissionAlreadyDeniedWithDoNotAskAgain();

        void onPermissionPendingRequest();
    }

    public interface OnResultPermissionListener {
        void onPermissionGranted();

        void onPermissionDeniedWithDoNotAskAgain();

        void onPermissionDeniedButShowRequestPermissionRationale();
    }
}
