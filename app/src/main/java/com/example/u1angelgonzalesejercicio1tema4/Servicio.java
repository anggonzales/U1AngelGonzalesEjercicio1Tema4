package com.example.u1angelgonzalesejercicio1tema4;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Servicio extends AppCompatActivity {

    private IntentFilter intentFilter;

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //notificationManager.cancel(2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(3);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter("RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(Servicio.this, ReceptorLlamadas.class));
            startForegroundService(new Intent(Servicio.this, ReceptorSMS.class));
            finish();
        } else {
            startService(new Intent(Servicio.this,
                    ReceptorLlamadas.class));
            startService(new Intent(Servicio.this,
                    ReceptorSMS.class));
            finish();
        }
    }
}
