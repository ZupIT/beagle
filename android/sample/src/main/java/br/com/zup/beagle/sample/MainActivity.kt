/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.sample.activities.NavigationBarActivity
import br.com.zup.beagle.sample.constants.SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.fragment.ComposeComponentFragment
import br.com.zup.beagle.sample.fragment.DisabledFormSubmitFragment
import br.com.zup.beagle.sample.fragment.FormFragment
import br.com.zup.beagle.sample.fragment.ImageViewFragment
import br.com.zup.beagle.sample.fragment.LazyComponentFragment
import br.com.zup.beagle.sample.fragment.ListViewFragment
import br.com.zup.beagle.sample.fragment.NavigationFragment
import br.com.zup.beagle.sample.fragment.PageViewFragment
import br.com.zup.beagle.sample.fragment.ScrollViewFragment
import br.com.zup.beagle.sample.fragment.StackViewFragment
import br.com.zup.beagle.sample.fragment.TabViewFragment
import br.com.zup.beagle.sample.fragment.TextFieldFragment
import br.com.zup.beagle.sample.fragment.WebViewFragment
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ScreenRequest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            title = "Beagle Sample"
            elevation = 4.0f.dp()
        }
        startActivity(BeagleActivity.newIntent(
            this,
           screen = Screen(
               child = Button(text = "asdasd")
           )
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuSelected(itemSelected = item.itemId)
        return super.onOptionsItemSelected(item)
    }

    @Suppress("ComplexMethod")
    private fun menuSelected(itemSelected: Int) {
        when (itemSelected) {
            R.id.textField -> goToFragment(TextFieldFragment.newInstance())
            R.id.scroll -> goToFragment(ScrollViewFragment.newInstance())
            R.id.lazycomponent -> goToFragment(LazyComponentFragment.newInstance())
            R.id.image -> goToFragment(ImageViewFragment.newInstance())
            R.id.pageView -> goToFragment(PageViewFragment.newInstance())
            // Navigation Bar requires an activity without toolbar
            R.id.navigationBar -> startActivity(NavigationBarActivity.newIntent(this))
            R.id.navigationFragment -> goToFragment(NavigationFragment.newInstance())
            R.id.navigation -> startActivity(
                BeagleActivity.newIntent(
                    this,
                    ScreenRequest("https://t001-2751a.firebaseapp.com/flow/step1.json")
                )
            )
            R.id.form -> goToFragment(FormFragment.newInstance())
            R.id.stack -> goToFragment(StackViewFragment.newInstance())
            R.id.tabBar -> goToFragment(TabViewFragment.newInstance())
            R.id.disabledFormSubmit -> goToFragment(DisabledFormSubmitFragment.newInstance())
            R.id.accessibility -> startActivity(BeagleActivity.newIntent(
                this,
                ScreenRequest("http://www.mocky.io/v2/5e4d46952d0000339ec0dce1")
            ))
            R.id.listView -> goToFragment(ListViewFragment.newInstance())
            R.id.webView -> goToFragment(WebViewFragment.newInstance())
            R.id.composeComponent -> goToFragment(ComposeComponentFragment.newInstance())
            R.id.sampleBff -> startActivity(BeagleActivity.newIntent(
                this,
                ScreenRequest(SAMPLE_ENDPOINT)
            ))
        }
    }

    private fun goToFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_content, fragment)
        fragmentTransaction.commit()
    }
}
