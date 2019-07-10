package br.com.lyins.push;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.*;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.Notification;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.os.Build;
import android.app.NotificationChannel;



public class ReceiverPush extends BroadcastReceiver {
    
	public static final String NOTIFICATION_CHANNEL_ID = "10001";
	
	@Override
    public void onReceive(Context context, Intent intent) 
	{
		Bundle extras = intent.getExtras();
		String Message, ID;
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		
		
		
        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
		{      
           
			
	
			Message = extras.getString("gcm.notification.message"); //Adquire o texto enviado no Firebase 
			ID 		= extras.getString("gcm.notification.id_push"); //Adquire o código enviado pelo Firebase
			
			//Código 1 que inicia a aplicação
			if  (ID.equals("1"))  {
				
				Intent launchintentApp = new Intent();
				launchintentApp.setClassName(context.getApplicationContext(), "com.embarcadero.firemonkey.FMXNativeActivity");
				launchintentApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(launchintentApp);	
				return;
			

			}
			//Codigo 2, abre o app e mostra mensagem enviada
			if (ID.equals("2")) 
			{			
				try 
				{
					File file = new File(context.getExternalFilesDir(null) + "/msg");
						if(file.exists()) {
							file.delete();
						}	
					FileWriter fw = new FileWriter(file);
					fw.write(Message);
					fw.close();
					
				} catch ( IOException io ) { }
				
				Intent launchintentApp = new Intent();
				launchintentApp.setClassName(context.getApplicationContext(), "com.embarcadero.firemonkey.FMXNativeActivity");
				launchintentApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(launchintentApp);	
				return;
			}
			//Codigo 3, apenas mostra a notificação push sem abrir o aplicativo
			if (ID.equals("3"))
			{ 
				/*
				**** deprecated ****		
				PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "");
				wakeLock.acquire();
				*/
				
				try 
				{
					File file = new File(context.getExternalFilesDir(null) + "/msg");
						if(file.exists()) {
							file.delete();
						}	
					FileWriter fw = new FileWriter(file);
					fw.write(Message);
					fw.close();
					
				} catch ( IOException io ) { }
				
				Drawable drawable = context.getApplicationInfo().loadIcon(context.getPackageManager());
				Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
				
				Intent AppClass = new Intent();
				AppClass.setClassName(context, "com.embarcadero.firemonkey.FMXNativeActivity");
				PendingIntent ContentIntent = PendingIntent.getActivity(context, 1, AppClass, 
							PendingIntent.FLAG_UPDATE_CURRENT);
			
				NotificationCompat.Builder b = new NotificationCompat.Builder(context);
				
			
				b.setAutoCancel(true);
				b.setWhen(System.currentTimeMillis());
				b.setSmallIcon(android.R.drawable.ic_dialog_info);
				b.setLargeIcon(bitmap);
				b.setTicker("Atenção");
				b.setContentTitle("Push");
				b.setContentText(extras.getString("gcm.notification.message"));
				b.setContentInfo("Push");
				b.setPriority(2);
				b.setDefaults(Notification.DEFAULT_SOUND);
				b.setVibrate(new long[]{100, 200, 300, 400, 500,400, 300, 200, 400}).build();
				b.setContentIntent(ContentIntent);
				
				NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
				{
					int Importance = NotificationManager.IMPORTANCE_HIGH;
					NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notificações do App", Importance);
					notificationChannel.enableLights(true);
					notificationChannel.enableVibration(true);
					notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
					assert nm != null;
					b.setChannelId(NOTIFICATION_CHANNEL_ID);
					nm.createNotificationChannel(notificationChannel);
					
				}
				nm.notify("PUSH", 0, b.build());
				
				//**** deprecated ****
				//wakeLock.release();
			}
			
			//Codigo 4, abre o app, destrava e vibra
			if (ID.equals("4")) 
			{			
			
				try 
				{
					File file = new File(context.getExternalFilesDir(null) + "/msg");
						if(file.exists()) {
							file.delete();
						}	
					FileWriter fw = new FileWriter(file);
					fw.write("4");
					fw.close();
					
				} catch ( IOException io ) { }
				
				Intent launchintentApp = new Intent();
				launchintentApp.setClassName(context.getApplicationContext(), "com.embarcadero.firemonkey.FMXNativeActivity");
				launchintentApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(launchintentApp);	
				
				return;
			}
			
        }
       
    }
	
	
}

