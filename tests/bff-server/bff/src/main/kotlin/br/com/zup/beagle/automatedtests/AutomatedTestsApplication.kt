package br.com.zup.beagle.automatedtests

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class AutomatedTestsApplication

fun main(args: Array<String>) {
	runApplication<AutomatedTestsApplication>(*args)
}
