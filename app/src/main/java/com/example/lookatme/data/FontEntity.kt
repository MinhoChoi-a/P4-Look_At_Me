package com.example.lookatme.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lookatme.NEW_FONT_ID
import com.example.lookatme.NEW_SET_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "fonts")
class FontEntity (
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var color: String, //gradient or video

) : Parcelable {
    constructor(): this(NEW_FONT_ID,"")
    constructor(color:String): this(NEW_FONT_ID,color)

}