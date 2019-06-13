package br.com.transferr.main.util

import android.os.AsyncTask.execute
import java.util.concurrent.*


/**
 * Created by idoctor on 13/06/2019.
 */
object ExecutorServiceUtil {
    val THREAD_POOL = Executors.newCachedThreadPool()
    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    fun <T> timedCall(c: Callable<T>, timeout: Long, timeUnit: TimeUnit): T {
        val task = FutureTask<T>(c)
        THREAD_POOL.execute(task)
        return task.get(timeout, timeUnit)
    }
}