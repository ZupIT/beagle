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

package br.com.zup.beagle.engine.renderer.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.BeagleServiceWrapper
import br.com.zup.beagle.data.FetchDataListener
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.UIViewRenderer
import br.com.zup.beagle.expression.Array
import br.com.zup.beagle.expression.JsonParser
import br.com.zup.beagle.expression.ModelValueBindingAdapter
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.ui.ListDirection
import br.com.zup.beagle.widget.ui.ListViewBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

internal class ListViewBindingRenderer(
    override val component: ListViewBinding,
    private val beagleService: BeagleServiceWrapper = BeagleServiceWrapper(),
    private val viewFactory: ViewFactory = ViewFactory()
) : UIViewRenderer<ListViewBinding>() {

    override fun buildView(rootView: RootView): View {
        val flex = Flex(size = Size(height = component.flex?.size?.height ?: 100.unitPercent()))

        return viewFactory.makeBeagleFlexView(rootView.getContext()).apply {
            addView(viewFactory.makeRecyclerView(rootView.getContext()).apply {
                val orientation = toRecyclerViewOrientation()

                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = orientation

                this.layoutManager = layoutManager

                val listViewAdapter = ListViewBindingRecyclerAdapter(
                    rootView,
                    viewFactory,
                    orientation,
                    component
                )

                adapter = listViewAdapter

                when {
                    component.listJson.isNullOrBlank().not() -> {
                        val array = JsonParser().parseJsonToArray(component.listJson ?: "")
                        setAdapterData(array, listViewAdapter)
                    }
                    component.listPath.isNullOrBlank().not() -> {
                        beagleService.fetchData(
                            ScreenRequest(url = component.listPath ?: ""),
                            object : FetchDataListener {
                                override fun onSuccess(json: String) {
                                    val array = JsonParser().parseJsonToArray(json)

                                    setAdapterData(array, listViewAdapter)
                                }

                                override fun onError(error: Throwable) {
                                    //TODO understand what to do with this error
                                    error.printStackTrace()
                                }
                            })
                    }
                    else -> {
                        throw IllegalStateException("Model for the listview not provided")
                    }
                }
            }, flex)

        }
    }

    private fun setAdapterData(array: Array, listViewAdapter: ListViewBindingRecyclerAdapter) {
        listViewAdapter.setData(array)
    }

    private fun toRecyclerViewOrientation() = if (component.direction == ListDirection.VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }
}

internal class ListViewBindingRecyclerAdapter(
    private val rootView: RootView,
    private val viewFactory: ViewFactory,
    private val orientation: Int,
    private val listViewBinding: ListViewBinding,
    private val modelValueBindingAdapter: ModelValueBindingAdapter = ModelValueBindingAdapter()
) : RecyclerView.Adapter<ViewBindingHolder>() {

    private val rowsArray = Array()

    fun setData(array: Array) {
        rowsArray.clear()
        rowsArray.addAll(array)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewBindingHolder {
        //Clones the themplate to avoid all the lines having the same values
        val templateCopy = listViewBinding.child.clone()
        val view = viewFactory.makeBeagleFlexView(
            rootView.getContext()
        ).also {
            val width = if (orientation == RecyclerView.VERTICAL)
                ViewGroup.LayoutParams.MATCH_PARENT else
                ViewGroup.LayoutParams.WRAP_CONTENT
            val layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.layoutParams = layoutParams

            it.addServerDrivenComponent(templateCopy, rootView)
        }
        return ViewBindingHolder(view, templateCopy)
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        //Get the row value for the position and triggers the evaluateBinding for the current template copy
        modelValueBindingAdapter.evaluateBinding(rowsArray[position], holder.dataBindingComponent)
    }

    override fun getItemCount(): Int = rowsArray.size()

}

//TODO Extract this function to somewhere else
@Suppress("UNCHECKED_CAST")
fun <T> T.clone(): T {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ObjectOutputStream(byteArrayOutputStream).use { outputStream ->
        outputStream.writeObject(this)
    }

    val bytes = byteArrayOutputStream.toByteArray()

    ObjectInputStream(ByteArrayInputStream(bytes)).use { inputStream ->
        return inputStream.readObject() as T
    }
}

internal class ViewBindingHolder(
    val view: View,
    val dataBindingComponent: ServerDrivenComponent
) : RecyclerView.ViewHolder(view)