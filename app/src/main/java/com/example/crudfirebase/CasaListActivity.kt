package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class CasaListActivity : AppCompatActivity() {


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


    private lateinit var userRecyclerView: RecyclerView
    private lateinit var casaArrayList: ArrayList<Casa>
    private lateinit var adaptador: AdapterCasa


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_casa_list)

        userRecyclerView = findViewById(R.id.userListRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        casaArrayList = arrayListOf<Casa>()
        adaptador = AdapterCasa(casaArrayList, contenidoIntentExplicito)
        userRecyclerView.adapter = adaptador

        consultarDocumentos()

        val btnCreateData = this.findViewById<Button>(R.id.btn_crear_casa)
        btnCreateData.setOnClickListener {
            irActividad(EditCasaInfoActivity::class.java)
        }

    }


    private fun consultarDocumentos() {
        val db = Firebase.firestore
        val colegioRef = db.collection("casa")
        limpiarArreglo()
        colegioRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val casa = Casa(
                    document.get("id") as String?,
                    document.get("direccion") as String?,
                    (document.get("supercifie") as Number?)?.toDouble(),
                    (document.get("pisos") as Number?)?.toInt(),
                    document.get("tienePatio") as Boolean?
                )
                casaArrayList.add(casa)
            }
            adaptador.casaList = casaArrayList
            adaptador.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d(null, "Error al obtener casas", exception)
        }
    }


    private fun limpiarArreglo() {
        this.casaArrayList.clear()
    }

    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}