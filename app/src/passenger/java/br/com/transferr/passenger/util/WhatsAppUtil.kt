package br.com.transferr.passenger.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder

/**
 * Created by Rafael Rocha on 29/08/2018.
 */
class WhatsAppUtil {
    companion object {
        fun callWhatsapp(phone:String,context:Context){
            val packageManager = context?.packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Olá", "UTF-8")
                i.`package` = "com.whatsapp"
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    context?.startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}