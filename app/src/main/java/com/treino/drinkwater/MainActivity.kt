package com.treino.drinkwater

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.treino.drinkwater.databinding.ActivityMainBinding
import com.treino.drinkwater.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var calcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoML = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        calcularIngestaoDiaria = CalcularIngestaoDiaria()


        binding.btCalcular.setOnClickListener {
            if (binding.editPeso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()
            } else if (binding.editIdade.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()
            } else {
                val peso = binding.editPeso.text.toString().toDouble()
                val idade = binding.editIdade.text.toString().toInt()
                calcularIngestaoDiaria.CalcularTotalML(peso, idade)
                resultadoML = calcularIngestaoDiaria.ResultadoML()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                binding.txtResultadoMl.text = formatar.format(resultadoML) + " " + " ml"
            }
        }
        binding.icRedefinir.setOnClickListener {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_desc)
                .setPositiveButton("Ok") { dialogInterface, i ->
                    binding.editPeso.setText("")
                    binding.editIdade.setText("")
                    binding.txtResultadoMl.text = ""
                }
            alertDialog.setNegativeButton("Cancelar") { dialogueInterface, i ->

            }
            val dialog = alertDialog.create()
            dialog.show()
        }

        binding.btDefinirLembrete.setOnClickListener {

            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog =
                TimePickerDialog(this, { timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                    binding.txtHora.text = String.format("%02d", hourOfDay)
                    binding.txtMinutos.text = String.format("02d", minutes)
                }, horaAtual, minutosAtuais, true)
            timePickerDialog.show()
        }

        binding.btAlarme.setOnClickListener {

            val txt_hora = binding.txtHora
            val txt_minutos = binding.txtMinutos

            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_message))
                startActivity(intent)

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }

    /*private fun IniciarComponentes() {
        edit_peso = findViewById(R.id.edit_peso)
        edit_idade = findViewById(R.id.edit_idade)
        bt_calcular = findViewById(R.id.bt_calcular)
        txt_resultado_ml = findViewById(R.id.txtResultado_ml)
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        bt_lembrete = findViewById(R.id.bt_definir_lembrete)
        bt_lembrete = findViewById(R.id.bt_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minutos = findViewById(R.id.txt_minutos)
    }*/

}


