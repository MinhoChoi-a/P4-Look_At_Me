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
                "#1B1E23", "#FFFFF", "#EFFD5F", "#FDE64B", "F9A602",  "#D30000", "#8D021F"
        )
    }

}