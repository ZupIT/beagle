package br.com.zup.beagle.automatedTests.utils

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import br.com.zup.beagle.automatedTests.utils.matcher.MatcherExtension


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