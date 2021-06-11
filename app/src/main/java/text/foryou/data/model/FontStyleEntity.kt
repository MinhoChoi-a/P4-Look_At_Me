package text.foryou.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import text.foryou.NEW_FONT_STYLE_ID
import kotlinx.android.parcel.Parcelize

//Parcelize: better way to serialize data on inter process communication
@Parcelize
@Entity(tableName = "fontstyle")
class FontStyleEntity (
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var style: String,
        var name: String,

) : Parcelable {
    constructor(): this(NEW_FONT_STYLE_ID,"","")
    constructor(style:String, name:String): this(NEW_FONT_STYLE_ID, style, name)

}