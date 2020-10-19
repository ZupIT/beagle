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

package br.com.zup.beagle.android.widget

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelStoreOwner
import android.content.Context

/**
 * Interface RootView holder the reference of activity or fragment.
 */
interface RootView {
    /**
     * Returns the application context.
     */
    fun getContext(): Context

    /**
     * Returns A class that has an Android lifecycle.
     */
    fun getLifecycleOwner(): LifecycleOwner

    /**
     *  Retrieve a ViewModelStore for activities and fragments.
     */
    fun getViewModelStoreOwner(): ViewModelStoreOwner

    /**
     * Returns the parent id of View that encapsulates all the content rendered by server-driven.
     */
    fun getParentId(): Int
}
