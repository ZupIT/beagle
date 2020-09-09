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

package br.com.zup.beagle.android.components

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.OnActionFinished
import br.com.zup.beagle.android.action.OnInitFinishedListener
import br.com.zup.beagle.android.action.OnInitableComponent
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.getContextData
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.utils.setContextData
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.MultiChildComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.widget.core.FlexDirection.COLUMN
import br.com.zup.beagle.widget.core.FlexDirection.ROW
import br.com.zup.beagle.widget.core.ListDirection

@RegisterWidget
data class ListView(
    override val context: ContextData? = null,
    override var onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>,
    val direction: ListDirection,
    val template: ServerDrivenComponent,
    val onScrollEnd: List<Action>? = null,
    val scrollThreshold: Int? = null,
    val useParentScroll: Boolean = false,
    val iteratorName: String = "item"

) : WidgetView(), ContextComponent, OnInitableComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    @Transient
    private lateinit var contextAdapter: ListViewContextAdapter2

    @Transient
    private var list: List<Any>? = null

    @Transient
    private var onInitDone = false

    @Transient
    private var onInitFinishedListener: OnInitFinishedListener? = null

    @Transient
    private lateinit var recyclerView: RecyclerView

    override fun buildView(rootView: RootView): View {
        val orientation = listDirectionToRecyclerViewOrientation()
        recyclerView = viewFactory.makeRecyclerView(rootView.getContext())
        setupRecyclerView(recyclerView, rootView, template, orientation)
        configDataSourceObserver(rootView, recyclerView)
        configRecyclerViewScrollListener(recyclerView, rootView)
        registerOnAttachStateChangeListener(recyclerView, rootView)

        return recyclerView
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        rootView: RootView,
        template: ServerDrivenComponent,
        orientation: Int
    ) {
        contextAdapter = ListViewContextAdapter2(template, orientation, rootView, iteratorName)
        recyclerView.apply {
            adapter = contextAdapter
            layoutManager = LinearLayoutManager(context, orientation, false)
            isNestedScrollingEnabled = useParentScroll
        }
    }

    private fun registerOnAttachStateChangeListener(recyclerView: RecyclerView, rootView: RootView) {
        var onInitCalled = false
        recyclerView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
            }

            override fun onViewAttachedToWindow(v: View?) {
                if (v == recyclerView) {
                    if (!onInitCalled) {
                        executeOnInit(rootView, onInitFinishedListener)
                        onInitCalled = true
                    }
                }
            }

        })
    }

    override fun executeOnInit(rootView: RootView, listener: OnInitFinishedListener?) {
        onInitFinishedListener = listener
        val onInitActions = onInit?.toMutableList()
        onInit?.forEach { action ->
            action.execute(rootView, recyclerView, object : OnActionFinished {
                override fun onActionFinished(action: Action) {
                    onInitActions?.remove(action)
                    if (onInitActions?.isEmpty() == true) {
                        onInitDone = true
                        onInitFinishedListener?.onInitFinished(this@ListView)
                    }
                }

            })
        }
    }

    override fun addOnInitFinishedListener(listener: OnInitFinishedListener) {
        onInitFinishedListener = listener

        if (onInitDone) {
            onInitFinishedListener?.onInitFinished(this)
        }
    }

    private fun listDirectionToRecyclerViewOrientation() = if (direction == ListDirection.VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }

    private fun configDataSourceObserver(rootView: RootView, recyclerView: RecyclerView) {
        observeBindChanges(rootView, recyclerView, dataSource) { value ->
            if (value != list) {
                if (value.isNullOrEmpty()) {
                    (recyclerView.adapter as ListViewContextAdapter2).clearList()
                } else {
                    (recyclerView.adapter as ListViewContextAdapter2).setList(value)
                    recyclerView.scrollToPosition(0)
                }
                list = value
            }

        }
    }

    private fun configRecyclerViewScrollListener(
        recyclerView: RecyclerView,
        rootView: RootView
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (needToExecuteOnScrollEnd(recyclerView)) {
                    onScrollEnd?.forEach { action ->
                        action.execute(rootView, recyclerView)
                    }
                }
            }
        })
    }

    private fun needToExecuteOnScrollEnd(recyclerView: RecyclerView): Boolean {
        val scrolledPercent = calculateScrolledPercent(recyclerView)
        scrollThreshold?.let {
            return scrolledPercent >= scrollThreshold
        }
        return scrolledPercent == 100f
    }

    private fun calculateScrolledPercent(recyclerView: RecyclerView): Float {
        val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
        var scrolled = 0f
        layoutManager?.let {
            val totalItemCount = it.itemCount.toFloat()
            val lastVisible = it.findLastVisibleItemPosition().toFloat()
            scrolled = (lastVisible / totalItemCount) * 100
        }

        return scrolled
    }
}

internal class ListViewContextAdapter2(
    template: ServerDrivenComponent,
    private val orientation: Int,
    private val rootView: RootView,
    private val iteratorName: String,
    private var listItems: ArrayList<Any> = ArrayList()
) : RecyclerView.Adapter<ContextViewHolderTwo>() {

    class BeagleAdapterItem(val data: Any,
                            var childContextMap: MutableMap<Int, String> = mutableMapOf(),
                            var initialized: Boolean = false)

    private var adapterItems = listOf<BeagleAdapterItem>()

    private val viewPool = RecyclerView.RecycledViewPool()

    // Mapeia os componentes dentro do viewHolder que iniciaram o bloco onInit e cujo bloco onInit ainda não terminou
    // de ser executado
    private val onInitControl = mutableMapOf<RecyclerView.ViewHolder, MutableList<ServerDrivenComponent>>()

    // Mapeia os componentes que implementam a interface OnInitableComponent dentro do template. Esse mapa é uma
    // conveniência para acessar rapidamente o viewHolder relacionado a um OnInitableComponent no método
    // resolveComponentFinishedOnInit()
    private val initiableComponentsMap = mutableMapOf<ServerDrivenComponent, RecyclerView.ViewHolder>()

    // lista de viewHolders criados em onCreateViewHolder. Essa lista é utilizada no método onBindViewHolder() para
    // identificar se um viewHolder foi reciclado
    private val createdViewHolderList = mutableListOf<RecyclerView.ViewHolder>()

    private val templateJson = BeagleSerializer().serializeComponent(template)

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContextViewHolderTwo {

        // Cria uma nova instância do template. Precisamos de uma instância nova, pois os itens da lista manipulam as
        // referências dentro do template.
        val newTemplate = BeagleSerializer().deserializeComponent(templateJson)

        val view = newTemplate
            .toView(rootView)
            .also {
                it.layoutParams = LayoutParams(layoutParamWidth(), layoutParamHeight())
            }

        // Procura todos os recyclerViews que são filhos DIRETOS do template e atribui um viewPool compartilhado para
        // eles. Como as views das listas filhas são sempre as mesmas, todas as listas podem compartilhar o mesmo
        // viewPool. Isso resulta em menos views sendo criadas.
        val recyclerViewList = mutableListOf<RecyclerView>()
        findNestedRecyclerViews(view, recyclerViewList)
        recyclerViewList.forEach {
            it.setRecycledViewPool(viewPool)
        }

        // TODO: esse bloco de código é basicamente o mesmo do onBindViewHolder. Verificar para fazer essa lógica
        // toda lá.
        val holder = ContextViewHolderTwo(newTemplate, view)
        val initableComponentsList = mutableListOf<ServerDrivenComponent>()
        findInitableComponents(newTemplate, initableComponentsList)
        initableComponentsList.forEach { component ->
            initiableComponentsMap[component] = holder
            if (!onInitControl.containsKey(holder)) {
                onInitControl[holder] = mutableListOf()
            }
            onInitControl[holder]?.add(component)
            if (holder.isRecyclable) {
                holder.setIsRecyclable(false)
            }

            (component as OnInitableComponent).addOnInitFinishedListener(object : OnInitFinishedListener {
                override fun onInitFinished(component: ServerDrivenComponent) {
                    resolveComponentFinishedOnInit(component)
                }

            })
        }

        // adiciona o holder na lista de holders recém criados. Utilizamos essa lista para identificar os holders que
        // ainda não foram reciclados
        createdViewHolderList.add(holder)
        return holder
    }

    private fun layoutParamWidth() = if (isOrientationVertical()) MATCH_PARENT else WRAP_CONTENT

    private fun layoutParamHeight() = if (isOrientationVertical()) WRAP_CONTENT else MATCH_PARENT

    private fun flexDirection() = if (isOrientationVertical()) COLUMN else ROW

    private fun isOrientationVertical() = (orientation == RecyclerView.VERTICAL)

    override fun onBindViewHolder(holder: ContextViewHolderTwo, position: Int) {
        holder.bind(adapterItems[position].data, iteratorName)

        // Se esse holder estiver em createdViewHolderList, significa que ele acabou de ser criado e que ainda não foi
        // reciclado. Nós só entramos no bloco abaixo, caso o holder esteja sendo reciclado. Nesse caso, nós temos duas
        // situações:
        //
        // 1 - Esse item (essa posição do adapter) ainda não foi inicializado e precisamos chamar os onInit de todos os
        //     componentes do template.
        //
        // 2 - Esse item (essa posição do adapter) já executou todas os onInit dos seus componentes e precisamos
        //     restaurar os contextos das views dentro do template, pois esse holder está sendo reciclado.
        if (!createdViewHolderList.contains(holder)) {

            // Caso 1 - Esse item (essa posição do adapter) ainda não foi inicializado e precisamos chamar os onInit de]
            //           todos os componentes do template.
            if (!adapterItems[position].initialized) {

                // resgata o template do holder. Não dá para utilizar sempre o mesmo template, pois o template de um
                // holder pode ter terminado o onInit, mas o template de outro holder ainda pode estar executando o
                // onInit
                val template = holder.template
                template.let {

                    // procura no template (que é um ServerDrivenComponent) por todos os componentes que são
                    // OnInitableComponent
                    val initableComponentsList = mutableListOf<ServerDrivenComponent>()
                    findInitableComponents(template, initableComponentsList)

                    // para cada OnInitableComponent:
                    //
                    // - Mapeia ele em relação ao holder (quando o onInit desse componente termina, precisamos saber
                    //   à qual holder ele pertence)
                    //
                    // - Armazena o componente na lista de componentes OnInitableComponent do holder.
                    //   é através dessa lista que controlamos se o holder ainda possui algum componente executando
                    //   oninit
                    //
                    // - Marca o holder como não reciclável (não podemos reciclar holders que possuem componentes
                    //   executando onInit
                    //
                    // - Executa o onInit do holder (como a view está sendo reciclada, é necessário chamar o onInit
                    //   para essa posição do adapter)


                    initableComponentsList.forEach { component ->
                        initiableComponentsMap[component] = holder
                        if (!onInitControl.containsKey(holder)) {
                            onInitControl[holder] = mutableListOf()
                        }
                        onInitControl[holder]?.add(component)

                        if (holder.isRecyclable) {
                            holder.setIsRecyclable(false)
                        }

                        (component as OnInitableComponent).executeOnInit(rootView, object : OnInitFinishedListener {
                            override fun onInitFinished(component: ServerDrivenComponent) {
                                resolveComponentFinishedOnInit(component)
                            }

                        })
                    }
                }
            }
            // Caso 2 - Esse item (essa posição do adapter) já executou todas os onInit dos seus componentes e
            // precisamos restaurar os contextos das views dentro do template, pois esse holder está sendo reciclado.
            else {
                restoreContexts(holder.itemView, adapterItems[position].childContextMap)
            }
        }

        // remove esse holder da lista de holders recém criados. A próxima vez que esse holder passar por aqui,
        // saberemos que esse holder foi reciclado.
        createdViewHolderList.remove(holder)
    }

    override fun onViewRecycled(holder: ContextViewHolderTwo) {
        super.onViewRecycled(holder)

        // Quando um item da lista saí da árvore de views, precisamos salvar o contexto de todas as views que possuem
        // contexto. Esses contextos são salvos aqui, pois enquanto o item estiver visível na lista, o usuário pode
        // interagir com algum contexto desse template.
        if (holder.adapterPosition > -1) {
            saveContexts(holder.itemView, adapterItems[holder.adapterPosition].childContextMap)
        }
    }

    override fun onViewAttachedToWindow(holder: ContextViewHolderTwo) {
        super.onViewAttachedToWindow(holder)

        // Quando um item entra na árvore de views, verificamos se ele foi completamente inicializado. Isso é feito aqui
        // novamente, pois caso o onInit de todos os componentes do template complete antes do item estar visível na
        // lista, o adapterPosition do holder é -1 e o método resolveComponentFinishedOnInit não consegue executar a
        // lógica completa.

        // O que fazemos aqui é indicar que para determinada posição do adapter, a execução de todos os onInit dentro
        // do template terminou.
        if (!adapterItems[holder.adapterPosition].initialized) {
            val holderIsCompletellyInitialized = onInitControl[holder]?.isEmpty() ?: true
            if (holderIsCompletellyInitialized) {
                holder.setIsRecyclable(true)
                adapterItems[holder.adapterPosition].initialized = true
            }
        }
    }

    fun setList(list: List<Any>) {
        try {
            adapterItems = list.map { BeagleAdapterItem(it) }
            notifyDataSetChanged()
        } catch (e: Exception) {

        }
    }

    fun clearList() {
        val initialSize = listItems.size
        listItems.clear()
        notifyItemRangeRemoved(0, initialSize)
    }

    override fun getItemCount(): Int = adapterItems.size

    private fun findNestedRecyclerViews(view: View, recyclerList: MutableList<RecyclerView>) {
        if (view !is ViewGroup) {
            return
        }
        if (view is RecyclerView) {
            recyclerList.add(view)
            return
        }
        val count = view.childCount
        for (i in 0 until count) {
            val child = view.getChildAt(i)
            findNestedRecyclerViews(child, recyclerList)
        }
    }

    data class ContextIndex(var index: Int = 0)

    private fun saveContexts(view: View, contextMap: MutableMap<Int, String>, contextIndex: ContextIndex = ContextIndex()) {

        //TODO: estamos salvando contextos que não seriam necessários... os itens do recyclerview interno, são
        // salvos pelo adapter desse recycler interno. Ver como fazer para não salvar aqui

        if (view.getContextData() != null) {
            contextMap[contextIndex.index] = BeagleMoshi.moshi.adapter(ContextData::class.java).toJson(view.getContextData())
            contextIndex.index++
        }

        if (view !is ViewGroup || view is RecyclerView) {
            return
        }

        val count = view.childCount
        for (i in 0 until count) {
            val child = view.getChildAt(i)
            saveContexts(child, contextMap, contextIndex)
        }
    }

    private fun restoreContexts(view: View, contextMap: MutableMap<Int, String>, contextIndex: ContextIndex = ContextIndex()) {
        if (view.getContextData() != null) {
            contextMap[contextIndex.index]?.let { contextString ->
                val context = BeagleMoshi.moshi.adapter(ContextData::class.java).fromJson(contextString)
                context?.let {
                    view.setContextData(it)
                }
            }
            contextIndex.index++
        }

        if (view !is ViewGroup || view is RecyclerView) {
            return
        }

        val count = view.childCount
        for (i in 0 until count) {
            val child = view.getChildAt(i)
            restoreContexts(child, contextMap, contextIndex)
        }
    }

    private fun findInitableComponents(component: ServerDrivenComponent, initableComponentsList: MutableList<ServerDrivenComponent>) {
        if (component is OnInitableComponent) {
            initableComponentsList.add(component)
        }

        if (component is MultiChildComponent) {
            component.children.forEach { childComponent ->
                findInitableComponents(childComponent, initableComponentsList)
            }
        } else if (component is SingleChildComponent) {
            findInitableComponents(component.child, initableComponentsList)
        }
    }

    fun resolveComponentFinishedOnInit(component: ServerDrivenComponent) {
        val holder = initiableComponentsMap[component]

        holder?.let {
            onInitControl[it]?.remove(component)

            val holderIsCompletellyInitialized = onInitControl[it]?.isEmpty() ?: true
            if (holderIsCompletellyInitialized) {
                if (holder.adapterPosition > -1) {
                    it.setIsRecyclable(true)
                    adapterItems[it.adapterPosition].initialized = true
                }
            }
        }
    }
}

internal class ContextViewHolderTwo(
    val template: ServerDrivenComponent,
    itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(data: Any, iteratorName: String) {
        itemView.setContextData(ContextData(id = iteratorName, value = data))
    }
}
