package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EditCasaInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_casa_info)
        var id = intent.getStringExtra("casaId")
        val direccion = intent.getStringExtra("casaDireccion")
        val supercifie = intent.getDoubleExtra("casaSuperficie", 0.0)
        val pisos = intent.getIntExtra("casaPisos", 0)
        val tienePatio = intent.getBooleanExtra("casaTienePatio", false)


        var textDireccion = this.findViewById<EditText>(R.id.tit_direccion)
        var textSuperficie = this.findViewById<EditText>(R.id.tit_superficie)
        var textPisos = this.findViewById<EditText>(R.id.tit_pisos)
        var textTienePatio = this.findViewById<RadioGroup>(R.id.rg_tiene_patio)
        var textOptionPatioYes = this.findViewById<RadioButton>(R.id.option_patio_si)
        var textOptionPatioNo = this.findViewById<RadioButton>(R.id.option_patio_no)

        if(tienePatio == true){
            textTienePatio.check(textOptionPatioYes.id)
        }else{
            textTienePatio.check(textOptionPatioNo.id)
        }

        var tienePatioText = false
        textTienePatio.setOnCheckedChangeListener { group, checkedId ->
            tienePatioText = checkedId == textOptionPatioYes.id
        }

        textDireccion.setText(direccion)
        textSuperficie.setText(supercifie.toString())
        textPisos.setText(pisos.toString())


        val btnSaveData = this.findViewById<Button>(R.id.btn_save)
        btnSaveData.setOnClickListener {


            if (!checkChanges(
                    direccion,
                    supercifie,
                    pisos,
                    tienePatio,
                    textDireccion.text.toString(),
                    textSuperficie.text.toString().toDouble(),
                    textPisos.text.toString().toInt(),
                    tienePatioText
                )
            ) {

                if (id == null) {
                    id = Date().time.toString()
                }
                saveData(
                    id!!,
                    textDireccion.text.toString(),
                    textSuperficie.text.toString().toDouble(),
                    textPisos.text.toString().toInt(),
                    tienePatioText
                )
            }
            irActividad(CasaListActivity::class.java)
        }


    }

    private fun checkChanges(
        direccion: String?,
        supercifie: Double,
        pisos: Int,
        tienePatio: Boolean,
        textDireccion: String,
        textSuperficie: Double,
        textPisos: Int,
        tienePatioText: Boolean
    ): Boolean {
        return (direccion == textDireccion && supercifie == textSuperficie && pisos == textPisos && tienePatio == tienePatioText)
    }

    private fun saveData(
        id: String,
        textDireccion: String,
        textSuperficie: Double,
        textPisos: Int,
        tienePatioText: Boolean
    ) {
        val db = Firebase.firestore
        val casa = db.collection("casa")

        val data1 = hashMapOf(
            "id" to id,
            "direccion" to textDireccion,
            "supercifie" to textSuperficie,
            "pisos" to textPisos,
            "tienePatio" to tienePatioText
           )
        casa.document(id).set(data1)
    }




    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}