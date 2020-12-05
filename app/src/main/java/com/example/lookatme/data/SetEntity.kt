package com.example.lookatme.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lookatme.NEW_SET_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "settings")
data class SetEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var type: Int, //gradient or video
        var title: String?,
        var res: String?

) : Parcelable {
    constructor(): this(NEW_SET_ID,0,"","")
    constructor(type:Int, title:String, res: String): this(NEW_SET_ID,type,title,res)

}