package dev.khoa.androidprocessexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log


class RemoteService : Service() {
    val TAG = "MyService"

    private val binder = object : IRemoteService.Stub() {
        override fun getPid(): Int =
            Process.myPid()

        override fun sendBasicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
            Log.i(
                TAG,
                "Received anInt: '$anInt', " +
                        "aLong: '$aLong', " +
                        "aBoolean: '$aBoolean', " +
                        "aFloat: '$aFloat', " +
                        "aDouble: '$aDouble', " +
                        "aString: '$aString'"
            )
        }

        override fun sendParcel(parcel: MyParcel, callback: ICallback) {
            Log.i(
                TAG,
                "Received parcel: anInt: '${parcel.anInt}', " +
                        "aLong: '${parcel.aLong}', " +
                        "aBoolean: '${parcel.aBoolean}', " +
                        "aFloat: '${parcel.aFloat}', " +
                        "aDouble: '${parcel.aDouble}', " +
                        "aString: '${parcel.aString}'"
            )

            callback.onFinished()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i(TAG, "onBind")
        return binder.asBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Received start id $startId: $intent")
        if (intent?.hasExtra("msg") == true) {
            val extras = intent.extras!!
            Log.i(TAG, "Extra ${extras.get("msg")}")
        }
        return START_NOT_STICKY
    }
}