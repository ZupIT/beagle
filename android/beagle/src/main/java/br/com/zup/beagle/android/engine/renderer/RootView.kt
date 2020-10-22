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

package br.com.zup.beagle.android.engine.renderer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import br.com.zup.beagle.android.widget.RootView

/**
 * The FragmentRootView holder the reference of a fragment.
 *
 * @param fragment that is the parent of a view.
 * @param parentId parent view id.
 */
class FragmentRootView(
    val fragment: Fragment,
    private val parentId: Int
) : RootView {

    override fun getContext(): Context = fragment.requireContext()

    override fun getLifecycleOwner(): LifecycleOwner = fragment.viewLifecycleOwner

    override fun getViewModelStoreOwner(): ViewModelStoreOwner = fragment

    override fun getParentId(): Int = parentId
}

/**
 * The ActivityRootView holder the reference of activity.
 *
 * @param activity that is the parent of a view.
 * @param parentId parent view id.
 */
class ActivityRootView(
    val activity: AppCompatActivity,
    private val parentId: Int
) : RootView {

    override fun getContext(): Context = activity

    override fun getLifecycleOwner(): LifecycleOwner = activity

    override fun getViewModelStoreOwner(): ViewModelStoreOwner = activity

    override fun getParentId(): Int = parentId
}
