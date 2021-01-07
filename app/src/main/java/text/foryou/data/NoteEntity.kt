package text.foryou.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import text.foryou.NEW_NOTE_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var text: String,
    var fontStyle: String,
    var fontColor: String,
    var backType: Int,
    var backRes: String

) : Parcelable {

    constructor(): this(NEW_NOTE_ID,"","","", 0, "")
    constructor(text:String, fs:String, fc:String, bt:Int, br:String): this(NEW_NOTE_ID,text,fs,fc,bt,br)
}