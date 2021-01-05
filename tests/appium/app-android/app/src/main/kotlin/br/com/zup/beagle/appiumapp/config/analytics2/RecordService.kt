package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsRecord

object RecordService {

    private lateinit var reportListener : ReportListener
    private lateinit var report : AnalyticsRecord
    
    fun setListener(reportListener: ReportListener){
        this.reportListener = reportListener
        if(this::report.isInitialized){
            reportListener.onReport(report)
        }

    }

    fun saveReport(report : AnalyticsRecord){
        if(this::reportListener.isInitialized){
            reportListener.onReport(report)
        }
        else{
            this.report = report
        }
    }
}