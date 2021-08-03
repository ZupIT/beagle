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

package br.com.zup.beagle.android.components.form.observer

import br.com.zup.beagle.android.components.form.core.Constants
import java.util.Vector

@Deprecated(Constants.FORM_DEPRECATED_MESSAGE)
class Observable<T> {
    private var changed = false
    private val obs: Vector<Observer<T>> = Vector()
    private var currentValue: T? = null

    @Synchronized
    fun addObserver(o: Observer<T>) {
        if (!obs.contains(o)) {
            obs.addElement(o)
        }

        if (hasChanged()) {
            currentValue?.let {
                notifyObservers(it)
            }
        }
    }

    @Synchronized
    fun deleteObserver(o: Observer<T>) {
        obs.removeElement(o)
    }

    fun notifyObservers(arg: T) {
        currentValue = arg
        setChanged()
        val arrLocal: Array<Any>

        synchronized(this) {
            if (!changed)
                return
            arrLocal = obs.toArray()
            clearChanged()
        }

        for (i in arrLocal.indices.reversed())
            (arrLocal[i] as Observer<T>).update(this, arg)
    }

    @Synchronized
    fun deleteObservers() {
        obs.removeAllElements()
    }

    @Synchronized
    private fun setChanged() {
        changed = true
    }

    @Synchronized
    private fun clearChanged() {
        changed = false
    }

    @Synchronized
    fun hasChanged(): Boolean {
        return changed
    }

    @Synchronized
    fun countObservers(): Int {
        return obs.size
    }

    @Synchronized
    fun getCurrentValue(): T? = currentValue
}
