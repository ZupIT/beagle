package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsRecord

interface ReportListener {

    fun onReport(report : AnalyticsRecord)
}