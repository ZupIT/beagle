package br.com.zup.beagle.appiumapp.config.newanalytics

import br.com.zup.beagle.newanalytics.AnalyticsConfig

class AnalyticsConfigImpl(
    override var enableScreenAnalytics: Boolean? = true,
    override var actions: Map<String, List<String>>? = hashMapOf(
        "beagle:confirm" to listOf(
            "title",
            "message"
        )
    )
) : AnalyticsConfig