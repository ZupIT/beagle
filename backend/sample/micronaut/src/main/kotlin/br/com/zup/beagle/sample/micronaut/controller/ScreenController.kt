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

@file:Suppress("TooManyFunctions", "LongParameterList")

package br.com.zup.beagle.sample.micronaut.controller

import br.com.zup.beagle.sample.constants.ACCESSIBILITY_SCREEN_ENDPOINT
import br.com.zup.beagle.sample.constants.NAVIGATION_TYPE_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_TYPE_STEP2_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_TYPE_STEP3_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_PRESENT_ENDPOINT
import br.com.zup.beagle.sample.constants.SAMPLE_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_CLICK_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ACTION_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_ANALYTICS_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_BUILDER_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_BUTTON_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_COMPONENTS_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_COMPOSE_COMPONENT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_EXAMPLE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_FORM_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_LAZY_COMPONENT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_LIST_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_NAVIGATION_BAR_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_NETWORK_IMAGE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_PAGE_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_SAFE_AREA
import br.com.zup.beagle.sample.constants.SCREEN_SCROLL_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TAB_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TEXT_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_TOUCHABLE_ENDPOINT
import br.com.zup.beagle.sample.constants.SCREEN_WEB_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.QA_FLAG
import br.com.zup.beagle.sample.constants.SCREEN_FLEX_ENDPOINT
import br.com.zup.beagle.sample.micronaut.service.AccessibilityService
import br.com.zup.beagle.sample.micronaut.service.SampleActionClickService
import br.com.zup.beagle.sample.micronaut.service.SampleActionService
import br.com.zup.beagle.sample.micronaut.service.SampleAnalyticsService
import br.com.zup.beagle.sample.micronaut.service.SampleButtonService
import br.com.zup.beagle.sample.micronaut.service.SampleComponentsService
import br.com.zup.beagle.sample.micronaut.service.SampleComposeComponentService
import br.com.zup.beagle.sample.micronaut.service.SampleFormService
import br.com.zup.beagle.sample.micronaut.service.SampleImageService
import br.com.zup.beagle.sample.micronaut.service.SampleLazyComponentService
import br.com.zup.beagle.sample.micronaut.service.SampleListViewService
import br.com.zup.beagle.sample.micronaut.service.SampleNavigationBarService
import br.com.zup.beagle.sample.micronaut.service.SampleNavigationTypeService
import br.com.zup.beagle.sample.micronaut.service.SampleImageRemoteService
import br.com.zup.beagle.sample.micronaut.service.SamplePageViewService
import br.com.zup.beagle.sample.micronaut.service.SampleSafeAreaService
import br.com.zup.beagle.sample.micronaut.service.SampleScreenBuilderService
import br.com.zup.beagle.sample.micronaut.service.SampleScrollViewService
import br.com.zup.beagle.sample.micronaut.service.SampleTabViewService
import br.com.zup.beagle.sample.micronaut.service.SampleTextService
import br.com.zup.beagle.sample.micronaut.service.SampleTouchableService
import br.com.zup.beagle.sample.micronaut.service.SampleViewService
import br.com.zup.beagle.sample.micronaut.service.SampleWebViewService
import br.com.zup.beagle.sample.micronaut.service.ScreenFlexService
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable

@Controller
class ScreenController(
    private val accessibilityService: AccessibilityService,
    private val sampleViewService: SampleViewService,
    private val sampleScreenBuilderService: SampleScreenBuilderService,
    private val sampleComponentsService: SampleComponentsService,
    private val sampleButtonService: SampleButtonService,
    private val sampleTextService: SampleTextService,
    private val sampleImageService: SampleImageService,
    private val sampleTabViewService: SampleTabViewService,
    private val sampleListViewService: SampleListViewService,
    private val sampleScrollViewService: SampleScrollViewService,
    private val samplePageViewService: SamplePageViewService,
    private val sampleActionService: SampleActionService,
    private val sampleFormService: SampleFormService,
    private val sampleLazyComponentService: SampleLazyComponentService,
    private val sampleNavigationBarService: SampleNavigationBarService,
    private val sampleNavigationTypeService: SampleNavigationTypeService,
    private val sampleComposeComponentService: SampleComposeComponentService,
    private val sampleImageRemoteService: SampleImageRemoteService,
    private val sampleTouchableService: SampleTouchableService,
    private val sampleActionClickService: SampleActionClickService,
    private val sampleAnalyticsService: SampleAnalyticsService,
    private val sampleWebViewService: SampleWebViewService,
    private val sampleSafeArea: SampleSafeAreaService,
    private val sampleFlex: ScreenFlexService
) {
    @Get(ACCESSIBILITY_SCREEN_ENDPOINT)
    fun getAccessibilityView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.accessibilityService.createAccessibilityView(qaFlag)

    @Get(SAMPLE_VIEW_ENDPOINT)
    fun getSampleView() =
        this.sampleViewService.createSampleView()

    @Get(SCREEN_BUILDER_ENDPOINT)
    fun getScreenBuilder(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleScreenBuilderService.createScreenBuilder(qaFlag)

    @Get(SCREEN_COMPONENTS_ENDPOINT)
    fun getSampleComponents(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleComponentsService.getCreateSampleComponentsView(qaFlag)

    @Get(SCREEN_BUTTON_ENDPOINT)
    fun getSampleButtonView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleButtonService.createButtonView(qaFlag)

    @Get(SCREEN_TEXT_ENDPOINT)
    fun getSampleTextView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTextService.createTextView(qaFlag)

    @Get(SCREEN_IMAGE_ENDPOINT)
    fun getSampleImageView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleImageService.createImageView(qaFlag)

    @Get(SCREEN_TAB_VIEW_ENDPOINT)
    fun getSampleTabViewView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTabViewService.createTabView(qaFlag)

    @Get(SCREEN_LIST_VIEW_ENDPOINT)
    fun getSampleListView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleListViewService.createListView(qaFlag)

    @Get(SCREEN_SCROLL_VIEW_ENDPOINT)
    fun getScrollView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleScrollViewService.createScrollView(qaFlag)

    @Get(SCREEN_PAGE_VIEW_ENDPOINT)
    fun getPageView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.samplePageViewService.createPageView(qaFlag)

    @Get(SCREEN_ACTION_ENDPOINT)
    fun getShowDialogAction(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleActionService.createAction(qaFlag)

    @Get(SCREEN_FORM_ENDPOINT)
    fun getSampleFormView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleFormService.createFormView(qaFlag)

    @Get(SCREEN_LAZY_COMPONENT_ENDPOINT)
    fun getSampleLazyComponentController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleLazyComponentService.createLazyComponent(qaFlag)

    @Get(SCREEN_NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBarController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationBarService.createNavigationBarView(qaFlag)

    @Get(REPRESENTATION_NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBar() = this.sampleNavigationBarService.navigationBar()

    @Get(REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT)
    fun getNavigationBarStyle() = this.sampleNavigationBarService.navigationBarStyle()

    @Get(REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT)
    fun getNavigationBarText() = this.sampleNavigationBarService.navigationBarWithTextAsItems()

    @Get(REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT)
    fun getNavigationBarImage() = this.sampleNavigationBarService.navigationBarWithImageAsItem()

    @Get(NAVIGATION_TYPE_ENDPOINT)
    fun getSampleNavigationTypeController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenNavigateType(qaFlag)

    @Get(REPRESENTATION_NAVIGATION_TYPE_STEP2_ENDPOINT)
    fun getNavigationStep2(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenSte2(qaFlag)

    @Get(REPRESENTATION_PRESENT_ENDPOINT)
    fun getNavigationPresentView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenPresentView(qaFlag)

    @Get(REPRESENTATION_NAVIGATION_TYPE_STEP3_ENDPOINT)
    fun getNavigationStep3(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenStep3(qaFlag)

    @Get(SCREEN_COMPOSE_COMPONENT_ENDPOINT)
    fun getComposeComponent(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleComposeComponentService.createComposeComponentView(qaFlag)

    @Get(SCREEN_NETWORK_IMAGE_ENDPOINT)
    fun getSampleImageRemoteView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleImageRemoteService.createImageRemote(qaFlag)

    @Get(SCREEN_TOUCHABLE_ENDPOINT)
    fun getTouchableView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTouchableService.createTouchableView(qaFlag)

    @Get(SCREEN_ACTION_CLICK_ENDPOINT)
    fun getSampleActionClickController() = this.sampleActionClickService.createActionClick()

    @Get(SCREEN_EXAMPLE_ENDPOINT)
    fun getNavigationExample() = this.sampleActionService.getNavigateExample()

    @Get(SCREEN_ANALYTICS_ENDPOINT)
    fun getAnalyticsExample(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleAnalyticsService.getAnalyticsExample(qaFlag)

    @Get(SCREEN_WEB_VIEW_ENDPOINT)
    fun getSampleWebViewService(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleWebViewService.createWebView(qaFlag)

    @Get(SCREEN_SAFE_AREA)
    fun getSampleSafeArea(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleSafeArea.createSafeArea(qaFlag)

    @Get(SCREEN_FLEX_ENDPOINT)
    fun getSampleFlex(@PathVariable(QA_FLAG) qaFlag: Boolean) = this.sampleFlex.createSampleFlex(qaFlag)
}