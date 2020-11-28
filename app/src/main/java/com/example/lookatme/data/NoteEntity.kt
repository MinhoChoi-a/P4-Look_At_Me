package com.example.lookatme.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lookatme.NEW_NOTE_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var text: String,
    var fontColor: String,
    var backColor: String
) : Parcelable {

    constructor(): this(NEW_NOTE_ID,"","","")

}