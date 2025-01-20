package com.catalia.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var pantalla: TextView
    private lateinit var historialView: TextView
    private var primerNumero: Double = 0.0
    private var operacionPendiente: String = ""
    private var nuevoNumero: Boolean = true
    private val historial = StringBuilder()

    companion object {
        private const val ERROR_MESSAGE = "Error"
        private const val DIVISION_BY_ZERO = "División por cero"
        private const val NEGATIVE_ROOT = "Raíz de número negativo"
        private const val MAX_DIGITS = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pantalla = findViewById(R.id.pantalla)
        historialView = findViewById(R.id.historial)

        // Configurar listeners para botones numéricos
        val botonesNumericos = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in botonesNumericos) {
            findViewById<Button>(id).setOnClickListener { numeroPresionado(it as Button) }
        }

        // Configurar listeners para operaciones básicas
        findViewById<Button>(R.id.btnSuma).setOnClickListener { operacionPresionada("+") }
        findViewById<Button>(R.id.btnResta).setOnClickListener { operacionPresionada("-") }
        findViewById<Button>(R.id.btnMultiplicar).setOnClickListener { operacionPresionada("×") }
        findViewById<Button>(R.id.btnDividir).setOnClickListener { operacionPresionada("÷") }
        findViewById<Button>(R.id.btnIgual).setOnClickListener { calcularResultado() }

        // Configurar listeners para funciones científicas
        findViewById<Button>(R.id.btnSin).setOnClickListener { funcionCientifica("sin") }
        findViewById<Button>(R.id.btnCos).setOnClickListener { funcionCientifica("cos") }
        findViewById<Button>(R.id.btnTan).setOnClickListener { funcionCientifica("tan") }
        findViewById<Button>(R.id.btnRaiz).setOnClickListener { funcionCientifica("√") }

        // Agregar nuevos botones
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { borrarDigito() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { resetCalculadora() }
        findViewById<Button>(R.id.btnClearEntry).setOnClickListener { 
            pantalla.text = "0"
            nuevoNumero = true
        }
    }

    private fun numeroPresionado(button: Button) {
        val numero = button.text.toString()
        if (pantalla.text.toString() == ERROR_MESSAGE) {
            pantalla.text = numero
            nuevoNumero = false
            return
        }
        
        if (pantalla.text.length >= MAX_DIGITS && !nuevoNumero) {
            return
        }

        if (nuevoNumero) {
            pantalla.text = numero
            nuevoNumero = false
        } else {
            pantalla.text = "${pantalla.text}$numero"
        }
    }

    private fun operacionPresionada(operacion: String) {
        try {
            val numero = pantalla.text.toString().toDouble()
            if (operacionPendiente.isEmpty()) {
                primerNumero = numero
            } else {
                primerNumero = calcular(primerNumero, numero, operacionPendiente)
                val resultado = formatearResultado(primerNumero)
                pantalla.text = resultado
                agregarAlHistorial("$primerNumero $operacionPendiente $numero = $resultado")
            }
            operacionPendiente = operacion
            nuevoNumero = true
        } catch (e: Exception) {
            pantalla.text = ERROR_MESSAGE
        }
    }

    private fun calcularResultado() {
        try {
            val segundoNumero = pantalla.text.toString().toDouble()
            val resultado = calcular(primerNumero, segundoNumero, operacionPendiente)
            val resultadoFormateado = formatearResultado(resultado)
            agregarAlHistorial("$primerNumero $operacionPendiente $segundoNumero = $resultadoFormateado")
            pantalla.text = resultadoFormateado
            primerNumero = resultado
            operacionPendiente = ""
            nuevoNumero = true
        } catch (e: Exception) {
            pantalla.text = ERROR_MESSAGE
        }
    }

    private fun calcular(a: Double, b: Double, operacion: String): Double {
        return when (operacion) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b != 0.0) a / b else throw ArithmeticException(DIVISION_BY_ZERO)
            else -> b
        }
    }

    private fun funcionCientifica(funcion: String) {
        try {
            val numero = pantalla.text.toString().toDouble()
            val expresion = "$funcion($numero)"
            
            val resultado = when (funcion) {
                "sin" -> Math.sin(Math.toRadians(numero))
                "cos" -> Math.cos(Math.toRadians(numero))
                "tan" -> Math.tan(Math.toRadians(numero))
                "√" -> if (numero >= 0) Math.sqrt(numero) 
                       else throw ArithmeticException(NEGATIVE_ROOT)
                else -> numero
            }
            
            val resultadoFormateado = formatearResultado(resultado)
            pantalla.text = resultadoFormateado
            agregarAlHistorial("$expresion = $resultadoFormateado")
            nuevoNumero = true
            
        } catch (e: Exception) {
            pantalla.text = ERROR_MESSAGE
        }
    }

    private fun formatearResultado(numero: Double): String {
        if (numero.isInfinite() || numero.isNaN()) {
            return ERROR_MESSAGE
        }

        if (Math.abs(numero) > 99999999 || (Math.abs(numero) < 0.00000001 && numero != 0.0)) {
            return String.format("%.2e", numero)
        }

        val resultado = if (numero == numero.toLong().toDouble()) {
            numero.toLong().toString()
        } else {
            String.format("%.8f", numero).trimEnd('0').trimEnd('.')
        }

        return if (resultado.length > MAX_DIGITS) {
            String.format("%.${MAX_DIGITS-4}e", numero)
        } else {
            resultado
        }
    }

    private fun borrarDigito() {
        val texto = pantalla.text.toString()
        if (texto.length > 1) {
            pantalla.text = texto.substring(0, texto.length - 1)
        } else {
            pantalla.text = "0"
            nuevoNumero = true
        }
    }

    private fun resetCalculadora() {
        primerNumero = 0.0
        operacionPendiente = ""
        nuevoNumero = true
        pantalla.text = "0"
    }

    private fun agregarAlHistorial(entrada: String) {
        historial.insert(0, "$entrada\n")
        historialView.text = historial.toString()
    }
} 