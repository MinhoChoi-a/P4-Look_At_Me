package text.foryou.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import text.foryou.NEW_SET_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "settings")
data class BackgroundEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var type: Int, //gradient or video
        var title: String?,
        var res: String?,
        var backImage: String?

) : Parcelable {
    constructor(): this(NEW_SET_ID,0,"","","")
    constructor(type:Int, title:String, res: String, image:String): this(NEW_SET_ID,type,title,res,image)

}