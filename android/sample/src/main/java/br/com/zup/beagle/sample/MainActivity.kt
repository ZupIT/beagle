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

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.refresh.PullToRefresh
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.ServerDrivenActivity
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.activities.NavigationBarActivity
import br.com.zup.beagle.sample.constants.SAMPLE_ENDPOINT
import br.com.zup.beagle.sample.fragment.ComposeComponentFragment
import br.com.zup.beagle.sample.fragment.ContextOperationsFragment
import br.com.zup.beagle.sample.fragment.DisabledFormSubmitFragment
import br.com.zup.beagle.sample.fragment.FormFragment
import br.com.zup.beagle.sample.fragment.GridViewFragment
import br.com.zup.beagle.sample.fragment.ImageViewFragment
import br.com.zup.beagle.sample.fragment.LazyComponentFragment
import br.com.zup.beagle.sample.fragment.NavigationFragment
import br.com.zup.beagle.sample.fragment.PageViewFragment
import br.com.zup.beagle.sample.fragment.ScrollViewFragment
import br.com.zup.beagle.sample.fragment.TabViewFragment
import br.com.zup.beagle.sample.fragment.TextInputFragment
import br.com.zup.beagle.sample.fragment.WebViewFragment
import br.com.zup.beagle.sample.fragment.list.ListViewFragment
import br.com.zup.beagle.widget.core.Size
import kotlin.random.Random

class MainActivity : AppCompatActivity(R.layout.activity_main) {

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
            R.id.gridView -> goToFragment(GridViewFragment.newInstance())
            R.id.webView -> goToFragment(WebViewFragment.newInstance())
            R.id.composeComponent -> goToFragment(ComposeComponentFragment.newInstance())
//            R.id.sampleBff -> startActivity(newServerDrivenIntent<ServerDrivenActivity>(RequestData(SAMPLE_ENDPOINT)))
            R.id.sampleBff -> startActivity(newServerDrivenIntent<ServerDrivenActivity>(
                Screen(
                    context = ContextData("listContext", listOf("teste", "teste2")),
                    child = PullToRefresh(
                        context = ContextData("refreshContext", RefreshStatus()),
                        onPull = listOf(
                            SetContext(
                                contextId = "refreshContext",
                                value = RefreshStatus(true, "#0000FF")
                            ),
                            SendRequest(
                                url = "/components",
                                onSuccess = listOf(
                                    SetContext(
                                        contextId = "refreshContext",
                                        value = RefreshStatus(false, "#FFFF00")
                                        //value = false,
                                        //path = "isRefreshing"
                                    ),
                                    SetContext(
                                        contextId = "listContext",
                                        value = "@{onSuccess.data}"
                                    )
                                )
                            )
                        ),
                        isRefreshing = expressionOf("@{refreshContext.isRefreshing}"),
                        color = expressionOf("@{refreshContext.spinColor}"),
                        //child = Container().applyStyle(
                        //   style = Style(
                        //       backgroundColor = "#FF0000",
                        //       size = Size(width = 200.unitReal(), height = 300.unitReal())
                        //   )
                        //)
                    child = ListView(
                        //context = ContextData("listContext", listOf("teste", "teste2")),
                        dataSource = expressionOf("@{listContext}"),
                        templates = listOf(
                            Template(
                                case = null,
                                view = Text(expressionOf("@{item}"))
                            )
                        )
                    ).applyStyle(
                        style = Style(
                                   backgroundColor = "#FF0000",
                                   size = Size(width = 200.unitReal(), height = 300.unitReal())
                              )
                    )
                    )
                )
            ))
        }
    }

    data class RefreshStatus(val isRefreshing: Boolean = false, val spinColor: String = "#000000")

    private fun randomColor() = String.format("#%06x", Random.nextInt(0xFFFFFF))

    private fun goToFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_content, fragment)
        fragmentTransaction.commit()
    }
}
