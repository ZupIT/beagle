package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsProvider
import br.com.zup.beagle.analytics2.AnalyticsRecord
import br.com.zup.beagle.analytics2.analyticsConfig
import br.com.zup.beagle.analytics2.startSession
import br.com.zup.beagle.android.annotation.BeagleComponent

@BeagleComponent
class AnalyticsProviderImpl : AnalyticsProvider {
    override fun getConfig(config: analyticsConfig) {
        config.invoke(AnalyticsConfigImpl())
    }

    override fun startSession(startSession: startSession) {
        startSession.invoke()
    }

    override fun createRecord(record: AnalyticsRecord) {
        RecordService.saveReport(record)
    }
}