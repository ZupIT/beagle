package br.com.zup.beagle.automatedTests.utils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
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