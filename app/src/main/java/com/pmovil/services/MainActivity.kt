package com.pmovil.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MessageListener {

    private var mBinder: BindService.LocalBinder? = null
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(AlarmReceiver(), IntentFilter(AppConstants.ACTION_ALARM))
        registerReceiver(
            ServicesMessagesReceiver(this),
            IntentFilter(AppConstants.ACTION_SERVICE_MESSAGE)
        )
    }

    fun createAlarm(view: View) {
        val delay = if (alarm_delay_time_five_seconds_rb.isChecked) 5 else 10

        val context: Context = this
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(AppConstants.ACTION_ALARM).apply {
            putExtra(AppConstants.EXTRA_DELAY_TIME, delay)
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + delay * 1000,
            pendingIntent
        )

        Toast.makeText(context, "Created Alarm", Toast.LENGTH_SHORT).show()
    }

    private fun getPrimeNumbersMaxValue(): Int {
        return when (prime_numbers_rg.checkedRadioButtonId) {
            R.id.prime_numbers_100k_rb -> 100000
            R.id.prime_numbers_300k_rb -> 300000
            R.id.prime_numbers_600k_rb -> 600000
            R.id.prime_numbers_900k_rb -> 900000
            else -> 1000000
        }
    }

    fun calculatePrimes(view: View) {
        val maxValue = getPrimeNumbersMaxValue()

        var primesCount = 0
        for (i in 1..maxValue) {
            if (AppUtils.isPrime(i)) {
                primesCount++
            }
        }

        prime_numbers_count_tv.text = "$primesCount"

        Log.d("MainActivity", "calculatePrimes: $primesCount")
    }

    fun calculatePrimesWithRunnable(view: View) {
        val maxValue = getPrimeNumbersMaxValue()

        val runnable = Runnable {
            var primesCount = 0
            for (i in 1..maxValue) {
                if (AppUtils.isPrime(i)) {
                    primesCount++
                }
            }

            prime_numbers_count_tv.text = "$primesCount"

            Log.d("MainActivity", "calculatePrimes: $primesCount")
        }

        Thread(runnable).start()
    }

    fun startIntentService(view: View) {
        val maxValue = getPrimeNumbersMaxValue()
        val intent = Intent(this, PrimesIntentService::class.java).apply {
            putExtra(AppConstants.EXTRA_MAX_VALUE, maxValue)
        }
        startService(intent)
    }

    fun startService(view: View) {
        val maxValue = getPrimeNumbersMaxValue()
        val intent = Intent(this, PrimesService::class.java).apply {
            putExtra(AppConstants.EXTRA_MAX_VALUE, maxValue)
        }
        startService(intent)
    }

    fun startServiceWithThread(view: View) {
        val maxValue = getPrimeNumbersMaxValue()
        val intent = Intent(this, PrimesService::class.java).apply {
            putExtra(AppConstants.EXTRA_MAX_VALUE, maxValue)
            putExtra(AppConstants.EXTRA_USE_THREAD, true)
        }
        startService(intent)
    }

    fun startForegroundService(view: View) {
        val maxValue = getPrimeNumbersMaxValue()

        val intent = Intent(this, ForegroundService::class.java).apply {
            putExtra(AppConstants.EXTRA_MAX_VALUE, maxValue)
            putExtra(AppConstants.EXTRA_USE_THREAD, true)
        }

        if (stopForegroundService.isChecked) {
            intent.putExtra(AppConstants.EXTRA_DELAY_TIME, 5)
        }

        startService(intent)

    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinder = service as BindService.LocalBinder
            mBound = true

            Toast.makeText(baseContext, "Bind Service Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
            Toast.makeText(baseContext, "Bind Service Disconnected", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onMessageReceived(intent: Intent) {
        val serviceName = intent.getStringExtra(AppConstants.EXTRA_SERVICE_NAME)
        val serviceStatus = intent.getStringExtra(AppConstants.EXTRA_SERVICE_STATUS)
        Toast.makeText(this, "$serviceName: $serviceStatus", Toast.LENGTH_SHORT).show()
    }

    fun bindService(view: View) {
        val intent = Intent(this, BindService::class.java)
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService(view: View) {
        unbindService(mServiceConnection)
        mBound = false
    }

    fun bindServiceFunctions(view: View) {
        if (mBound) {
            when (view.id) {
                R.id.bind_function_primes -> mBinder?.calculatePrimes(getPrimeNumbersMaxValue())
                R.id.bind_function_random -> {
                    Toast.makeText(
                        this,
                        "Random number ${mBinder?.getRandomNumber()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Service is not bounded", Toast.LENGTH_SHORT).show()
        }
    }
}