package pl.sdacademy.grabyourphoto;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by mikr on 10/08/17.
 */

public class Permission {

    private static final int PICK_IMAGE = 888;

    public static void checkPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {
                showExplanation(activity, "Potrzebujemy pozwolenia", "Daj je nam bym załadować foto",
                        Manifest.permission.READ_EXTERNAL_STORAGE, PICK_IMAGE);
            } else {
                requestPermissions(activity, Manifest.permission.READ_EXTERNAL_STORAGE, PICK_IMAGE);
            }
        }
    }


    public static void requestPermissions(Activity activity, String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionName}, permissionRequestCode);
    }

    public static void showExplanation(final Activity activity, String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                requestPermissions(activity, permission, permissionRequestCode);
            }
        });
        builder.show();
    }

}
