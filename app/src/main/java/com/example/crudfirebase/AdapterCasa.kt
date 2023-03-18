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


class AdapterCasa(var casaList: ArrayList<Casa>, private val contenidoIntentExplicito: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<AdapterCasa.MyViewHolderUser>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUser {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.casa_item, parent, false)

        return MyViewHolderUser(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolderUser, position: Int) {
        val currentItem = casaList[position]
        holder.idCasa.text = currentItem.id
        holder.superficieCasa.text = currentItem.supercifie.toString()
        holder.direccionCasa.text = currentItem.direccion
        holder.pisos.text = currentItem.pisos.toString()
        holder.tienePatio.text = currentItem.tienePatio.toString()
        holder.btnEditar.setOnClickListener {
            abrirActividadConParametros(EditCasaInfoActivity::class.java, currentItem, it)
        }


        holder.btnEliminar.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmacion de eliminación")
            builder.setMessage("Estás seguro que lo deseas eliminar??")
            builder.setPositiveButton("Si") { dialog, _ ->
                // Delete the item
                dialog.dismiss()
                // Perform the deletion here
                deleteCasa(currentItem.id!!)
                this.casaList.remove(currentItem)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        holder.btnVerPersonas.setOnClickListener {
            abrirActividadConParametros(PersonaListActivity::class.java, currentItem, it)
        }
    }

    private fun deleteCasa(id: String) {
        val db = Firebase.firestore
        val casa = db.collection("casa")
        val casaDoc = casa.document(id)
        casaDoc.delete().addOnSuccessListener {

        }.addOnFailureListener {

        }


    }

    private fun abrirActividadConParametros(clase: Class<*>, casa: Casa, it: View?) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("casaId", casa!!.id)
        intent.putExtra("casaDireccion", casa!!.direccion)
        intent.putExtra("casaSuperficie", casa!!.supercifie)
        intent.putExtra("casaPisos", casa!!.pisos)
        intent.putExtra("casaTienePatio", casa!!.tienePatio)
        contenidoIntentExplicito.launch(intent)
    }

    override fun getItemCount(): Int {
        return casaList.size
    }

    

    class MyViewHolderUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val direccionCasa = itemView.findViewById<TextView>(R.id.tv_direccion_casa)
        val btnEditar = itemView.findViewById<Button>(R.id.btn_editar)
        val btnEliminar = itemView.findViewById<Button>(R.id.btn_eliminar)
        val btnVerPersonas = itemView.findViewById<Button>(R.id.btn_ver_persona)
        val idCasa = itemView.findViewById<TextView>(R.id.tv_id_casa)
        val superficieCasa = itemView.findViewById<TextView>(R.id.tv_superficie_casa)
        val pisos = itemView.findViewById<TextView>(R.id.tv_pisos_casa)
        val tienePatio = itemView.findViewById<TextView>(R.id.tv_tienePatio_casa)
    }


}