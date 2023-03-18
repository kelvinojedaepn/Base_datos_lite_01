package com.example.crudfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterPersona(
    var personaList: ArrayList<Persona>,
    private val contenidoIntentExplicito: ActivityResultLauncher<Intent>,
    val idCasa: String
) : RecyclerView.Adapter<AdapterPersona.MyViewHolderCar>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCar {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.persona_item, parent, false)
        return MyViewHolderCar(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderCar, position: Int) {
        val currentPersona = personaList[position]
        holder.idPersona.text = currentPersona.id
        holder.nombrePersona.text = currentPersona.nombre
        holder.altura.text = currentPersona.altura.toString()
        holder.tieneSeguro.text = currentPersona.tieneSeguro.toString()
        holder.fechaNacimiento.text = currentPersona.fechaNacimiento
        holder.btnEditarPersona.setOnClickListener {
            abrirActividadConParametros(EditPersonaInfoActivity::class.java, currentPersona, it, idCasa)
        }
        holder.btnEliminarPersona.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("Estás seguro que lo quieres eliminar?")
            builder.setPositiveButton("Si") { dialog, _ ->
                dialog.dismiss()
                deleteCar(currentPersona.id!!, idCasa)
                this.personaList.remove(currentPersona)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }

    private fun deleteCar(id: String, idCasa: String) {
        val db = Firebase.firestore
        val users = db.collection("casa")
        val carsCollectionsRef = users.document(idCasa).collection("persona")
        carsCollectionsRef.document(id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }



    }

    override fun getItemCount(): Int {
        return this.personaList.size
    }

    private fun abrirActividadConParametros(clase: Class<*>, persona: Persona, it: View?, idCasa: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("personaId", persona!!.id)
        intent.putExtra("personaeNombre", persona!!.nombre)
        intent.putExtra("casaId", idCasa)
        intent.putExtra("personaAltura", persona!!.altura)
        intent.putExtra("personaNacimiento", persona!!.fechaNacimiento)
        intent.putExtra("personaTieneSeguro", persona!!.tieneSeguro)
        contenidoIntentExplicito.launch(intent)
    }

    class MyViewHolderCar(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idPersona = itemView.findViewById<TextView>(R.id.tv_id_persona)
        val nombrePersona = itemView.findViewById<TextView>(R.id.tv_nombre_persona)
        val btnEliminarPersona = itemView.findViewById<Button>(R.id.btn_eliminar_persona)
        val btnEditarPersona = itemView.findViewById<Button>(R.id.btn_editar_persona)
        val altura = itemView.findViewById<TextView>(R.id.tv_altura)
        val tieneSeguro = itemView.findViewById<TextView>(R.id.tv_tiene_seguro)
        val fechaNacimiento = itemView.findViewById<TextView>(R.id.tv_fecha_nacimiento)
    }


}