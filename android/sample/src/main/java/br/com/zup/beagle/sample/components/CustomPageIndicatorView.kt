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
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.sample.R
import kotlinx.android.synthetic.main.custom_page_indicator.view.btContinue
import kotlinx.android.synthetic.main.custom_page_indicator.view.btBack

typealias OnIndexChanged = (index: Int) -> Unit

class CustomPageIndicatorView(context: Context) : RelativeLayout(context) {

    private val selectedColor: Int = Color.WHITE
    private val unselectedColor: Int = Color.GRAY
    private var selectedItem = 0
    private var pagesCount = 0
    private val llPageIndicator: LinearLayout
    private var onIndexChanged: OnIndexChanged? = null

    init {
        inflate(context, R.layout.custom_page_indicator, this)
        llPageIndicator = findViewById(R.id.llPageIndicator)
        bind()
    }

    private fun bind() {
        val btBack = findViewById<Button>(R.id.btBack)
        val btContinue = findViewById<Button>(R.id.btContinue)
        var newIndex: Int

        btContinue.setOnClickListener {
            newIndex = selectedItem + 1
            if (newIndex < pagesCount) {
                setCurrentIndex(newIndex)
                onIndexChanged?.invoke(newIndex)
            }
        }

        btBack.setOnClickListener {
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

            llPageIndicator.addView(dot)

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
        (llPageIndicator.getChildAt(selectedItem) as ImageView).setColorFilter(unselectedColor)
        (llPageIndicator.getChildAt(newIndex) as ImageView).setColorFilter(selectedColor)
        selectedItem = newIndex
    }

    fun setIndexChangedListener(onIndexChanged: OnIndexChanged) {
        this.onIndexChanged = onIndexChanged
    }

    fun setContinueButtonVisibility(visibility: Int) {
        btContinue.visibility = visibility
    }

    fun setBackButtonVisibility(visibility: Int) {
        btBack.visibility = visibility
    }
}
