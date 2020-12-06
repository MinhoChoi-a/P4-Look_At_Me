package com.example.lookatme.data

import java.util.*

class DefaultSetProvider {

    //working like static values in java
    companion object {

        fun getSettings() = arrayListOf(
                SetEntity(1, "Moon White","back_moonwhite"),
                SetEntity(1, "Simple Black", "back_simpleblack"),
                SetEntity(2, "Gradient 1", "back_grad1"),
                SetEntity(2, "Gradient 2", "back_grad2"),
                SetEntity(2, "Gradient 3", "back_grad3"),
                SetEntity(3, "Starry Night", "starry")
        )

        fun getFontColor() = arrayListOf(

                FontEntity("black"),
                FontEntity("white"),
                FontEntity("second_c"),
                FontEntity("third_c"),
                FontEntity("fourth_c"),
                FontEntity("fifth_c"),
                FontEntity("sixth_c")
        )
    }
}