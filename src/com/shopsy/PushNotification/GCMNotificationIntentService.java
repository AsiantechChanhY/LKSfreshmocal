package com.shopsy.PushNotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.shopsy.NavigationDrawerPage;
import com.shopsy.OpenShopPage;
import com.shopsy.R;
import com.shopsy.Iconstant.Iconstant;
import com.shopsy.Utils.SessionManager;

/**
 * @author Anitha
 *
 */

@SuppressWarnings({"unused","deprecation"})
public class GCMNotificationIntentService extends IntentService
{
	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;
	private SessionManager session;
	private Context context=GCMNotificationIntentService.this;
	
	String key="data", Url_Key="data", Url_Key_Name="data", Url_Key_Image="data", Url_Key_message="data", 
			Url_Key_subject="data", Url_Key_message_id="data";
	
	public GCMNotificationIntentService() 
	{
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) 
		{
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) 
			{
				System.out.println("Push"+extras.toString());
				sendNotification( "Send error: " + extras.toString());
			} 
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) 
			{
				System.out.println("Push"+extras.toString());
				sendNotification( "Deleted messages on server: "+ extras.toString());
			} 
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) 
			{
				for (int i = 0; i < 3; i++) 
				{
					Log.d("Messsage Coming... ","" + (i + 1) + "/5 @ "+ SystemClock.elapsedRealtime());
					try 
					{
						Thread.sleep(5000);
					}
					catch (InterruptedException e) 
					{
					
					}
				}
				Log.e("Message Completed work @ ","" + SystemClock.elapsedRealtime());
				
				Log.e("Received: ","" + extras.toString());
				Log.e("Received: ","" + extras.toString());
				
				 session = new SessionManager(context);

				
				 if(extras!=null)
				 {
					 key= extras.get(Iconstant.MESSAGE_KEY_K).toString();
					 Url_Key= extras.get(Iconstant.MESSAGE_Url_1).toString();
						if (key.equals("contact") || key.equals("message")) 
						{
							Url_Key_Name= extras.get(Iconstant.MESSAGE_Url_4).toString();
							Url_Key_Image= extras.get(Iconstant.MESSAGE_Url_3).toString();
							
							Url_Key_message= extras.get(Iconstant.MESSAGE_Url_5).toString();
							Url_Key_subject= extras.get(Iconstant.MESSAGE_Url_6).toString();
							Url_Key_message_id= extras.get(Iconstant.MESSAGE_Url_7).toString();
							
							Log.e("Notifi--in---key: ","" + key);
							Log.e("Notifi--in---Url_Key_Name: ","" + Url_Key_Name);
							Log.e("Notifi--in---Url_Key_Image: ","" + Url_Key_Image);
							
						
						}
						sendNotification(extras.get(Iconstant.MESSAGE_KEY).toString());
						System.out.println("Push"+extras.toString());
				 }
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	
	
	
	private void sendNotification(String msg)
	{
		Intent notificationIntent = null;
		notificationIntent = new Intent(GCMNotificationIntentService.this, OpenShopPage.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		
		PendingIntent contentIntent = PendingIntent.getActivity(GCMNotificationIntentService.this,0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) GCMNotificationIntentService.this.getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = GCMNotificationIntentService.this.getResources();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMNotificationIntentService.this);
		builder.setContentIntent(contentIntent)
		       .setSmallIcon(R.drawable.shopsylogonew)
		       .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.shopsylogonew))
		       .setTicker(msg)
		       .setWhen(System.currentTimeMillis())
		       .setAutoCancel(true)
		       .setContentTitle("Shopsy")
		       .setLights(0xffff0000,100,2000)
		       .setPriority(Notification.DEFAULT_SOUND)
		       .setContentText(msg);

		   Notification n = builder.getNotification();
		
		n.defaults |= Notification.DEFAULT_ALL;
		nm.notify(0, n);	

	}
	
	
	public boolean doesPackageExist(String targetPackage) 
	{
	    PackageManager pm = getPackageManager();
	    try {
			PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
	    } catch (NameNotFoundException e) 
	    {
	        return false;
	    }
	    return true;    
	}
}