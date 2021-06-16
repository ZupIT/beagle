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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.utils.newServerDrivenIntent
import br.com.zup.beagle.android.view.ServerDrivenActivity
import br.com.zup.beagle.sample.databinding.FragmentListViewBinding

class ListViewFragment : Fragment() {

    private var _binding: FragmentListViewBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btDeprecatedList.setOnClickListener {
            startActivity(Intent(context, DeprecatedListViewActivity::class.java))
        }
        binding.btLocalList.setOnClickListener {
            startActivity(Intent(context, ListViewActivity::class.java))
        }
        binding.btNestedList.setOnClickListener {
            startActivity(
                context?.newServerDrivenIntent<ServerDrivenActivity>(
                    RequestData("https://storage.googleapis.com/lucasaraujo/dev/listview.json")
                )
            )
        }
        binding.btNestedImageList.setOnClickListener {
            startActivity(
                context?.newServerDrivenIntent<ServerDrivenActivity>(
                    RequestData("https://run.mocky.io/v3/9aa31d46-15c8-4bdf-95a3-01491e468846")
                )
            )
        }
    }

    companion object {
        fun newInstance() = ListViewFragment()
    }
}
