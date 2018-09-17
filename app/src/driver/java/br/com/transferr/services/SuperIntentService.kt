package br.com.transferr.services

import android.app.IntentService
import android.content.Intent
import br.com.transferr.extensions.log

/**
 * Created by idoctor on 09/02/2018.
 */
class SuperIntentService : IntentService("super-intent"){
    private var count:Int = 0
    private var running:Boolean = false
    private val MAX = 10
    private val TAG = "livro"
    override fun onHandleIntent(p0: Intent?) {
        log("Service name power init")
        running = true
        while (running && count < MAX){
            Thread.sleep(2000)
            log("executing >>> $count")
            count++
        }
        log("Execution finished")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("destroy >>> Intent super-intent")
        running = false
    }

}