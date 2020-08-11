package com.example.automated_tests.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.automated_tests.utils.matcher.MatcherExtension


object WaitHelper {

    fun waitForWithId(id : Int){
        var element : ViewInteraction
        do{
            element = onView(withId(id))
        } while (!MatcherExtension.exists(element))
    }

    fun waitForWithElement(element : ViewInteraction){
        if(MatcherExtension.exists(element))
            return
        else
            waitForWithElement(element)
    }
}