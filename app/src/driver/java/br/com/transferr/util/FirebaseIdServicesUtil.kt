package br.com.transferr.util

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by idoctor on 02/02/2018.
 */
class FirebaseIdServicesUtil : FirebaseInstanceIdService() {
    val CURRENT_TOKEN: String = "TOKEN"
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val  recentToken: String? = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(recentToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?){
        Log.d(CURRENT_TOKEN,refreshedToken)
        //tOKEN FOR TEST EMULADOR:d0i402gcN1g:APA91bEOAkS3nasKu9kiPAx4Du0B_fEaqzF-pZRy8SF2PXv4QFDYXdkbNHDKX8DG3EilOA-TO0htXRqbWFODkZ9yARfYa9oOmDiQBjMwAcrcegMNBXMWUll8Q8lJqei6yZlysj_u9zAt
    }
}