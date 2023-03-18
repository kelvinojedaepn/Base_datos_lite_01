package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Casa(
    val id: String?=null,
    var direccion: String? = null,
    var supercifie: Double? = 0.0,
    var pisos: Int? = 0,
    var tienePatio: Boolean? = false,
    ): Parcelable{
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readBoolean()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(direccion)
        supercifie?.let { parcel.writeDouble(it) }
        pisos?.let { parcel.writeInt(it) }
        tienePatio?.let { parcel.writeBoolean(it) }
    }

    companion object CREATOR : Parcelable.Creator<Casa> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Casa {
            return Casa(parcel)
        }

        override fun newArray(size: Int): Array<Casa?> {
            return arrayOfNulls(size)
        }
    }
}
