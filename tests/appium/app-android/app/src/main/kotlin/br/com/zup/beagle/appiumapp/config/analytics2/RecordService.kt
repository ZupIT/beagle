package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsRecord

object RecordService {

    private lateinit var reportListener : ReportListener

    fun setListener(reportListener: ReportListener){
        this.reportListener = reportListener
    }

    fun saveReport(report : AnalyticsRecord){
        if(this::reportListener.isInitialized){
            reportListener.onReport(report)
        }
    }
}