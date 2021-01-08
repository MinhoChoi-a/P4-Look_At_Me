package text.foryou.data

import text.foryou.data.model.BackgroundEntity
import text.foryou.data.model.FontColorEntity
import text.foryou.data.model.FontStyleEntity
import text.foryou.data.model.NoteEntity

class DefaultSetProvider {

    //working like static values in java
    companion object {

        fun getSettings() = arrayListOf(
                BackgroundEntity(1, "White", "back_moonwhite", "back_moonwhite"),
                BackgroundEntity(1, "Simple Black", "back_simpleblack", "back_simpleblack"),
                BackgroundEntity(2, "Sky Blue", "back_grad1", "back_grad1_start"),
                BackgroundEntity(2, "Illumina", "back_grad2", "back_grad2_start"),
                BackgroundEntity(2, "Warm Heart", "back_grad3", "back_grad3_start"),
                BackgroundEntity(3, "Snow Flakes", "snowflakes", "snowflakes"),
                BackgroundEntity(3, "Fire Work", "fireworks", "firework"),
                BackgroundEntity(3, "Flames", "flames", "flame")
        )

        fun getFontColor() = arrayListOf(

                FontColorEntity("black", "Black"),
                FontColorEntity("white", "White"),
                FontColorEntity("second_c", "Yellow"),
                FontColorEntity("third_c", "Bold Yellow"),
                FontColorEntity("fourth_c", "Light Orange"),
                FontColorEntity("fifth_c", "Free Red"),
                FontColorEntity("sixth_c", "Wine Red"),
                FontColorEntity("teal_700", "Teal Blue"),
                FontColorEntity("purple_700", "Ultra Marine"),
                FontColorEntity("purple_200", "Wisteria")
        )



        fun getFontStyle() = arrayListOf(

                FontStyleEntity("robotoblack", "Roboto"),
                FontStyleEntity("annietelescope", "Annie Telescope"),
                FontStyleEntity("dancingscript", "Dancing Script"),
                FontStyleEntity("alice", "Alice Regular"),
                FontStyleEntity("bungee", "Bungee Bold"),
                FontStyleEntity("eastseadokdo", "East Sea Dokdo"),
                FontStyleEntity("nerkoone", "Nerko One"),
                FontStyleEntity("shiningstar", "Shining Star")

        )

        fun getNotes() = arrayListOf(
                NoteEntity("Hello World", "robotoblack", "third_c", 2, "back_grad1", ),
                NoteEntity("White Christmas", "annietelescope", "white", 3, "snowflakes", ),
                NoteEntity("I Love You", "dancingscript", "purple_200", 2, "back_grad3", )
        )
    }
}