package br.com.zup.beagle.appiumapp.config.analytics2

import br.com.zup.beagle.analytics2.AnalyticsRecord
import java.util.ArrayList

object RecordService {

    private lateinit var reportListener: ReportListener
    private var report: ArrayList<AnalyticsRecord> = ArrayList()

    fun setListener(reportListener: ReportListener) {
        this.reportListener = reportListener
        if(report.size > 1){
            val reportToShow = report.get(report.size - 1)
            reportListener.onReport(reportToShow)
            report.remove(reportToShow)
        }
    }

    fun saveReport(report: AnalyticsRecord) {
        this.report.add(report)
        if(this::reportListener.isInitialized){
            reportListener.onReport(report)
        }
    }
}