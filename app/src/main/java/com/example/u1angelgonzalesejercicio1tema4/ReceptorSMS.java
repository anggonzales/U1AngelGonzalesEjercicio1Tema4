package com.example.u1angelgonzalesejercicio1tema4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ReceptorSMS extends BroadcastReceiver {

    private static final int ID_NOTIFICACION_CREAR = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final String NOTIFICATION_CHANNEL_NAME = "UNJBG";

    @Override public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, Servicio.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int j = 0; j < pdusObj.length; j++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[j]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phoneNumber, null, message,null,null);

                    NotificationCompat.Builder notificacion = new
                            NotificationCompat.Builder(context)
                            .setContentTitle("El número investigado es :" + senderNum)
                            .setContentText(message)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(PendingIntent.getActivity(context, 0,
                                    new Intent(context, Servicio.class), 0));
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel =
                                new NotificationChannel(
                                        NOTIFICATION_CHANNEL_ID,
                                        NOTIFICATION_CHANNEL_NAME,
                                        NotificationManager.IMPORTANCE_LOW);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(R.color.colorAccent);
                        notificationManager.createNotificationChannel(notificationChannel);
                        notificacion.setChannelId(NOTIFICATION_CHANNEL_ID);
                    }
                    notificationManager.notify(ID_NOTIFICACION_CREAR, notificacion.build());
                    notificationManager.notify(1, notificacion.build());
                    //notificationManager.notify(0, notificacion.build());

                    Log.i("receptorsms", "senderNum: "+ senderNum + "; message: " + message);
                    //Toast.makeText(context, "prueba:" + message,  Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }
}
