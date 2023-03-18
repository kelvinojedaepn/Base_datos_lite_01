package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class EditPersonaInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_persona_info)
        var id = intent.getStringExtra("casaId")
        var estudianteId = intent.getStringExtra("personaId")
        var nombre = intent.getStringExtra("personaeNombre")
        var altura = intent.getDoubleExtra("personaAltura", 0.0)
        var fechaNacimiento = intent.getStringExtra("personaNacimiento")
        var tieneSeguro = intent.getBooleanExtra("personaTieneSeguro", false)


        var textNombre = this.findViewById<EditText>(R.id.tit_nombre_estudiante)
        textNombre.setText(nombre)
        var textAltura = this.findViewById<EditText>(R.id.tit_altura)
        textAltura.setText(altura.toString())
        val datePicker = findViewById<DatePicker>(R.id.date_picker_nacimiento)
        var textTieneSeguro = this.findViewById<RadioGroup>(R.id.rg_tiene_seguro)
        var textOptionSeguroYes = this.findViewById<RadioButton>(R.id.option_seguro_si)
        var textOptionSeguroNo = this.findViewById<RadioButton>(R.id.option_seguro_no)

        if(tieneSeguro == true){
            textTieneSeguro.check(textOptionSeguroYes.id)
        }else{
            textTieneSeguro.check(textOptionSeguroNo.id)
        }

        var tieneSeguroText = false
        textTieneSeguro.setOnCheckedChangeListener { group, checkedId ->
            tieneSeguroText = checkedId == textOptionSeguroYes.id
        }

        if(fechaNacimiento != null){
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse(fechaNacimiento)
            val calendar = Calendar.getInstance()
            calendar.time = date

            datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        var textFechaNacimiento: String = ""
        var year = datePicker.year
        var month = datePicker.month
        var dayOfMonth = datePicker.dayOfMonth
        var textCalendar = Calendar.getInstance()
        textCalendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        textFechaNacimiento = dateFormat.format(textCalendar.time)
        datePicker.init(
            year,
            month,
            dayOfMonth,
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                val textCalendar = Calendar.getInstance()
                textCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textFechaNacimiento = dateFormat.format(textCalendar.time)
            }
        )




        val btnGuardarEstudiante = this.findViewById<Button>(R.id.btn_guardar_persona)
        btnGuardarEstudiante.setOnClickListener {
            if (!checkChange(
                    nombre,
                    altura,
                    fechaNacimiento,
                    tieneSeguro,
                    textNombre.text.toString(),
                    textAltura.text.toString(),
                    textFechaNacimiento,
                    tieneSeguroText

                )
            ) {

                if (estudianteId == null) {
                    estudianteId = Date().time.toString()
                }
                saveData(
                    id!!,
                    estudianteId!!,
                    textNombre.text.toString(),
                    textAltura.text.toString().toDouble(),
                    textFechaNacimiento,
                    tieneSeguroText
                )
            }
            abrirActividadConParametros(PersonaListActivity::class.java, it, id!!)

        }


    }

    private fun saveData(
        id: String,
        personaId: String,
        textNombre: String,
        textAltura: Double,
        textFechaNacimiento: String,
        tieneSeguro: Boolean
    ) {

        val db = Firebase.firestore
        val casa = db.collection("casa")
        val casaCollectionsRef = casa.document(id!!).collection("persona")
        val dataPersona = hashMapOf(
            "id" to personaId,
            "nombre" to textNombre,
            "altura" to textAltura,
            "fechaNacimiento" to textFechaNacimiento,
            "tieneSeguro" to tieneSeguro
        )
        casaCollectionsRef.document(personaId).set(dataPersona)

    }

    private fun checkChange(
        nombre: String?,
        altura: Double,
        fechaNacimiento: String?,
        tieneSeguro: Boolean?,
        textNombre: String,
        textAltura: String,
        textFechaNacimiento: String,
        textTieneSeguro: Boolean
    ): Boolean {
        return (nombre == textNombre && altura == textAltura.toDouble() && fechaNacimiento == textFechaNacimiento && tieneSeguro == textTieneSeguro)
    }

    private val contenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data
                Log.i("Intente-epn", "${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, casaId: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("casaId", casaId)
        contenidoIntentExplicito.launch(intent)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}