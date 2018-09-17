package br.com.transferr.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import br.com.transferr.R

/**
 * Created by root on 02/02/18.
 */
object NotificationUtils {
    fun create(context:Context, id : Int,intent: Intent,contentTitle: String, contentText:String){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Intent to triggers the broadcast
        val p = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        //Creates the notification
        val builder = NotificationCompat.Builder(context,"id")
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
        //Dispara a notificação
        val n = builder.build()
        manager.notify(id,n)
    }
}