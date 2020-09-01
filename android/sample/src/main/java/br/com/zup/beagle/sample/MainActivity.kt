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
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.sample.activities.NavigationBarActivity
import br.com.zup.beagle.sample.activities.SampleServerDrivenActivity
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
import br.com.zup.beagle.sample.fragment.TabViewFragment
import br.com.zup.beagle.sample.fragment.TextInputFragment
import br.com.zup.beagle.sample.fragment.WebViewFragment
import br.com.zup.beagle.sample.fragment.ContextOperationsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            R.id.contextOperations -> goToFragment(ContextOperationsFragment.newInstance())
            R.id.textInput -> goToFragment(TextInputFragment.newInstance())
            R.id.scroll -> goToFragment(ScrollViewFragment.newInstance())
            R.id.lazycomponent -> goToFragment(LazyComponentFragment.newInstance())
            R.id.image -> goToFragment(ImageViewFragment.newInstance())
            R.id.pageView -> goToFragment(PageViewFragment.newInstance())
            // Navigation Bar requires an activity without toolbar
            R.id.navigationBar -> startActivity(NavigationBarActivity.newIntent(this))
            R.id.navigationFragment -> goToFragment(NavigationFragment.newInstance())
            R.id.form -> goToFragment(FormFragment.newInstance())
            R.id.tabBar -> goToFragment(TabViewFragment.newInstance())
            R.id.disabledFormSubmit -> goToFragment(DisabledFormSubmitFragment.newInstance())
            R.id.listView -> goToFragment(ListViewFragment.newInstance())
            R.id.webView -> goToFragment(WebViewFragment.newInstance())
            R.id.composeComponent -> goToFragment(ComposeComponentFragment.newInstance())
            R.id.sampleBff -> startActivity(
                newServerDrivenIntent<SampleServerDrivenActivity>(ScreenRequest(SAMPLE_ENDPOINT)
                ))
        }
    }

    private fun goToFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_content, fragment)
        fragmentTransaction.commit()
    }
}
