package com.getcinderella.app.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Map;

import com.getcinderella.app.Activities.Splash;
import com.getcinderella.app.Models.UserModel;
import com.getcinderella.app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationHelper {
    private Context mContext;
    private String CHANNEL_ID = "Cinderella Push Notification Channel";
    private static final String CHANNEL_ONE_NAME = "Cinderella Notifications";
    private static final String CHANNEL_TWO_NAME = "Cinderella Missed Calls";

    public void createMissedCallNotification(String name) {
        if (StringUtils.isName(name))
            buildNotification("A Partner tried contacting you","You have missed a chat from your partner "+StringUtils.removeFirstChar(name),CHANNEL_TWO_NAME,(int)System.currentTimeMillis());
        else{
            FirebaseFirestore.getInstance().collection(mContext.getString(R.string.user_db))
                    .document(StringUtils.removeFirstChar(name))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserModel remote = documentSnapshot.toObject(UserModel.class);
                            String n=StringUtils.extractFirstName(remote.getA());
                            buildNotification("A Partner tried contacting you","You have missed a chat from your partner "+n,CHANNEL_TWO_NAME,(int)System.currentTimeMillis());
                        }
                    });
        }
    }

    public void createFCMNotification(Map data) {
        buildNotification(data.get("title").toString(),data.get("msg").toString(),CHANNEL_ONE_NAME,1);
    }

    public void createNotification(Map data) {
        try {
            buildNotification(data.get("msgTitle").toString(), data.get("msg").toString(), CHANNEL_ONE_NAME, 1);
        }catch (Exception ignore){
            buildNotification("You Have New Scenes!","Click Here to explore!", CHANNEL_ONE_NAME, 1);
        }
    }

    public void createClaimPixieNotification() {
        buildNotification("Claim Free Pixies","You can now claim Pixies for free! Hoo-ray!",CHANNEL_ONE_NAME,2);
    }

    public NotificationHelper(Context context) {
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager,String Channel) {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    Channel, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorAccent);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void buildNotification(String title, String msg,String Channel,int id){
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(mNotificationManager,Channel);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, Splash.class),  PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        mNotificationManager.notify(id, mBuilder.build());
    }

}
