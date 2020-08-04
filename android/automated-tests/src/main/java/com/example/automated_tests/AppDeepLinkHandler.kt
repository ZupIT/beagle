package com.example.automated_tests

import android.content.Intent
import br.com.zup.beagle.android.annotation.BeagleComponent
import br.com.zup.beagle.android.navigation.DeepLinkHandler

@BeagleComponent
class AppDeepLinkHandler : DeepLinkHandler {
    override fun getDeepLinkIntent(
        path: String,
        data: Map<String, String>?,
        shouldResetApplication: Boolean
    ): Intent = Intent(path)
}