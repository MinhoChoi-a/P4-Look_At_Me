package com.example.lookatme.data

class DefaultSetProvider {

    //working like static values in java
    companion object {

        fun getSettings() = arrayListOf(
                BackgroundEntity(1, "Moon White","back_moonwhite"),
                BackgroundEntity(1, "Simple Black", "back_simpleblack"),
                BackgroundEntity(2, "Gradient 1", "back_grad1"),
                BackgroundEntity(2, "Gradient 2", "back_grad2"),
                BackgroundEntity(2, "Gradient 3", "back_grad3"),
                BackgroundEntity(3, "Starry Night", "starry")
        )

        fun getFontColor() = arrayListOf(

                FontColorEntity("black", "Black"),
                FontColorEntity("white", "White"),
                FontColorEntity("second_c", "Yellow"),
                FontColorEntity("third_c", "Bold Yellow"),
                FontColorEntity("fourth_c", "Nothing"),
                FontColorEntity("fifth_c"," TBA"),
                FontColorEntity("sixth_c", "TBA2")
        )

        fun getFontStyle() = arrayListOf(

            FontStyleEntity("robotoblack", "Roboto"),
            FontStyleEntity("annietelescope", "Annie Telescope"),
            FontStyleEntity("dancingscript", "Dancing Script")

        )

        fun getNotes() = arrayListOf(
                NoteEntity("Hello World", "robotoblack", "white", 2, "back_grad1",),
                NoteEntity("Starry Starry Night", "annietelescope", "second_c", 3, "starry",),
                NoteEntity("I Love You", "dancingscript", "white", 2, "back_grad3",)
        )
    }
}