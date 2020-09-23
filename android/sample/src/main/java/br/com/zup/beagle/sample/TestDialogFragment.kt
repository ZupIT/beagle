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

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

interface ClickInterface {
    fun clickOk()
}
class TestDialogFragment: DialogFragment {
//    private val dialogViewModel: TestDialogFragmentViewModel by viewModels({ requireActivity() })
//    private val dialogViewModel: TestDialogFragmentViewModel = ViewModelProvider(this.requireParentFragment().requireActivity()).get(TestDialogFragmentViewModel::class.java)

    lateinit var clickInterface: ClickInterface
    constructor()
    constructor(click: ClickInterface) {
        clickInterface = click
    }

    companion object {
        fun newInstance(clickInterface: ClickInterface): TestDialogFragment {
            return TestDialogFragment(clickInterface)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("Test")
            .setMessage("this is a test")
            .setPositiveButton("OK") { _, _ ->
                clickInterface.clickOk()
            }
            .create()
    }
}