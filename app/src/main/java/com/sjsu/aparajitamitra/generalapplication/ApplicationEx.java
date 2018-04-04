package com.sjsu.aparajitamitra.generalapplication;

/**
 * Created by aparajitamitra on 4/26/17.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ApplicationEx extends android.app.Application {

    public static final CharSequence[] flag_options = {"BIND_AUTO_CREATE",
            "BIND_ADJUST_WITH_ACTIVITY"};
    public static Context context;
    public static String dataType;
    public static String fileExtension;
    public static int selectedFlag = 0;

    public static void writeLogsToFile(String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "Output Logs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File outputLogFile = new File(root, "Output Log File.txt");
            BufferedWriter bufferedWritter;
            bufferedWritter = new BufferedWriter(new FileWriter(outputLogFile,
                    true));
            bufferedWritter.write(sBody);
            bufferedWritter.newLine();
            bufferedWritter.flush();
            bufferedWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileType() {
        String fileName = "";

        if (ApplicationEx.fileExtension.equalsIgnoreCase(".pdf"))
            fileName = "PDF File";

        return fileName;
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = conn
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = conn
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetwork != null && wifiNetwork.isAvailable() == true
                && wifiNetwork.isConnectedOrConnecting() == true) {
            return true;
        } else if (mobileNetwork != null && mobileNetwork.isAvailable() == true
                && mobileNetwork.isConnectedOrConnecting() == true) {
            return true;
        } else
            return false;
    }

    public static boolean isDirectoryPresent(String directoryName) {
        File directory = new File( Environment.getExternalStorageDirectory(),
                directoryName);
        if (directory.exists()) {
            return true;
        }
        return false;

    }

    public static void showDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                context);
        dialog.setTitle(context.getResources().getString(
                R.string.flag_bind_adjust));
        dialog.setMessage(context.getResources().getString(
                R.string.flag_bind_adjust_message));
        dialog.setCancelable(false);
        dialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int whichButton) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}

