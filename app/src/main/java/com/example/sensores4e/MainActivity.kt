package com.example.sensores4e

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var layout : View

    private lateinit var tvTitulo : TextView
    private lateinit var tvValores : TextView
    private lateinit var tvEstado : TextView

    //crear un atributo que nos permita manejar el sensor
    private lateinit var sensorManager : SensorManager

    //atributon que permite usar el sensor
    private var sensorLuz : Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        layout = findViewById(R.id.main)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvValores = findViewById(R.id.tvValores)
        tvEstado = findViewById(R.id.tvEstado)

        //indicar que el sensor ocupará los servicios del sistema
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //hacer uso del sensor de luz
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        //verificar si el sensor de luz esta disponible
        if(sensorLuz == null){
            tvEstado.text = "No hay sensor de luz : ()"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    //MÉTODOS OBLIGATORIOS DE LA CLASE

    //método para ver los cambios en el sensor
    override fun onSensorChanged(event: SensorEvent?) {
        //validar que el sensor sea el correcto
        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            //crear una variable que guarde valores
            val luz = event.values[0]
            tvValores.text = "Luz: $luz lx"
            if(luz < 50){
                tvEstado.text = "Modo oscuro"
                layout.setBackgroundColor(Color.BLACK)
                tvEstado.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvTitulo.setTextColor(Color.WHITE)

            }else if(luz > 500){
                tvEstado.text = "Modo claro)"
                layout.setBackgroundColor(Color.WHITE)
                tvEstado.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
                tvTitulo.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorLuz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    /*
    //método para hacer cambios en los valores matemáticos
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //MÉTODO PARA APAGAR EL COMPONENTE (SENSOR)
        super.onResume()

        sensorLuz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
                )

        } */

}