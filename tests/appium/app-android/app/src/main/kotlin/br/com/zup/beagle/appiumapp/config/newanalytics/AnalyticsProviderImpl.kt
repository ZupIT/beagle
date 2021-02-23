package br.com.zup.beagle.appiumapp.config.newanalytics

import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.newanalytics.AnalyticsProvider
import br.com.zup.beagle.newanalytics.AnalyticsRecord

@BeagleComponent
class AnalyticsProviderImpl : AnalyticsProvider {
    override fun getConfig() = AnalyticsConfigImpl()

    override fun createRecord(record: AnalyticsRecord) {
        RecordService.saveReport(record)
    }
}
