package br.com.zup.beagle.appiumapp.config.newanalytics

import br.com.zup.beagle.newanalytics.AnalyticsRecord

interface ReportListener {

    fun onReport(report : AnalyticsRecord)
}