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

package br.com.zup.beagle.sample.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MaskApplier(
    private val editText: EditText,
    private val mask: String
) : TextWatcher {

    private var isUpdating: Boolean = false
    private var old = ""
    private var beforeEdit: String = ""
    private var afterEdit: String = ""

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeEdit = editText.text.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        afterEdit = editText.text.toString()
        if (beforeEdit.length <= afterEdit.length) {
            var selectionPlus = 0
            val selection = editText.selectionStart
            val str = unmask(s.toString())
            var mascara = ""
            if (isUpdating) {
                old = str
                isUpdating = false
                return
            }
            var i = 0
            for (m in mask.toCharArray()) {
                if (str.length + selectionPlus <= i)
                    break

                if (m == '#') {
                    mascara += str.get(i - selectionPlus)
                } else {
                    mascara += StringBuilder().append(m)
                    selectionPlus++
                }
                i++
            }
            isUpdating = true
            beforeEdit = mascara
            editText.setText(mascara)

            editText.setSelection(
                if (selection + selectionPlus > mascara.length)
                    mascara.length else selection + selectionPlus
            )
        }
    }

    private fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
            .replace("[)]".toRegex(), "")
    }
}