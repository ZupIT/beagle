package br.com.zup.beagle.appiumapp.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.appiumapp.R

class BffDeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data: Uri? = intent?.data
        val bffUrl = data.toString().substringAfter("appiumapp://bffurl/")

        val intent = this.newServerDrivenIntent<AppBeagleActivity>(ScreenRequest(bffUrl))
        startActivity(intent)
        finish()
    }
}