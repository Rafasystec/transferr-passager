package br.com.transferr.util

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by idoctor on 02/02/2018.
 */
class FirebaseMessagingServiceUtil: FirebaseMessagingService() {
    val TAG = "FIRE_BASE"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //When we receive the message
        handleNotification(remoteMessage!!.notification?.body)

        Log.d(TAG,"onMessageReceived was called")
        if(remoteMessage.notification != null){
            val title = remoteMessage.notification?.title
            val msg = remoteMessage.notification?.body
            Log.d(TAG,"Título da msg"+title)
            Log.d(TAG,"corpo da msg"+msg)
            //NotificationUtils.create()
        }
    }

    private fun handleNotification(body: String?) {
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra("message",body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}