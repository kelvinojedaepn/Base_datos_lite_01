package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Persona(
    val id: String? = null,
    var nombre: String? = null,
    var altura: Double?= 0.0,
    var fechaNacimiento: String?= "",
    var tieneSeguro: Boolean? = false

):Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readBoolean()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        altura?.let { parcel.writeDouble(it) }
        tieneSeguro?.let { parcel.writeBoolean(it) }
    }
    companion object CREATOR : Parcelable.Creator<Persona> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Persona {
            return Persona(parcel)
        }

        override fun newArray(size: Int): Array<Persona?> {
            return arrayOfNulls(size)
        }
    }
}