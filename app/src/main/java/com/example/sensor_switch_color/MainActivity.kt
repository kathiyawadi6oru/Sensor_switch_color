package com.example.sensor_switch_color

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast


class MainActivity : Activity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var isColor = false
    private lateinit var view: View
    private var lastUpdate: Long = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.textView)
        view.setBackgroundColor(Color.GREEN)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

    }



    //overriding two methods of SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]
        val accelationSquareRoot = ((x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH))
        val actualTime = System.currentTimeMillis()
        Toast.makeText(applicationContext, accelationSquareRoot.toString() + " " +
                SensorManager.GRAVITY_EARTH, Toast.LENGTH_SHORT).show()
        if (accelationSquareRoot >= 2) //it will be executed if you shuffle
        {
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime //updating lastUpdate for next shuffle
            if (isColor) {
                view!!.setBackgroundColor(Color.GREEN)
            } else {
                view!!.setBackgroundColor(Color.RED)
            }
            isColor = !isColor
        }
    }

    override fun onResume() {
        super.onResume()
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // unregister listener
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}
