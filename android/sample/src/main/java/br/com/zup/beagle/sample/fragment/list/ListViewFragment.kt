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

package br.com.zup.beagle.sample.fragment.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ServerDrivenActivity
import br.com.zup.beagle.sample.R
import kotlinx.android.synthetic.main.fragment_list_view.*

class ListViewFragment : Fragment(R.layout.fragment_list_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btDeprecatedList.setOnClickListener {
            startActivity(Intent(context, DeprecatedListViewActivity::class.java))
        }
        btLocalList.setOnClickListener {
            startActivity(Intent(context, ListViewActivity::class.java))
        }
        btNestedList.setOnClickListener {
            startActivity(
                context?.newServerDrivenIntent<ServerDrivenActivity>(
                    ScreenRequest("https://storage.googleapis.com/lucasaraujo/dev/listview.json")
                )
            )
        }
        btNestedImageList.setOnClickListener {
            startActivity(
                context?.newServerDrivenIntent<ServerDrivenActivity>(
                    ScreenRequest("https://run.mocky.io/v3/9df55f30-9c82-4837-988d-f3d751d6f4e6")
                )
            )
        }
    }

    companion object {
        fun newInstance() = ListViewFragment()
    }
}
