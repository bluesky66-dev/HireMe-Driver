package com.grepix.grepixutils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by grepixinfotech on 08/07/16.
 */
public class Utils {

    public interface  AlertCallBack
    {
         void onClickAlertButton(boolean isYes);
    }

    public static Uri buildURI(String url, Map<String, String> params) {

        // build url with parameters.
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }
    public static void showAlert(Context context, int titleId, int messageId, final AlertCallBack callBack) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(titleId);
        builder.setCancelable(false);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                callBack.onClickAlertButton(false);
            }
        });
        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onClickAlertButton(true);
            }
        });
        builder.show();
    }

    public static boolean net_connection_check(Context context) {
        ConnectionManager cm = new ConnectionManager(context);

        boolean connection = cm.isConnectingToInternet();

        if (!connection) {

            Toast toast = Toast.makeText(context.getApplicationContext(), "There is no network please connect.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 70);
            toast.show();
        }
        return connection;
    }
}
