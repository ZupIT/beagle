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

package br.com.zup.beagle.android.view.custom

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.ViewGroup

internal class BeagleTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int
) : TabLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val tabLayout = getChildAt(0) as ViewGroup
        val childCount = tabLayout.childCount

        if (childCount != 0) {
            val displayMetrics = context.resources.displayMetrics
            val tabMinWidth = displayMetrics.widthPixels / childCount

            for (i in 0 until childCount) {
                tabLayout.getChildAt(i).minimumWidth = tabMinWidth
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
