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

package br.com.zup.beagle.sample.components

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.sample.R
import br.com.zup.beagle.sample.databinding.CustomPageIndicatorBinding

typealias OnIndexChanged = (index: Int) -> Unit

class CustomPageIndicatorView(context: Context) : RelativeLayout(context) {

    private val selectedColor: Int = Color.WHITE
    private val unselectedColor: Int = Color.GRAY
    private var selectedItem = 0
    private var pagesCount = 0
    private var onIndexChanged: OnIndexChanged? = null
    private val binding: CustomPageIndicatorBinding = CustomPageIndicatorBinding.inflate(LayoutInflater.from(context))


    init {
        addView(binding.root)
        bind()
    }

    private fun bind() {
        var newIndex: Int

        binding.btContinue.setOnClickListener {
            newIndex = selectedItem + 1
            if (newIndex < pagesCount) {
                setCurrentIndex(newIndex)
                onIndexChanged?.invoke(newIndex)
            }
        }

        binding.btBack.setOnClickListener {
            newIndex = selectedItem - 1
            if (newIndex >= 0) {
                setCurrentIndex(newIndex)
                onIndexChanged?.invoke(newIndex)
            }
        }
    }

    fun setCount(pages: Int) {
        for (i in 0 until pages) {
            val dot = ImageView(context)
            dot.id = i
            dot.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_default))
            if (i == 0) {
                dot.setColorFilter(selectedColor)
            } else {
                dot.setColorFilter(unselectedColor)
            }

            binding.llPageIndicator.addView(dot)

            val dotParams = dot.layoutParams as LinearLayout.LayoutParams
            val defaultSize = 10.dp()
            val horizontalMargin = 6.dp()
            val verticalMargin = 6.dp()
            dotParams.height = defaultSize
            dotParams.width = defaultSize
            dotParams.setMargins(
                horizontalMargin,
                verticalMargin,
                horizontalMargin,
                verticalMargin
            )
            dot.layoutParams = dotParams
        }
        pagesCount = pages
    }

    fun setCurrentIndex(newIndex: Int) {
        (binding.llPageIndicator.getChildAt(selectedItem) as ImageView).setColorFilter(unselectedColor)
        (binding.llPageIndicator.getChildAt(newIndex) as ImageView).setColorFilter(selectedColor)
        selectedItem = newIndex
    }

    fun setIndexChangedListener(onIndexChanged: OnIndexChanged) {
        this.onIndexChanged = onIndexChanged
    }

    fun setContinueButtonVisibility(visibility: Int) {
        binding.btContinue.visibility = visibility
    }

    fun setBackButtonVisibility(visibility: Int) {
        binding.btBack.visibility = visibility
    }
}
