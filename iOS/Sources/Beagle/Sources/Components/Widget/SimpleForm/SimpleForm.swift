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

/// Component will define a submit handler for a `SimpleForm`.
public struct SimpleForm: ServerDrivenComponent, HasContext, AutoInitiableAndDecodable {
    
    /// Defines the contextData that be set to form.
    public var context: Context?
    
    /// Defines the actions you want to execute when action submit form.
    public let onSubmit: [Action]?
    
    /// This event is executed every time a form is submitted, but because of a validation error, the onSubmit event is not run.
    public let onValidationError: [Action]?
    
    /// Defines the items on the simple form.
    public let children: [ServerDrivenComponent]?
    
    /// Properties that all widgets have in common.
    public var widgetProperties: WidgetProperties
    
// sourcery:inline:auto:SimpleForm.Init
    public init(
        context: Context? = nil,
        onSubmit: [Action]? = nil,
        onValidationError: [Action]? = nil,
        children: [ServerDrivenComponent]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties()
    ) {
        self.context = context
        self.onSubmit = onSubmit
        self.onValidationError = onValidationError
        self.children = children
        self.widgetProperties = widgetProperties
    }
// sourcery:end
    
    public init(
        context: Context? = nil,
        onSubmit: [Action]? = nil,
        onValidationError: [Action]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildrenBuilder
        _ children: () -> [ServerDrivenComponent]
    ) {
        self.init(context: context, onSubmit: onSubmit, onValidationError: onValidationError, children: children(), widgetProperties: widgetProperties)
    }
    
    #if swift(<5.3)
    public init(
        context: Context? = nil,
        onSubmit: [Action]? = nil,
        onValidationError: [Action]? = nil,
        widgetProperties: WidgetProperties = WidgetProperties(),
        @ChildBuilder
        _ children: () -> ServerDrivenComponent
    ) {
        self.init(context: context, onSubmit: onSubmit, onValidationError: onValidationError, children: [children()], widgetProperties: widgetProperties)
    }
    #endif
}
