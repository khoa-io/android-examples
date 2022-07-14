package dev.khoa.androidprocessexample.myclient

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dev.khoa.androidprocessexample.ICallback
import dev.khoa.androidprocessexample.IRemoteService
import dev.khoa.androidprocessexample.MyParcel
import dev.khoa.androidprocessexample.myclient.databinding.ActivityClientMainBinding

class ClientMainActivity : AppCompatActivity() {
    private val TAG = "MyServiceClient"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityClientMainBinding
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private var iRemoteService: IRemoteService? = null

    private var callback = object : ICallback.Stub() {
        override fun onFinished() {
            Log.i(TAG, "sendParcel execution has been done")
        }
    }

    private val mConnection = object : ServiceConnection {

        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            iRemoteService = IRemoteService.Stub.asInterface(service)
            Log.i(TAG, "${IRemoteService::class.java} has been bound")
            Log.i(TAG, "RemotePID ${iRemoteService!!.pid}")
            Log.i(TAG, "Calling other methods")
            iRemoteService!!.sendBasicTypes(0x12345678, 0x12345678abcd, true, 0.12f, 0.13, "Text")
            val parcel = MyParcel()
            parcel.anInt = 0xfffffff
            parcel.aLong = 0x12348765abcdffff
            parcel.aBoolean = false
            parcel.aFloat = 0.21f
            parcel.aDouble = 0.21
            parcel.aString = "A String"
            iRemoteService!!.sendParcel(parcel, callback)
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.i(TAG, "onBindingDied")
            iRemoteService = null
        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            prefs.unregisterOnSharedPreferenceChangeListener(this.listener)
        }
        getPreferences(0).registerOnSharedPreferenceChangeListener(listener)

        binding = ActivityClientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_client_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (iRemoteService != null) {
            Log.w(TAG, "${IRemoteService::class.java} has already been bound")
        }

        val pkgName = "dev.khoa.androidprocessexample"
        val serviceName = "${pkgName}.RemoteService"
        val comp = startService(Intent().apply {
            component = ComponentName(pkgName, serviceName)
            putExtra("msg", "Intent3")
        })
        if (comp == null) {
            Log.e(TAG, "$serviceName could not be found")
        } else {
            Log.i(TAG, "Found $serviceName")
        }

        Log.i(TAG, "Binding $serviceName")
        val intent = Intent().apply {
            setPackage(pkgName)
            setClassName(pkgName, serviceName)
        }

        val result = bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        if (!result) {
            Log.e(TAG, "Cannot bind $serviceName")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_client_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_client_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()

        if (iRemoteService != null) {
            Log.i(TAG, "Unbinding service ${IRemoteService::class.simpleName}")
            unbindService(mConnection)
            iRemoteService = null
        }
    }
}