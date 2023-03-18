package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonaListActivity : AppCompatActivity() {

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

    private lateinit var carRecyclerView: RecyclerView
    private lateinit var personaArrayList: ArrayList<Persona>
    private lateinit var adapterCar: AdapterPersona

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persona_list)

        var id = intent.getStringExtra("casaId")

        carRecyclerView = findViewById(R.id.carListRecyclerView)
        carRecyclerView.layoutManager = LinearLayoutManager(this)
        carRecyclerView.setHasFixedSize(true)
        personaArrayList = arrayListOf<Persona>()
        if (id != null) {
            consultarDocumentosColegio(id!!)
        }
        adapterCar = AdapterPersona(personaArrayList, contenidoIntentExplicito, id!!)
        carRecyclerView.adapter = adapterCar

        val btnNuevoEstudiante = findViewById<Button>(R.id.btn_nueva_persona)
        btnNuevoEstudiante.setOnClickListener {
            abrirActividadConParametros(EditPersonaInfoActivity::class.java, it, id!!)
        }
    }

    private fun consultarDocumentosColegio(id: String) {

        val db = Firebase.firestore
        val colegioRef = db.collection("casa").document(id)
        val estudianteCollectionRef = colegioRef.collection("persona")
        limpiarArreglo()
        estudianteCollectionRef.get().addOnSuccessListener { querySnaps ->
            for (document in querySnaps.documents) {
                val persona = Persona(
                    document.get("id") as String?,
                    document.get("nombre") as String?,
                    (document.get("altura") as Number?)?.toDouble(),
                    document.get("fechaNacimiento") as String?,
                    document.get("tieneSeguro") as Boolean?
                )
                    this.personaArrayList.add(persona)

            }
            adapterCar.personaList = personaArrayList
            adapterCar.notifyDataSetChanged()
        }

    }

    private fun limpiarArreglo() {
        this.personaArrayList.clear()
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, casaId: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("casaId", casaId)
        contenidoIntentExplicito.launch(intent)
    }
}