package com.example.myapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    boolean on=true;
    String respostaserver = null;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        final Button botaooffline = findViewById(R.id.BotaoOffline);

        botaooffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, App_Monitor.class));
                on=false;
                finish();
            }
        });
        Log.d("local", "MainActivity1");
        getConexao();
    }
    private Object setAtraso(){
        CountDownTimer countDownTimer=new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                if (on==true){
                    getConexao();
                }

            }
        }.start();

        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    Uri som= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getConexao(){

        SegundoPlano segundoPlano=new SegundoPlano();
        try {
            respostaserver=segundoPlano.execute().get();
            JSONArray json = new JSONArray(respostaserver);
            for(int i = 0; i < json.length(); i++){
                JSONObject o = json.getJSONObject(i);
                if (o.get("status").toString()=="true"){
                    notification(o.get("id"),o.get("nome"));
                }
            }
        } catch (JSONException | InterruptedException jsonException) {
            jsonException.printStackTrace();
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        }
        Log.d("RespostaServer: ",respostaserver);
        setAtraso();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notification(Object id, Object nome){

        NotificationManager nm=(NotificationManager)  getSystemService(NOTIFICATION_SERVICE);

        //Parte Responsavel por Mostrar Notificação
        final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
        int number_not= (int) ((int)(Math.random()*1000)-(Math.random()*2));
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
              "Fall-Detection2",
           NotificationManager.IMPORTANCE_HIGH);

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setTicker("Teste");
        builder.setContentTitle("Alerta Possivel Queda!!");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_warning_24));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentTitle("Fall-Detection");
        builder.setSound(som);
        builder.setVibrate(new long[]{1500,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("O Usuario: " +String.valueOf(nome)+ " de id: "+String.valueOf(id)+" pode ter sofrido uma queda."));

        nm.notify(String.valueOf(number_not),R.drawable.ic_launcher_background,builder.build());

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
    }

}

