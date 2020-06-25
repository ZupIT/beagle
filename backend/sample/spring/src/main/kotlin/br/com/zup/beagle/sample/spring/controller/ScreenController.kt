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

package br.com.zup.beagle.sample.spring.controller

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
import br.com.zup.beagle.sample.spring.service.AccessibilityService
import br.com.zup.beagle.sample.spring.service.SampleActionClickService
import br.com.zup.beagle.sample.spring.service.SampleActionService
import br.com.zup.beagle.sample.spring.service.SampleAnalyticsService
import br.com.zup.beagle.sample.spring.service.SampleButtonService
import br.com.zup.beagle.sample.spring.service.SampleComponentsService
import br.com.zup.beagle.sample.spring.service.SampleComposeComponentService
import br.com.zup.beagle.sample.spring.service.SampleFormService
import br.com.zup.beagle.sample.spring.service.SampleImageService
import br.com.zup.beagle.sample.spring.service.SampleLazyComponentService
import br.com.zup.beagle.sample.spring.service.SampleListViewService
import br.com.zup.beagle.sample.spring.service.SampleNavigationBarService
import br.com.zup.beagle.sample.spring.service.SampleNavigationTypeService
import br.com.zup.beagle.sample.spring.service.SampleImageRemoteService
import br.com.zup.beagle.sample.spring.service.SamplePageViewService
import br.com.zup.beagle.sample.spring.service.SampleSafeAreaService
import br.com.zup.beagle.sample.spring.service.SampleScreenBuilderService
import br.com.zup.beagle.sample.spring.service.SampleScrollViewService
import br.com.zup.beagle.sample.spring.service.SampleTabViewService
import br.com.zup.beagle.sample.spring.service.SampleTextService
import br.com.zup.beagle.sample.spring.service.SampleTouchableService
import br.com.zup.beagle.sample.spring.service.SampleViewService
import br.com.zup.beagle.sample.spring.service.SampleWebViewService
import br.com.zup.beagle.sample.spring.service.ScreenFlexService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

@RestController
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
    @GetMapping(ACCESSIBILITY_SCREEN_ENDPOINT)
    fun getAccessibilityView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.accessibilityService.createAccessibilityView(qaFlag)

    @GetMapping(SAMPLE_VIEW_ENDPOINT)
    fun getSampleView() =
        this.sampleViewService.createSampleView()

    @GetMapping(SCREEN_BUILDER_ENDPOINT)
    fun getScreenBuilder(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleScreenBuilderService.createScreenBuilder(qaFlag)

    @GetMapping(SCREEN_COMPONENTS_ENDPOINT)
    fun getSampleComponents(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleComponentsService.getCreateSampleComponentsView(qaFlag)

    @GetMapping(SCREEN_BUTTON_ENDPOINT)
    fun getSampleButtonView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleButtonService.createButtonView(qaFlag)

    @GetMapping(SCREEN_TEXT_ENDPOINT)
    fun getSampleTextView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTextService.createTextView(qaFlag)

    @GetMapping(SCREEN_IMAGE_ENDPOINT)
    fun getSampleImageView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleImageService.createImageView(qaFlag)

    @GetMapping(SCREEN_TAB_VIEW_ENDPOINT)
    fun getSampleTabViewView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTabViewService.createTabView(qaFlag)

    @GetMapping(SCREEN_LIST_VIEW_ENDPOINT)
    fun getSampleListView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleListViewService.createListView(qaFlag)

    @GetMapping(SCREEN_SCROLL_VIEW_ENDPOINT)
    fun getScrollView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleScrollViewService.createScrollView(qaFlag)

    @GetMapping(SCREEN_PAGE_VIEW_ENDPOINT)
    fun getPageView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.samplePageViewService.createPageView(qaFlag)

    @GetMapping(SCREEN_ACTION_ENDPOINT)
    fun getShowDialogAction(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleActionService.createAction(qaFlag)

    @GetMapping(SCREEN_FORM_ENDPOINT)
    fun getSampleFormView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleFormService.createFormView(qaFlag)

    @GetMapping(SCREEN_LAZY_COMPONENT_ENDPOINT)
    fun getSampleLazyComponentController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleLazyComponentService.createLazyComponent(qaFlag)

    @GetMapping(SCREEN_NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBarController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationBarService.createNavigationBarView(qaFlag)

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_ENDPOINT)
    fun getSampleNavigationBar() = this.sampleNavigationBarService.navigationBar()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_STYLE_ENDPOINT)
    fun getNavigationBarStyle() = this.sampleNavigationBarService.navigationBarStyle()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_TEXT_ENDPOINT)
    fun getNavigationBarText() = this.sampleNavigationBarService.navigationBarWithTextAsItems()

    @GetMapping(REPRESENTATION_NAVIGATION_BAR_IMAGE_ENDPOINT)
    fun getNavigationBarImage() = this.sampleNavigationBarService.navigationBarWithImageAsItem()

    @GetMapping(NAVIGATION_TYPE_ENDPOINT)
    fun getSampleNavigationTypeController(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenNavigateType(qaFlag)

    @GetMapping(REPRESENTATION_NAVIGATION_TYPE_STEP2_ENDPOINT)
    fun getNavigationStep2(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenSte2(qaFlag)

    @GetMapping(REPRESENTATION_PRESENT_ENDPOINT)
    fun getNavigationPresentView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenPresentView(qaFlag)

    @GetMapping(REPRESENTATION_NAVIGATION_TYPE_STEP3_ENDPOINT)
    fun getNavigationStep3(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleNavigationTypeService.getScreenStep3(qaFlag)

    @GetMapping(SCREEN_COMPOSE_COMPONENT_ENDPOINT)
    fun getComposeComponent(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleComposeComponentService.createComposeComponentView(qaFlag)

    @GetMapping(SCREEN_NETWORK_IMAGE_ENDPOINT)
    fun getSampleImageRemote(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleImageRemoteService.createImageRemote(qaFlag)

    @GetMapping(SCREEN_TOUCHABLE_ENDPOINT)
    fun getTouchableView(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleTouchableService.createTouchableView(qaFlag)

    @GetMapping(SCREEN_ACTION_CLICK_ENDPOINT)
    fun getSampleActionClickController() = this.sampleActionClickService.createActionClick()

    @GetMapping(SCREEN_EXAMPLE_ENDPOINT)
    fun getNavigationExample() = this.sampleActionService.getNavigateExample()

    @GetMapping(SCREEN_ANALYTICS_ENDPOINT)
    fun getAnalyticsExample(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleAnalyticsService.getAnalyticsExample(qaFlag)

    @GetMapping(SCREEN_WEB_VIEW_ENDPOINT)
    fun getSampleWebViewService(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleWebViewService.createWebView(qaFlag)

    @GetMapping(SCREEN_SAFE_AREA)
    fun getSampleSafeArea(@PathVariable(QA_FLAG) qaFlag: Boolean) =
        this.sampleSafeArea.createSafeArea(qaFlag)

    @GetMapping(SCREEN_FLEX_ENDPOINT)
    fun getSampleFlex(@PathVariable(QA_FLAG) qaFlag: Boolean) = this.sampleFlex.createSampleFlex(qaFlag)
}