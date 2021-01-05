package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsConfig

class AnalyticsConfigImpl(
    override var enableScreenAnalytics: Boolean? = true,
    override var actions: Map<String, List<String>>? = hashMapOf("beagle:alert" to listOf("title"), "beagle:confirm" to listOf("title"))
) : AnalyticsConfig