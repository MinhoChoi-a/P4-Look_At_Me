package com.example.lookatme.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lookatme.NEW_FONT_ID
import com.example.lookatme.NEW_FONT_STYLE_ID
import com.example.lookatme.NEW_SET_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "fontstyle")
class FontStyleEntity (
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var style: String,
        var name: String,

) : Parcelable {
    constructor(): this(NEW_FONT_STYLE_ID,"","")
    constructor(style:String, name:String): this(NEW_FONT_STYLE_ID,style, name)

}