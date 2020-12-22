/******/ (() => { // webpackBootstrap
/******/ 	var __webpack_modules__ = ({

/***/ 8266:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var beagle_tree_1 = __importDefault(__webpack_require__(7853));
var logger_1 = __importDefault(__webpack_require__(2611));
var addChildren = function (_a) {
    var action = _a.action, beagleView = _a.beagleView;
    var componentId = action.componentId, value = action.value, _b = action.mode, mode = _b === void 0 ? 'append' : _b;
    var uiTree = beagleView.getTree();
    var component = beagle_tree_1.default.findById(uiTree, componentId);
    if (!component) {
        logger_1.default.warn("No component with id " + componentId + " has been found in the tree");
        return;
    }
    var currentChildren = component.children || [];
    if (mode.toLowerCase() === 'append')
        component.children = __spreadArrays(currentChildren, value);
    if (mode.toLowerCase() === 'prepend')
        component.children = __spreadArrays(value, currentChildren);
    if (mode.toLowerCase() === 'replace')
        component.children = value;
    beagleView.getRenderer().doFullRender(component, component.id);
};
exports.default = addChildren;
//# sourceMappingURL=add-children.js.map

/***/ }),

/***/ 7357:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
var alert = function (_a) {
    var action = _a.action, executeAction = _a.executeAction;
    var message = action.message, onPressOk = action.onPressOk;
    window.alert(typeof message === 'string' ? message : JSON.stringify(message));
    if (onPressOk)
        executeAction(onPressOk);
};
exports.default = alert;
//# sourceMappingURL=alert.js.map

/***/ }),

/***/ 6958:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
var condition = function (_a) {
    var action = _a.action, executeAction = _a.executeAction;
    var condition = action.condition, onTrue = action.onTrue, onFalse = action.onFalse;
    if (condition && onTrue)
        executeAction(onTrue);
    if (!condition && onFalse)
        executeAction(onFalse);
};
exports.default = condition;
//# sourceMappingURL=condition.js.map

/***/ }),

/***/ 8384:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
var confirm = function (_a) {
    var action = _a.action, executeAction = _a.executeAction;
    var message = action.message, onPressOk = action.onPressOk, onPressCancel = action.onPressCancel;
    var hasConfirmed = window.confirm(message);
    if (hasConfirmed)
        onPressOk && executeAction(onPressOk);
    else
        onPressCancel && executeAction(onPressCancel);
};
exports.default = confirm;
//# sourceMappingURL=confirm.js.map

/***/ }),

/***/ 3368:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var add_children_1 = __importDefault(__webpack_require__(8266));
var set_context_1 = __importDefault(__webpack_require__(120));
var send_request_1 = __importDefault(__webpack_require__(9204));
var alert_1 = __importDefault(__webpack_require__(7357));
var confirm_1 = __importDefault(__webpack_require__(8384));
var submit_form_1 = __importDefault(__webpack_require__(6132));
var condition_1 = __importDefault(__webpack_require__(6958));
var navigation_1 = __importDefault(__webpack_require__(1922));
var defaultActionHandlers = __assign({ 'beagle:addChildren': add_children_1.default, 'beagle:setContext': set_context_1.default, 'beagle:sendRequest': send_request_1.default, 'beagle:alert': alert_1.default, 'beagle:confirm': confirm_1.default, 'beagle:submitForm': submit_form_1.default, 'beagle:condition': condition_1.default }, navigation_1.default);
exports.default = defaultActionHandlers;
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 1922:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var url_1 = __importDefault(__webpack_require__(5905));
var string_1 = __importDefault(__webpack_require__(8490));
var object_1 = __importDefault(__webpack_require__(2413));
var logger_1 = __importDefault(__webpack_require__(2611));
var NavigationActions = {};
var openExternalURL = function (_a) {
    var action = _a.action;
    var url = action.url;
    window.open(url);
};
var openNativeRoute = function (_a) {
    var action = _a.action;
    var route = action.route, data = action.data;
    var origin = window.location.origin;
    var qs = data && url_1.default.createQueryString(data);
    var prefixedRoute = string_1.default.addPrefix(route, '/');
    window.location.href = "" + origin + prefixedRoute + (qs || '');
};
var navigateBeagleView = function (_a) {
    var action = _a.action, beagleView = _a.beagleView;
    return __awaiter(void 0, void 0, void 0, function () {
        var actionNameLowercase, actionName, navigationType, error_1;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0:
                    actionNameLowercase = action._beagleAction_.toLowerCase();
                    actionName = object_1.default.getOriginalKeyByCaseInsensitiveKey(NavigationActions, actionNameLowercase);
                    navigationType = actionName.replace(/^beagle:/, '');
                    _b.label = 1;
                case 1:
                    _b.trys.push([1, 3, , 4]);
                    return [4 /*yield*/, beagleView.getNavigator().navigate(navigationType, action.route, action.controllerId)];
                case 2:
                    _b.sent();
                    return [3 /*break*/, 4];
                case 3:
                    error_1 = _b.sent();
                    logger_1.default.error(error_1.message || error_1);
                    return [3 /*break*/, 4];
                case 4: return [2 /*return*/];
            }
        });
    });
};
NavigationActions = {
    'beagle:openExternalURL': openExternalURL,
    'beagle:openNativeRoute': openNativeRoute,
    'beagle:pushStack': navigateBeagleView,
    'beagle:popStack': navigateBeagleView,
    'beagle:pushView': navigateBeagleView,
    'beagle:popView': navigateBeagleView,
    'beagle:popToView': navigateBeagleView,
    'beagle:resetStack': navigateBeagleView,
    'beagle:resetApplication': navigateBeagleView,
};
exports.default = NavigationActions;
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 9204:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var BeagleNetworkError_1 = __importDefault(__webpack_require__(3869));
var sendRequest = function (_a) {
    var action = _a.action, executeAction = _a.executeAction, beagleView = _a.beagleView;
    return __awaiter(void 0, void 0, void 0, function () {
        var url, _b, method, data, headers, onSuccess, onError, onFinish, _c, httpClient, urlBuilder, contextResponse, response, resultText, resultData, error_1, event_1;
        return __generator(this, function (_d) {
            switch (_d.label) {
                case 0:
                    url = action.url, _b = action.method, method = _b === void 0 ? 'get' : _b, data = action.data, headers = action.headers, onSuccess = action.onSuccess, onError = action.onError, onFinish = action.onFinish;
                    _c = beagleView.getBeagleService(), httpClient = _c.httpClient, urlBuilder = _c.urlBuilder;
                    contextResponse = {};
                    _d.label = 1;
                case 1:
                    _d.trys.push([1, 4, 5, 6]);
                    return [4 /*yield*/, httpClient.fetch(urlBuilder.build(url), { method: method, body: JSON.stringify(data), headers: headers })];
                case 2:
                    response = _d.sent();
                    return [4 /*yield*/, response.text()];
                case 3:
                    resultText = _d.sent();
                    contextResponse.status = response.status;
                    contextResponse.statusText = response.statusText;
                    contextResponse.data = resultText;
                    try {
                        resultData = resultText && JSON.parse(resultText);
                        contextResponse.data = resultData;
                    }
                    catch (_e) {
                        contextResponse.data = resultText;
                    }
                    if (!response.ok)
                        throw new BeagleNetworkError_1.default(url, response);
                    onSuccess && executeAction(onSuccess, 'onSuccess', contextResponse);
                    return [3 /*break*/, 6];
                case 4:
                    error_1 = _d.sent();
                    logger_1.default.error(error_1);
                    event_1 = __assign(__assign({}, contextResponse), { message: error_1.message || 'Unexpected error' });
                    onError && executeAction(onError, 'onError', event_1);
                    return [3 /*break*/, 6];
                case 5:
                    onFinish && executeAction(onFinish);
                    return [7 /*endfinally*/];
                case 6: return [2 /*return*/];
            }
        });
    });
};
exports.default = sendRequest;
//# sourceMappingURL=send-request.js.map

/***/ }),

/***/ 120:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var set_1 = __importDefault(__webpack_require__(6968));
var context_1 = __importDefault(__webpack_require__(395));
var setContext = function (_a) {
    var action = _a.action, element = _a.element, beagleView = _a.beagleView;
    var value = action.value, contextId = action.contextId, path = action.path;
    var globalContext = beagleView.getBeagleService().globalContext;
    var uiTree = beagleView.getTree();
    var globalContexts = [globalContext.getAsDataContext()];
    var contextHierarchy = context_1.default.evaluate(uiTree, globalContexts, false)[element.id];
    if (!contextHierarchy) {
        return logger_1.default.warn("The component of type \"" + element._beagleComponent_ + "\" and id \"" + element.id + "\" is detached from the current tree and attempted to change the value of the context \"" + contextId + "\".", 'This behavior is not supported, please, hide the component instead of removing it if you still need it to perform an action in the tree.');
    }
    var context = context_1.default.find(contextHierarchy, contextId);
    if (context && context.id === 'global') {
        globalContext.set(value, path);
        return;
    }
    if (!context) {
        var specificContextMessage = ("Could not find context with id \"" + contextId + "\" for element of type \"" + element._beagleComponent_ + "\" and id \"" + element.id + "\"");
        var anyContextMessage = ("Could not find any context for element of type \"" + element._beagleComponent_ + "\" and id \"" + element.id + "\"");
        logger_1.default.warn(contextId ? specificContextMessage : anyContextMessage);
        return;
    }
    if (!path)
        context.value = value;
    else {
        context.value = context.value || {};
        set_1.default(context.value, path, value);
    }
    beagleView.getRenderer().doPartialRender(uiTree);
};
exports.default = setContext;
//# sourceMappingURL=set-context.js.map

/***/ }),

/***/ 6132:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var submitForm = function (_a) {
    var element = _a.element;
    var domNode = document.querySelector("[data-beagle-id=\"" + element.id + "\"]");
    if (!domNode) {
        logger_1.default.error('Could not submit the form because the element who triggered the action is not in the dom anymore.');
        return;
    }
    var form = domNode.closest('form');
    if (!form) {
        logger_1.default.error('Could not submit because the element who triggered the action "submitForm" is not inside any form.');
        return;
    }
    form.requestSubmit();
};
exports.default = submitForm;
//# sourceMappingURL=submit-form.js.map

/***/ }),

/***/ 7853:
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
var iteration_1 = __webpack_require__(9632);
var manipulation_1 = __webpack_require__(7677);
var reading_1 = __webpack_require__(2334);
exports.default = {
    /**
     * Uses a depth first search algorithm to traverse the tree. The iteratee function will be run for
     * each node (component). The iteratee function is triggered once a node is visited, i.e. the
     * first node to run the function is the root node and not the deepest left-most node.
     *
     * The children of a node must be called "children" and be an array.
     *
     * @param tree the tree to traverse
     * @param iteratee the function to call for each node of the tree
     */
    forEach: iteration_1.forEach,
    /**
     * Uses a depth first search algorithm to traverse the tree and exposes this functionality as an
     * iterator. Each call to next() walks a step in the tree.
     *
     * The children of a node must be called "children" and be an array.
     *
     * @param tree the tree to traverse
     * @returns the iterator to iterate over the nodes
     */
    iterator: iteration_1.iterator,
    /**
     * Does the same as forEach (depth-first-search), the difference is that the iteratee function
     * expects a return value, which will be used to replace the current node in the tree. A value
     * must be returned by the iteratee function, if you don't want to change the current node, just
     * return the same node you received. If a node is replaced by another one, the tree will be
     * updated and the next node to run the iteratee function will be the first child of the new node
     * (if it has any children).
     *
     * The children of a node must be called "children" and be an array.
     *
     * @param tree the tree to traverse
     * @param iteratee the function to call for each node of the tree. This function must return a
     * node, which will be used to replace the current node of the tree.
     * @returns the new tree
     */
    replaceEach: iteration_1.replaceEach,
    /**
     * Adds a child element to the target tree. If the mode is "append", the child will be added as
     * the last element of the target's children. If "prepend", it will be added as the first child.
     *
     * This function modifies `target`, it's not a pure function.
     *
     * @param target the tree to be modified by receiving the new node `child`
     * @param child the node to insert into the tree `target`
     * @param mode the insertion strategy. Prepend (insert as first child), append (insert as last
     * child) or replace (removes all children and inserts `child`).
     */
    addChild: manipulation_1.addChild,
    /**
     * Deep-clones the tree passed as parameter.
     *
     * @param tree the tree to be cloned
     * @returns the clone of the tree
     */
    clone: manipulation_1.clone,
    /**
     * Combine two trees. This function inserts the tree `source` into the tree `target` at the node
     * referred by `anchor`. To tell which position `source` should occupy in the array of children of
     * `anchor`, you should use the last parameter `mode`.
     *
     * If there's no node with id `anchor`, the tree will be left untouched.
     *
     * This function modifies `target`, it's not a pure function.
     *
     * @param target the tree to be modified by receiving the new branch `source`
     * @param source the tree to be inserted into `target`
     * @param anchor the id of the node to attach the new branch to
     * @param mode the insertion strategy. Prepend (insert as first child of `anchor`), append (insert
     * as last child of `anchor`) or replace (removes all children of `anchor` and inserts `child`).
     */
    insertIntoTree: manipulation_1.insertIntoTree,
    /**
     * Just like `insertIntoTree`, `replaceInTree` combines two trees. But, instead of inserting the
     * new branch as a child of the node referred by `anchor`, it completely replaces `anchor`, i.e.
     * after this function runs, `anchor` doesn't exist in `target` anymore, it gets replaced by the
     * tree `source`.
     *
     * If there's no node with id `anchor` or if `anchor` is the root node, the tree will be left
     * untouched.
     *
     * This function modifies `target`, it's not a pure function.
     *
     * @param target the tree to be modified by receiving the new branch `source`
     * @param source the tree to be inserted into `target`
     * @param anchor the id of the node to be replaced by the new branch `source`
     */
    replaceInTree: manipulation_1.replaceInTree,
    /**
     * Finds every node in a tree where the value of `attributeName` is `attributeValue`. When
     * `attributeValue` is omitted, all nodes with a parameter called `attributeName` will be
     * returned. If no node is found, an empty array is returned.
     *
     * @param tree the tree where to search the nodes
     * @param attributeName the attribute name to look for
     * @param attributeValue optional. The value of `attributeName` to look for. If not specified,
     * any value is accepted, i.e. a node, to be found, will only need to have a parameter
     * `attributeName`, no matter the value of it.
     * @returns an array with all nodes found
     */
    findByAttribute: reading_1.findByAttribute,
    /**
     * Finds a node by its id. If no node is found, null is returned.
     *
     * @param tree the tree where to search the node
     * @param id the id of the node to find
     * @returns the node with the given id or null if `tree` has no node with id `id`.
     */
    findById: reading_1.findById,
    /**
     * Finds all nodes with a given type. The type of a node is defined by the property
     * `_beagleComponent_`. If no node is found, an empty array is returned.
     *
     * @param tree the tree where to search the nodes
     * @param type the type to look for
     * @returns an array with all nodes found
     */
    findByType: reading_1.findByType,
    /**
     * Looks for a node with id `childId` and returns its parent. If no node is found, null is returned.
     *
     * @param tree the tree where to search the node
     * @param childId the id the child node to find
     * @returns the parent node of `childId` or null if no node with id `childId` exists or if `childId`
     * is the root node.
     */
    findParentByChildId: reading_1.findParentByChildId,
    /**
     * Finds the position of the child with the given id in the array of children of a node. If no node
     * is found, -1 is returned.
     *
     * @param node the node where to look for the child
     * @param childId the id of the child to look for
     * @returns the position of the child in the array of children or -1 if such node doesn't exist.
     */
    indexOf: reading_1.indexOf,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 9632:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
function forEach(tree, iteratee) {
    if (Object.keys(tree).length === 0)
        return;
    var index = 0;
    function run(node) {
        iteratee(node, index++);
        if (node.children)
            node.children.forEach(function (child) { return run(child); });
    }
    run(tree);
}
exports.forEach = forEach;
function replaceEach(tree, iteratee) {
    if (Object.keys(tree).length === 0)
        return tree;
    var index = 0;
    function run(node) {
        var newNode = iteratee(node, index++);
        if (!newNode.children)
            return newNode;
        for (var i = 0; i < newNode.children.length; i++) {
            newNode.children[i] = run(newNode.children[i]);
        }
        return newNode;
    }
    return run(tree);
}
exports.replaceEach = replaceEach;
function iterator(tree) {
    if (Object.keys(tree).length === 0)
        return (function () { return __generator(this, function (_a) {
            return [2 /*return*/];
        }); })();
    function generator(node) {
        var i, childGenerator, next;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, node];
                case 1:
                    _a.sent();
                    if (!node.children)
                        return [2 /*return*/];
                    i = 0;
                    _a.label = 2;
                case 2:
                    if (!(i < node.children.length)) return [3 /*break*/, 6];
                    childGenerator = generator(node.children[i]);
                    next = childGenerator.next();
                    _a.label = 3;
                case 3:
                    if (!!next.done) return [3 /*break*/, 5];
                    return [4 /*yield*/, next.value];
                case 4:
                    _a.sent();
                    next = childGenerator.next();
                    return [3 /*break*/, 3];
                case 5:
                    i++;
                    return [3 /*break*/, 2];
                case 6: return [2 /*return*/];
            }
        });
    }
    return generator(tree);
}
exports.iterator = iterator;
//# sourceMappingURL=iteration.js.map

/***/ }),

/***/ 7677:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var cloneDeep_1 = __importDefault(__webpack_require__(361));
var reading_1 = __webpack_require__(2334);
function addChild(target, child, mode) {
    target.children = target.children || [];
    var modeHandlers = {
        append: function () { return target.children.push(child); },
        prepend: function () { return target.children.unshift(child); },
        replace: function () { return target.children = [child]; },
    };
    modeHandlers[mode]();
}
exports.addChild = addChild;
function insertIntoTree(target, source, anchor, mode) {
    if (mode === void 0) { mode = 'append'; }
    var element = reading_1.findById(target, anchor);
    if (!element)
        return;
    addChild(element, source, mode);
}
exports.insertIntoTree = insertIntoTree;
function replaceInTree(target, source, anchor) {
    var parent = reading_1.findParentByChildId(target, anchor);
    if (!parent)
        return;
    var index = reading_1.indexOf(parent, anchor);
    parent.children.splice(index, 1, source);
}
exports.replaceInTree = replaceInTree;
function clone(tree) {
    return cloneDeep_1.default(tree);
}
exports.clone = clone;
//# sourceMappingURL=manipulation.js.map

/***/ }),

/***/ 2334:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
function findById(tree, id) {
    if (tree.id === id)
        return tree;
    var component = null;
    if (tree.children) {
        var i = 0;
        while (i < tree.children.length && !component) {
            component = findById(tree.children[i], id);
            i++;
        }
    }
    return component;
}
exports.findById = findById;
function findByAttribute(tree, attributeName, attributeValue) {
    var components = [];
    if (tree.children) {
        components = tree.children.reduce(function (result, child) { return (__spreadArrays(result, findByAttribute(child, attributeName, attributeValue))); }, []);
    }
    var hasAttributeName = tree[attributeName] !== undefined;
    var hasCorrectAttributeValue = attributeValue === undefined
        || tree[attributeName] === attributeValue;
    if (hasAttributeName && hasCorrectAttributeValue)
        components.push(tree);
    return components;
}
exports.findByAttribute = findByAttribute;
function findByType(tree, type) {
    return findByAttribute(tree, '_beagleComponent_', type);
}
exports.findByType = findByType;
function findParentByChildId(tree, childId) {
    if (!tree.children)
        return null;
    var i = 0;
    var parent = null;
    while (i < tree.children.length && !parent) {
        var child = tree.children[i];
        if (child.id === childId)
            parent = tree;
        else
            parent = findParentByChildId(child, childId);
        i++;
    }
    return parent;
}
exports.findParentByChildId = findParentByChildId;
function indexOf(node, childId) {
    if (!node.children)
        return -1;
    for (var i = 0; i < node.children.length; i++) {
        if (node.children[i].id === childId)
            return i;
    }
    return -1;
}
exports.indexOf = indexOf;
//# sourceMappingURL=reading.js.map

/***/ }),

/***/ 7129:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var beagle_tree_1 = __importDefault(__webpack_require__(7853));
var string_1 = __importDefault(__webpack_require__(8490));
var string_2 = __importDefault(__webpack_require__(8490));
var render_1 = __importDefault(__webpack_require__(7622));
var navigator_1 = __importDefault(__webpack_require__(7774));
function createBeagleView(beagleService, networkOptions, initialControllerId) {
    var currentUITree;
    var listeners = [];
    var errorListeners = [];
    var navigationControllers = beagleService.getConfig().navigationControllers;
    var initialNavigationHistory = [{ routes: [], controllerId: initialControllerId }];
    var navigator = navigator_1.default.create(navigationControllers, initialNavigationHistory);
    var renderer = {};
    var unsubscribeFromGlobalContext = function () { };
    function subscribe(listener) {
        listeners.push(listener);
        return function () {
            var index = listeners.indexOf(listener);
            if (index !== -1)
                listeners.splice(index, 1);
        };
    }
    function addErrorListener(listener) {
        errorListeners.push(listener);
        return function () {
            var index = errorListeners.indexOf(listener);
            if (index !== -1)
                errorListeners.splice(index, 1);
        };
    }
    function setTree(newUITree) {
        currentUITree = newUITree;
    }
    function runListeners(viewTree) {
        listeners.forEach(function (l) { return l(viewTree); });
    }
    function runErrorListeners(errors) {
        errorListeners.forEach(function (l) { return l(errors); });
    }
    /* todo: beagleView.fetch has been deprecated. This  function is still needed for internal usage,
    but we can probably simplify it a lot once we no longer need to support beagleView.fetch
    (v2.0). */
    function fetch(params, elementId, mode) {
        if (mode === void 0) { mode = 'replaceComponent'; }
        return __awaiter(this, void 0, void 0, function () {
            function onChangeTree(loadedTree) {
                setTree(originalTree); // changes should be made based on the original tree
                renderer.doFullRender(loadedTree, elementId, mode);
            }
            var path, url, originalTree, fallbackUIElement, initialNavigationHistory_1, errors_1;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        path = string_1.default.addPrefix(params.path, '/');
                        url = beagleService.urlBuilder.build(path);
                        originalTree = currentUITree;
                        fallbackUIElement = params.fallback;
                        // todo: legacy code. remove the following "if" in v2.0.
                        // if this is equivalent to a first navigation, reflect it in the navigator
                        if (navigator.isEmpty() && !elementId && mode === 'replaceComponent') {
                            initialNavigationHistory_1 = [
                                {
                                    routes: [{ url: path }],
                                    controllerId: initialControllerId,
                                }
                            ];
                            navigator = navigator_1.default.create(navigationControllers, initialNavigationHistory_1);
                            // eslint-disable-next-line
                            setupNavigation();
                        }
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, beagleService.viewClient.load({
                                url: url,
                                fallbackUIElement: fallbackUIElement,
                                onChangeTree: onChangeTree,
                                errorComponent: params.errorComponent,
                                loadingComponent: params.loadingComponent,
                                headers: params.headers,
                                method: params.method,
                                shouldShowError: params.shouldShowError,
                                shouldShowLoading: params.shouldShowLoading,
                                strategy: params.strategy,
                                retry: function () { return fetch(params, elementId, mode); },
                            })];
                    case 2:
                        _a.sent();
                        return [3 /*break*/, 4];
                    case 3:
                        errors_1 = _a.sent();
                        // removes the loading component when an error component should not be rendered
                        if (params.shouldShowLoading && !params.shouldShowError)
                            setTree(originalTree);
                        if (errorListeners.length === 0)
                            logger_1.default.error.apply(logger_1.default, errors_1);
                        runErrorListeners(errors_1);
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    function getTree() {
        // to avoid errors, we should never give access to our own tree to third parties
        return beagle_tree_1.default.clone(currentUITree);
    }
    function destroy() {
        unsubscribeFromGlobalContext();
        navigator.destroy();
    }
    // todo: legacy code. Remove this function with v2.0.
    function updateWithFetch(params, elementId, mode) {
        if (mode === void 0) { mode = 'replace'; }
        var newMode = mode === 'replace' ? 'replaceComponent' : mode;
        return fetch(params, elementId, newMode);
    }
    // todo: legacy code. Remove this function with v2.0.
    function updateWithTree(_a) {
        var sourceTree = _a.sourceTree, _b = _a.middlewares, middlewares = _b === void 0 ? [] : _b, _c = _a.mode, mode = _c === void 0 ? 'replace' : _c, _d = _a.shouldRunListeners, shouldRunListeners = _d === void 0 ? true : _d, _e = _a.shouldRunMiddlewares, shouldRunMiddlewares = _e === void 0 ? true : _e, elementId = _a.elementId;
        var warnings = [
            '"updateWithTree" has been deprecated and now exists only as a compatibility mode. This will be fully removed in v2.0. Please, consider updating your code.',
        ];
        var errors = [];
        if (middlewares.length) {
            errors.push('The option "middlewares" in "updateWithTree" is no longer supported. Please contact the Beagle WEB team for further guidance.');
        }
        if (!shouldRunListeners) {
            errors.push('The option "shouldRunListeners" in "updateWithTree" is no longer supported. Please contact the Beagle WEB team for further guidance.');
        }
        var newMode = mode === 'replace' ? 'replaceComponent' : mode;
        if (shouldRunMiddlewares)
            renderer.doFullRender(sourceTree, elementId, newMode);
        else {
            warnings.push('The option "shouldRunMiddlewares" in "updateWithTree" is no longer fully supported and might not have the desired effect. If the behavior you\'re getting differs from what you expected, please contact the Beagle WEB team.');
            renderer.doPartialRender(sourceTree, elementId, newMode);
        }
        logger_1.default.warn.apply(logger_1.default, warnings);
        if (errors.length)
            logger_1.default.error.apply(logger_1.default, errors);
    }
    function getNetworkOptions() {
        return networkOptions && __assign({}, networkOptions);
    }
    var beagleView = {
        subscribe: subscribe,
        addErrorListener: addErrorListener,
        getRenderer: function () { return renderer; },
        getTree: getTree,
        getNavigator: function () { return navigator; },
        getBeagleService: function () { return beagleService; },
        destroy: destroy,
        // todo: legacy code. Remove the following 3 properties with v2.0.
        fetch: function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i] = arguments[_i];
            }
            logger_1.default.warn('beagleView.fetch has been deprecated to avoid inconsistencies with the internal Beagle Navigator. It will be removed with version 2.0. If you want to change the current view according to a new url, consider using the navigator instead.');
            return fetch.apply(void 0, args);
        },
        updateWithFetch: updateWithFetch,
        updateWithTree: updateWithTree,
        getNetworkOptions: getNetworkOptions,
    };
    function createRenderer() {
        renderer = render_1.default.create({
            beagleView: beagleView,
            actionHandlers: beagleService.actionHandlers,
            operationHandlers: beagleService.operationHandlers,
            childrenMetadata: beagleService.childrenMetadata,
            executionMode: 'development',
            lifecycleHooks: beagleService.lifecycleHooks,
            renderToScreen: runListeners,
            setTree: setTree,
            typesMetadata: {},
        });
    }
    function setupNavigation() {
        var _this = this;
        navigator.subscribe(function (route, navigationController) { return __awaiter(_this, void 0, void 0, function () {
            var urlBuilder, preFetcher, analyticsService, screen, _a, url, fallback, shouldPrefetch, path, preFetchedUrl, preFetchedView, _b, platform;
            return __generator(this, function (_c) {
                switch (_c.label) {
                    case 0:
                        urlBuilder = beagleService.urlBuilder, preFetcher = beagleService.preFetcher, analyticsService = beagleService.analyticsService;
                        screen = route.screen;
                        _a = route, url = _a.url, fallback = _a.fallback, shouldPrefetch = _a.shouldPrefetch;
                        if (screen)
                            return [2 /*return*/, renderer.doFullRender(screen)];
                        if (!shouldPrefetch) return [3 /*break*/, 4];
                        path = string_2.default.addPrefix(url, '/');
                        preFetchedUrl = urlBuilder.build(path);
                        _c.label = 1;
                    case 1:
                        _c.trys.push([1, 3, , 4]);
                        return [4 /*yield*/, preFetcher.recover(preFetchedUrl)];
                    case 2:
                        preFetchedView = _c.sent();
                        return [2 /*return*/, renderer.doFullRender(preFetchedView)];
                    case 3:
                        _b = _c.sent();
                        return [3 /*break*/, 4];
                    case 4: return [4 /*yield*/, fetch(__assign(__assign({ path: url, fallback: fallback }, networkOptions), navigationController))];
                    case 5:
                        _c.sent();
                        platform = beagleService.getConfig().platform;
                        analyticsService.createScreenRecord(route, platform);
                        return [2 /*return*/];
                }
            });
        }); });
    }
    function setupGlobalContext() {
        unsubscribeFromGlobalContext = beagleService.globalContext.subscribe(function () { return renderer.doPartialRender(getTree()); });
    }
    createRenderer();
    setupNavigation();
    setupGlobalContext();
    return beagleView;
}
exports.default = {
    create: createBeagleView,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 7774:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var cloneDeep_1 = __importDefault(__webpack_require__(361));
var last_1 = __importDefault(__webpack_require__(928));
var nth_1 = __importDefault(__webpack_require__(8491));
var find_1 = __importDefault(__webpack_require__(3311));
var BeagleNavigationError_1 = __importDefault(__webpack_require__(2443));
var logger_1 = __importDefault(__webpack_require__(2611));
var findLastIndex_1 = __importDefault(__webpack_require__(7436));
var createBeagleNavigator = function (navigationControllers, initialValue) {
    var navigation = initialValue ? cloneDeep_1.default(initialValue) : [];
    var isNavigationInProgress = false;
    var isDestroyed = false;
    var defaultNavigationController = find_1.default(navigationControllers, { default: true }) || {};
    var listeners = [];
    function getNavigationController(controllerId) {
        if (!controllerId)
            return defaultNavigationController;
        if (!navigationControllers || !navigationControllers[controllerId]) {
            logger_1.default.warn("No navigation controller with id " + controllerId + " has been found. Using the default navigation controller.");
            return defaultNavigationController;
        }
        return navigationControllers[controllerId];
    }
    function subscribe(listener) {
        listeners.push(listener);
        return function () {
            var index = listeners.indexOf(listener);
            if (index !== -1)
                listeners.splice(index, 1);
        };
    }
    function isSingleStack() {
        return navigation.length < 2;
    }
    function isSingleRoute() {
        return isSingleStack() && (!navigation[0] || navigation[0].routes.length < 2);
    }
    function getCurrentStack() {
        var stack = last_1.default(navigation);
        if (!stack)
            throw new BeagleNavigationError_1.default('Navigation has no stacks!');
        return stack;
    }
    function getCurrentRoute() {
        var stack = getCurrentStack();
        var currentRoute = last_1.default(stack.routes);
        return currentRoute;
    }
    function getPreviousStack() {
        var stack = nth_1.default(navigation, -2);
        if (!stack)
            throw new BeagleNavigationError_1.default('Only one navigation stack! Can\'t get previous!');
        return stack;
    }
    function getPreviousRoute() {
        var currentStack = getCurrentStack();
        var route = currentStack.routes.length > 1
            ? nth_1.default(currentStack.routes, -2)
            : last_1.default(getPreviousStack().routes);
        if (!route)
            throw new BeagleNavigationError_1.default('Only one route! Can\'t get previous!');
        return route;
    }
    function runListeners(route) {
        var controllerId = navigation.length ? getCurrentStack().controllerId : undefined;
        var navigationController = getNavigationController(controllerId);
        return Promise.all(listeners.map(function (l) { return l(route, navigationController); }));
    }
    function isRouteIdentifiedBy(route, id) {
        return ('url' in route && route.url === id) ||
            // todo: remove screenComponent.identifier with the release of v2.0.0"
            ('screen' in route && (route.screen.identifier === id || route.screen.id === id));
    }
    function navigate(type, route, controllerId) {
        return __awaiter(this, void 0, void 0, function () {
            var handlers;
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        handlers = {
                            pushStack: function () { return __awaiter(_this, void 0, void 0, function () {
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (!route || typeof route === 'string') {
                                                throw new BeagleNavigationError_1.default("Invalid route for pushStack. Expected: Route object. Received: " + route + ".");
                                            }
                                            return [4 /*yield*/, runListeners(route)];
                                        case 1:
                                            _a.sent();
                                            navigation.push({ routes: [route], controllerId: controllerId });
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            popStack: function () { return __awaiter(_this, void 0, void 0, function () {
                                var route;
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (isSingleStack()) {
                                                throw new BeagleNavigationError_1.default('It was not possible to pop a stack because Beagle Navigator has not than one recorded stack.');
                                            }
                                            route = last_1.default(getPreviousStack().routes);
                                            return [4 /*yield*/, runListeners(route)];
                                        case 1:
                                            _a.sent();
                                            navigation.pop();
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            pushView: function () { return __awaiter(_this, void 0, void 0, function () {
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (!route || typeof route === 'string') {
                                                throw new BeagleNavigationError_1.default("Invalid route for pushView. Expected: Route object. Received: " + route + ".");
                                            }
                                            return [4 /*yield*/, runListeners(route)];
                                        case 1:
                                            _a.sent();
                                            if (navigation.length === 0)
                                                navigation.push({ routes: [] });
                                            getCurrentStack().routes.push(route);
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            popView: function () { return __awaiter(_this, void 0, void 0, function () {
                                var currentStack;
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (isSingleRoute()) {
                                                throw new BeagleNavigationError_1.default('It was not possible to pop a view because Beagle Navigator has not more than one recorded route');
                                            }
                                            return [4 /*yield*/, runListeners(getPreviousRoute())];
                                        case 1:
                                            _a.sent();
                                            currentStack = getCurrentStack();
                                            currentStack.routes.pop();
                                            if (currentStack.routes.length <= 0)
                                                navigation.pop();
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            popToView: function () { return __awaiter(_this, void 0, void 0, function () {
                                var currentStack, index;
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (!route || typeof route !== 'string') {
                                                throw new BeagleNavigationError_1.default("Invalid route for popToView. Expected: string. Received: " + route + ".");
                                            }
                                            currentStack = getCurrentStack();
                                            index = findLastIndex_1.default(currentStack.routes, function (r) { return isRouteIdentifiedBy(r, route); });
                                            if (index === -1)
                                                throw new BeagleNavigationError_1.default('The route does not exist in the current stack');
                                            return [4 /*yield*/, runListeners(currentStack.routes[index])];
                                        case 1:
                                            _a.sent();
                                            currentStack.routes.splice(index + 1);
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            resetStack: function () { return __awaiter(_this, void 0, void 0, function () {
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (!route || typeof route === 'string') {
                                                throw new BeagleNavigationError_1.default("Invalid route for pushView. Expected: Route object. Received: " + route + ".");
                                            }
                                            return [4 /*yield*/, runListeners(route)];
                                        case 1:
                                            _a.sent();
                                            navigation.pop();
                                            navigation.push({ routes: [route], controllerId: controllerId });
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                            resetApplication: function () { return __awaiter(_this, void 0, void 0, function () {
                                return __generator(this, function (_a) {
                                    switch (_a.label) {
                                        case 0:
                                            if (!route || typeof route === 'string') {
                                                throw new BeagleNavigationError_1.default("Invalid route for resetApplication. Expected: Route object. Received: " + route + ".");
                                            }
                                            return [4 /*yield*/, runListeners(route)];
                                        case 1:
                                            _a.sent();
                                            navigation = [{ routes: [route], controllerId: controllerId }];
                                            return [2 /*return*/];
                                    }
                                });
                            }); },
                        };
                        if (isDestroyed) {
                            throw new BeagleNavigationError_1.default('Can\'t perform navigation on a navigator that has already been destroyed.');
                        }
                        if (isNavigationInProgress) {
                            throw new BeagleNavigationError_1.default("Another navigation is already in progress. Can't navigate to " + route + ".");
                        }
                        isNavigationInProgress = true;
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, , 3, 4]);
                        return [4 /*yield*/, handlers[type]()];
                    case 2:
                        _a.sent();
                        return [3 /*break*/, 4];
                    case 3:
                        isNavigationInProgress = false;
                        return [7 /*endfinally*/];
                    case 4: return [2 /*return*/];
                }
            });
        });
    }
    function get() {
        return cloneDeep_1.default(navigation);
    }
    function destroy() {
        isDestroyed = true;
    }
    function isEmpty() {
        var numberOfRoutes = navigation.reduce(function (result, stack) { return result + stack.routes.length; }, 0);
        return numberOfRoutes === 0;
    }
    return {
        pushStack: function (route, controllerId) { return navigate('pushStack', route, controllerId); },
        popStack: function () { return navigate('popStack'); },
        pushView: function (route) { return navigate('pushView', route); },
        popView: function () { return navigate('popView'); },
        popToView: function (route) { return navigate('popToView', route); },
        resetStack: function (route, controllerId) { return navigate('resetStack', route, controllerId); },
        resetApplication: function (route, controllerId) { return (navigate('resetApplication', route, controllerId)); },
        subscribe: subscribe,
        get: get,
        navigate: navigate,
        isEmpty: isEmpty,
        destroy: destroy,
        getCurrentRoute: getCurrentRoute,
    };
};
exports.default = {
    create: createBeagleNavigator,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 5252:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var object_1 = __importDefault(__webpack_require__(2413));
var expression_1 = __importDefault(__webpack_require__(2937));
var IGNORE_COMPONENT_KEYS = ['id', 'context', 'children', '_beagleComponent_'];
function isBeagleAction(data) {
    return (data
        && typeof data === 'object'
        && (data._beagleAction_ || (Array.isArray(data) && data[0] && data[0]._beagleAction_)));
}
function deserializeAction(actionOrActionList, eventName, params) {
    var actionList = Array.isArray(actionOrActionList) ? actionOrActionList : [actionOrActionList];
    var beagleService = params.beagleView.getBeagleService();
    return function (event) {
        var hierarchy = event !== undefined
            ? __spreadArrays(params.contextHierarchy, [{ id: eventName, value: event }]) : params.contextHierarchy;
        // function used to deserialize and execute actions inside another action
        var executeSubAction = function (actionOrActionList, eventName, event) {
            if (eventName === void 0) { eventName = ''; }
            deserializeAction(actionOrActionList, eventName, __assign(__assign({}, params), { contextHierarchy: hierarchy }))(event);
        };
        actionList.forEach(function (action) {
            var handler = object_1.default.getValueByCaseInsensitiveKey(params.actionHandlers, action._beagleAction_);
            if (!handler) {
                logger_1.default.warn("Beagle: couldn't find an action handler for \"" + action._beagleAction_ + "\"");
                return;
            }
            handler({
                action: expression_1.default.resolveForAction(action, hierarchy, params.operationHandlers),
                beagleView: params.beagleView,
                element: params.component,
                executeAction: executeSubAction,
            });
            var route = params.beagleView.getNavigator().getCurrentRoute();
            var platform = beagleService.getConfig().platform || '';
            if (route)
                beagleService.analyticsService.createActionRecord(action, eventName, params.component, platform, route);
        });
    };
}
function findAndDeserializeActions(data, propertyName, params) {
    if (data && typeof data === 'object' && data._beagleComponent_)
        return data;
    if (isBeagleAction(data))
        return deserializeAction(data, propertyName, params);
    if (Array.isArray(data)) {
        return data.map(function (item) { return findAndDeserializeActions(item, propertyName, params); });
    }
    if (data && typeof data === 'object') {
        var keys = Object.keys(data);
        keys.forEach(function (key) { return data[key] = findAndDeserializeActions(data[key], key, params); });
    }
    return data;
}
/**
 * De-serializes every Beagle Action in the component into a function. If any expression is used
 * inside the Beagle Action, `params.contextHierarchy` will be used as the data source to retrieve
 * the expressions values.
 *
 * Actions that are properties of another action are not deserialized by this function. This
 * function only de-serializes actions that are part of the component interface. The responsibility
 * to de-serialize an action inside another action is of the action handler. See the example below:
 *
 * ```json
 * {
 *   "_beagleComponent_": "beagle:button",
 *   "text": "click to send request",
 *   "onPress": {
 *     "_beagleAction_": "beagle:sendRequest",
 *     "url": "https://docs.usebeagle.com",
 *     "onSuccess": {
 *       "_beagleAction_": "beagle:alert",
 *       "message": "Successfully recovered the docs!"
 *     }
 *   }
 * }
 * ```
 *
 * In the example above, "onPress" is deserialized when running this function, but "onSuccess" is
 * not. The action handler for the action "beagle:sendRequest" is the one responsible for
 * deserializing "onSuccess".
 *
 * This function alters `params.component`, i.e. this is not a pure function.
 *
 * @param params set of parameters to perform the de-serialization. The key-value map must contain
 * the following:
 * - `component`: the component whose actions must be deserialized.
 * - `contextHierarchy`: context hierarchy to use for expressions inside a Beagle Action.
 * - `beagleView`: the current BeagleView, needed to perform view updates on certain actions.
 * - `actionHandlers`: map relating each beagle action identifier to an action handler.
 */
function deserialize(params) {
    var keys = Object.keys(params.component);
    keys.forEach(function (key) {
        if (IGNORE_COMPONENT_KEYS.includes(key))
            return;
        params.component[key] = findAndDeserializeActions(params.component[key], key, params);
    });
}
exports.default = {
    deserialize: deserialize,
};
//# sourceMappingURL=action.js.map

/***/ }),

/***/ 6574:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var CHILDREN_PROPERTY_NAMES = ['child'];
exports.ID_PREFIX = '_beagle_';
var nextId = 1;
/**
 * Guarantees that the children of the component will be called "children" and will be an array.
 *
 * This function alters the parameter `component`.
 *
 * @param component the component to have its children property formatted
 * @param childrenMetadata the children metadata of the component, it tells which property of the
 * component should act as its children
 */
function formatChildrenProperty(component, childrenMetadata) {
    var properties = childrenMetadata && childrenMetadata.property
        ? __spreadArrays([childrenMetadata.property], CHILDREN_PROPERTY_NAMES) : CHILDREN_PROPERTY_NAMES;
    properties.find(function (property) {
        if (!component[property])
            return false;
        component.children = component[property];
        delete component[property];
        return true;
    });
    if (component.children && !Array.isArray(component.children)) {
        component.children = [component.children];
    }
}
/**
 * Assigns an id to the component if it doesn't have an id yet. The id follows the format
 * `_beagle_%d`, where %d is in incremental integer, starting at 1.
 *
 * This function alters the parameter `component`.
 *
 * @param component the component to have its id assigned
 */
function assignId(component) {
    component.id = component.id || "" + exports.ID_PREFIX + nextId++;
}
/**
 * Removes every property in `component` that has the value `null`. Ignores properties inside
 * `children`.
 *
 * This function alters the parameter `component`.
 *
 * @param component the component to have the null properties removed
 */
function eraseNullProperties(component) {
    function eraseNulls(data) {
        if (!data || typeof data !== 'object')
            return;
        if (Array.isArray(data)) {
            data.forEach(eraseNulls);
            return;
        }
        var keys = Object.keys(data);
        keys.forEach(function (key) {
            if (data[key] === null)
                delete data[key];
            else if (data !== component || key !== 'children')
                eraseNulls(data[key]);
        });
    }
    eraseNulls(component);
}
exports.default = {
    formatChildrenProperty: formatChildrenProperty,
    assignId: assignId,
    eraseNullProperties: eraseNullProperties,
};
//# sourceMappingURL=component.js.map

/***/ }),

/***/ 395:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var findLast_1 = __importDefault(__webpack_require__(988));
var last_1 = __importDefault(__webpack_require__(928));
var BeagleParseError_1 = __importDefault(__webpack_require__(7689));
var RESERVED_WORDS = ['global', 'true', 'false', 'null'];
function checkContextId(component) {
    if (!component.context)
        return;
    var contextId = component.context.id;
    if (component.context.id.match(/^\d+(\.\d+)?$/)) {
        throw new BeagleParseError_1.default("Numbers are not valid context ids. Please, rename the context with id \"" + contextId + "\".");
    }
    RESERVED_WORDS.forEach(function (word) {
        if (contextId === word) {
            throw new BeagleParseError_1.default("The context id \"" + word + "\" is a reserved word and can't be used as a context id. Please, rename your context.");
        }
    });
}
function getContexts(component, includeImplicitContexts) {
    var contexts = [];
    if (component.context)
        contexts.push(component.context);
    if (includeImplicitContexts && component._implicitContexts_) {
        component._implicitContexts_.forEach(function (c) { return contexts.push(c); });
    }
    return contexts;
}
/**
 * Parses a tree looking for the context hierarchy of each component. The context hierarchy of a
 * component is a stack of contexts. The top position of the stack will be the closest context
 * to the component, while the bottom will be the farthest.
 *
 * @param viewTree the tree to parse the contexts from
 * @param globalContexts optional. Will consider these global contexts when evaluating the tree
 * @param includeImplicitContexts optional, default is true. When false, contexts declared via the
 * key `_implicitContexts_` will be ignored.
 * @returns a map where the key corresponds to the component id and the value to the context
 * hierarchy of the component.
 */
function evaluate(viewTree, globalContexts, includeImplicitContexts) {
    if (globalContexts === void 0) { globalContexts = []; }
    if (includeImplicitContexts === void 0) { includeImplicitContexts = true; }
    var contextMap = {};
    function evaluateContextHierarchy(component, contextHierarchy) {
        checkContextId(component);
        var hierarchy = __spreadArrays(contextHierarchy, getContexts(component, includeImplicitContexts));
        contextMap[component.id] = hierarchy;
        if (!component.children)
            return;
        component.children.forEach(function (child) { return evaluateContextHierarchy(child, hierarchy); });
    }
    evaluateContextHierarchy(viewTree, globalContexts);
    return contextMap;
}
/**
 * Finds a context in a context hierarchy. A context hierarchy is the stack of contexts available
 * to a component or action.
 *
 * If there's no context with `id === contextId`, undefined is returned.
 *
 * @param contextHierarchy stack of contexts available, top value has the greatest priority
 * @param contextId the id of the context to find. If not specified, the context in the top of the
 * stack is returned
 */
function find(contextHierarchy, contextId) {
    return contextId
        ? findLast_1.default(contextHierarchy, { id: contextId })
        : last_1.default(contextHierarchy);
}
exports.default = {
    evaluate: evaluate,
    find: find,
};
//# sourceMappingURL=context.js.map

/***/ }),

/***/ 2937:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var get_1 = __importDefault(__webpack_require__(7361));
var automaton_1 = __importDefault(__webpack_require__(3039));
var BeagleNotFoundError_1 = __importDefault(__webpack_require__(8284));
var BeagleParseError_1 = __importDefault(__webpack_require__(7689));
var context_1 = __importDefault(__webpack_require__(395));
var expressionRegex = /(\\*)@\{(([^'\}]|('([^'\\]|\\.)*'))*)\}/g;
var fullExpressionRegex = /^@\{(([^'\}]|('([^'\\]|\\.)*'))*)\}$/;
var IGNORE_COMPONENT_KEYS = ['id', 'context', 'children', '_beagleComponent_'];
function getContextBindingValue(path, contextHierarchy) {
    if (!path.match(/^[\w\d_]+(\[\d+\])*(\.([\w\d_]+(\[\d+\])*))*$/)) {
        throw new BeagleParseError_1.default("invalid path \"" + path + "\". Please, make sure your variable names contain only letters, numbers and the symbol \"_\". To access substructures use \".\" and to access array indexes use \"[index]\".");
    }
    var pathMatch = path.match(/^([^\.\[\]]+)\.?(.*)/);
    if (!pathMatch || pathMatch.length < 1)
        return;
    var contextId = pathMatch[1];
    var contextPath = pathMatch[2];
    var context = context_1.default.find(contextHierarchy, contextId);
    if (!context)
        throw new BeagleNotFoundError_1.default("Couldn't find context with id \"" + contextId + "\"");
    return contextPath ? get_1.default(context.value, contextPath, null) : context.value;
}
/**
 * The parameters of an operation can only be represented by a context free grammar, i.e. we can't
 * recognize each parameter with a simple regular expression. We need a deterministic pushdown
 * automaton (DPA) to do this. This function is the automaton to solve this problem, it recognizes
 * each parameter in the operation and return an array of parameters as its result.
 *
 * @param input the input to the automaton
 * @returns an array of parameters
 */
function parseParameters(parameterString) {
    var transitions = {
        initial: [
            { read: /,|$/, next: 'final' },
            { read: '(', push: '(', next: 'insideParameterList' },
            { read: /'([^']|(\\.))*'/, next: 'initial' },
            { read: /[^\)]/, next: 'initial' },
        ],
        insideParameterList: [
            { read: '(', push: '(', next: 'insideParameterList' },
            { read: ')', pop: '(', next: 'isParameterListOver' },
            { read: /'([^']|(\\.))*'/, next: 'insideParameterList' },
            { read: /./, next: 'insideParameterList' },
        ],
        isParameterListOver: [
            { pop: automaton_1.default.empty, next: 'initial' },
            { next: 'insideParameterList' },
        ],
    };
    var dpa = automaton_1.default.createDPA({ initial: 'initial', final: 'final', transitions: transitions });
    var parameters = [];
    var position = 0;
    while (position < parameterString.length) {
        var match = dpa.match(parameterString.substr(position));
        if (!match)
            throw new BeagleParseError_1.default("wrong format for parameters: " + parameterString);
        parameters.push(match.replace(/,$/, '').trim());
        position += match.length;
    }
    return parameters;
}
function getOperationValue(operation, contextHierarchy, operationHandlers) {
    var match = operation.match(/^(\w+)\((.*)\)$/);
    if (!match) {
        throw new BeagleParseError_1.default("invalid operation in expression: " + operation);
    }
    var operationName = match[1];
    var paramString = match[2];
    if (!operationHandlers[operationName]) {
        throw new BeagleNotFoundError_1.default("operation with name \"" + operationName + "\" doesn't exist.");
    }
    var params = parseParameters(paramString);
    // eslint-disable-next-line
    var resolvedParams = params.map(function (param) { return evaluateExpression(param, contextHierarchy, operationHandlers); });
    var fn = operationHandlers[operationName];
    return fn.apply(void 0, resolvedParams);
}
function getLiteralValue(literal) {
    // true, false or null
    if (literal === 'true')
        return true;
    if (literal === 'false')
        return false;
    if (literal === 'null')
        return null;
    // number
    if (literal.match(/^\d+(\.\d+)?$/))
        return parseFloat(literal);
    // string
    if (literal.startsWith('\'') && literal.endsWith('\'')) {
        return literal.replace(/(^')|('$)/g, '').replace(/\\'/g, '\'');
    }
}
function evaluateExpression(expression, contextHierarchy, operationHandlers) {
    var literalValue = getLiteralValue(expression);
    if (literalValue !== undefined)
        return literalValue;
    var isOperation = expression.includes('(');
    if (isOperation)
        return getOperationValue(expression, contextHierarchy, operationHandlers);
    // otherwise, it's a context binding
    return getContextBindingValue(expression, contextHierarchy);
}
function resolveExpressionsInString(str, contextHierarchy, operationHandlers) {
    var fullMatch = str.match(fullExpressionRegex);
    if (fullMatch) {
        try {
            var bindingValue = evaluateExpression(fullMatch[1], contextHierarchy, operationHandlers);
            return bindingValue === undefined ? str : bindingValue;
        }
        catch (error) {
            logger_1.default.warn(error);
            return null;
        }
    }
    return str.replace(expressionRegex, function (bindingStr, slashes, path) {
        var isBindingScaped = slashes.length % 2 === 1;
        var scapedSlashes = slashes.replace(/\\\\/g, '\\');
        if (isBindingScaped)
            return scapedSlashes.replace(/\\$/, '') + "@{" + path + "}";
        var bindingValue;
        try {
            bindingValue = evaluateExpression(path, contextHierarchy, operationHandlers);
        }
        catch (error) {
            logger_1.default.warn(error);
        }
        var asString = (bindingValue && typeof bindingValue === 'object') ? JSON.stringify(bindingValue) : bindingValue;
        return (bindingValue === undefined || bindingValue === null) ? scapedSlashes : "" + scapedSlashes + asString;
    });
}
/**
 * Replaces every reference to an expression in `data` (parameter) by its value in the
 * `contextHierarchy` (parameter). An expression is every non scaped string in the format
 * `@{expression}`. The context hierarchy is a stack of contexts where the top position has the top
 * priority, i.e. will be the first to be matched against the expression.
 *
 * If an expression has no match, it will be kept unchanged.
 *
 * If the third parameter (`shouldIgnore`) is passed, before evaluating a property of an object,
 * this function will be called with the value and key of the property. If
 * `shouldIgnore(value, key)` returns true, the entire property will be skipped and its expressions
 * won't be evaluated.
 *
 * This function doesn't alter its parameters, instead, the data with the expressions replaced will
 * be its return value.
 *
 * @param data the data with the expressions to be replaced.
 * @param contextHierarchy the data source to search for the values of the expressions.
 * @param shouldIgnore optional. Function to verify if a property of an object should be ignored
 * @returns data with all its expressions replaced by their corresponding values.
 */
function resolve(data, contextHierarchy, operationHandlers, shouldIgnore) {
    if (typeof data === 'string')
        return resolveExpressionsInString(data, contextHierarchy, operationHandlers);
    if (Array.isArray(data)) {
        return data.map(function (item) { return resolve(item, contextHierarchy, operationHandlers, shouldIgnore); });
    }
    if (data && typeof data === 'object') {
        var map_1 = data;
        return Object.keys(map_1).reduce(function (result, key) {
            var _a, _b;
            var value = map_1[key];
            return (shouldIgnore && shouldIgnore(value, key))
                ? __assign(__assign({}, result), (_a = {}, _a[key] = value, _a)) : __assign(__assign({}, result), (_b = {}, _b[key] = resolve(value, contextHierarchy, operationHandlers, shouldIgnore), _b));
        }, {});
    }
    return data;
}
exports.resolve = resolve;
function isComponentOrComponentList(data) {
    return (data
        && typeof data === 'object'
        && (data._beagleComponent_ || (Array.isArray(data) && data[0] && data[0]._beagleComponent_)));
}
function isActionOrActionList(data) {
    return (data
        && typeof data === 'object'
        && (data._beagleAction_ || (Array.isArray(data) && data[0] && data[0]._beagleAction_)));
}
/**
 * Similar to `resolve`. It replaces every reference to an expression in `component` (parameter) by
 * its value in the `contextHierarchy` (parameter). The difference is that it takes into
 * consideration that it is a component and doesn't evaluate sub-components, actions or properties
 * that can't contain expressions (`id`, `context`, `children` and `_beagleComponent_`).
 *
 * When resolving expressions in a component it is important to ignore actions because an action
 * should only be evaluated when it's executed, and not when it's rendered.
 *
 * When resolving expressions in a component, it is important to ignore sub-components because
 * they haven't been rendered yet. Take for instance a component called list-view that has the
 * property "template", template is an array of components that doesn't render as soon as the view
 * is rendered, instead, it is used to create the component's children at runtime. We don't need to
 * resolve its expressions when rendering the list-view.
 *
 * @param component the component with the expressions to be replaced
 * @param contextHierarchy the data source to search for the values of the expressions
 * @returns the component with all its expressions replaced by their corresponding values
 */
function resolveForComponent(component, contextHierarchy, operationHandlers) {
    var shouldIgnore = function (value, key) { return (isComponentOrComponentList(value)
        || isActionOrActionList(value)
        || IGNORE_COMPONENT_KEYS.includes(key)); };
    return resolve(component, contextHierarchy, operationHandlers, shouldIgnore);
}
/**
 * Similar to `resolve`. It replaces every reference to an expression in `action` (parameter) by
 * its value in the `contextHierarchy` (parameter). The difference is that it takes into
 * consideration that it is an action and doesn't evaluate sub-components or sub-actions.
 *
 * When resolving expressions inside an action it is important to ignore sub-actions because an
 * action should only be evaluated when it's executed. Take the action "sendRequest" for instance,
 * we should not resolve the expressions in the sub-action "onSuccess", these should be resolved
 * only when "onSuccess" is actually executed, i.e. when the request completes successfully.
 *
 * When resolving expressions inside an action, it is important to ignore components because they
 * haven't been rendered yet. Take for instance the action "addChildren", we don't need to evaluate
 * the expressions of the components in "value", they will be evaluated when they actually get
 * rendered.
 *
 * @param action the action with the expressions to be replaced
 * @param contextHierarchy the data source to search for the values of the expressions
 * @returns the action with all its expressions replaced by their corresponding values
 */
function resolveForAction(action, contextHierarchy, operationHandlers) {
    var shouldIgnore = function (value) { return (isComponentOrComponentList(value)
        || isActionOrActionList(value)); };
    return resolve(action, contextHierarchy, operationHandlers, shouldIgnore);
}
exports.default = {
    resolve: resolve,
    resolveForComponent: resolveForComponent,
    resolveForAction: resolveForAction,
};
//# sourceMappingURL=expression.js.map

/***/ }),

/***/ 7622:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var beagle_tree_1 = __importDefault(__webpack_require__(7853));
var BeagleParseError_1 = __importDefault(__webpack_require__(7689));
var component_1 = __importDefault(__webpack_require__(6574));
var navigation_1 = __importDefault(__webpack_require__(4822));
var expression_1 = __importDefault(__webpack_require__(2937));
var action_1 = __importDefault(__webpack_require__(5252));
var context_1 = __importDefault(__webpack_require__(395));
var styling_1 = __importDefault(__webpack_require__(8360));
var type_checker_1 = __importDefault(__webpack_require__(611));
function createRenderer(_a) {
    var beagleView = _a.beagleView, setTree = _a.setTree, typesMetadata = _a.typesMetadata, renderToScreen = _a.renderToScreen, lifecycleHooks = _a.lifecycleHooks, childrenMetadata = _a.childrenMetadata, executionMode = _a.executionMode, actionHandlers = _a.actionHandlers, operationHandlers = _a.operationHandlers;
    var _b = beagleView.getBeagleService(), urlBuilder = _b.urlBuilder, preFetcher = _b.preFetcher, globalContext = _b.globalContext;
    function runGlobalLifecycleHook(viewTree, lifecycle) {
        if (Object.keys(viewTree).length === 0)
            return viewTree;
        var hook = lifecycleHooks[lifecycle].global;
        if (!hook)
            return viewTree;
        var newTree = hook(viewTree);
        return newTree || viewTree;
    }
    function isMalFormedComponent(component) {
        return !component || !component._beagleComponent_;
    }
    function runComponentLifecycleHook(component, lifecycle) {
        if (isMalFormedComponent(component)) {
            var componentStr = JSON.stringify(component, null, 2) || typeof component;
            throw new BeagleParseError_1.default("You have a malformed component, please check the view json. Detected at lifecycle \"" + lifecycle + "\". Component value:\n" + componentStr);
        }
        var hook = lifecycleHooks[lifecycle].components[component._beagleComponent_.toLowerCase()];
        if (!hook)
            return component;
        var newComponent = hook(component);
        return newComponent || component;
    }
    function runLifecycle(viewTree, lifecycle) {
        viewTree = runGlobalLifecycleHook(viewTree, lifecycle);
        return beagle_tree_1.default.replaceEach(viewTree, function (component) { return (runComponentLifecycleHook(component, lifecycle)); });
    }
    function preProcess(viewTree) {
        beagle_tree_1.default.forEach(viewTree, function (component) {
            component_1.default.formatChildrenProperty(component, childrenMetadata[component._beagleComponent_]);
            component_1.default.assignId(component);
            component_1.default.eraseNullProperties(component);
            navigation_1.default.preFetchViews(component, urlBuilder, preFetcher);
        });
        return viewTree;
    }
    function takeViewSnapshot(viewTree, anchor, mode) {
        var currentTree = beagleView.getTree();
        if (!currentTree)
            return setTree(viewTree);
        anchor = anchor || currentTree.id;
        if (mode === 'replaceComponent') {
            if (anchor === currentTree.id)
                currentTree = viewTree;
            else
                beagle_tree_1.default.replaceInTree(currentTree, viewTree, anchor);
        }
        else {
            beagle_tree_1.default.insertIntoTree(currentTree, viewTree, anchor, mode);
        }
        setTree(currentTree);
    }
    function evaluateComponents(viewTree) {
        var contextMap = context_1.default.evaluate(viewTree, [globalContext.getAsDataContext()]);
        return beagle_tree_1.default.replaceEach(viewTree, function (component) {
            action_1.default.deserialize({
                component: component,
                contextHierarchy: contextMap[component.id],
                actionHandlers: actionHandlers,
                operationHandlers: operationHandlers,
                beagleView: beagleView,
            });
            var resolved = expression_1.default.resolveForComponent(component, contextMap[component.id], operationHandlers);
            styling_1.default.convert(resolved);
            return resolved;
        });
    }
    function checkTypes(viewTree) {
        if (executionMode !== 'development')
            return;
        beagle_tree_1.default.forEach(viewTree, function (component) { return (type_checker_1.default.check(component, typesMetadata[component.id], childrenMetadata[component.id])); });
    }
    function doPartialRender(viewTree, anchor, mode) {
        if (anchor === void 0) { anchor = ''; }
        if (mode === void 0) { mode = 'replaceComponent'; }
        takeViewSnapshot(viewTree, anchor, mode);
        var view = beagleView.getTree();
        /* Next we are going to reprocess the entire tree. We're doing this because we need to guarantee
        that every action or expression will be correctly parsed. But, considering the occasions we'll
        be updating just a part of the tree, can't we store the last processed tree and use it instead
        of processing everything again? Todo: study this performance enhancement. */
        view = runLifecycle(view, 'afterViewSnapshot');
        view = evaluateComponents(view);
        view = runLifecycle(view, 'beforeRender');
        checkTypes(view);
        renderToScreen(view);
    }
    function doFullRender(viewTree, anchor, mode) {
        if (anchor === void 0) { anchor = ''; }
        if (mode === void 0) { mode = 'replaceComponent'; }
        viewTree = runLifecycle(viewTree, 'beforeStart');
        var viewTreeWithIds = preProcess(viewTree);
        viewTreeWithIds = runLifecycle(viewTreeWithIds, 'beforeViewSnapshot');
        doPartialRender(viewTreeWithIds, anchor, mode);
    }
    return {
        doPartialRender: doPartialRender,
        doFullRender: doFullRender,
    };
}
exports.default = {
    create: createRenderer,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 4822:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var flatten_1 = __importDefault(__webpack_require__(5564));
var navigation_1 = __importDefault(__webpack_require__(1922));
var string_1 = __importDefault(__webpack_require__(8490));
var logger_1 = __importDefault(__webpack_require__(2611));
var lowerCaseNavigationActions = Object.keys(navigation_1.default).map(function (key) { return key.toLowerCase(); });
function findNavigationActions(data, shouldIgnoreComponents) {
    if (shouldIgnoreComponents === void 0) { shouldIgnoreComponents = true; }
    if (!data || typeof data !== 'object' || (shouldIgnoreComponents && data._beagleComponent_)) {
        return [];
    }
    if (Array.isArray(data))
        return flatten_1.default(data.map(function (item) { return findNavigationActions(item); }));
    var result = [];
    var isNavigationAction = (typeof data._beagleAction_ === 'string'
        && lowerCaseNavigationActions.includes(data._beagleAction_.toLowerCase()));
    if (isNavigationAction)
        result.push(data);
    var keys = Object.keys(data);
    keys.forEach(function (key) { return result = __spreadArrays(result, findNavigationActions(data[key])); });
    return result;
}
function validateUrl(url) {
    if (!url)
        return false;
    var isDynamic = !!url.match(/@\{.+\}/);
    if (isDynamic)
        logger_1.default.warn("Dynamic URLs cannot be pre-fetched: " + url);
    return !isDynamic;
}
function preFetchWithWarning(preFetcher, url) {
    return __awaiter(this, void 0, void 0, function () {
        var error_1;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    _a.trys.push([0, 2, , 3]);
                    return [4 /*yield*/, preFetcher.fetch(url)];
                case 1:
                    _a.sent();
                    return [3 /*break*/, 3];
                case 2:
                    error_1 = _a.sent();
                    logger_1.default.warn(error_1);
                    return [3 /*break*/, 3];
                case 3: return [2 /*return*/];
            }
        });
    });
}
function preFetchViews(component, urlBuilder, preFetcher) {
    return __awaiter(this, void 0, void 0, function () {
        var navigationActions, promises;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    navigationActions = findNavigationActions(component, false);
                    promises = [];
                    navigationActions.forEach(function (action) {
                        var shouldPrefetch = action.route && action.route.shouldPrefetch;
                        var isUrlValid = action.route && validateUrl(action.route.url);
                        if (shouldPrefetch && isUrlValid) {
                            var path = string_1.default.addPrefix(action.route.url, '/');
                            var url = urlBuilder.build(path);
                            promises.push(preFetchWithWarning(preFetcher, url));
                        }
                    });
                    return [4 /*yield*/, Promise.all(promises)];
                case 1:
                    _a.sent();
                    return [2 /*return*/];
            }
        });
    });
}
exports.default = {
    preFetchViews: preFetchViews,
};
//# sourceMappingURL=navigation.js.map

/***/ }),

/***/ 8360:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var string_1 = __importDefault(__webpack_require__(8490));
var BEAGLE_STYLE_KEYS = [
    'size',
    'position',
    'flex',
    'cornerRadius',
    'margin',
    'padding',
    'positionType',
    'display',
    'backgroundColor',
    'borderWidth',
    'borderColor',
];
var UNITY_TYPE = {
    'REAL': 'px',
    'PERCENT': '%',
    'AUTO': 'auto',
};
var SINGLE_ATTRIBUTES = {
    'positionType': 'position',
    'backgroundColor': 'backgroundColor',
    'display': 'display',
    'borderColor': 'borderColor',
};
var EDGE_SPECIAL_VALUES = {
    'all': ['right', 'left', 'top', 'bottom'],
    'horizontal': ['right', 'left'],
    'vertical': ['top', 'bottom'],
};
var FLEX_PROPERTIES_TO_RENAME = {
    'grow': 'flexGrow',
    'shrink': 'flexShrink',
    'basis': 'flexBasis',
};
var SPECIAL_VALUES = {
    'NO_WRAP': 'nowrap',
};
var EDGE_ORDER = {
    'all': false,
    'vertical': false,
    'horizontal': false,
    'top': false,
    'right': false,
    'left': false,
    'bottom': false,
};
var POSITION_ORDER = {
    'all': false,
    'top': false,
    'right': false,
    'left': false,
    'bottom': false,
    'vertical': false,
    'horizontal': false,
};
function handleAutoAsValue(value, parsedUnitType) {
    return parsedUnitType === UNITY_TYPE.AUTO ? UNITY_TYPE.AUTO : "" + value + parsedUnitType;
}
function toLowerCase(value) {
    return typeof value === 'string' ? value.toLowerCase() : value;
}
function replace(text, value, targetValue) {
    return typeof text === 'string' ? text.replace(value, targetValue) : text;
}
function parseValuesWithUnit(unitType, value) {
    var parsedUnitType = UNITY_TYPE[unitType];
    if (value === undefined || value === null)
        return '';
    return handleAutoAsValue(value.toString(), parsedUnitType);
}
function handleAspectRatio(valueAspectRatio, heightData) {
    if (valueAspectRatio && heightData && heightData.value) {
        var value = heightData.value * valueAspectRatio;
        var valueWithType = parseValuesWithUnit(heightData.type, value);
        return { width: valueWithType };
    }
    return {};
}
function formatSizeProperty(size) {
    var css = {};
    if (!size || typeof size !== 'object')
        return css;
    var keys = Object.keys(size);
    var heightData = {};
    var valueAspectRatio = null;
    keys.forEach(function (key) {
        if (typeof size[key] === 'string')
            return;
        if (key !== 'aspectRatio') {
            css[key] = parseValuesWithUnit(size[key].type, size[key].value);
            if (key === 'height') {
                heightData = size[key];
            }
        }
        else {
            valueAspectRatio = size[key];
        }
    });
    return __assign(__assign({}, css), handleAspectRatio(valueAspectRatio, heightData));
}
function handleSpecialPosition(key, value) {
    var parsedNames = EDGE_SPECIAL_VALUES[key];
    return parsedNames.reduce(function (css, name) {
        var _a;
        return (__assign(__assign({}, css), (_a = {}, _a[name] = value, _a)));
    }, {});
}
function orderKeys(keys, orderRule) {
    var objectWithOrderRule = __assign({}, orderRule);
    keys.forEach(function (key) { return objectWithOrderRule[key] = true; });
    return Object.keys(objectWithOrderRule).filter(function (key) { return objectWithOrderRule[key]; });
}
function formatPositionProperty(position) {
    var css = {};
    if (!position)
        return css;
    if (typeof position !== 'object')
        return { position: position };
    var keys = Object.keys(position);
    keys = orderKeys(keys, POSITION_ORDER);
    keys.forEach(function (key) {
        if (typeof position[key] !== 'object')
            return;
        var valueWithType = parseValuesWithUnit(position[key].type, position[key].value);
        if (Object.keys(EDGE_SPECIAL_VALUES).includes(key)) {
            css = __assign(__assign({}, css), handleSpecialPosition(key, valueWithType));
        }
        else {
            css[key] = valueWithType;
        }
    });
    return css;
}
function formatFlexAttributes(flex) {
    var css = {};
    if (!flex)
        return css;
    if (typeof flex !== 'object')
        return { flex: flex };
    var keys = Object.keys(flex);
    keys.forEach(function (key) {
        var attributeName = FLEX_PROPERTIES_TO_RENAME[key] || key;
        var parsedValue;
        if (flex[key] && typeof flex[key] === 'object') {
            parsedValue = parseValuesWithUnit(flex[key].type, flex[key].value);
        }
        else {
            var hasSpecialValues = SPECIAL_VALUES[flex[key]];
            parsedValue = hasSpecialValues ? hasSpecialValues : replace(flex[key], '_', '-');
            parsedValue = toLowerCase(parsedValue);
        }
        css[attributeName] = parsedValue;
    });
    return css;
}
function formatCornerRadiusAttributes(cornerRadius) {
    return cornerRadius && typeof cornerRadius === 'object' && cornerRadius.radius
        ? { borderRadius: cornerRadius.radius + "px" }
        : {};
}
function handleSpecialEdge(key, value, prefixName) {
    var _a;
    if (key === 'all')
        return _a = {}, _a[prefixName] = value, _a;
    var parsedNames = EDGE_SPECIAL_VALUES[key];
    return parsedNames.reduce(function (css, name) {
        var _a;
        var cssName = "" + prefixName + string_1.default.capitalizeFirstLetter(name);
        return __assign(__assign({}, css), (_a = {}, _a[cssName] = value, _a));
    }, {});
}
function formatBorderStyle(style) {
    if (style.borderColor || style.borderWidth && !style.hasOwnProperty('borderStyle'))
        return { borderStyle: 'solid' };
}
function formatBorderWidthAttributes(style) {
    if (style)
        return { borderWidth: style + "px" };
}
function formatEdgeAttributes(style, edgeType) {
    var _a;
    var css = {};
    var edge = style[edgeType];
    if (!edge)
        return css;
    if (typeof edge !== 'object')
        return _a = {}, _a[edgeType] = edge, _a;
    var keys = Object.keys(edge);
    keys = orderKeys(keys, EDGE_ORDER);
    keys.forEach(function (key) {
        if (!edge[key] || typeof edge[key] !== 'object')
            return;
        var valueWithType = parseValuesWithUnit(edge[key].type, edge[key].value);
        if (Object.keys(EDGE_SPECIAL_VALUES).includes(key)) {
            css = __assign(__assign({}, css), handleSpecialEdge(key, valueWithType, edgeType));
        }
        else {
            var edgePosition = "" + edgeType + string_1.default.capitalizeFirstLetter(key);
            css[edgePosition] = valueWithType;
        }
    });
    return css;
}
function formatSingleAttributes(beagleStyle) {
    var keys = Object.keys(beagleStyle);
    var singleAttributes = Object.keys(SINGLE_ATTRIBUTES);
    return keys.reduce(function (result, prop) {
        var _a;
        if (!singleAttributes.includes(prop))
            return result;
        var propName = SINGLE_ATTRIBUTES[prop];
        return __assign(__assign({}, result), (_a = {}, _a[propName] = toLowerCase(beagleStyle[prop]), _a));
    }, {});
}
function formatNonBeagleProperties(beagleStyle) {
    var keys = Object.keys(beagleStyle);
    return keys.reduce(function (result, key) {
        var _a;
        return (BEAGLE_STYLE_KEYS.includes(key) ? result : __assign(__assign({}, result), (_a = {}, _a[key] = beagleStyle[key], _a)));
    }, {});
}
function convertToCSS(style) {
    if (style.hasOwnProperty('position') && !style.hasOwnProperty('positionType')) {
        style.positionType = 'relative';
    }
    var css = formatSizeProperty(style.size);
    css = __assign(__assign({}, css), formatBorderWidthAttributes(style.borderWidth));
    css = __assign(__assign({}, css), formatBorderStyle(style));
    css = __assign(__assign({}, css), formatPositionProperty(style.position));
    css = __assign(__assign({}, css), formatFlexAttributes(style.flex));
    css = __assign(__assign({}, css), formatCornerRadiusAttributes(style.cornerRadius));
    css = __assign(__assign({}, css), formatEdgeAttributes(style, 'margin'));
    css = __assign(__assign({}, css), formatEdgeAttributes(style, 'padding'));
    css = __assign(__assign({}, css), formatSingleAttributes(style));
    css = __assign(__assign({}, css), formatNonBeagleProperties(style));
    return css;
}
function convertStyleIdToClass(styleId) {
    return styleId.replace(/([a-z])([A-Z])/g, '$1-$2').replace(/\./g, '-').toLowerCase();
}
function convert(component) {
    if (component.style && typeof component.style === 'object') {
        component.style = convertToCSS(component.style);
    }
    if (component.styleId) {
        component.styleId = convertStyleIdToClass(component.styleId);
    }
}
exports.default = {
    convert: convert,
};
//# sourceMappingURL=styling.js.map

/***/ }),

/***/ 611:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
/**
 * Checks if the component (1st parameter) matches the type definition declared in `typeMetadata`
 * (2nd parameter) and if its children match the restrictions provided by `childrenMetadata` (3rd)
 * parameter.
 *
 * If the component doesn't match the metadata, an error is thrown with a message that helps
 * identifying the underlying problem.
 *
 * @param component the component to check the types and children
 * @param typeMetadata the type metadata of the component
 * @param childrenMetadata the children metadata of the component
 */
function check(
// eslint-disable-next-line
component, 
// eslint-disable-next-line
typeMetadata, 
// eslint-disable-next-line
childrenMetadata) {
    // todo
}
exports.default = {
    check: check,
};
//# sourceMappingURL=type-checker.js.map

/***/ }),

/***/ 0:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
var BeagleCacheError = /** @class */ (function (_super) {
    __extends(BeagleCacheError, _super);
    function BeagleCacheError(path) {
        return _super.call(this, "cache for " + path + " has not been found.") || this;
    }
    return BeagleCacheError;
}(BeagleError_1.default));
exports.default = BeagleCacheError;
//# sourceMappingURL=BeagleCacheError.js.map

/***/ }),

/***/ 110:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError = /** @class */ (function (_super) {
    __extends(BeagleError, _super);
    function BeagleError(message) {
        return _super.call(this, "Beagle: " + message) || this;
    }
    BeagleError.prototype.getSerializableError = function () {
        return { message: this.message };
    };
    return BeagleError;
}(Error));
exports.default = BeagleError;
function isBeagleError(error) {
    return !!(error.message.startsWith('Beagle') && error.getSerializableError);
}
exports.isBeagleError = isBeagleError;
//# sourceMappingURL=BeagleError.js.map

/***/ }),

/***/ 7930:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
var BeagleExpiredCacheError = /** @class */ (function (_super) {
    __extends(BeagleExpiredCacheError, _super);
    function BeagleExpiredCacheError(path) {
        return _super.call(this, "cache for " + path + " has expired.") || this;
    }
    return BeagleExpiredCacheError;
}(BeagleError_1.default));
exports.default = BeagleExpiredCacheError;
//# sourceMappingURL=BeagleExpiredCacheError.js.map

/***/ }),

/***/ 2443:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
var BeagleNavigationError = /** @class */ (function (_super) {
    __extends(BeagleNavigationError, _super);
    function BeagleNavigationError(message) {
        return _super.call(this, "navigation error: " + message) || this;
    }
    return BeagleNavigationError;
}(BeagleError_1.default));
exports.default = BeagleNavigationError;
//# sourceMappingURL=BeagleNavigationError.js.map

/***/ }),

/***/ 3869:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
function buildMessage(path, responseOrMessage) {
    var additionalMessage = typeof responseOrMessage === 'string' ? " " + responseOrMessage : '';
    return "network error while trying to access " + path + "." + additionalMessage;
}
var BeagleNetworkError = /** @class */ (function (_super) {
    __extends(BeagleNetworkError, _super);
    function BeagleNetworkError(path, responseOrMessage) {
        var _this = _super.call(this, buildMessage(path, responseOrMessage)) || this;
        _this.response = typeof responseOrMessage === 'string' ? undefined : responseOrMessage;
        return _this;
    }
    BeagleNetworkError.prototype.getSerializableError = function () {
        return __awaiter(this, void 0, void 0, function () {
            var text, json;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!this.response)
                            return [2 /*return*/, _super.prototype.getSerializableError.call(this)];
                        return [4 /*yield*/, this.response.text()];
                    case 1:
                        text = _a.sent();
                        try {
                            json = JSON.parse(text);
                        }
                        catch (_b) { }
                        return [2 /*return*/, {
                                message: this.message,
                                response: {
                                    status: this.response.status,
                                    statusText: this.response.statusText,
                                    ok: this.response.ok,
                                    type: this.response.type,
                                    redirected: this.response.redirected,
                                    url: this.response.url,
                                    headers: this.response.headers,
                                    text: text,
                                    json: json,
                                },
                            }];
                }
            });
        });
    };
    return BeagleNetworkError;
}(BeagleError_1.default));
exports.default = BeagleNetworkError;
//# sourceMappingURL=BeagleNetworkError.js.map

/***/ }),

/***/ 8284:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
var BeagleNotFoundError = /** @class */ (function (_super) {
    __extends(BeagleNotFoundError, _super);
    function BeagleNotFoundError(message) {
        return _super.call(this, "not found error: " + message) || this;
    }
    return BeagleNotFoundError;
}(BeagleError_1.default));
exports.default = BeagleNotFoundError;
//# sourceMappingURL=BeagleNotFoundError.js.map

/***/ }),

/***/ 7689:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
var BeagleParseError = /** @class */ (function (_super) {
    __extends(BeagleParseError, _super);
    function BeagleParseError(message) {
        return _super.call(this, "parse error: " + message) || this;
    }
    return BeagleParseError;
}(BeagleError_1.default));
exports.default = BeagleParseError;
//# sourceMappingURL=BeagleParseError.js.map

/***/ }),

/***/ 9731:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
function __export(m) {
    for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
}
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
// logger
var logger_1 = __importDefault(__webpack_require__(2611));
exports.logger = logger_1.default;
// beagle utilities
var beagle_tree_1 = __importDefault(__webpack_require__(7853));
exports.Tree = beagle_tree_1.default;
var component_1 = __importDefault(__webpack_require__(6574));
exports.Component = component_1.default;
// general utilities
var object_1 = __importDefault(__webpack_require__(2413));
exports.ObjectUtils = object_1.default;
var string_1 = __importDefault(__webpack_require__(8490));
exports.StringUtils = string_1.default;
var url_1 = __importDefault(__webpack_require__(5905));
exports.UrlUtils = url_1.default;
var automaton_1 = __importDefault(__webpack_require__(3039));
exports.Automaton = automaton_1.default;
// errors
var BeagleError_1 = __importDefault(__webpack_require__(110));
exports.BeagleError = BeagleError_1.default;
var BeagleNetworkError_1 = __importDefault(__webpack_require__(3869));
exports.BeagleNetworkError = BeagleNetworkError_1.default;
var BeagleCacheError_1 = __importDefault(__webpack_require__(0));
exports.BeagleCacheError = BeagleCacheError_1.default;
var BeagleExpiredCacheError_1 = __importDefault(__webpack_require__(7930));
exports.BeagleExpiredCacheError = BeagleExpiredCacheError_1.default;
// decorators
__export(__webpack_require__(9826));
// beagle service: default exportation
var beagle_service_1 = __importDefault(__webpack_require__(3807));
exports.default = beagle_service_1.default.create;
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 7171:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
function updateMiddlewaresInConfiguration(config) {
    if (config.middlewares) {
        logger_1.default.warn('Middlewares are deprecated. Consider using lifecycles instead.');
        config.lifecycles = config.lifecycles || {};
        var originalBeforeViewSnapshot_1 = config.lifecycles.beforeViewSnapshot;
        config.lifecycles.beforeViewSnapshot = function (viewTree) {
            var result = originalBeforeViewSnapshot_1 ? originalBeforeViewSnapshot_1(viewTree) : viewTree;
            if (!result)
                result = viewTree;
            config.middlewares.forEach(function (middleware) {
                result = middleware(result);
            });
            return result;
        };
    }
}
exports.updateMiddlewaresInConfiguration = updateMiddlewaresInConfiguration;
//# sourceMappingURL=middlewares.js.map

/***/ }),

/***/ 2611:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var DEFAULT_TYPES_TO_LOG = ['error', 'warn'];
var logFn = {
    error: console.error,
    warn: console.warn,
    info: console.info,
    lifecycle: console.log,
    expression: console.log,
};
var logColors = {
    info: '#338dcc',
    lifecycle: '#BF80FD',
    expression: '#476bfc',
    error: '#fc4747',
    warn: '#DBCA1C',
};
function createLogger() {
    var isEnabled = true;
    var typesToLog = DEFAULT_TYPES_TO_LOG;
    var customLogger = null;
    function log(type) {
        var logItems = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            logItems[_i - 1] = arguments[_i];
        }
        if (isEnabled && typesToLog.includes(type)) {
            if (customLogger)
                return customLogger.apply(void 0, __spreadArrays([type], logItems));
            console.group("%cBeagle (" + type.toLowerCase() + ")", "color: " + logColors[type]);
            var logFunction_1 = logFn[type] || console.log;
            logItems.forEach(function (item) { return logFunction_1(item); });
            console.groupEnd();
        }
    }
    return {
        enable: function () { return isEnabled = true; },
        disable: function () { return isEnabled = false; },
        setTypesToLog: function (newTypesToLog) { return typesToLog = newTypesToLog || DEFAULT_TYPES_TO_LOG; },
        setCustomLogFunction: function (customLogFunction) { return customLogger = customLogFunction; },
        info: function () {
            var logItems = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                logItems[_i] = arguments[_i];
            }
            return log.apply(void 0, __spreadArrays(['info'], logItems));
        },
        warn: function () {
            var logItems = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                logItems[_i] = arguments[_i];
            }
            return log.apply(void 0, __spreadArrays(['warn'], logItems));
        },
        error: function () {
            var logItems = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                logItems[_i] = arguments[_i];
            }
            return log.apply(void 0, __spreadArrays(['error'], logItems));
        },
        log: log,
    };
}
var logger = createLogger();
exports.default = logger;
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 9826:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
function registerLifecycleToComponent(lifecycleName, lifecycleHook, component) {
    component.beagleMetadata = component.beagleMetadata || {};
    component.beagleMetadata.lifecycles = component.beagleMetadata.lifecycles || {};
    component.beagleMetadata.lifecycles[lifecycleName] = lifecycleHook;
}
function BeforeStart(hook) {
    return function (target) {
        registerLifecycleToComponent('beforeStart', hook, target);
    };
}
exports.BeforeStart = BeforeStart;
function BeforeViewSnapshot(hook) {
    return function (target) {
        registerLifecycleToComponent('beforeViewSnapshot', hook, target);
    };
}
exports.BeforeViewSnapshot = BeforeViewSnapshot;
function AfterViewSnapshot(hook) {
    return function (target) {
        registerLifecycleToComponent('afterViewSnapshot', hook, target);
    };
}
exports.AfterViewSnapshot = AfterViewSnapshot;
function BeforeRender(hook) {
    return function (target) {
        registerLifecycleToComponent('beforeRender', hook, target);
    };
}
exports.BeforeRender = BeforeRender;
function BeagleChildren(childrenMetadata) {
    return function (target) {
        var component = target;
        component.beagleMetadata = component.beagleMetadata || {};
        component.beagleMetadata.children = childrenMetadata;
    };
}
exports.BeagleChildren = BeagleChildren;
//# sourceMappingURL=decorator.js.map

/***/ }),

/***/ 648:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
function extract(components) {
    var keys = Object.keys(components);
    var extractedMetadata = {
        children: {},
        lifecycles: {
            afterViewSnapshot: {},
            beforeRender: {},
            beforeStart: {},
            beforeViewSnapshot: {},
        },
    };
    keys.forEach(function (key) {
        var component = components[key];
        var metadata = component.beagleMetadata;
        if (!metadata)
            return;
        if (metadata.children)
            extractedMetadata.children[key.toLowerCase()] = metadata.children;
        if (metadata.lifecycles) {
            var lifecycleKeys = Object.keys(metadata.lifecycles);
            lifecycleKeys.forEach(function (lifecycleKey) {
                var hook = metadata.lifecycles[lifecycleKey];
                if (!hook)
                    return;
                extractedMetadata.lifecycles[lifecycleKey][key.toLowerCase()] = hook;
            });
        }
    });
    return extractedMetadata;
}
exports.default = {
    extract: extract,
};
//# sourceMappingURL=parser.js.map

/***/ }),

/***/ 4592:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var isEqual_1 = __importDefault(__webpack_require__(8446));
var find_1 = __importDefault(__webpack_require__(3311));
var remove_1 = __importDefault(__webpack_require__(2729));
exports.default = {
    insert: function (array, element, index) {
        var answer = __spreadArrays(array);
        if (index !== undefined)
            answer.splice(index, 0, element);
        else
            answer.push(element);
        return answer;
    },
    remove: function (array, element) {
        var answer = __spreadArrays(array);
        remove_1.default(answer, function (item) { return isEqual_1.default(item, element); });
        return answer;
    },
    removeIndex: function (array, index) {
        var answer = __spreadArrays(array);
        if (index !== undefined)
            answer.splice(index, 1);
        else
            answer.pop();
        return answer;
    },
    contains: function (array, element) { return !!find_1.default(array, function (item) { return isEqual_1.default(item, element); }); },
    union: function () {
        var arrays = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            arrays[_i] = arguments[_i];
        }
        return arrays.reduce(function (result, current) { return __spreadArrays(result, current); }, []);
    },
};
//# sourceMappingURL=array.js.map

/***/ }),

/***/ 9131:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var array_1 = __importDefault(__webpack_require__(4592));
var logic_1 = __importDefault(__webpack_require__(8049));
var number_1 = __importDefault(__webpack_require__(5012));
var other_1 = __importDefault(__webpack_require__(5439));
var string_1 = __importDefault(__webpack_require__(6257));
exports.default = __assign(__assign(__assign(__assign(__assign({}, array_1.default), logic_1.default), number_1.default), other_1.default), string_1.default);
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 8049:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.default = {
    condition: function (premise, trueValue, falseValue) { return (premise ? trueValue : falseValue); },
    not: function (value) { return !value; },
    and: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return !args.includes(false);
    },
    or: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.includes(true);
    },
};
//# sourceMappingURL=logic.js.map

/***/ }),

/***/ 5012:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.default = {
    sum: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.reduce(function (result, current) { return result + current; }, 0);
    },
    subtract: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.reduce(function (result, current) { return result - current; }, args[0] * 2);
    },
    multiply: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.reduce(function (result, current) { return result * current; }, 1);
    },
    divide: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return (args.reduce(function (result, current) { return result / current; }, args[0] * args[0]));
    },
    gt: function (a, b) { return a > b; },
    gte: function (a, b) { return a >= b; },
    lt: function (a, b) { return a < b; },
    lte: function (a, b) { return a <= b; },
};
//# sourceMappingURL=number.js.map

/***/ }),

/***/ 5439:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var isEqual_1 = __importDefault(__webpack_require__(8446));
var isEmpty_1 = __importDefault(__webpack_require__(1609));
exports.default = {
    eq: function (a, b) { return isEqual_1.default(a, b); },
    isNull: function (value) { return value === null || value === undefined; },
    isEmpty: function (value) { return isEmpty_1.default(value); },
    length: function (value) { return value.length; },
};
//# sourceMappingURL=other.js.map

/***/ }),

/***/ 6257:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var capitalize_1 = __importDefault(__webpack_require__(8403));
exports.default = {
    concat: function () {
        var args = [];
        for (var _i = 0; _i < arguments.length; _i++) {
            args[_i] = arguments[_i];
        }
        return args.reduce(function (result, current) { return "" + result + current; }, '');
    },
    capitalize: function (text) { return capitalize_1.default(text); },
    uppercase: function (text) { return text.toUpperCase(); },
    lowercase: function (text) { return text.toLowerCase(); },
    substr: function (text, from, length) { return text.substr(from, length); },
};
//# sourceMappingURL=string.js.map

/***/ }),

/***/ 2141:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var get_1 = __importDefault(__webpack_require__(7361));
var html_1 = __webpack_require__(6419);
/**
 * This function generates a new `Record<string, any>` with the attributes that were passed along
 * with the analytics provider configuration or the ones from the actions
 * @param action the `BeagleAction` to which try to extract the analytics config
 * @param whiteListedAttributesInConfig the list of attributes passed through the `AnalyticsProvider` config
 * @returns the Record of white listed attributes from the `AnalyticsProvider` or the action itself
 */
function createActionAttributes(action, whiteListedAttributesInConfig) {
    var whiteListedAttributesInAction = action && action.analytics && action.analytics.attributes;
    var attributes = whiteListedAttributesInAction || whiteListedAttributesInConfig;
    if (attributes)
        return attributes.reduce(function (result, attribute) {
            var _a;
            return (__assign(__assign({}, result), (_a = {}, _a[attribute] = get_1.default(action, attribute), _a)));
        }, {});
}
/**
 * This function formats Action Record
 * @param action the `BeagleAction` to use in the record
 * @param eventName the event that triggered the action
 * @param config the `AnalyticsConfig` from the `AnalyticsProvider`
 * @param component the `IdentifiableBeagleUIElement` to which the action belongs to
 * @param beagleView the `BeagleView` to use in the record
 * @returns the formatted `AnalyticsRecord`
 */
function formatActionRecord(action, eventName, config, component, currentPlatform, route) {
    var currentRoute = route;
    var platform = currentPlatform;
    var element = html_1.getElementByBeagleId(component.id);
    var position = element && html_1.getElementPosition(element);
    var xPath = element && html_1.getPath(element);
    var record = __assign({ type: 'action', platform: "WEB " + platform, event: eventName, component: {
            type: component && component._beagleComponent_,
            id: component && component.id,
            position: position,
            xPath: xPath,
        }, beagleAction: action._beagleAction_ }, createActionAttributes(action, config.actions[action._beagleAction_]));
    if (action.analytics && action.analytics.additionalEntries)
        record = __assign(__assign({}, record), action.analytics.additionalEntries);
    if (currentRoute) {
        if ('screen' in currentRoute)
            record.screenId = currentRoute.screen.identifier || currentRoute.screen.id;
        else
            record.url = currentRoute.url;
    }
    return record;
}
exports.default = formatActionRecord;
//# sourceMappingURL=actions.js.map

/***/ }),

/***/ 7912:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var actions_1 = __importDefault(__webpack_require__(2141));
function createAnalyticsService(provider) {
    var sessionPromise;
    var configPromise;
    function createScreenRecord(route, platform) {
        return __awaiter(this, void 0, void 0, function () {
            var config, record;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!provider)
                            return [2 /*return*/];
                        return [4 /*yield*/, sessionPromise];
                    case 1:
                        _a.sent();
                        return [4 /*yield*/, configPromise];
                    case 2:
                        config = _a.sent();
                        if (!config.enableScreenAnalytics)
                            return [2 /*return*/];
                        record = {
                            type: 'screen',
                            platform: "WEB " + platform,
                        };
                        if ('screen' in route)
                            record.screenId = route.screen.identifier || route.screen.id;
                        else
                            record.url = route.url;
                        provider.createRecord(record);
                        return [2 /*return*/];
                }
            });
        });
    }
    function createActionRecord(action, eventName, component, platform, route) {
        return __awaiter(this, void 0, void 0, function () {
            var config, isActionDisabled, isActionEnabled, isActionEnabledInConfig, shouldGenerateAnalytics, record;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!provider)
                            return [2 /*return*/];
                        return [4 /*yield*/, sessionPromise];
                    case 1:
                        _a.sent();
                        return [4 /*yield*/, configPromise];
                    case 2:
                        config = _a.sent();
                        isActionDisabled = action.analytics && action.analytics.enable === false;
                        isActionEnabled = action.analytics && action.analytics.enable === true;
                        isActionEnabledInConfig = config.actions[action._beagleAction_];
                        shouldGenerateAnalytics = (isActionEnabled || (!isActionDisabled && isActionEnabledInConfig));
                        if (shouldGenerateAnalytics) {
                            record = actions_1.default(action, eventName, config, component, platform, route);
                            provider.createRecord(record);
                        }
                        return [2 /*return*/];
                }
            });
        });
    }
    function start() {
        if (!provider)
            return;
        sessionPromise = provider.startSession();
        configPromise = provider.getConfig();
    }
    start();
    return {
        createScreenRecord: createScreenRecord,
        createActionRecord: createActionRecord,
    };
}
exports.default = {
    create: createAnalyticsService,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 4775:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var mapKeys_1 = __importDefault(__webpack_require__(7523));
var action_1 = __importDefault(__webpack_require__(3368));
var parser_1 = __importDefault(__webpack_require__(648));
var middlewares_1 = __webpack_require__(7171);
var BeagleError_1 = __importDefault(__webpack_require__(110));
var operation_1 = __importDefault(__webpack_require__(9131));
var logger_1 = __importDefault(__webpack_require__(2611));
function checkPrefix(items) {
    mapKeys_1.default(items, function (value, key) {
        if (!key.startsWith('custom:') && !key.startsWith('beagle:')) {
            throw new BeagleError_1.default("Please check your config. The " + key + " is not a valid name. Yours components or actions should always start with \"beagle:\" if it's overwriting a default component or an action, \"custom:\" if it's a custom component or an action");
        }
    });
}
function getLifecycleHookMap(globalLifecycleHooks, componentLifecycleHooks) {
    return {
        beforeStart: {
            components: componentLifecycleHooks.beforeStart,
            global: globalLifecycleHooks && globalLifecycleHooks.beforeStart,
        },
        beforeViewSnapshot: {
            components: componentLifecycleHooks.beforeViewSnapshot,
            global: globalLifecycleHooks && globalLifecycleHooks.beforeViewSnapshot,
        },
        afterViewSnapshot: {
            components: componentLifecycleHooks.afterViewSnapshot,
            global: globalLifecycleHooks && globalLifecycleHooks.afterViewSnapshot,
        },
        beforeRender: {
            components: componentLifecycleHooks.beforeRender,
            global: globalLifecycleHooks && globalLifecycleHooks.beforeRender,
        },
    };
}
function checkOperationNames(operations) {
    if (!operations)
        return;
    Object.keys(operations).forEach(function (key) {
        if (operation_1.default[key])
            logger_1.default.warn("You are overriding a default operation \"" + key + "\"");
        var match = key.match(/^\w*[A-z_]+\w*$/);
        if (!match)
            throw new BeagleError_1.default("Operation names must have only letters, numbers and the character _. An operation name must have at least one character and must never be comprised of only numbers. \"" + key + "\"");
    });
}
function update(config) {
    // todo: remove with version 2.0
    middlewares_1.updateMiddlewaresInConfiguration(config);
}
function validate(config) {
    checkPrefix(config.components);
    checkOperationNames(config.customOperations);
    config.customActions && checkPrefix(config.customActions);
}
function process(config) {
    var actionHandlers = __assign(__assign({}, action_1.default), config.customActions);
    var operationHandlers = __assign(__assign({}, operation_1.default), config.customOperations);
    var componentMetadata = parser_1.default.extract(config.components);
    var lifecycleHooks = getLifecycleHookMap(config.lifecycles, componentMetadata.lifecycles);
    return { actionHandlers: actionHandlers, lifecycleHooks: lifecycleHooks, childrenMetadata: componentMetadata.children, operationHandlers: operationHandlers };
}
exports.default = {
    update: update,
    validate: validate,
    process: process,
};
//# sourceMappingURL=configuration.js.map

/***/ }),

/***/ 3807:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var beagle_view_1 = __importDefault(__webpack_require__(7129));
var configuration_1 = __importDefault(__webpack_require__(4775));
var services_1 = __webpack_require__(2626);
function createBeagleUIService(config) {
    configuration_1.default.update(config);
    configuration_1.default.validate(config);
    var processedConfig = configuration_1.default.process(config);
    var services = services_1.createServices(config);
    var beagleService = __assign(__assign(__assign({}, services), processedConfig), { getConfig: function () { return config; }, createView: function (networkOptions, initialControllerId) { return (beagle_view_1.default.create(beagleService, networkOptions, initialControllerId)); } });
    return beagleService;
}
exports.default = {
    create: createBeagleUIService,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 2626:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var remote_cache_1 = __importDefault(__webpack_require__(3122));
var default_headers_1 = __importDefault(__webpack_require__(561));
var url_builder_1 = __importDefault(__webpack_require__(6513));
var view_client_1 = __importDefault(__webpack_require__(9036));
var pre_fetcher_1 = __importDefault(__webpack_require__(8397));
var global_context_1 = __importDefault(__webpack_require__(6715));
var view_content_manager_1 = __importDefault(__webpack_require__(7167));
var analytics_1 = __importDefault(__webpack_require__(7912));
function createServices(config) {
    var httpClient = {
        fetch: function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i] = arguments[_i];
            }
            return (config.fetchData ? config.fetchData.apply(config, args) : fetch.apply(void 0, args));
        },
    };
    var storage = config.customStorage || localStorage;
    var urlBuilder = url_builder_1.default.create(config.baseUrl);
    var analytics = config.analytics;
    var remoteCache = remote_cache_1.default.create(storage);
    var defaultHeaders = default_headers_1.default.create(remoteCache, config.useBeagleHeaders);
    var viewClient = view_client_1.default.create(storage, defaultHeaders, remoteCache, httpClient, config.strategy);
    var preFetcher = pre_fetcher_1.default.create(viewClient);
    var globalContext = global_context_1.default.create();
    var viewContentManagerMap = view_content_manager_1.default.create();
    var analyticsService = analytics_1.default.create(config.analyticsProvider);
    return {
        storage: storage,
        httpClient: httpClient,
        urlBuilder: urlBuilder,
        analytics: analytics,
        remoteCache: remoteCache,
        viewClient: viewClient,
        defaultHeaders: defaultHeaders,
        preFetcher: preFetcher,
        globalContext: globalContext,
        viewContentManagerMap: viewContentManagerMap,
        analyticsService: analyticsService,
    };
}
exports.createServices = createServices;
//# sourceMappingURL=services.js.map

/***/ }),

/***/ 6715:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var logger_1 = __importDefault(__webpack_require__(2611));
var set_1 = __importDefault(__webpack_require__(6968));
var get_1 = __importDefault(__webpack_require__(7361));
var unset_1 = __importDefault(__webpack_require__(9233));
var has_1 = __importDefault(__webpack_require__(8721));
var cloneDeep_1 = __importDefault(__webpack_require__(361));
function createGlobalContext() {
    var listeners = [];
    var globalContext = {
        id: 'global',
        value: null,
    };
    function subscribe(listener) {
        listeners.push(listener);
        return function () {
            var index = listeners.indexOf(listener);
            if (index !== -1)
                listeners.splice(index, 1);
        };
    }
    function callUpdateListeners() {
        listeners.forEach(function (listener) { return listener(); });
    }
    function getAsDataContext() {
        return cloneDeep_1.default(globalContext);
    }
    function get(path) {
        if (!path)
            return cloneDeep_1.default(globalContext.value);
        return get_1.default(globalContext.value, path);
    }
    function set(value, path) {
        if (!path)
            globalContext.value = value;
        else {
            globalContext.value = globalContext.value || {};
            set_1.default(globalContext.value, path, value);
        }
        callUpdateListeners();
    }
    function clear(path) {
        if (!path) {
            globalContext.value = null;
            callUpdateListeners();
        }
        else {
            if (has_1.default(globalContext.value, path)) {
                unset_1.default(globalContext.value, path);
                callUpdateListeners();
            }
            else
                logger_1.default.warn("Invalid path: The path you are trying to clean " + path + " doesn't exist in the global context");
        }
    }
    return {
        get: get,
        set: set,
        clear: clear,
        getAsDataContext: getAsDataContext,
        subscribe: subscribe,
    };
}
exports.default = {
    create: createGlobalContext,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 561:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var defaultHeaders = { 'beagle-platform': 'WEB' };
function createDefaultHeaders(remoteCache, useDefaultHeaders) {
    if (useDefaultHeaders === void 0) { useDefaultHeaders = true; }
    function get(url, method) {
        return __awaiter(this, void 0, void 0, function () {
            var hash, requestHeaders;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (!!useDefaultHeaders) return [3 /*break*/, 1];
                        return [2 /*return*/, {}];
                    case 1: return [4 /*yield*/, remoteCache.getHash(url, method)];
                    case 2:
                        hash = _a.sent();
                        requestHeaders = __assign({}, defaultHeaders);
                        if (hash) {
                            requestHeaders = __assign(__assign({}, defaultHeaders), { 'beagle-hash': hash });
                        }
                        return [2 /*return*/, requestHeaders];
                }
            });
        });
    }
    return {
        get: get,
    };
}
exports.default = {
    create: createDefaultHeaders,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 8397:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __importDefault(__webpack_require__(110));
function createPreFetcher(viewClient) {
    var views = {};
    function fetch(url) {
        return __awaiter(this, void 0, void 0, function () {
            var error;
            var _this = this;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        error = new BeagleError_1.default("Failed to pre-fetch view " + url + ".");
                        views[url] = new Promise(function (resolve, reject) { return __awaiter(_this, void 0, void 0, function () {
                            var view, errors_1;
                            return __generator(this, function (_a) {
                                switch (_a.label) {
                                    case 0:
                                        view = null;
                                        _a.label = 1;
                                    case 1:
                                        _a.trys.push([1, 3, , 4]);
                                        return [4 /*yield*/, viewClient.load({
                                                onChangeTree: function (v) { return view = v; },
                                                url: url,
                                                retry: function () { },
                                            })];
                                    case 2:
                                        _a.sent();
                                        if (view)
                                            resolve(view);
                                        else
                                            reject(error);
                                        return [3 /*break*/, 4];
                                    case 3:
                                        errors_1 = _a.sent();
                                        reject(error);
                                        return [3 /*break*/, 4];
                                    case 4: return [2 /*return*/];
                                }
                            });
                        }); });
                        return [4 /*yield*/, views[url]];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    function recover(url) {
        return views[url] || Promise.reject(new BeagleError_1.default("The view \"" + url + "\" is not pre-fetched."));
    }
    return {
        fetch: fetch,
        recover: recover,
    };
}
exports.default = {
    create: createPreFetcher,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 3122:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.beagleCacheNamespace = '@beagle-web/beagle-cache';
function createRemoteCache(storage) {
    function getMetadata(url, method) {
        return __awaiter(this, void 0, void 0, function () {
            var cacheMetadataFromStorage;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, storage.getItem(exports.beagleCacheNamespace + "/" + url + "/" + method)];
                    case 1:
                        cacheMetadataFromStorage = (_a.sent());
                        return [2 /*return*/, cacheMetadataFromStorage ? JSON.parse(cacheMetadataFromStorage) : null];
                }
            });
        });
    }
    function getHash(url, method) {
        return __awaiter(this, void 0, void 0, function () {
            var cacheMetadataFromStorage;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, getMetadata(url, method)];
                    case 1:
                        cacheMetadataFromStorage = _a.sent();
                        return [2 /*return*/, cacheMetadataFromStorage ? cacheMetadataFromStorage.beagleHash : ''];
                }
            });
        });
    }
    function updateMetadata(metadata, url, method) {
        storage.setItem(exports.beagleCacheNamespace + "/" + url + "/" + method, JSON.stringify(metadata));
    }
    return {
        updateMetadata: updateMetadata,
        getHash: getHash,
        getMetadata: getMetadata,
    };
}
exports.default = {
    create: createRemoteCache,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 6513:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var string_1 = __importDefault(__webpack_require__(8490));
function createURLBuilder(baseUrl) {
    if (baseUrl === void 0) { baseUrl = ''; }
    function shouldEncodeUrl(baseUrl) {
        return decodeURI(baseUrl) === baseUrl;
    }
    return {
        build: function (path) {
            // According to the convention the relative path should start with '/'
            var relativePathRegex = /^\/+(\b|$)/;
            var base = string_1.default.removeSuffix(baseUrl, '/');
            var url = path.match(relativePathRegex) ? "" + base + path : path;
            return shouldEncodeUrl(url) ? encodeURI(url) : url;
        },
    };
}
exports.default = {
    create: createURLBuilder,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 9036:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var BeagleError_1 = __webpack_require__(110);
var BeagleCacheError_1 = __importDefault(__webpack_require__(0));
var BeagleNetworkError_1 = __importDefault(__webpack_require__(3869));
var BeagleExpiredCacheError_1 = __importDefault(__webpack_require__(7930));
exports.namespace = '@beagle-web/cache';
var DEFAULT_STRATEGY = 'beagle-with-fallback-to-cache';
var strategyNameToStrategyArrays = {
    'beagle-cache-only': { fetch: ['cache-ttl', 'network-beagle'], fallback: [] },
    'beagle-with-fallback-to-cache': { fetch: ['cache-ttl', 'network-beagle'], fallback: ['cache'] },
    'network-with-fallback-to-cache': { fetch: ['network'], fallback: ['cache'] },
    'cache-with-fallback-to-network': { fetch: ['cache'], fallback: ['network'] },
    'cache-only': { fetch: ['cache'], fallback: [] },
    'network-only': { fetch: ['network'], fallback: [] },
    'cache-first': { fetch: ['cache', 'network'], fallback: [] },
};
function createViewClient(storage, defaultHeadersService, remoteCache, httpClient, globalStrategy) {
    if (globalStrategy === void 0) { globalStrategy = DEFAULT_STRATEGY; }
    /* The following function is async for future compatibility with environments other than web. React
    native's localStorage, for instance, always returns promises. */
    function loadFromCache(url, method) {
        if (method === void 0) { method = 'get'; }
        return __awaiter(this, void 0, void 0, function () {
            var fromStorage, uiTree;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, storage.getItem(exports.namespace + "/" + url + "/" + method)];
                    case 1:
                        fromStorage = _a.sent();
                        uiTree = fromStorage ? JSON.parse(fromStorage) : null;
                        if (!uiTree)
                            throw new BeagleCacheError_1.default(url);
                        return [2 /*return*/, uiTree];
                }
            });
        });
    }
    function loadFromCacheCheckingTTL(url, method) {
        if (method === void 0) { method = 'get'; }
        return __awaiter(this, void 0, void 0, function () {
            var metadata, timeInMs, isCacheValid;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0: return [4 /*yield*/, remoteCache.getMetadata(url, method)];
                    case 1:
                        metadata = _a.sent();
                        timeInMs = Date.now();
                        isCacheValid = (metadata
                            && (timeInMs - metadata.requestTime) / 1000 < parseInt(metadata.ttl));
                        if (!metadata || !isCacheValid) {
                            throw new BeagleExpiredCacheError_1.default(url);
                        }
                        return [2 /*return*/, loadFromCache(url, method)];
                }
            });
        });
    }
    function updateCacheMetadataFromResponse(response, requestTime, url, method) {
        var beagleHash = response.headers.get('beagle-hash') || '';
        var cacheControl = response.headers.get('cache-control') || '';
        var maxAge = cacheControl && cacheControl.match(/max-age=(\d+)/);
        var ttl = (maxAge && maxAge[1]) || '';
        var metadata = {
            beagleHash: beagleHash,
            requestTime: requestTime,
            ttl: ttl,
        };
        remoteCache.updateMetadata(metadata, url, method);
    }
    function getUITreeCacheProtocol(response, url, method, shouldSaveCache) {
        return __awaiter(this, void 0, void 0, function () {
            var uiTree;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        uiTree = {};
                        if (!(response.status === 304)) return [3 /*break*/, 2];
                        return [4 /*yield*/, loadFromCache(url, method)];
                    case 1:
                        uiTree = _a.sent();
                        return [3 /*break*/, 4];
                    case 2: return [4 /*yield*/, response.json()];
                    case 3:
                        uiTree = (_a.sent());
                        if (shouldSaveCache) {
                            storage.setItem(exports.namespace + "/" + url + "/" + method, JSON.stringify(uiTree));
                        }
                        _a.label = 4;
                    case 4: return [2 /*return*/, uiTree];
                }
            });
        });
    }
    function loadFromServer(url, method, headers, shouldSaveCache, useBeagleCacheProtocol) {
        if (method === void 0) { method = 'get'; }
        if (shouldSaveCache === void 0) { shouldSaveCache = true; }
        if (useBeagleCacheProtocol === void 0) { useBeagleCacheProtocol = true; }
        return __awaiter(this, void 0, void 0, function () {
            var response, requestTime, defaultHeaders, allHeaders, error_1, uiTree;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        requestTime = Date.now();
                        return [4 /*yield*/, defaultHeadersService.get(url, method)];
                    case 1:
                        defaultHeaders = _a.sent();
                        allHeaders = __assign(__assign({}, headers), defaultHeaders);
                        _a.label = 2;
                    case 2:
                        _a.trys.push([2, 4, , 5]);
                        return [4 /*yield*/, httpClient.fetch(url, { method: method, headers: allHeaders })];
                    case 3:
                        response = _a.sent();
                        return [3 /*break*/, 5];
                    case 4:
                        error_1 = _a.sent();
                        throw new BeagleNetworkError_1.default(url, error_1.message);
                    case 5:
                        if (response.status < 100 || response.status >= 400)
                            throw new BeagleNetworkError_1.default(url, response);
                        uiTree = {};
                        if (!useBeagleCacheProtocol) return [3 /*break*/, 7];
                        updateCacheMetadataFromResponse(response, requestTime, url, method);
                        return [4 /*yield*/, getUITreeCacheProtocol(response, url, method, shouldSaveCache)];
                    case 6:
                        uiTree = _a.sent();
                        return [3 /*break*/, 9];
                    case 7: return [4 /*yield*/, response.json()];
                    case 8:
                        uiTree = (_a.sent());
                        if (shouldSaveCache) {
                            storage.setItem(exports.namespace + "/" + url + "/" + method, JSON.stringify(uiTree));
                        }
                        _a.label = 9;
                    case 9: return [2 /*return*/, uiTree];
                }
            });
        });
    }
    function buildSerializableErrorsArray(errors) {
        var promises = errors.map(function (e) {
            if (BeagleError_1.isBeagleError(e)) {
                var serializableError = e.getSerializableError();
                return serializableError instanceof Promise
                    ? serializableError
                    : Promise.resolve(serializableError);
            }
            return Promise.resolve({ message: e.message });
        });
        return Promise.all(promises);
    }
    function load(_a) {
        var url = _a.url, fallbackUIElement = _a.fallbackUIElement, _b = _a.method, method = _b === void 0 ? 'get' : _b, headers = _a.headers, _c = _a.strategy, strategy = _c === void 0 ? globalStrategy : _c, _d = _a.loadingComponent, loadingComponent = _d === void 0 ? 'custom:loading' : _d, _e = _a.errorComponent, errorComponent = _e === void 0 ? 'custom:error' : _e, _f = _a.shouldShowLoading, shouldShowLoading = _f === void 0 ? true : _f, _g = _a.shouldShowError, shouldShowError = _g === void 0 ? true : _g, onChangeTree = _a.onChangeTree, retry = _a.retry;
        return __awaiter(this, void 0, void 0, function () {
            function loadNetwork(hasPreviousSuccess, useBeagleCacheProtocol) {
                if (hasPreviousSuccess === void 0) { hasPreviousSuccess = false; }
                if (useBeagleCacheProtocol === void 0) { useBeagleCacheProtocol = true; }
                return __awaiter(this, void 0, void 0, function () {
                    var tree;
                    return __generator(this, function (_a) {
                        switch (_a.label) {
                            case 0:
                                if (shouldShowLoading && !hasPreviousSuccess) {
                                    onChangeTree({ _beagleComponent_: loadingComponent });
                                }
                                return [4 /*yield*/, loadFromServer(url, method, headers, strategy !== 'network-only', useBeagleCacheProtocol)];
                            case 1:
                                tree = _a.sent();
                                onChangeTree(tree);
                                return [2 /*return*/];
                        }
                    });
                });
            }
            function loadCache() {
                return __awaiter(this, void 0, void 0, function () {
                    var _a;
                    return __generator(this, function (_b) {
                        switch (_b.label) {
                            case 0:
                                _a = onChangeTree;
                                return [4 /*yield*/, loadFromCache(url, method)];
                            case 1:
                                _a.apply(void 0, [_b.sent()]);
                                return [2 /*return*/];
                        }
                    });
                });
            }
            function loadCacheTTL() {
                return __awaiter(this, void 0, void 0, function () {
                    var _a;
                    return __generator(this, function (_b) {
                        switch (_b.label) {
                            case 0:
                                _a = onChangeTree;
                                return [4 /*yield*/, loadFromCacheCheckingTTL(url, method)];
                            case 1:
                                _a.apply(void 0, [_b.sent()]);
                                return [2 /*return*/];
                        }
                    });
                });
            }
            function runStrategies(strategies, stopOnSuccess) {
                return __awaiter(this, void 0, void 0, function () {
                    var errors, hasSuccess, isBeagleCache, i, error_2;
                    return __generator(this, function (_a) {
                        switch (_a.label) {
                            case 0:
                                errors = [];
                                hasSuccess = false;
                                isBeagleCache = false;
                                i = 0;
                                _a.label = 1;
                            case 1:
                                if (!(i < strategies.length)) return [3 /*break*/, 13];
                                _a.label = 2;
                            case 2:
                                _a.trys.push([2, 11, , 12]);
                                if (!(strategies[i] === 'network')) return [3 /*break*/, 4];
                                return [4 /*yield*/, loadNetwork(hasSuccess, false)];
                            case 3:
                                _a.sent();
                                return [3 /*break*/, 10];
                            case 4:
                                if (!(strategies[i] === 'network-beagle')) return [3 /*break*/, 6];
                                return [4 /*yield*/, loadNetwork(hasSuccess, true)];
                            case 5:
                                _a.sent();
                                isBeagleCache = true;
                                return [3 /*break*/, 10];
                            case 6:
                                if (!(strategies[i] === 'cache-ttl')) return [3 /*break*/, 8];
                                return [4 /*yield*/, loadCacheTTL()];
                            case 7:
                                _a.sent();
                                isBeagleCache = true;
                                return [3 /*break*/, 10];
                            case 8: return [4 /*yield*/, loadCache()];
                            case 9:
                                _a.sent();
                                _a.label = 10;
                            case 10:
                                hasSuccess = true;
                                if (stopOnSuccess || isBeagleCache)
                                    return [2 /*return*/, [hasSuccess, errors]];
                                return [3 /*break*/, 12];
                            case 11:
                                error_2 = _a.sent();
                                errors.push(error_2);
                                return [3 /*break*/, 12];
                            case 12:
                                i++;
                                return [3 /*break*/, 1];
                            case 13: return [2 /*return*/, [hasSuccess, errors]];
                        }
                    });
                });
            }
            function start() {
                return __awaiter(this, void 0, void 0, function () {
                    var _a, fetch, fallbackStrategy, _b, hasFetchSuccess, fetchErrors, _c, hasFallbackStrategySuccess, fallbackStrategyErrors, errors, errorUITree, _d;
                    return __generator(this, function (_e) {
                        switch (_e.label) {
                            case 0:
                                _a = strategyNameToStrategyArrays[strategy], fetch = _a.fetch, fallbackStrategy = _a.fallback;
                                return [4 /*yield*/, runStrategies(fetch, false)];
                            case 1:
                                _b = _e.sent(), hasFetchSuccess = _b[0], fetchErrors = _b[1];
                                if (hasFetchSuccess)
                                    return [2 /*return*/];
                                if (fallbackUIElement)
                                    return [2 /*return*/, onChangeTree(fallbackUIElement)];
                                return [4 /*yield*/, runStrategies(fallbackStrategy, true)];
                            case 2:
                                _c = _e.sent(), hasFallbackStrategySuccess = _c[0], fallbackStrategyErrors = _c[1];
                                if (hasFallbackStrategySuccess)
                                    return [2 /*return*/];
                                errors = __spreadArrays(fetchErrors, fallbackStrategyErrors);
                                if (!shouldShowError) return [3 /*break*/, 4];
                                _d = {
                                    _beagleComponent_: errorComponent,
                                    retry: retry
                                };
                                return [4 /*yield*/, buildSerializableErrorsArray(errors)];
                            case 3:
                                errorUITree = (_d.errors = _e.sent(),
                                    _d);
                                onChangeTree(errorUITree);
                                _e.label = 4;
                            case 4: throw errors;
                        }
                    });
                });
            }
            return __generator(this, function (_h) {
                switch (_h.label) {
                    case 0: return [4 /*yield*/, start()];
                    case 1:
                        _h.sent();
                        return [2 /*return*/];
                }
            });
        });
    }
    return {
        load: load,
        loadFromCache: loadFromCache,
        loadFromCacheCheckingTTL: loadFromCacheCheckingTTL,
        loadFromServer: loadFromServer,
    };
}
exports.default = {
    create: createViewClient,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 7167:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var beagle_tree_1 = __importDefault(__webpack_require__(7853));
var BeagleError_1 = __importDefault(__webpack_require__(110));
function createViewContentManagerMap() {
    var views = {};
    function create(view, elementId) {
        return {
            getElementId: function () { return elementId; },
            getElement: function () { return beagle_tree_1.default.findById(view.getTree(), elementId); },
            getView: function () { return view; },
        };
    }
    function get(viewId, elementId) {
        if (!viewId || !elementId) {
            throw new BeagleError_1.default('ViewContentManagerMap couldn\'t find viewId or elementId');
        }
        var view = views[viewId];
        if (!view)
            throw new BeagleError_1.default("ViewContentManagerMap couldn't find view with id " + viewId);
        return create(view, elementId);
    }
    function register(viewId, view) {
        views[viewId] = view;
    }
    function unregister(viewId) {
        delete views[viewId];
    }
    return {
        get: get,
        register: register,
        unregister: unregister,
        isRegistered: function (viewId) { return !!views[viewId]; },
    };
}
exports.default = {
    create: createViewContentManagerMap,
};
//# sourceMappingURL=index.js.map

/***/ }),

/***/ 3039:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var last_1 = __importDefault(__webpack_require__(928));
var empty = '∅';
/**
 * Creates a Deterministic Pushdown Automaton (DPA) according to states and transitions passed as
 * parameters.
 *
 * A DPA is oftenly used to recognize patterns in a string. Although regular expressions also
 * recognizes patterns in strings, a DPA is more powerful and can recognize more complex structures.
 * A regular expression can only recognize regular languages, while a DPA can also recognize
 * free context languages.
 *
 * This function creates an automaton with a single function: `match`. `match` is always run for
 * a string and, if a match is found, the string matching the pattern is returned. If no match is
 * found, `null` is returned.
 *
 * Every time `match` is called, the automaton starts at the state `param.initial` and if it ever
 * reaches `param.final`, it stops with a match. If the input ends and the current automaton state
 * is not the final, there's no match. Also, if there's no transition for the current input, the
 * automaton stops without a match.
 *
 * To represent end of stack, use the constant `Automaton.empty`.
 *
 * Note about reading an input: to make the representation easier, we can read multiple characters
 * at once from the input. This means that, in a state transition, `read` can be something like
 * `An entire paragraph.`, instead of sequence of transitions for each letter. Moreover, to simplify
 * the state machine, we can also use a regular expression in the property `read` of the transition.
 *
 * @param param an object with the initial state (string), final state (string) and the state
 * transitions. The state transitions are a map where the key is the state and the value is an array
 * of transitions for the state. A transition is composed by `read` (the string or regex to read
 * from the input string); `push` (the value to push to the stack); `pop` (the value to pop from
 * the stack); and `next` (the next state). The only required property for a transition is `next`.
 */
function createDPA(_a) {
    var initial = _a.initial, final = _a.final, transitions = _a.transitions;
    function match(input) {
        var stack = [];
        var currentState = initial;
        var remainingInput = input;
        var _loop_1 = function () {
            var availableTransitions = transitions[currentState];
            var matchedValue = '';
            var transition = availableTransitions.find(function (_a) {
                var read = _a.read, pop = _a.pop;
                if (pop !== undefined && last_1.default(stack) !== pop && (pop === empty && stack.length > 0)) {
                    return false;
                }
                if (read === undefined)
                    return true;
                if (typeof read === 'string') {
                    matchedValue = read;
                    return remainingInput.startsWith(read);
                }
                var match = remainingInput.match(new RegExp("^(" + read.source + ")"));
                if (match) {
                    matchedValue = match[0];
                    return true;
                }
                return false;
            });
            if (!transition)
                return { value: null };
            remainingInput = remainingInput.substring(matchedValue.length);
            currentState = transition.next;
            if (transition.pop !== undefined)
                stack.pop();
            if (transition.push !== undefined)
                stack.push(transition.push);
        };
        while (currentState !== final) {
            var state_1 = _loop_1();
            if (typeof state_1 === "object")
                return state_1.value;
        }
        return input.substr(0, input.length - remainingInput.length);
    }
    return { match: match };
}
exports.default = {
    /**
     * The empty symbol for transitions
     */
    empty: empty,
    createDPA: createDPA,
};
//# sourceMappingURL=automaton.js.map

/***/ }),

/***/ 6419:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

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
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
var findIndex_1 = __importDefault(__webpack_require__(998));
/**
  * Calculates the xPath for the given HTML element
  * @param element the element for which to start the building of the xPath
  * @returns the xPath of the given element
  */
function getPath(element) {
    function getPathRecursively(element, accumulator) {
        if (!element.parentNode)
            return;
        if (element.nodeName === 'BODY') {
            return 'BODY/' + accumulator;
        }
        var siblings = Array.from(element.parentNode.childNodes);
        var elementIndex = findIndex_1.default(siblings, element);
        var currentNode = siblings[elementIndex];
        accumulator = "" + currentNode.nodeName + (elementIndex > 0 ? "[" + elementIndex + "]" : '') + "/" + accumulator;
        return currentNode.parentNode && getPathRecursively(currentNode.parentNode, accumulator);
    }
    return getPathRecursively(element, '');
}
exports.getPath = getPath;
/**
  * Get the element of the given Beagle Id,
  * if the environment is other than Web this function returns undefined
  * @param elementId the beagle element id
  * @returns the element of the given Id
  */
function getElementByBeagleId(elementId) {
    if (!document || !document.querySelector)
        return;
    return document.querySelector("[data-beagle-id=\"" + elementId + "\"]");
}
exports.getElementByBeagleId = getElementByBeagleId;
/**
  * Finds the position of the given `Element`
  * @param elementId the beagle element id
  * @returns Returns the position of the element `{ x, y }`
  */
function getElementPosition(element) {
    if (!element)
        return;
    return {
        x: element.getBoundingClientRect().left,
        y: element.getBoundingClientRect().top,
    };
}
exports.getElementPosition = getElementPosition;
//# sourceMappingURL=html.js.map

/***/ }),

/***/ 2413:
/***/ (function(__unused_webpack_module, exports) {

"use strict";

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
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
function createMapOfKeys(values) {
    var keys = Object.keys(values);
    return keys.reduce(function (result, key) {
        var _a;
        return (__assign(__assign({}, result), (_a = {}, _a[key.toLowerCase()] = key, _a)));
    }, {});
}
var getLowercaseMapOfKeys = (function () {
    var memo = new Map();
    return function (components) {
        if (!memo.has(components))
            memo.set(components, createMapOfKeys(components));
        return memo.get(components);
    };
})();
/**
 * Gets the value for `key` in `object`. Using this function, `key` is case-insensitive, i.e,
 * `object[foo]`, `object[Foo]`, `object[fOo]` are all the same in the eyes of this function.
 *
 * @param object the object to retrieve the value from
 * @param key the property key (case-insensitive)
 * @returns the value `object[key]` where `key` is case insensitive
 */
function getValueByCaseInsensitiveKey(object, key) {
    var lowercaseKeyMap = getLowercaseMapOfKeys(object) || {};
    var originalKey = lowercaseKeyMap[key.toLowerCase()];
    return object[originalKey];
}
/**
 * Given an object and a key, finds if the object has the property `key`, but in this function, the
 * search is done in a case insensitive manner. If the key exists in the object, the original key
 * (case-sensitive) is returned, otherwise, undefined is returned.
 *
 * Example: suppose an object with a property called `Name`. If this function is called with this
 * object and the second parameter `name` or `Name` or `nAMe`, the result will be `Name`, the
 * original name of the property.
 *
 * @param object the object to retrieve the original key from
 * @param key the property key (case-insensitive)
 * @returns the original case-sensitive key
 */
function getOriginalKeyByCaseInsensitiveKey(object, key) {
    var lowercaseKeyMap = getLowercaseMapOfKeys(object) || {};
    var originalKey = lowercaseKeyMap[key.toLowerCase()];
    return originalKey;
}
exports.default = {
    getValueByCaseInsensitiveKey: getValueByCaseInsensitiveKey,
    getOriginalKeyByCaseInsensitiveKey: getOriginalKeyByCaseInsensitiveKey,
};
//# sourceMappingURL=object.js.map

/***/ }),

/***/ 8490:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
/**
 * Removes a prefix from the string.
 *
 * @param str the string to have the `prefix` removed from
 * @param prefix the prefix to remove from the string
 * @returns the string without the prefix
 */
function removePrefix(str, prefix) {
    return str.replace(new RegExp("^" + prefix), '');
}
/**
 * Adds a single character to start of the string if the string doesn't yet start with this
 * character.
 *
 * @param str the string to add the prefix character to
 * @param prefix the single character to add as prefix
 * @returns the resulting string
 */
function addPrefix(str, prefix) {
    return (!str || str[0] !== prefix) ? "" + prefix + str : str;
}
/**
 * Removes a suffix from the string.
 *
 * @param str the string to have the `suffix` removed from
 * @param suffix the suffix to remove from the string
 * @returns the string without the suffix
 */
function removeSuffix(str, suffix) {
    return str.replace(new RegExp(suffix + "$"), '');
}
/**
 * Transforms the first letter of the string into an uppercase letter.
 *
 * @param str the string to capitalize
 * @returns the resulting string
 */
function capitalizeFirstLetter(str) {
    if (!str.length)
        return str;
    return "" + str.charAt(0).toUpperCase() + str.slice(1);
}
exports.default = {
    removePrefix: removePrefix,
    addPrefix: addPrefix,
    removeSuffix: removeSuffix,
    capitalizeFirstLetter: capitalizeFirstLetter,
};
//# sourceMappingURL=string.js.map

/***/ }),

/***/ 5905:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

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
Object.defineProperty(exports, "__esModule", ({ value: true }));
/**
 * Creates a query parameter string according to a <key, value> map.
 *
 * Example: for the input `{ name: 'Shallan Davar', 'birth place': 'Jah Keved' }`, the result
 * would be `?name=Shallan%20Davar&birth%20place=Jah%20Keved`.
 *
 * @param data the <key, value> map
 * @returns the query string
 */
function createQueryString(data) {
    if (!data || !Object.keys(data).length)
        return '';
    var keys = Object.keys(data);
    var params = keys.map(function (key) { return encodeURIComponent(key) + "=" + encodeURIComponent(data[key]); });
    return "?" + params.join('&');
}
exports.default = {
    createQueryString: createQueryString,
};
//# sourceMappingURL=url.js.map

/***/ }),

/***/ 8552:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852),
    root = __webpack_require__(5639);

/* Built-in method references that are verified to be native. */
var DataView = getNative(root, 'DataView');

module.exports = DataView;


/***/ }),

/***/ 1989:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var hashClear = __webpack_require__(1789),
    hashDelete = __webpack_require__(401),
    hashGet = __webpack_require__(7667),
    hashHas = __webpack_require__(1327),
    hashSet = __webpack_require__(1866);

/**
 * Creates a hash object.
 *
 * @private
 * @constructor
 * @param {Array} [entries] The key-value pairs to cache.
 */
function Hash(entries) {
  var index = -1,
      length = entries == null ? 0 : entries.length;

  this.clear();
  while (++index < length) {
    var entry = entries[index];
    this.set(entry[0], entry[1]);
  }
}

// Add methods to `Hash`.
Hash.prototype.clear = hashClear;
Hash.prototype['delete'] = hashDelete;
Hash.prototype.get = hashGet;
Hash.prototype.has = hashHas;
Hash.prototype.set = hashSet;

module.exports = Hash;


/***/ }),

/***/ 8407:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var listCacheClear = __webpack_require__(7040),
    listCacheDelete = __webpack_require__(4125),
    listCacheGet = __webpack_require__(2117),
    listCacheHas = __webpack_require__(7518),
    listCacheSet = __webpack_require__(4705);

/**
 * Creates an list cache object.
 *
 * @private
 * @constructor
 * @param {Array} [entries] The key-value pairs to cache.
 */
function ListCache(entries) {
  var index = -1,
      length = entries == null ? 0 : entries.length;

  this.clear();
  while (++index < length) {
    var entry = entries[index];
    this.set(entry[0], entry[1]);
  }
}

// Add methods to `ListCache`.
ListCache.prototype.clear = listCacheClear;
ListCache.prototype['delete'] = listCacheDelete;
ListCache.prototype.get = listCacheGet;
ListCache.prototype.has = listCacheHas;
ListCache.prototype.set = listCacheSet;

module.exports = ListCache;


/***/ }),

/***/ 7071:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852),
    root = __webpack_require__(5639);

/* Built-in method references that are verified to be native. */
var Map = getNative(root, 'Map');

module.exports = Map;


/***/ }),

/***/ 3369:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var mapCacheClear = __webpack_require__(4785),
    mapCacheDelete = __webpack_require__(1285),
    mapCacheGet = __webpack_require__(6000),
    mapCacheHas = __webpack_require__(9916),
    mapCacheSet = __webpack_require__(5265);

/**
 * Creates a map cache object to store key-value pairs.
 *
 * @private
 * @constructor
 * @param {Array} [entries] The key-value pairs to cache.
 */
function MapCache(entries) {
  var index = -1,
      length = entries == null ? 0 : entries.length;

  this.clear();
  while (++index < length) {
    var entry = entries[index];
    this.set(entry[0], entry[1]);
  }
}

// Add methods to `MapCache`.
MapCache.prototype.clear = mapCacheClear;
MapCache.prototype['delete'] = mapCacheDelete;
MapCache.prototype.get = mapCacheGet;
MapCache.prototype.has = mapCacheHas;
MapCache.prototype.set = mapCacheSet;

module.exports = MapCache;


/***/ }),

/***/ 3818:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852),
    root = __webpack_require__(5639);

/* Built-in method references that are verified to be native. */
var Promise = getNative(root, 'Promise');

module.exports = Promise;


/***/ }),

/***/ 8525:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852),
    root = __webpack_require__(5639);

/* Built-in method references that are verified to be native. */
var Set = getNative(root, 'Set');

module.exports = Set;


/***/ }),

/***/ 8668:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var MapCache = __webpack_require__(3369),
    setCacheAdd = __webpack_require__(619),
    setCacheHas = __webpack_require__(2385);

/**
 *
 * Creates an array cache object to store unique values.
 *
 * @private
 * @constructor
 * @param {Array} [values] The values to cache.
 */
function SetCache(values) {
  var index = -1,
      length = values == null ? 0 : values.length;

  this.__data__ = new MapCache;
  while (++index < length) {
    this.add(values[index]);
  }
}

// Add methods to `SetCache`.
SetCache.prototype.add = SetCache.prototype.push = setCacheAdd;
SetCache.prototype.has = setCacheHas;

module.exports = SetCache;


/***/ }),

/***/ 6384:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var ListCache = __webpack_require__(8407),
    stackClear = __webpack_require__(7465),
    stackDelete = __webpack_require__(3779),
    stackGet = __webpack_require__(7599),
    stackHas = __webpack_require__(4758),
    stackSet = __webpack_require__(4309);

/**
 * Creates a stack cache object to store key-value pairs.
 *
 * @private
 * @constructor
 * @param {Array} [entries] The key-value pairs to cache.
 */
function Stack(entries) {
  var data = this.__data__ = new ListCache(entries);
  this.size = data.size;
}

// Add methods to `Stack`.
Stack.prototype.clear = stackClear;
Stack.prototype['delete'] = stackDelete;
Stack.prototype.get = stackGet;
Stack.prototype.has = stackHas;
Stack.prototype.set = stackSet;

module.exports = Stack;


/***/ }),

/***/ 2705:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var root = __webpack_require__(5639);

/** Built-in value references. */
var Symbol = root.Symbol;

module.exports = Symbol;


/***/ }),

/***/ 1149:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var root = __webpack_require__(5639);

/** Built-in value references. */
var Uint8Array = root.Uint8Array;

module.exports = Uint8Array;


/***/ }),

/***/ 577:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852),
    root = __webpack_require__(5639);

/* Built-in method references that are verified to be native. */
var WeakMap = getNative(root, 'WeakMap');

module.exports = WeakMap;


/***/ }),

/***/ 7412:
/***/ ((module) => {

/**
 * A specialized version of `_.forEach` for arrays without support for
 * iteratee shorthands.
 *
 * @private
 * @param {Array} [array] The array to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Array} Returns `array`.
 */
function arrayEach(array, iteratee) {
  var index = -1,
      length = array == null ? 0 : array.length;

  while (++index < length) {
    if (iteratee(array[index], index, array) === false) {
      break;
    }
  }
  return array;
}

module.exports = arrayEach;


/***/ }),

/***/ 4963:
/***/ ((module) => {

/**
 * A specialized version of `_.filter` for arrays without support for
 * iteratee shorthands.
 *
 * @private
 * @param {Array} [array] The array to iterate over.
 * @param {Function} predicate The function invoked per iteration.
 * @returns {Array} Returns the new filtered array.
 */
function arrayFilter(array, predicate) {
  var index = -1,
      length = array == null ? 0 : array.length,
      resIndex = 0,
      result = [];

  while (++index < length) {
    var value = array[index];
    if (predicate(value, index, array)) {
      result[resIndex++] = value;
    }
  }
  return result;
}

module.exports = arrayFilter;


/***/ }),

/***/ 4636:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseTimes = __webpack_require__(2545),
    isArguments = __webpack_require__(5694),
    isArray = __webpack_require__(1469),
    isBuffer = __webpack_require__(4144),
    isIndex = __webpack_require__(5776),
    isTypedArray = __webpack_require__(6719);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Creates an array of the enumerable property names of the array-like `value`.
 *
 * @private
 * @param {*} value The value to query.
 * @param {boolean} inherited Specify returning inherited property names.
 * @returns {Array} Returns the array of property names.
 */
function arrayLikeKeys(value, inherited) {
  var isArr = isArray(value),
      isArg = !isArr && isArguments(value),
      isBuff = !isArr && !isArg && isBuffer(value),
      isType = !isArr && !isArg && !isBuff && isTypedArray(value),
      skipIndexes = isArr || isArg || isBuff || isType,
      result = skipIndexes ? baseTimes(value.length, String) : [],
      length = result.length;

  for (var key in value) {
    if ((inherited || hasOwnProperty.call(value, key)) &&
        !(skipIndexes && (
           // Safari 9 has enumerable `arguments.length` in strict mode.
           key == 'length' ||
           // Node.js 0.10 has enumerable non-index properties on buffers.
           (isBuff && (key == 'offset' || key == 'parent')) ||
           // PhantomJS 2 has enumerable non-index properties on typed arrays.
           (isType && (key == 'buffer' || key == 'byteLength' || key == 'byteOffset')) ||
           // Skip index properties.
           isIndex(key, length)
        ))) {
      result.push(key);
    }
  }
  return result;
}

module.exports = arrayLikeKeys;


/***/ }),

/***/ 9932:
/***/ ((module) => {

/**
 * A specialized version of `_.map` for arrays without support for iteratee
 * shorthands.
 *
 * @private
 * @param {Array} [array] The array to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Array} Returns the new mapped array.
 */
function arrayMap(array, iteratee) {
  var index = -1,
      length = array == null ? 0 : array.length,
      result = Array(length);

  while (++index < length) {
    result[index] = iteratee(array[index], index, array);
  }
  return result;
}

module.exports = arrayMap;


/***/ }),

/***/ 2488:
/***/ ((module) => {

/**
 * Appends the elements of `values` to `array`.
 *
 * @private
 * @param {Array} array The array to modify.
 * @param {Array} values The values to append.
 * @returns {Array} Returns `array`.
 */
function arrayPush(array, values) {
  var index = -1,
      length = values.length,
      offset = array.length;

  while (++index < length) {
    array[offset + index] = values[index];
  }
  return array;
}

module.exports = arrayPush;


/***/ }),

/***/ 2908:
/***/ ((module) => {

/**
 * A specialized version of `_.some` for arrays without support for iteratee
 * shorthands.
 *
 * @private
 * @param {Array} [array] The array to iterate over.
 * @param {Function} predicate The function invoked per iteration.
 * @returns {boolean} Returns `true` if any element passes the predicate check,
 *  else `false`.
 */
function arraySome(array, predicate) {
  var index = -1,
      length = array == null ? 0 : array.length;

  while (++index < length) {
    if (predicate(array[index], index, array)) {
      return true;
    }
  }
  return false;
}

module.exports = arraySome;


/***/ }),

/***/ 4286:
/***/ ((module) => {

/**
 * Converts an ASCII `string` to an array.
 *
 * @private
 * @param {string} string The string to convert.
 * @returns {Array} Returns the converted array.
 */
function asciiToArray(string) {
  return string.split('');
}

module.exports = asciiToArray;


/***/ }),

/***/ 4865:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseAssignValue = __webpack_require__(9465),
    eq = __webpack_require__(7813);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Assigns `value` to `key` of `object` if the existing value is not equivalent
 * using [`SameValueZero`](http://ecma-international.org/ecma-262/7.0/#sec-samevaluezero)
 * for equality comparisons.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {string} key The key of the property to assign.
 * @param {*} value The value to assign.
 */
function assignValue(object, key, value) {
  var objValue = object[key];
  if (!(hasOwnProperty.call(object, key) && eq(objValue, value)) ||
      (value === undefined && !(key in object))) {
    baseAssignValue(object, key, value);
  }
}

module.exports = assignValue;


/***/ }),

/***/ 8470:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var eq = __webpack_require__(7813);

/**
 * Gets the index at which the `key` is found in `array` of key-value pairs.
 *
 * @private
 * @param {Array} array The array to inspect.
 * @param {*} key The key to search for.
 * @returns {number} Returns the index of the matched value, else `-1`.
 */
function assocIndexOf(array, key) {
  var length = array.length;
  while (length--) {
    if (eq(array[length][0], key)) {
      return length;
    }
  }
  return -1;
}

module.exports = assocIndexOf;


/***/ }),

/***/ 4037:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var copyObject = __webpack_require__(8363),
    keys = __webpack_require__(3674);

/**
 * The base implementation of `_.assign` without support for multiple sources
 * or `customizer` functions.
 *
 * @private
 * @param {Object} object The destination object.
 * @param {Object} source The source object.
 * @returns {Object} Returns `object`.
 */
function baseAssign(object, source) {
  return object && copyObject(source, keys(source), object);
}

module.exports = baseAssign;


/***/ }),

/***/ 3886:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var copyObject = __webpack_require__(8363),
    keysIn = __webpack_require__(1704);

/**
 * The base implementation of `_.assignIn` without support for multiple sources
 * or `customizer` functions.
 *
 * @private
 * @param {Object} object The destination object.
 * @param {Object} source The source object.
 * @returns {Object} Returns `object`.
 */
function baseAssignIn(object, source) {
  return object && copyObject(source, keysIn(source), object);
}

module.exports = baseAssignIn;


/***/ }),

/***/ 9465:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var defineProperty = __webpack_require__(8777);

/**
 * The base implementation of `assignValue` and `assignMergeValue` without
 * value checks.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {string} key The key of the property to assign.
 * @param {*} value The value to assign.
 */
function baseAssignValue(object, key, value) {
  if (key == '__proto__' && defineProperty) {
    defineProperty(object, key, {
      'configurable': true,
      'enumerable': true,
      'value': value,
      'writable': true
    });
  } else {
    object[key] = value;
  }
}

module.exports = baseAssignValue;


/***/ }),

/***/ 5990:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Stack = __webpack_require__(6384),
    arrayEach = __webpack_require__(7412),
    assignValue = __webpack_require__(4865),
    baseAssign = __webpack_require__(4037),
    baseAssignIn = __webpack_require__(3886),
    cloneBuffer = __webpack_require__(4626),
    copyArray = __webpack_require__(278),
    copySymbols = __webpack_require__(8805),
    copySymbolsIn = __webpack_require__(1911),
    getAllKeys = __webpack_require__(8234),
    getAllKeysIn = __webpack_require__(6904),
    getTag = __webpack_require__(4160),
    initCloneArray = __webpack_require__(3824),
    initCloneByTag = __webpack_require__(9148),
    initCloneObject = __webpack_require__(8517),
    isArray = __webpack_require__(1469),
    isBuffer = __webpack_require__(4144),
    isMap = __webpack_require__(6688),
    isObject = __webpack_require__(3218),
    isSet = __webpack_require__(2928),
    keys = __webpack_require__(3674),
    keysIn = __webpack_require__(1704);

/** Used to compose bitmasks for cloning. */
var CLONE_DEEP_FLAG = 1,
    CLONE_FLAT_FLAG = 2,
    CLONE_SYMBOLS_FLAG = 4;

/** `Object#toString` result references. */
var argsTag = '[object Arguments]',
    arrayTag = '[object Array]',
    boolTag = '[object Boolean]',
    dateTag = '[object Date]',
    errorTag = '[object Error]',
    funcTag = '[object Function]',
    genTag = '[object GeneratorFunction]',
    mapTag = '[object Map]',
    numberTag = '[object Number]',
    objectTag = '[object Object]',
    regexpTag = '[object RegExp]',
    setTag = '[object Set]',
    stringTag = '[object String]',
    symbolTag = '[object Symbol]',
    weakMapTag = '[object WeakMap]';

var arrayBufferTag = '[object ArrayBuffer]',
    dataViewTag = '[object DataView]',
    float32Tag = '[object Float32Array]',
    float64Tag = '[object Float64Array]',
    int8Tag = '[object Int8Array]',
    int16Tag = '[object Int16Array]',
    int32Tag = '[object Int32Array]',
    uint8Tag = '[object Uint8Array]',
    uint8ClampedTag = '[object Uint8ClampedArray]',
    uint16Tag = '[object Uint16Array]',
    uint32Tag = '[object Uint32Array]';

/** Used to identify `toStringTag` values supported by `_.clone`. */
var cloneableTags = {};
cloneableTags[argsTag] = cloneableTags[arrayTag] =
cloneableTags[arrayBufferTag] = cloneableTags[dataViewTag] =
cloneableTags[boolTag] = cloneableTags[dateTag] =
cloneableTags[float32Tag] = cloneableTags[float64Tag] =
cloneableTags[int8Tag] = cloneableTags[int16Tag] =
cloneableTags[int32Tag] = cloneableTags[mapTag] =
cloneableTags[numberTag] = cloneableTags[objectTag] =
cloneableTags[regexpTag] = cloneableTags[setTag] =
cloneableTags[stringTag] = cloneableTags[symbolTag] =
cloneableTags[uint8Tag] = cloneableTags[uint8ClampedTag] =
cloneableTags[uint16Tag] = cloneableTags[uint32Tag] = true;
cloneableTags[errorTag] = cloneableTags[funcTag] =
cloneableTags[weakMapTag] = false;

/**
 * The base implementation of `_.clone` and `_.cloneDeep` which tracks
 * traversed objects.
 *
 * @private
 * @param {*} value The value to clone.
 * @param {boolean} bitmask The bitmask flags.
 *  1 - Deep clone
 *  2 - Flatten inherited properties
 *  4 - Clone symbols
 * @param {Function} [customizer] The function to customize cloning.
 * @param {string} [key] The key of `value`.
 * @param {Object} [object] The parent object of `value`.
 * @param {Object} [stack] Tracks traversed objects and their clone counterparts.
 * @returns {*} Returns the cloned value.
 */
function baseClone(value, bitmask, customizer, key, object, stack) {
  var result,
      isDeep = bitmask & CLONE_DEEP_FLAG,
      isFlat = bitmask & CLONE_FLAT_FLAG,
      isFull = bitmask & CLONE_SYMBOLS_FLAG;

  if (customizer) {
    result = object ? customizer(value, key, object, stack) : customizer(value);
  }
  if (result !== undefined) {
    return result;
  }
  if (!isObject(value)) {
    return value;
  }
  var isArr = isArray(value);
  if (isArr) {
    result = initCloneArray(value);
    if (!isDeep) {
      return copyArray(value, result);
    }
  } else {
    var tag = getTag(value),
        isFunc = tag == funcTag || tag == genTag;

    if (isBuffer(value)) {
      return cloneBuffer(value, isDeep);
    }
    if (tag == objectTag || tag == argsTag || (isFunc && !object)) {
      result = (isFlat || isFunc) ? {} : initCloneObject(value);
      if (!isDeep) {
        return isFlat
          ? copySymbolsIn(value, baseAssignIn(result, value))
          : copySymbols(value, baseAssign(result, value));
      }
    } else {
      if (!cloneableTags[tag]) {
        return object ? value : {};
      }
      result = initCloneByTag(value, tag, isDeep);
    }
  }
  // Check for circular references and return its corresponding clone.
  stack || (stack = new Stack);
  var stacked = stack.get(value);
  if (stacked) {
    return stacked;
  }
  stack.set(value, result);

  if (isSet(value)) {
    value.forEach(function(subValue) {
      result.add(baseClone(subValue, bitmask, customizer, subValue, value, stack));
    });
  } else if (isMap(value)) {
    value.forEach(function(subValue, key) {
      result.set(key, baseClone(subValue, bitmask, customizer, key, value, stack));
    });
  }

  var keysFunc = isFull
    ? (isFlat ? getAllKeysIn : getAllKeys)
    : (isFlat ? keysIn : keys);

  var props = isArr ? undefined : keysFunc(value);
  arrayEach(props || value, function(subValue, key) {
    if (props) {
      key = subValue;
      subValue = value[key];
    }
    // Recursively populate clone (susceptible to call stack limits).
    assignValue(result, key, baseClone(subValue, bitmask, customizer, key, value, stack));
  });
  return result;
}

module.exports = baseClone;


/***/ }),

/***/ 3118:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isObject = __webpack_require__(3218);

/** Built-in value references. */
var objectCreate = Object.create;

/**
 * The base implementation of `_.create` without support for assigning
 * properties to the created object.
 *
 * @private
 * @param {Object} proto The object to inherit from.
 * @returns {Object} Returns the new object.
 */
var baseCreate = (function() {
  function object() {}
  return function(proto) {
    if (!isObject(proto)) {
      return {};
    }
    if (objectCreate) {
      return objectCreate(proto);
    }
    object.prototype = proto;
    var result = new object;
    object.prototype = undefined;
    return result;
  };
}());

module.exports = baseCreate;


/***/ }),

/***/ 1848:
/***/ ((module) => {

/**
 * The base implementation of `_.findIndex` and `_.findLastIndex` without
 * support for iteratee shorthands.
 *
 * @private
 * @param {Array} array The array to inspect.
 * @param {Function} predicate The function invoked per iteration.
 * @param {number} fromIndex The index to search from.
 * @param {boolean} [fromRight] Specify iterating from right to left.
 * @returns {number} Returns the index of the matched value, else `-1`.
 */
function baseFindIndex(array, predicate, fromIndex, fromRight) {
  var length = array.length,
      index = fromIndex + (fromRight ? 1 : -1);

  while ((fromRight ? index-- : ++index < length)) {
    if (predicate(array[index], index, array)) {
      return index;
    }
  }
  return -1;
}

module.exports = baseFindIndex;


/***/ }),

/***/ 1078:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayPush = __webpack_require__(2488),
    isFlattenable = __webpack_require__(7285);

/**
 * The base implementation of `_.flatten` with support for restricting flattening.
 *
 * @private
 * @param {Array} array The array to flatten.
 * @param {number} depth The maximum recursion depth.
 * @param {boolean} [predicate=isFlattenable] The function invoked per iteration.
 * @param {boolean} [isStrict] Restrict to values that pass `predicate` checks.
 * @param {Array} [result=[]] The initial result value.
 * @returns {Array} Returns the new flattened array.
 */
function baseFlatten(array, depth, predicate, isStrict, result) {
  var index = -1,
      length = array.length;

  predicate || (predicate = isFlattenable);
  result || (result = []);

  while (++index < length) {
    var value = array[index];
    if (depth > 0 && predicate(value)) {
      if (depth > 1) {
        // Recursively flatten arrays (susceptible to call stack limits).
        baseFlatten(value, depth - 1, predicate, isStrict, result);
      } else {
        arrayPush(result, value);
      }
    } else if (!isStrict) {
      result[result.length] = value;
    }
  }
  return result;
}

module.exports = baseFlatten;


/***/ }),

/***/ 8483:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var createBaseFor = __webpack_require__(5063);

/**
 * The base implementation of `baseForOwn` which iterates over `object`
 * properties returned by `keysFunc` and invokes `iteratee` for each property.
 * Iteratee functions may exit iteration early by explicitly returning `false`.
 *
 * @private
 * @param {Object} object The object to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @param {Function} keysFunc The function to get the keys of `object`.
 * @returns {Object} Returns `object`.
 */
var baseFor = createBaseFor();

module.exports = baseFor;


/***/ }),

/***/ 7816:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseFor = __webpack_require__(8483),
    keys = __webpack_require__(3674);

/**
 * The base implementation of `_.forOwn` without support for iteratee shorthands.
 *
 * @private
 * @param {Object} object The object to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Object} Returns `object`.
 */
function baseForOwn(object, iteratee) {
  return object && baseFor(object, iteratee, keys);
}

module.exports = baseForOwn;


/***/ }),

/***/ 7786:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var castPath = __webpack_require__(1811),
    toKey = __webpack_require__(327);

/**
 * The base implementation of `_.get` without support for default values.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {Array|string} path The path of the property to get.
 * @returns {*} Returns the resolved value.
 */
function baseGet(object, path) {
  path = castPath(path, object);

  var index = 0,
      length = path.length;

  while (object != null && index < length) {
    object = object[toKey(path[index++])];
  }
  return (index && index == length) ? object : undefined;
}

module.exports = baseGet;


/***/ }),

/***/ 8866:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayPush = __webpack_require__(2488),
    isArray = __webpack_require__(1469);

/**
 * The base implementation of `getAllKeys` and `getAllKeysIn` which uses
 * `keysFunc` and `symbolsFunc` to get the enumerable property names and
 * symbols of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {Function} keysFunc The function to get the keys of `object`.
 * @param {Function} symbolsFunc The function to get the symbols of `object`.
 * @returns {Array} Returns the array of property names and symbols.
 */
function baseGetAllKeys(object, keysFunc, symbolsFunc) {
  var result = keysFunc(object);
  return isArray(object) ? result : arrayPush(result, symbolsFunc(object));
}

module.exports = baseGetAllKeys;


/***/ }),

/***/ 4239:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705),
    getRawTag = __webpack_require__(9607),
    objectToString = __webpack_require__(2333);

/** `Object#toString` result references. */
var nullTag = '[object Null]',
    undefinedTag = '[object Undefined]';

/** Built-in value references. */
var symToStringTag = Symbol ? Symbol.toStringTag : undefined;

/**
 * The base implementation of `getTag` without fallbacks for buggy environments.
 *
 * @private
 * @param {*} value The value to query.
 * @returns {string} Returns the `toStringTag`.
 */
function baseGetTag(value) {
  if (value == null) {
    return value === undefined ? undefinedTag : nullTag;
  }
  return (symToStringTag && symToStringTag in Object(value))
    ? getRawTag(value)
    : objectToString(value);
}

module.exports = baseGetTag;


/***/ }),

/***/ 8565:
/***/ ((module) => {

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * The base implementation of `_.has` without support for deep paths.
 *
 * @private
 * @param {Object} [object] The object to query.
 * @param {Array|string} key The key to check.
 * @returns {boolean} Returns `true` if `key` exists, else `false`.
 */
function baseHas(object, key) {
  return object != null && hasOwnProperty.call(object, key);
}

module.exports = baseHas;


/***/ }),

/***/ 13:
/***/ ((module) => {

/**
 * The base implementation of `_.hasIn` without support for deep paths.
 *
 * @private
 * @param {Object} [object] The object to query.
 * @param {Array|string} key The key to check.
 * @returns {boolean} Returns `true` if `key` exists, else `false`.
 */
function baseHasIn(object, key) {
  return object != null && key in Object(object);
}

module.exports = baseHasIn;


/***/ }),

/***/ 9454:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetTag = __webpack_require__(4239),
    isObjectLike = __webpack_require__(7005);

/** `Object#toString` result references. */
var argsTag = '[object Arguments]';

/**
 * The base implementation of `_.isArguments`.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an `arguments` object,
 */
function baseIsArguments(value) {
  return isObjectLike(value) && baseGetTag(value) == argsTag;
}

module.exports = baseIsArguments;


/***/ }),

/***/ 939:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsEqualDeep = __webpack_require__(2492),
    isObjectLike = __webpack_require__(7005);

/**
 * The base implementation of `_.isEqual` which supports partial comparisons
 * and tracks traversed objects.
 *
 * @private
 * @param {*} value The value to compare.
 * @param {*} other The other value to compare.
 * @param {boolean} bitmask The bitmask flags.
 *  1 - Unordered comparison
 *  2 - Partial comparison
 * @param {Function} [customizer] The function to customize comparisons.
 * @param {Object} [stack] Tracks traversed `value` and `other` objects.
 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
 */
function baseIsEqual(value, other, bitmask, customizer, stack) {
  if (value === other) {
    return true;
  }
  if (value == null || other == null || (!isObjectLike(value) && !isObjectLike(other))) {
    return value !== value && other !== other;
  }
  return baseIsEqualDeep(value, other, bitmask, customizer, baseIsEqual, stack);
}

module.exports = baseIsEqual;


/***/ }),

/***/ 2492:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Stack = __webpack_require__(6384),
    equalArrays = __webpack_require__(7114),
    equalByTag = __webpack_require__(8351),
    equalObjects = __webpack_require__(6096),
    getTag = __webpack_require__(4160),
    isArray = __webpack_require__(1469),
    isBuffer = __webpack_require__(4144),
    isTypedArray = __webpack_require__(6719);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1;

/** `Object#toString` result references. */
var argsTag = '[object Arguments]',
    arrayTag = '[object Array]',
    objectTag = '[object Object]';

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * A specialized version of `baseIsEqual` for arrays and objects which performs
 * deep comparisons and tracks traversed objects enabling objects with circular
 * references to be compared.
 *
 * @private
 * @param {Object} object The object to compare.
 * @param {Object} other The other object to compare.
 * @param {number} bitmask The bitmask flags. See `baseIsEqual` for more details.
 * @param {Function} customizer The function to customize comparisons.
 * @param {Function} equalFunc The function to determine equivalents of values.
 * @param {Object} [stack] Tracks traversed `object` and `other` objects.
 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
 */
function baseIsEqualDeep(object, other, bitmask, customizer, equalFunc, stack) {
  var objIsArr = isArray(object),
      othIsArr = isArray(other),
      objTag = objIsArr ? arrayTag : getTag(object),
      othTag = othIsArr ? arrayTag : getTag(other);

  objTag = objTag == argsTag ? objectTag : objTag;
  othTag = othTag == argsTag ? objectTag : othTag;

  var objIsObj = objTag == objectTag,
      othIsObj = othTag == objectTag,
      isSameTag = objTag == othTag;

  if (isSameTag && isBuffer(object)) {
    if (!isBuffer(other)) {
      return false;
    }
    objIsArr = true;
    objIsObj = false;
  }
  if (isSameTag && !objIsObj) {
    stack || (stack = new Stack);
    return (objIsArr || isTypedArray(object))
      ? equalArrays(object, other, bitmask, customizer, equalFunc, stack)
      : equalByTag(object, other, objTag, bitmask, customizer, equalFunc, stack);
  }
  if (!(bitmask & COMPARE_PARTIAL_FLAG)) {
    var objIsWrapped = objIsObj && hasOwnProperty.call(object, '__wrapped__'),
        othIsWrapped = othIsObj && hasOwnProperty.call(other, '__wrapped__');

    if (objIsWrapped || othIsWrapped) {
      var objUnwrapped = objIsWrapped ? object.value() : object,
          othUnwrapped = othIsWrapped ? other.value() : other;

      stack || (stack = new Stack);
      return equalFunc(objUnwrapped, othUnwrapped, bitmask, customizer, stack);
    }
  }
  if (!isSameTag) {
    return false;
  }
  stack || (stack = new Stack);
  return equalObjects(object, other, bitmask, customizer, equalFunc, stack);
}

module.exports = baseIsEqualDeep;


/***/ }),

/***/ 5588:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getTag = __webpack_require__(4160),
    isObjectLike = __webpack_require__(7005);

/** `Object#toString` result references. */
var mapTag = '[object Map]';

/**
 * The base implementation of `_.isMap` without Node.js optimizations.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a map, else `false`.
 */
function baseIsMap(value) {
  return isObjectLike(value) && getTag(value) == mapTag;
}

module.exports = baseIsMap;


/***/ }),

/***/ 2958:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Stack = __webpack_require__(6384),
    baseIsEqual = __webpack_require__(939);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1,
    COMPARE_UNORDERED_FLAG = 2;

/**
 * The base implementation of `_.isMatch` without support for iteratee shorthands.
 *
 * @private
 * @param {Object} object The object to inspect.
 * @param {Object} source The object of property values to match.
 * @param {Array} matchData The property names, values, and compare flags to match.
 * @param {Function} [customizer] The function to customize comparisons.
 * @returns {boolean} Returns `true` if `object` is a match, else `false`.
 */
function baseIsMatch(object, source, matchData, customizer) {
  var index = matchData.length,
      length = index,
      noCustomizer = !customizer;

  if (object == null) {
    return !length;
  }
  object = Object(object);
  while (index--) {
    var data = matchData[index];
    if ((noCustomizer && data[2])
          ? data[1] !== object[data[0]]
          : !(data[0] in object)
        ) {
      return false;
    }
  }
  while (++index < length) {
    data = matchData[index];
    var key = data[0],
        objValue = object[key],
        srcValue = data[1];

    if (noCustomizer && data[2]) {
      if (objValue === undefined && !(key in object)) {
        return false;
      }
    } else {
      var stack = new Stack;
      if (customizer) {
        var result = customizer(objValue, srcValue, key, object, source, stack);
      }
      if (!(result === undefined
            ? baseIsEqual(srcValue, objValue, COMPARE_PARTIAL_FLAG | COMPARE_UNORDERED_FLAG, customizer, stack)
            : result
          )) {
        return false;
      }
    }
  }
  return true;
}

module.exports = baseIsMatch;


/***/ }),

/***/ 8458:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isFunction = __webpack_require__(3560),
    isMasked = __webpack_require__(5346),
    isObject = __webpack_require__(3218),
    toSource = __webpack_require__(346);

/**
 * Used to match `RegExp`
 * [syntax characters](http://ecma-international.org/ecma-262/7.0/#sec-patterns).
 */
var reRegExpChar = /[\\^$.*+?()[\]{}|]/g;

/** Used to detect host constructors (Safari). */
var reIsHostCtor = /^\[object .+?Constructor\]$/;

/** Used for built-in method references. */
var funcProto = Function.prototype,
    objectProto = Object.prototype;

/** Used to resolve the decompiled source of functions. */
var funcToString = funcProto.toString;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/** Used to detect if a method is native. */
var reIsNative = RegExp('^' +
  funcToString.call(hasOwnProperty).replace(reRegExpChar, '\\$&')
  .replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g, '$1.*?') + '$'
);

/**
 * The base implementation of `_.isNative` without bad shim checks.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a native function,
 *  else `false`.
 */
function baseIsNative(value) {
  if (!isObject(value) || isMasked(value)) {
    return false;
  }
  var pattern = isFunction(value) ? reIsNative : reIsHostCtor;
  return pattern.test(toSource(value));
}

module.exports = baseIsNative;


/***/ }),

/***/ 9221:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getTag = __webpack_require__(4160),
    isObjectLike = __webpack_require__(7005);

/** `Object#toString` result references. */
var setTag = '[object Set]';

/**
 * The base implementation of `_.isSet` without Node.js optimizations.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a set, else `false`.
 */
function baseIsSet(value) {
  return isObjectLike(value) && getTag(value) == setTag;
}

module.exports = baseIsSet;


/***/ }),

/***/ 8749:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetTag = __webpack_require__(4239),
    isLength = __webpack_require__(1780),
    isObjectLike = __webpack_require__(7005);

/** `Object#toString` result references. */
var argsTag = '[object Arguments]',
    arrayTag = '[object Array]',
    boolTag = '[object Boolean]',
    dateTag = '[object Date]',
    errorTag = '[object Error]',
    funcTag = '[object Function]',
    mapTag = '[object Map]',
    numberTag = '[object Number]',
    objectTag = '[object Object]',
    regexpTag = '[object RegExp]',
    setTag = '[object Set]',
    stringTag = '[object String]',
    weakMapTag = '[object WeakMap]';

var arrayBufferTag = '[object ArrayBuffer]',
    dataViewTag = '[object DataView]',
    float32Tag = '[object Float32Array]',
    float64Tag = '[object Float64Array]',
    int8Tag = '[object Int8Array]',
    int16Tag = '[object Int16Array]',
    int32Tag = '[object Int32Array]',
    uint8Tag = '[object Uint8Array]',
    uint8ClampedTag = '[object Uint8ClampedArray]',
    uint16Tag = '[object Uint16Array]',
    uint32Tag = '[object Uint32Array]';

/** Used to identify `toStringTag` values of typed arrays. */
var typedArrayTags = {};
typedArrayTags[float32Tag] = typedArrayTags[float64Tag] =
typedArrayTags[int8Tag] = typedArrayTags[int16Tag] =
typedArrayTags[int32Tag] = typedArrayTags[uint8Tag] =
typedArrayTags[uint8ClampedTag] = typedArrayTags[uint16Tag] =
typedArrayTags[uint32Tag] = true;
typedArrayTags[argsTag] = typedArrayTags[arrayTag] =
typedArrayTags[arrayBufferTag] = typedArrayTags[boolTag] =
typedArrayTags[dataViewTag] = typedArrayTags[dateTag] =
typedArrayTags[errorTag] = typedArrayTags[funcTag] =
typedArrayTags[mapTag] = typedArrayTags[numberTag] =
typedArrayTags[objectTag] = typedArrayTags[regexpTag] =
typedArrayTags[setTag] = typedArrayTags[stringTag] =
typedArrayTags[weakMapTag] = false;

/**
 * The base implementation of `_.isTypedArray` without Node.js optimizations.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a typed array, else `false`.
 */
function baseIsTypedArray(value) {
  return isObjectLike(value) &&
    isLength(value.length) && !!typedArrayTags[baseGetTag(value)];
}

module.exports = baseIsTypedArray;


/***/ }),

/***/ 7206:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseMatches = __webpack_require__(1573),
    baseMatchesProperty = __webpack_require__(6432),
    identity = __webpack_require__(6557),
    isArray = __webpack_require__(1469),
    property = __webpack_require__(9601);

/**
 * The base implementation of `_.iteratee`.
 *
 * @private
 * @param {*} [value=_.identity] The value to convert to an iteratee.
 * @returns {Function} Returns the iteratee.
 */
function baseIteratee(value) {
  // Don't store the `typeof` result in a variable to avoid a JIT bug in Safari 9.
  // See https://bugs.webkit.org/show_bug.cgi?id=156034 for more details.
  if (typeof value == 'function') {
    return value;
  }
  if (value == null) {
    return identity;
  }
  if (typeof value == 'object') {
    return isArray(value)
      ? baseMatchesProperty(value[0], value[1])
      : baseMatches(value);
  }
  return property(value);
}

module.exports = baseIteratee;


/***/ }),

/***/ 280:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isPrototype = __webpack_require__(5726),
    nativeKeys = __webpack_require__(6916);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * The base implementation of `_.keys` which doesn't treat sparse arrays as dense.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function baseKeys(object) {
  if (!isPrototype(object)) {
    return nativeKeys(object);
  }
  var result = [];
  for (var key in Object(object)) {
    if (hasOwnProperty.call(object, key) && key != 'constructor') {
      result.push(key);
    }
  }
  return result;
}

module.exports = baseKeys;


/***/ }),

/***/ 313:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isObject = __webpack_require__(3218),
    isPrototype = __webpack_require__(5726),
    nativeKeysIn = __webpack_require__(3498);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * The base implementation of `_.keysIn` which doesn't treat sparse arrays as dense.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function baseKeysIn(object) {
  if (!isObject(object)) {
    return nativeKeysIn(object);
  }
  var isProto = isPrototype(object),
      result = [];

  for (var key in object) {
    if (!(key == 'constructor' && (isProto || !hasOwnProperty.call(object, key)))) {
      result.push(key);
    }
  }
  return result;
}

module.exports = baseKeysIn;


/***/ }),

/***/ 1573:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsMatch = __webpack_require__(2958),
    getMatchData = __webpack_require__(1499),
    matchesStrictComparable = __webpack_require__(2634);

/**
 * The base implementation of `_.matches` which doesn't clone `source`.
 *
 * @private
 * @param {Object} source The object of property values to match.
 * @returns {Function} Returns the new spec function.
 */
function baseMatches(source) {
  var matchData = getMatchData(source);
  if (matchData.length == 1 && matchData[0][2]) {
    return matchesStrictComparable(matchData[0][0], matchData[0][1]);
  }
  return function(object) {
    return object === source || baseIsMatch(object, source, matchData);
  };
}

module.exports = baseMatches;


/***/ }),

/***/ 6432:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsEqual = __webpack_require__(939),
    get = __webpack_require__(7361),
    hasIn = __webpack_require__(9095),
    isKey = __webpack_require__(5403),
    isStrictComparable = __webpack_require__(9162),
    matchesStrictComparable = __webpack_require__(2634),
    toKey = __webpack_require__(327);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1,
    COMPARE_UNORDERED_FLAG = 2;

/**
 * The base implementation of `_.matchesProperty` which doesn't clone `srcValue`.
 *
 * @private
 * @param {string} path The path of the property to get.
 * @param {*} srcValue The value to match.
 * @returns {Function} Returns the new spec function.
 */
function baseMatchesProperty(path, srcValue) {
  if (isKey(path) && isStrictComparable(srcValue)) {
    return matchesStrictComparable(toKey(path), srcValue);
  }
  return function(object) {
    var objValue = get(object, path);
    return (objValue === undefined && objValue === srcValue)
      ? hasIn(object, path)
      : baseIsEqual(srcValue, objValue, COMPARE_PARTIAL_FLAG | COMPARE_UNORDERED_FLAG);
  };
}

module.exports = baseMatchesProperty;


/***/ }),

/***/ 873:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isIndex = __webpack_require__(5776);

/**
 * The base implementation of `_.nth` which doesn't coerce arguments.
 *
 * @private
 * @param {Array} array The array to query.
 * @param {number} n The index of the element to return.
 * @returns {*} Returns the nth element of `array`.
 */
function baseNth(array, n) {
  var length = array.length;
  if (!length) {
    return;
  }
  n += n < 0 ? length : 0;
  return isIndex(n, length) ? array[n] : undefined;
}

module.exports = baseNth;


/***/ }),

/***/ 371:
/***/ ((module) => {

/**
 * The base implementation of `_.property` without support for deep paths.
 *
 * @private
 * @param {string} key The key of the property to get.
 * @returns {Function} Returns the new accessor function.
 */
function baseProperty(key) {
  return function(object) {
    return object == null ? undefined : object[key];
  };
}

module.exports = baseProperty;


/***/ }),

/***/ 9152:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGet = __webpack_require__(7786);

/**
 * A specialized version of `baseProperty` which supports deep paths.
 *
 * @private
 * @param {Array|string} path The path of the property to get.
 * @returns {Function} Returns the new accessor function.
 */
function basePropertyDeep(path) {
  return function(object) {
    return baseGet(object, path);
  };
}

module.exports = basePropertyDeep;


/***/ }),

/***/ 5742:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseUnset = __webpack_require__(7406),
    isIndex = __webpack_require__(5776);

/** Used for built-in method references. */
var arrayProto = Array.prototype;

/** Built-in value references. */
var splice = arrayProto.splice;

/**
 * The base implementation of `_.pullAt` without support for individual
 * indexes or capturing the removed elements.
 *
 * @private
 * @param {Array} array The array to modify.
 * @param {number[]} indexes The indexes of elements to remove.
 * @returns {Array} Returns `array`.
 */
function basePullAt(array, indexes) {
  var length = array ? indexes.length : 0,
      lastIndex = length - 1;

  while (length--) {
    var index = indexes[length];
    if (length == lastIndex || index !== previous) {
      var previous = index;
      if (isIndex(index)) {
        splice.call(array, index, 1);
      } else {
        baseUnset(array, index);
      }
    }
  }
  return array;
}

module.exports = basePullAt;


/***/ }),

/***/ 2491:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assignValue = __webpack_require__(4865),
    castPath = __webpack_require__(1811),
    isIndex = __webpack_require__(5776),
    isObject = __webpack_require__(3218),
    toKey = __webpack_require__(327);

/**
 * The base implementation of `_.set`.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {Array|string} path The path of the property to set.
 * @param {*} value The value to set.
 * @param {Function} [customizer] The function to customize path creation.
 * @returns {Object} Returns `object`.
 */
function baseSet(object, path, value, customizer) {
  if (!isObject(object)) {
    return object;
  }
  path = castPath(path, object);

  var index = -1,
      length = path.length,
      lastIndex = length - 1,
      nested = object;

  while (nested != null && ++index < length) {
    var key = toKey(path[index]),
        newValue = value;

    if (key === '__proto__' || key === 'constructor' || key === 'prototype') {
      return object;
    }

    if (index != lastIndex) {
      var objValue = nested[key];
      newValue = customizer ? customizer(objValue, key, nested) : undefined;
      if (newValue === undefined) {
        newValue = isObject(objValue)
          ? objValue
          : (isIndex(path[index + 1]) ? [] : {});
      }
    }
    assignValue(nested, key, newValue);
    nested = nested[key];
  }
  return object;
}

module.exports = baseSet;


/***/ }),

/***/ 4259:
/***/ ((module) => {

/**
 * The base implementation of `_.slice` without an iteratee call guard.
 *
 * @private
 * @param {Array} array The array to slice.
 * @param {number} [start=0] The start position.
 * @param {number} [end=array.length] The end position.
 * @returns {Array} Returns the slice of `array`.
 */
function baseSlice(array, start, end) {
  var index = -1,
      length = array.length;

  if (start < 0) {
    start = -start > length ? 0 : (length + start);
  }
  end = end > length ? length : end;
  if (end < 0) {
    end += length;
  }
  length = start > end ? 0 : ((end - start) >>> 0);
  start >>>= 0;

  var result = Array(length);
  while (++index < length) {
    result[index] = array[index + start];
  }
  return result;
}

module.exports = baseSlice;


/***/ }),

/***/ 2545:
/***/ ((module) => {

/**
 * The base implementation of `_.times` without support for iteratee shorthands
 * or max array length checks.
 *
 * @private
 * @param {number} n The number of times to invoke `iteratee`.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Array} Returns the array of results.
 */
function baseTimes(n, iteratee) {
  var index = -1,
      result = Array(n);

  while (++index < n) {
    result[index] = iteratee(index);
  }
  return result;
}

module.exports = baseTimes;


/***/ }),

/***/ 531:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705),
    arrayMap = __webpack_require__(9932),
    isArray = __webpack_require__(1469),
    isSymbol = __webpack_require__(3448);

/** Used as references for various `Number` constants. */
var INFINITY = 1 / 0;

/** Used to convert symbols to primitives and strings. */
var symbolProto = Symbol ? Symbol.prototype : undefined,
    symbolToString = symbolProto ? symbolProto.toString : undefined;

/**
 * The base implementation of `_.toString` which doesn't convert nullish
 * values to empty strings.
 *
 * @private
 * @param {*} value The value to process.
 * @returns {string} Returns the string.
 */
function baseToString(value) {
  // Exit early for strings to avoid a performance hit in some environments.
  if (typeof value == 'string') {
    return value;
  }
  if (isArray(value)) {
    // Recursively convert values (susceptible to call stack limits).
    return arrayMap(value, baseToString) + '';
  }
  if (isSymbol(value)) {
    return symbolToString ? symbolToString.call(value) : '';
  }
  var result = (value + '');
  return (result == '0' && (1 / value) == -INFINITY) ? '-0' : result;
}

module.exports = baseToString;


/***/ }),

/***/ 1717:
/***/ ((module) => {

/**
 * The base implementation of `_.unary` without support for storing metadata.
 *
 * @private
 * @param {Function} func The function to cap arguments for.
 * @returns {Function} Returns the new capped function.
 */
function baseUnary(func) {
  return function(value) {
    return func(value);
  };
}

module.exports = baseUnary;


/***/ }),

/***/ 7406:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var castPath = __webpack_require__(1811),
    last = __webpack_require__(928),
    parent = __webpack_require__(292),
    toKey = __webpack_require__(327);

/**
 * The base implementation of `_.unset`.
 *
 * @private
 * @param {Object} object The object to modify.
 * @param {Array|string} path The property path to unset.
 * @returns {boolean} Returns `true` if the property is deleted, else `false`.
 */
function baseUnset(object, path) {
  path = castPath(path, object);
  object = parent(object, path);
  return object == null || delete object[toKey(last(path))];
}

module.exports = baseUnset;


/***/ }),

/***/ 4757:
/***/ ((module) => {

/**
 * Checks if a `cache` value for `key` exists.
 *
 * @private
 * @param {Object} cache The cache to query.
 * @param {string} key The key of the entry to check.
 * @returns {boolean} Returns `true` if an entry for `key` exists, else `false`.
 */
function cacheHas(cache, key) {
  return cache.has(key);
}

module.exports = cacheHas;


/***/ }),

/***/ 1811:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isArray = __webpack_require__(1469),
    isKey = __webpack_require__(5403),
    stringToPath = __webpack_require__(5514),
    toString = __webpack_require__(9833);

/**
 * Casts `value` to a path array if it's not one.
 *
 * @private
 * @param {*} value The value to inspect.
 * @param {Object} [object] The object to query keys on.
 * @returns {Array} Returns the cast property path array.
 */
function castPath(value, object) {
  if (isArray(value)) {
    return value;
  }
  return isKey(value, object) ? [value] : stringToPath(toString(value));
}

module.exports = castPath;


/***/ }),

/***/ 180:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseSlice = __webpack_require__(4259);

/**
 * Casts `array` to a slice if it's needed.
 *
 * @private
 * @param {Array} array The array to inspect.
 * @param {number} start The start position.
 * @param {number} [end=array.length] The end position.
 * @returns {Array} Returns the cast slice.
 */
function castSlice(array, start, end) {
  var length = array.length;
  end = end === undefined ? length : end;
  return (!start && end >= length) ? array : baseSlice(array, start, end);
}

module.exports = castSlice;


/***/ }),

/***/ 4318:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Uint8Array = __webpack_require__(1149);

/**
 * Creates a clone of `arrayBuffer`.
 *
 * @private
 * @param {ArrayBuffer} arrayBuffer The array buffer to clone.
 * @returns {ArrayBuffer} Returns the cloned array buffer.
 */
function cloneArrayBuffer(arrayBuffer) {
  var result = new arrayBuffer.constructor(arrayBuffer.byteLength);
  new Uint8Array(result).set(new Uint8Array(arrayBuffer));
  return result;
}

module.exports = cloneArrayBuffer;


/***/ }),

/***/ 4626:
/***/ ((module, exports, __webpack_require__) => {

/* module decorator */ module = __webpack_require__.nmd(module);
var root = __webpack_require__(5639);

/** Detect free variable `exports`. */
var freeExports =  true && exports && !exports.nodeType && exports;

/** Detect free variable `module`. */
var freeModule = freeExports && "object" == 'object' && module && !module.nodeType && module;

/** Detect the popular CommonJS extension `module.exports`. */
var moduleExports = freeModule && freeModule.exports === freeExports;

/** Built-in value references. */
var Buffer = moduleExports ? root.Buffer : undefined,
    allocUnsafe = Buffer ? Buffer.allocUnsafe : undefined;

/**
 * Creates a clone of  `buffer`.
 *
 * @private
 * @param {Buffer} buffer The buffer to clone.
 * @param {boolean} [isDeep] Specify a deep clone.
 * @returns {Buffer} Returns the cloned buffer.
 */
function cloneBuffer(buffer, isDeep) {
  if (isDeep) {
    return buffer.slice();
  }
  var length = buffer.length,
      result = allocUnsafe ? allocUnsafe(length) : new buffer.constructor(length);

  buffer.copy(result);
  return result;
}

module.exports = cloneBuffer;


/***/ }),

/***/ 7157:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var cloneArrayBuffer = __webpack_require__(4318);

/**
 * Creates a clone of `dataView`.
 *
 * @private
 * @param {Object} dataView The data view to clone.
 * @param {boolean} [isDeep] Specify a deep clone.
 * @returns {Object} Returns the cloned data view.
 */
function cloneDataView(dataView, isDeep) {
  var buffer = isDeep ? cloneArrayBuffer(dataView.buffer) : dataView.buffer;
  return new dataView.constructor(buffer, dataView.byteOffset, dataView.byteLength);
}

module.exports = cloneDataView;


/***/ }),

/***/ 3147:
/***/ ((module) => {

/** Used to match `RegExp` flags from their coerced string values. */
var reFlags = /\w*$/;

/**
 * Creates a clone of `regexp`.
 *
 * @private
 * @param {Object} regexp The regexp to clone.
 * @returns {Object} Returns the cloned regexp.
 */
function cloneRegExp(regexp) {
  var result = new regexp.constructor(regexp.source, reFlags.exec(regexp));
  result.lastIndex = regexp.lastIndex;
  return result;
}

module.exports = cloneRegExp;


/***/ }),

/***/ 419:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705);

/** Used to convert symbols to primitives and strings. */
var symbolProto = Symbol ? Symbol.prototype : undefined,
    symbolValueOf = symbolProto ? symbolProto.valueOf : undefined;

/**
 * Creates a clone of the `symbol` object.
 *
 * @private
 * @param {Object} symbol The symbol object to clone.
 * @returns {Object} Returns the cloned symbol object.
 */
function cloneSymbol(symbol) {
  return symbolValueOf ? Object(symbolValueOf.call(symbol)) : {};
}

module.exports = cloneSymbol;


/***/ }),

/***/ 7133:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var cloneArrayBuffer = __webpack_require__(4318);

/**
 * Creates a clone of `typedArray`.
 *
 * @private
 * @param {Object} typedArray The typed array to clone.
 * @param {boolean} [isDeep] Specify a deep clone.
 * @returns {Object} Returns the cloned typed array.
 */
function cloneTypedArray(typedArray, isDeep) {
  var buffer = isDeep ? cloneArrayBuffer(typedArray.buffer) : typedArray.buffer;
  return new typedArray.constructor(buffer, typedArray.byteOffset, typedArray.length);
}

module.exports = cloneTypedArray;


/***/ }),

/***/ 278:
/***/ ((module) => {

/**
 * Copies the values of `source` to `array`.
 *
 * @private
 * @param {Array} source The array to copy values from.
 * @param {Array} [array=[]] The array to copy values to.
 * @returns {Array} Returns `array`.
 */
function copyArray(source, array) {
  var index = -1,
      length = source.length;

  array || (array = Array(length));
  while (++index < length) {
    array[index] = source[index];
  }
  return array;
}

module.exports = copyArray;


/***/ }),

/***/ 8363:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assignValue = __webpack_require__(4865),
    baseAssignValue = __webpack_require__(9465);

/**
 * Copies properties of `source` to `object`.
 *
 * @private
 * @param {Object} source The object to copy properties from.
 * @param {Array} props The property identifiers to copy.
 * @param {Object} [object={}] The object to copy properties to.
 * @param {Function} [customizer] The function to customize copied values.
 * @returns {Object} Returns `object`.
 */
function copyObject(source, props, object, customizer) {
  var isNew = !object;
  object || (object = {});

  var index = -1,
      length = props.length;

  while (++index < length) {
    var key = props[index];

    var newValue = customizer
      ? customizer(object[key], source[key], key, object, source)
      : undefined;

    if (newValue === undefined) {
      newValue = source[key];
    }
    if (isNew) {
      baseAssignValue(object, key, newValue);
    } else {
      assignValue(object, key, newValue);
    }
  }
  return object;
}

module.exports = copyObject;


/***/ }),

/***/ 8805:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var copyObject = __webpack_require__(8363),
    getSymbols = __webpack_require__(9551);

/**
 * Copies own symbols of `source` to `object`.
 *
 * @private
 * @param {Object} source The object to copy symbols from.
 * @param {Object} [object={}] The object to copy symbols to.
 * @returns {Object} Returns `object`.
 */
function copySymbols(source, object) {
  return copyObject(source, getSymbols(source), object);
}

module.exports = copySymbols;


/***/ }),

/***/ 1911:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var copyObject = __webpack_require__(8363),
    getSymbolsIn = __webpack_require__(1442);

/**
 * Copies own and inherited symbols of `source` to `object`.
 *
 * @private
 * @param {Object} source The object to copy symbols from.
 * @param {Object} [object={}] The object to copy symbols to.
 * @returns {Object} Returns `object`.
 */
function copySymbolsIn(source, object) {
  return copyObject(source, getSymbolsIn(source), object);
}

module.exports = copySymbolsIn;


/***/ }),

/***/ 4429:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var root = __webpack_require__(5639);

/** Used to detect overreaching core-js shims. */
var coreJsData = root['__core-js_shared__'];

module.exports = coreJsData;


/***/ }),

/***/ 5063:
/***/ ((module) => {

/**
 * Creates a base function for methods like `_.forIn` and `_.forOwn`.
 *
 * @private
 * @param {boolean} [fromRight] Specify iterating from right to left.
 * @returns {Function} Returns the new base function.
 */
function createBaseFor(fromRight) {
  return function(object, iteratee, keysFunc) {
    var index = -1,
        iterable = Object(object),
        props = keysFunc(object),
        length = props.length;

    while (length--) {
      var key = props[fromRight ? length : ++index];
      if (iteratee(iterable[key], key, iterable) === false) {
        break;
      }
    }
    return object;
  };
}

module.exports = createBaseFor;


/***/ }),

/***/ 8882:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var castSlice = __webpack_require__(180),
    hasUnicode = __webpack_require__(2689),
    stringToArray = __webpack_require__(3140),
    toString = __webpack_require__(9833);

/**
 * Creates a function like `_.lowerFirst`.
 *
 * @private
 * @param {string} methodName The name of the `String` case method to use.
 * @returns {Function} Returns the new case function.
 */
function createCaseFirst(methodName) {
  return function(string) {
    string = toString(string);

    var strSymbols = hasUnicode(string)
      ? stringToArray(string)
      : undefined;

    var chr = strSymbols
      ? strSymbols[0]
      : string.charAt(0);

    var trailing = strSymbols
      ? castSlice(strSymbols, 1).join('')
      : string.slice(1);

    return chr[methodName]() + trailing;
  };
}

module.exports = createCaseFirst;


/***/ }),

/***/ 7740:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIteratee = __webpack_require__(7206),
    isArrayLike = __webpack_require__(8612),
    keys = __webpack_require__(3674);

/**
 * Creates a `_.find` or `_.findLast` function.
 *
 * @private
 * @param {Function} findIndexFunc The function to find the collection index.
 * @returns {Function} Returns the new find function.
 */
function createFind(findIndexFunc) {
  return function(collection, predicate, fromIndex) {
    var iterable = Object(collection);
    if (!isArrayLike(collection)) {
      var iteratee = baseIteratee(predicate, 3);
      collection = keys(collection);
      predicate = function(key) { return iteratee(iterable[key], key, iterable); };
    }
    var index = findIndexFunc(collection, predicate, fromIndex);
    return index > -1 ? iterable[iteratee ? collection[index] : index] : undefined;
  };
}

module.exports = createFind;


/***/ }),

/***/ 8777:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852);

var defineProperty = (function() {
  try {
    var func = getNative(Object, 'defineProperty');
    func({}, '', {});
    return func;
  } catch (e) {}
}());

module.exports = defineProperty;


/***/ }),

/***/ 7114:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var SetCache = __webpack_require__(8668),
    arraySome = __webpack_require__(2908),
    cacheHas = __webpack_require__(4757);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1,
    COMPARE_UNORDERED_FLAG = 2;

/**
 * A specialized version of `baseIsEqualDeep` for arrays with support for
 * partial deep comparisons.
 *
 * @private
 * @param {Array} array The array to compare.
 * @param {Array} other The other array to compare.
 * @param {number} bitmask The bitmask flags. See `baseIsEqual` for more details.
 * @param {Function} customizer The function to customize comparisons.
 * @param {Function} equalFunc The function to determine equivalents of values.
 * @param {Object} stack Tracks traversed `array` and `other` objects.
 * @returns {boolean} Returns `true` if the arrays are equivalent, else `false`.
 */
function equalArrays(array, other, bitmask, customizer, equalFunc, stack) {
  var isPartial = bitmask & COMPARE_PARTIAL_FLAG,
      arrLength = array.length,
      othLength = other.length;

  if (arrLength != othLength && !(isPartial && othLength > arrLength)) {
    return false;
  }
  // Check that cyclic values are equal.
  var arrStacked = stack.get(array);
  var othStacked = stack.get(other);
  if (arrStacked && othStacked) {
    return arrStacked == other && othStacked == array;
  }
  var index = -1,
      result = true,
      seen = (bitmask & COMPARE_UNORDERED_FLAG) ? new SetCache : undefined;

  stack.set(array, other);
  stack.set(other, array);

  // Ignore non-index properties.
  while (++index < arrLength) {
    var arrValue = array[index],
        othValue = other[index];

    if (customizer) {
      var compared = isPartial
        ? customizer(othValue, arrValue, index, other, array, stack)
        : customizer(arrValue, othValue, index, array, other, stack);
    }
    if (compared !== undefined) {
      if (compared) {
        continue;
      }
      result = false;
      break;
    }
    // Recursively compare arrays (susceptible to call stack limits).
    if (seen) {
      if (!arraySome(other, function(othValue, othIndex) {
            if (!cacheHas(seen, othIndex) &&
                (arrValue === othValue || equalFunc(arrValue, othValue, bitmask, customizer, stack))) {
              return seen.push(othIndex);
            }
          })) {
        result = false;
        break;
      }
    } else if (!(
          arrValue === othValue ||
            equalFunc(arrValue, othValue, bitmask, customizer, stack)
        )) {
      result = false;
      break;
    }
  }
  stack['delete'](array);
  stack['delete'](other);
  return result;
}

module.exports = equalArrays;


/***/ }),

/***/ 8351:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705),
    Uint8Array = __webpack_require__(1149),
    eq = __webpack_require__(7813),
    equalArrays = __webpack_require__(7114),
    mapToArray = __webpack_require__(8776),
    setToArray = __webpack_require__(1814);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1,
    COMPARE_UNORDERED_FLAG = 2;

/** `Object#toString` result references. */
var boolTag = '[object Boolean]',
    dateTag = '[object Date]',
    errorTag = '[object Error]',
    mapTag = '[object Map]',
    numberTag = '[object Number]',
    regexpTag = '[object RegExp]',
    setTag = '[object Set]',
    stringTag = '[object String]',
    symbolTag = '[object Symbol]';

var arrayBufferTag = '[object ArrayBuffer]',
    dataViewTag = '[object DataView]';

/** Used to convert symbols to primitives and strings. */
var symbolProto = Symbol ? Symbol.prototype : undefined,
    symbolValueOf = symbolProto ? symbolProto.valueOf : undefined;

/**
 * A specialized version of `baseIsEqualDeep` for comparing objects of
 * the same `toStringTag`.
 *
 * **Note:** This function only supports comparing values with tags of
 * `Boolean`, `Date`, `Error`, `Number`, `RegExp`, or `String`.
 *
 * @private
 * @param {Object} object The object to compare.
 * @param {Object} other The other object to compare.
 * @param {string} tag The `toStringTag` of the objects to compare.
 * @param {number} bitmask The bitmask flags. See `baseIsEqual` for more details.
 * @param {Function} customizer The function to customize comparisons.
 * @param {Function} equalFunc The function to determine equivalents of values.
 * @param {Object} stack Tracks traversed `object` and `other` objects.
 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
 */
function equalByTag(object, other, tag, bitmask, customizer, equalFunc, stack) {
  switch (tag) {
    case dataViewTag:
      if ((object.byteLength != other.byteLength) ||
          (object.byteOffset != other.byteOffset)) {
        return false;
      }
      object = object.buffer;
      other = other.buffer;

    case arrayBufferTag:
      if ((object.byteLength != other.byteLength) ||
          !equalFunc(new Uint8Array(object), new Uint8Array(other))) {
        return false;
      }
      return true;

    case boolTag:
    case dateTag:
    case numberTag:
      // Coerce booleans to `1` or `0` and dates to milliseconds.
      // Invalid dates are coerced to `NaN`.
      return eq(+object, +other);

    case errorTag:
      return object.name == other.name && object.message == other.message;

    case regexpTag:
    case stringTag:
      // Coerce regexes to strings and treat strings, primitives and objects,
      // as equal. See http://www.ecma-international.org/ecma-262/7.0/#sec-regexp.prototype.tostring
      // for more details.
      return object == (other + '');

    case mapTag:
      var convert = mapToArray;

    case setTag:
      var isPartial = bitmask & COMPARE_PARTIAL_FLAG;
      convert || (convert = setToArray);

      if (object.size != other.size && !isPartial) {
        return false;
      }
      // Assume cyclic values are equal.
      var stacked = stack.get(object);
      if (stacked) {
        return stacked == other;
      }
      bitmask |= COMPARE_UNORDERED_FLAG;

      // Recursively compare objects (susceptible to call stack limits).
      stack.set(object, other);
      var result = equalArrays(convert(object), convert(other), bitmask, customizer, equalFunc, stack);
      stack['delete'](object);
      return result;

    case symbolTag:
      if (symbolValueOf) {
        return symbolValueOf.call(object) == symbolValueOf.call(other);
      }
  }
  return false;
}

module.exports = equalByTag;


/***/ }),

/***/ 6096:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getAllKeys = __webpack_require__(8234);

/** Used to compose bitmasks for value comparisons. */
var COMPARE_PARTIAL_FLAG = 1;

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * A specialized version of `baseIsEqualDeep` for objects with support for
 * partial deep comparisons.
 *
 * @private
 * @param {Object} object The object to compare.
 * @param {Object} other The other object to compare.
 * @param {number} bitmask The bitmask flags. See `baseIsEqual` for more details.
 * @param {Function} customizer The function to customize comparisons.
 * @param {Function} equalFunc The function to determine equivalents of values.
 * @param {Object} stack Tracks traversed `object` and `other` objects.
 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
 */
function equalObjects(object, other, bitmask, customizer, equalFunc, stack) {
  var isPartial = bitmask & COMPARE_PARTIAL_FLAG,
      objProps = getAllKeys(object),
      objLength = objProps.length,
      othProps = getAllKeys(other),
      othLength = othProps.length;

  if (objLength != othLength && !isPartial) {
    return false;
  }
  var index = objLength;
  while (index--) {
    var key = objProps[index];
    if (!(isPartial ? key in other : hasOwnProperty.call(other, key))) {
      return false;
    }
  }
  // Check that cyclic values are equal.
  var objStacked = stack.get(object);
  var othStacked = stack.get(other);
  if (objStacked && othStacked) {
    return objStacked == other && othStacked == object;
  }
  var result = true;
  stack.set(object, other);
  stack.set(other, object);

  var skipCtor = isPartial;
  while (++index < objLength) {
    key = objProps[index];
    var objValue = object[key],
        othValue = other[key];

    if (customizer) {
      var compared = isPartial
        ? customizer(othValue, objValue, key, other, object, stack)
        : customizer(objValue, othValue, key, object, other, stack);
    }
    // Recursively compare objects (susceptible to call stack limits).
    if (!(compared === undefined
          ? (objValue === othValue || equalFunc(objValue, othValue, bitmask, customizer, stack))
          : compared
        )) {
      result = false;
      break;
    }
    skipCtor || (skipCtor = key == 'constructor');
  }
  if (result && !skipCtor) {
    var objCtor = object.constructor,
        othCtor = other.constructor;

    // Non `Object` object instances with different constructors are not equal.
    if (objCtor != othCtor &&
        ('constructor' in object && 'constructor' in other) &&
        !(typeof objCtor == 'function' && objCtor instanceof objCtor &&
          typeof othCtor == 'function' && othCtor instanceof othCtor)) {
      result = false;
    }
  }
  stack['delete'](object);
  stack['delete'](other);
  return result;
}

module.exports = equalObjects;


/***/ }),

/***/ 1957:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

/** Detect free variable `global` from Node.js. */
var freeGlobal = typeof __webpack_require__.g == 'object' && __webpack_require__.g && __webpack_require__.g.Object === Object && __webpack_require__.g;

module.exports = freeGlobal;


/***/ }),

/***/ 8234:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetAllKeys = __webpack_require__(8866),
    getSymbols = __webpack_require__(9551),
    keys = __webpack_require__(3674);

/**
 * Creates an array of own enumerable property names and symbols of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names and symbols.
 */
function getAllKeys(object) {
  return baseGetAllKeys(object, keys, getSymbols);
}

module.exports = getAllKeys;


/***/ }),

/***/ 6904:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetAllKeys = __webpack_require__(8866),
    getSymbolsIn = __webpack_require__(1442),
    keysIn = __webpack_require__(1704);

/**
 * Creates an array of own and inherited enumerable property names and
 * symbols of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names and symbols.
 */
function getAllKeysIn(object) {
  return baseGetAllKeys(object, keysIn, getSymbolsIn);
}

module.exports = getAllKeysIn;


/***/ }),

/***/ 5050:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isKeyable = __webpack_require__(7019);

/**
 * Gets the data for `map`.
 *
 * @private
 * @param {Object} map The map to query.
 * @param {string} key The reference key.
 * @returns {*} Returns the map data.
 */
function getMapData(map, key) {
  var data = map.__data__;
  return isKeyable(key)
    ? data[typeof key == 'string' ? 'string' : 'hash']
    : data.map;
}

module.exports = getMapData;


/***/ }),

/***/ 1499:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isStrictComparable = __webpack_require__(9162),
    keys = __webpack_require__(3674);

/**
 * Gets the property names, values, and compare flags of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the match data of `object`.
 */
function getMatchData(object) {
  var result = keys(object),
      length = result.length;

  while (length--) {
    var key = result[length],
        value = object[key];

    result[length] = [key, value, isStrictComparable(value)];
  }
  return result;
}

module.exports = getMatchData;


/***/ }),

/***/ 852:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsNative = __webpack_require__(8458),
    getValue = __webpack_require__(7801);

/**
 * Gets the native function at `key` of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {string} key The key of the method to get.
 * @returns {*} Returns the function if it's native, else `undefined`.
 */
function getNative(object, key) {
  var value = getValue(object, key);
  return baseIsNative(value) ? value : undefined;
}

module.exports = getNative;


/***/ }),

/***/ 5924:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var overArg = __webpack_require__(5569);

/** Built-in value references. */
var getPrototype = overArg(Object.getPrototypeOf, Object);

module.exports = getPrototype;


/***/ }),

/***/ 9607:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Used to resolve the
 * [`toStringTag`](http://ecma-international.org/ecma-262/7.0/#sec-object.prototype.tostring)
 * of values.
 */
var nativeObjectToString = objectProto.toString;

/** Built-in value references. */
var symToStringTag = Symbol ? Symbol.toStringTag : undefined;

/**
 * A specialized version of `baseGetTag` which ignores `Symbol.toStringTag` values.
 *
 * @private
 * @param {*} value The value to query.
 * @returns {string} Returns the raw `toStringTag`.
 */
function getRawTag(value) {
  var isOwn = hasOwnProperty.call(value, symToStringTag),
      tag = value[symToStringTag];

  try {
    value[symToStringTag] = undefined;
    var unmasked = true;
  } catch (e) {}

  var result = nativeObjectToString.call(value);
  if (unmasked) {
    if (isOwn) {
      value[symToStringTag] = tag;
    } else {
      delete value[symToStringTag];
    }
  }
  return result;
}

module.exports = getRawTag;


/***/ }),

/***/ 9551:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayFilter = __webpack_require__(4963),
    stubArray = __webpack_require__(479);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Built-in value references. */
var propertyIsEnumerable = objectProto.propertyIsEnumerable;

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeGetSymbols = Object.getOwnPropertySymbols;

/**
 * Creates an array of the own enumerable symbols of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of symbols.
 */
var getSymbols = !nativeGetSymbols ? stubArray : function(object) {
  if (object == null) {
    return [];
  }
  object = Object(object);
  return arrayFilter(nativeGetSymbols(object), function(symbol) {
    return propertyIsEnumerable.call(object, symbol);
  });
};

module.exports = getSymbols;


/***/ }),

/***/ 1442:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayPush = __webpack_require__(2488),
    getPrototype = __webpack_require__(5924),
    getSymbols = __webpack_require__(9551),
    stubArray = __webpack_require__(479);

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeGetSymbols = Object.getOwnPropertySymbols;

/**
 * Creates an array of the own and inherited enumerable symbols of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of symbols.
 */
var getSymbolsIn = !nativeGetSymbols ? stubArray : function(object) {
  var result = [];
  while (object) {
    arrayPush(result, getSymbols(object));
    object = getPrototype(object);
  }
  return result;
};

module.exports = getSymbolsIn;


/***/ }),

/***/ 4160:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var DataView = __webpack_require__(8552),
    Map = __webpack_require__(7071),
    Promise = __webpack_require__(3818),
    Set = __webpack_require__(8525),
    WeakMap = __webpack_require__(577),
    baseGetTag = __webpack_require__(4239),
    toSource = __webpack_require__(346);

/** `Object#toString` result references. */
var mapTag = '[object Map]',
    objectTag = '[object Object]',
    promiseTag = '[object Promise]',
    setTag = '[object Set]',
    weakMapTag = '[object WeakMap]';

var dataViewTag = '[object DataView]';

/** Used to detect maps, sets, and weakmaps. */
var dataViewCtorString = toSource(DataView),
    mapCtorString = toSource(Map),
    promiseCtorString = toSource(Promise),
    setCtorString = toSource(Set),
    weakMapCtorString = toSource(WeakMap);

/**
 * Gets the `toStringTag` of `value`.
 *
 * @private
 * @param {*} value The value to query.
 * @returns {string} Returns the `toStringTag`.
 */
var getTag = baseGetTag;

// Fallback for data views, maps, sets, and weak maps in IE 11 and promises in Node.js < 6.
if ((DataView && getTag(new DataView(new ArrayBuffer(1))) != dataViewTag) ||
    (Map && getTag(new Map) != mapTag) ||
    (Promise && getTag(Promise.resolve()) != promiseTag) ||
    (Set && getTag(new Set) != setTag) ||
    (WeakMap && getTag(new WeakMap) != weakMapTag)) {
  getTag = function(value) {
    var result = baseGetTag(value),
        Ctor = result == objectTag ? value.constructor : undefined,
        ctorString = Ctor ? toSource(Ctor) : '';

    if (ctorString) {
      switch (ctorString) {
        case dataViewCtorString: return dataViewTag;
        case mapCtorString: return mapTag;
        case promiseCtorString: return promiseTag;
        case setCtorString: return setTag;
        case weakMapCtorString: return weakMapTag;
      }
    }
    return result;
  };
}

module.exports = getTag;


/***/ }),

/***/ 7801:
/***/ ((module) => {

/**
 * Gets the value at `key` of `object`.
 *
 * @private
 * @param {Object} [object] The object to query.
 * @param {string} key The key of the property to get.
 * @returns {*} Returns the property value.
 */
function getValue(object, key) {
  return object == null ? undefined : object[key];
}

module.exports = getValue;


/***/ }),

/***/ 222:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var castPath = __webpack_require__(1811),
    isArguments = __webpack_require__(5694),
    isArray = __webpack_require__(1469),
    isIndex = __webpack_require__(5776),
    isLength = __webpack_require__(1780),
    toKey = __webpack_require__(327);

/**
 * Checks if `path` exists on `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {Array|string} path The path to check.
 * @param {Function} hasFunc The function to check properties.
 * @returns {boolean} Returns `true` if `path` exists, else `false`.
 */
function hasPath(object, path, hasFunc) {
  path = castPath(path, object);

  var index = -1,
      length = path.length,
      result = false;

  while (++index < length) {
    var key = toKey(path[index]);
    if (!(result = object != null && hasFunc(object, key))) {
      break;
    }
    object = object[key];
  }
  if (result || ++index != length) {
    return result;
  }
  length = object == null ? 0 : object.length;
  return !!length && isLength(length) && isIndex(key, length) &&
    (isArray(object) || isArguments(object));
}

module.exports = hasPath;


/***/ }),

/***/ 2689:
/***/ ((module) => {

/** Used to compose unicode character classes. */
var rsAstralRange = '\\ud800-\\udfff',
    rsComboMarksRange = '\\u0300-\\u036f',
    reComboHalfMarksRange = '\\ufe20-\\ufe2f',
    rsComboSymbolsRange = '\\u20d0-\\u20ff',
    rsComboRange = rsComboMarksRange + reComboHalfMarksRange + rsComboSymbolsRange,
    rsVarRange = '\\ufe0e\\ufe0f';

/** Used to compose unicode capture groups. */
var rsZWJ = '\\u200d';

/** Used to detect strings with [zero-width joiners or code points from the astral planes](http://eev.ee/blog/2015/09/12/dark-corners-of-unicode/). */
var reHasUnicode = RegExp('[' + rsZWJ + rsAstralRange  + rsComboRange + rsVarRange + ']');

/**
 * Checks if `string` contains Unicode symbols.
 *
 * @private
 * @param {string} string The string to inspect.
 * @returns {boolean} Returns `true` if a symbol is found, else `false`.
 */
function hasUnicode(string) {
  return reHasUnicode.test(string);
}

module.exports = hasUnicode;


/***/ }),

/***/ 1789:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var nativeCreate = __webpack_require__(4536);

/**
 * Removes all key-value entries from the hash.
 *
 * @private
 * @name clear
 * @memberOf Hash
 */
function hashClear() {
  this.__data__ = nativeCreate ? nativeCreate(null) : {};
  this.size = 0;
}

module.exports = hashClear;


/***/ }),

/***/ 401:
/***/ ((module) => {

/**
 * Removes `key` and its value from the hash.
 *
 * @private
 * @name delete
 * @memberOf Hash
 * @param {Object} hash The hash to modify.
 * @param {string} key The key of the value to remove.
 * @returns {boolean} Returns `true` if the entry was removed, else `false`.
 */
function hashDelete(key) {
  var result = this.has(key) && delete this.__data__[key];
  this.size -= result ? 1 : 0;
  return result;
}

module.exports = hashDelete;


/***/ }),

/***/ 7667:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var nativeCreate = __webpack_require__(4536);

/** Used to stand-in for `undefined` hash values. */
var HASH_UNDEFINED = '__lodash_hash_undefined__';

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Gets the hash value for `key`.
 *
 * @private
 * @name get
 * @memberOf Hash
 * @param {string} key The key of the value to get.
 * @returns {*} Returns the entry value.
 */
function hashGet(key) {
  var data = this.__data__;
  if (nativeCreate) {
    var result = data[key];
    return result === HASH_UNDEFINED ? undefined : result;
  }
  return hasOwnProperty.call(data, key) ? data[key] : undefined;
}

module.exports = hashGet;


/***/ }),

/***/ 1327:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var nativeCreate = __webpack_require__(4536);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Checks if a hash value for `key` exists.
 *
 * @private
 * @name has
 * @memberOf Hash
 * @param {string} key The key of the entry to check.
 * @returns {boolean} Returns `true` if an entry for `key` exists, else `false`.
 */
function hashHas(key) {
  var data = this.__data__;
  return nativeCreate ? (data[key] !== undefined) : hasOwnProperty.call(data, key);
}

module.exports = hashHas;


/***/ }),

/***/ 1866:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var nativeCreate = __webpack_require__(4536);

/** Used to stand-in for `undefined` hash values. */
var HASH_UNDEFINED = '__lodash_hash_undefined__';

/**
 * Sets the hash `key` to `value`.
 *
 * @private
 * @name set
 * @memberOf Hash
 * @param {string} key The key of the value to set.
 * @param {*} value The value to set.
 * @returns {Object} Returns the hash instance.
 */
function hashSet(key, value) {
  var data = this.__data__;
  this.size += this.has(key) ? 0 : 1;
  data[key] = (nativeCreate && value === undefined) ? HASH_UNDEFINED : value;
  return this;
}

module.exports = hashSet;


/***/ }),

/***/ 3824:
/***/ ((module) => {

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Initializes an array clone.
 *
 * @private
 * @param {Array} array The array to clone.
 * @returns {Array} Returns the initialized clone.
 */
function initCloneArray(array) {
  var length = array.length,
      result = new array.constructor(length);

  // Add properties assigned by `RegExp#exec`.
  if (length && typeof array[0] == 'string' && hasOwnProperty.call(array, 'index')) {
    result.index = array.index;
    result.input = array.input;
  }
  return result;
}

module.exports = initCloneArray;


/***/ }),

/***/ 9148:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var cloneArrayBuffer = __webpack_require__(4318),
    cloneDataView = __webpack_require__(7157),
    cloneRegExp = __webpack_require__(3147),
    cloneSymbol = __webpack_require__(419),
    cloneTypedArray = __webpack_require__(7133);

/** `Object#toString` result references. */
var boolTag = '[object Boolean]',
    dateTag = '[object Date]',
    mapTag = '[object Map]',
    numberTag = '[object Number]',
    regexpTag = '[object RegExp]',
    setTag = '[object Set]',
    stringTag = '[object String]',
    symbolTag = '[object Symbol]';

var arrayBufferTag = '[object ArrayBuffer]',
    dataViewTag = '[object DataView]',
    float32Tag = '[object Float32Array]',
    float64Tag = '[object Float64Array]',
    int8Tag = '[object Int8Array]',
    int16Tag = '[object Int16Array]',
    int32Tag = '[object Int32Array]',
    uint8Tag = '[object Uint8Array]',
    uint8ClampedTag = '[object Uint8ClampedArray]',
    uint16Tag = '[object Uint16Array]',
    uint32Tag = '[object Uint32Array]';

/**
 * Initializes an object clone based on its `toStringTag`.
 *
 * **Note:** This function only supports cloning values with tags of
 * `Boolean`, `Date`, `Error`, `Map`, `Number`, `RegExp`, `Set`, or `String`.
 *
 * @private
 * @param {Object} object The object to clone.
 * @param {string} tag The `toStringTag` of the object to clone.
 * @param {boolean} [isDeep] Specify a deep clone.
 * @returns {Object} Returns the initialized clone.
 */
function initCloneByTag(object, tag, isDeep) {
  var Ctor = object.constructor;
  switch (tag) {
    case arrayBufferTag:
      return cloneArrayBuffer(object);

    case boolTag:
    case dateTag:
      return new Ctor(+object);

    case dataViewTag:
      return cloneDataView(object, isDeep);

    case float32Tag: case float64Tag:
    case int8Tag: case int16Tag: case int32Tag:
    case uint8Tag: case uint8ClampedTag: case uint16Tag: case uint32Tag:
      return cloneTypedArray(object, isDeep);

    case mapTag:
      return new Ctor;

    case numberTag:
    case stringTag:
      return new Ctor(object);

    case regexpTag:
      return cloneRegExp(object);

    case setTag:
      return new Ctor;

    case symbolTag:
      return cloneSymbol(object);
  }
}

module.exports = initCloneByTag;


/***/ }),

/***/ 8517:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseCreate = __webpack_require__(3118),
    getPrototype = __webpack_require__(5924),
    isPrototype = __webpack_require__(5726);

/**
 * Initializes an object clone.
 *
 * @private
 * @param {Object} object The object to clone.
 * @returns {Object} Returns the initialized clone.
 */
function initCloneObject(object) {
  return (typeof object.constructor == 'function' && !isPrototype(object))
    ? baseCreate(getPrototype(object))
    : {};
}

module.exports = initCloneObject;


/***/ }),

/***/ 7285:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Symbol = __webpack_require__(2705),
    isArguments = __webpack_require__(5694),
    isArray = __webpack_require__(1469);

/** Built-in value references. */
var spreadableSymbol = Symbol ? Symbol.isConcatSpreadable : undefined;

/**
 * Checks if `value` is a flattenable `arguments` object or array.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is flattenable, else `false`.
 */
function isFlattenable(value) {
  return isArray(value) || isArguments(value) ||
    !!(spreadableSymbol && value && value[spreadableSymbol]);
}

module.exports = isFlattenable;


/***/ }),

/***/ 5776:
/***/ ((module) => {

/** Used as references for various `Number` constants. */
var MAX_SAFE_INTEGER = 9007199254740991;

/** Used to detect unsigned integer values. */
var reIsUint = /^(?:0|[1-9]\d*)$/;

/**
 * Checks if `value` is a valid array-like index.
 *
 * @private
 * @param {*} value The value to check.
 * @param {number} [length=MAX_SAFE_INTEGER] The upper bounds of a valid index.
 * @returns {boolean} Returns `true` if `value` is a valid index, else `false`.
 */
function isIndex(value, length) {
  var type = typeof value;
  length = length == null ? MAX_SAFE_INTEGER : length;

  return !!length &&
    (type == 'number' ||
      (type != 'symbol' && reIsUint.test(value))) &&
        (value > -1 && value % 1 == 0 && value < length);
}

module.exports = isIndex;


/***/ }),

/***/ 5403:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isArray = __webpack_require__(1469),
    isSymbol = __webpack_require__(3448);

/** Used to match property names within property paths. */
var reIsDeepProp = /\.|\[(?:[^[\]]*|(["'])(?:(?!\1)[^\\]|\\.)*?\1)\]/,
    reIsPlainProp = /^\w*$/;

/**
 * Checks if `value` is a property name and not a property path.
 *
 * @private
 * @param {*} value The value to check.
 * @param {Object} [object] The object to query keys on.
 * @returns {boolean} Returns `true` if `value` is a property name, else `false`.
 */
function isKey(value, object) {
  if (isArray(value)) {
    return false;
  }
  var type = typeof value;
  if (type == 'number' || type == 'symbol' || type == 'boolean' ||
      value == null || isSymbol(value)) {
    return true;
  }
  return reIsPlainProp.test(value) || !reIsDeepProp.test(value) ||
    (object != null && value in Object(object));
}

module.exports = isKey;


/***/ }),

/***/ 7019:
/***/ ((module) => {

/**
 * Checks if `value` is suitable for use as unique object key.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is suitable, else `false`.
 */
function isKeyable(value) {
  var type = typeof value;
  return (type == 'string' || type == 'number' || type == 'symbol' || type == 'boolean')
    ? (value !== '__proto__')
    : (value === null);
}

module.exports = isKeyable;


/***/ }),

/***/ 5346:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var coreJsData = __webpack_require__(4429);

/** Used to detect methods masquerading as native. */
var maskSrcKey = (function() {
  var uid = /[^.]+$/.exec(coreJsData && coreJsData.keys && coreJsData.keys.IE_PROTO || '');
  return uid ? ('Symbol(src)_1.' + uid) : '';
}());

/**
 * Checks if `func` has its source masked.
 *
 * @private
 * @param {Function} func The function to check.
 * @returns {boolean} Returns `true` if `func` is masked, else `false`.
 */
function isMasked(func) {
  return !!maskSrcKey && (maskSrcKey in func);
}

module.exports = isMasked;


/***/ }),

/***/ 5726:
/***/ ((module) => {

/** Used for built-in method references. */
var objectProto = Object.prototype;

/**
 * Checks if `value` is likely a prototype object.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a prototype, else `false`.
 */
function isPrototype(value) {
  var Ctor = value && value.constructor,
      proto = (typeof Ctor == 'function' && Ctor.prototype) || objectProto;

  return value === proto;
}

module.exports = isPrototype;


/***/ }),

/***/ 9162:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isObject = __webpack_require__(3218);

/**
 * Checks if `value` is suitable for strict equality comparisons, i.e. `===`.
 *
 * @private
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` if suitable for strict
 *  equality comparisons, else `false`.
 */
function isStrictComparable(value) {
  return value === value && !isObject(value);
}

module.exports = isStrictComparable;


/***/ }),

/***/ 7040:
/***/ ((module) => {

/**
 * Removes all key-value entries from the list cache.
 *
 * @private
 * @name clear
 * @memberOf ListCache
 */
function listCacheClear() {
  this.__data__ = [];
  this.size = 0;
}

module.exports = listCacheClear;


/***/ }),

/***/ 4125:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assocIndexOf = __webpack_require__(8470);

/** Used for built-in method references. */
var arrayProto = Array.prototype;

/** Built-in value references. */
var splice = arrayProto.splice;

/**
 * Removes `key` and its value from the list cache.
 *
 * @private
 * @name delete
 * @memberOf ListCache
 * @param {string} key The key of the value to remove.
 * @returns {boolean} Returns `true` if the entry was removed, else `false`.
 */
function listCacheDelete(key) {
  var data = this.__data__,
      index = assocIndexOf(data, key);

  if (index < 0) {
    return false;
  }
  var lastIndex = data.length - 1;
  if (index == lastIndex) {
    data.pop();
  } else {
    splice.call(data, index, 1);
  }
  --this.size;
  return true;
}

module.exports = listCacheDelete;


/***/ }),

/***/ 2117:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assocIndexOf = __webpack_require__(8470);

/**
 * Gets the list cache value for `key`.
 *
 * @private
 * @name get
 * @memberOf ListCache
 * @param {string} key The key of the value to get.
 * @returns {*} Returns the entry value.
 */
function listCacheGet(key) {
  var data = this.__data__,
      index = assocIndexOf(data, key);

  return index < 0 ? undefined : data[index][1];
}

module.exports = listCacheGet;


/***/ }),

/***/ 7518:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assocIndexOf = __webpack_require__(8470);

/**
 * Checks if a list cache value for `key` exists.
 *
 * @private
 * @name has
 * @memberOf ListCache
 * @param {string} key The key of the entry to check.
 * @returns {boolean} Returns `true` if an entry for `key` exists, else `false`.
 */
function listCacheHas(key) {
  return assocIndexOf(this.__data__, key) > -1;
}

module.exports = listCacheHas;


/***/ }),

/***/ 4705:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var assocIndexOf = __webpack_require__(8470);

/**
 * Sets the list cache `key` to `value`.
 *
 * @private
 * @name set
 * @memberOf ListCache
 * @param {string} key The key of the value to set.
 * @param {*} value The value to set.
 * @returns {Object} Returns the list cache instance.
 */
function listCacheSet(key, value) {
  var data = this.__data__,
      index = assocIndexOf(data, key);

  if (index < 0) {
    ++this.size;
    data.push([key, value]);
  } else {
    data[index][1] = value;
  }
  return this;
}

module.exports = listCacheSet;


/***/ }),

/***/ 4785:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var Hash = __webpack_require__(1989),
    ListCache = __webpack_require__(8407),
    Map = __webpack_require__(7071);

/**
 * Removes all key-value entries from the map.
 *
 * @private
 * @name clear
 * @memberOf MapCache
 */
function mapCacheClear() {
  this.size = 0;
  this.__data__ = {
    'hash': new Hash,
    'map': new (Map || ListCache),
    'string': new Hash
  };
}

module.exports = mapCacheClear;


/***/ }),

/***/ 1285:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getMapData = __webpack_require__(5050);

/**
 * Removes `key` and its value from the map.
 *
 * @private
 * @name delete
 * @memberOf MapCache
 * @param {string} key The key of the value to remove.
 * @returns {boolean} Returns `true` if the entry was removed, else `false`.
 */
function mapCacheDelete(key) {
  var result = getMapData(this, key)['delete'](key);
  this.size -= result ? 1 : 0;
  return result;
}

module.exports = mapCacheDelete;


/***/ }),

/***/ 6000:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getMapData = __webpack_require__(5050);

/**
 * Gets the map value for `key`.
 *
 * @private
 * @name get
 * @memberOf MapCache
 * @param {string} key The key of the value to get.
 * @returns {*} Returns the entry value.
 */
function mapCacheGet(key) {
  return getMapData(this, key).get(key);
}

module.exports = mapCacheGet;


/***/ }),

/***/ 9916:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getMapData = __webpack_require__(5050);

/**
 * Checks if a map value for `key` exists.
 *
 * @private
 * @name has
 * @memberOf MapCache
 * @param {string} key The key of the entry to check.
 * @returns {boolean} Returns `true` if an entry for `key` exists, else `false`.
 */
function mapCacheHas(key) {
  return getMapData(this, key).has(key);
}

module.exports = mapCacheHas;


/***/ }),

/***/ 5265:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getMapData = __webpack_require__(5050);

/**
 * Sets the map `key` to `value`.
 *
 * @private
 * @name set
 * @memberOf MapCache
 * @param {string} key The key of the value to set.
 * @param {*} value The value to set.
 * @returns {Object} Returns the map cache instance.
 */
function mapCacheSet(key, value) {
  var data = getMapData(this, key),
      size = data.size;

  data.set(key, value);
  this.size += data.size == size ? 0 : 1;
  return this;
}

module.exports = mapCacheSet;


/***/ }),

/***/ 8776:
/***/ ((module) => {

/**
 * Converts `map` to its key-value pairs.
 *
 * @private
 * @param {Object} map The map to convert.
 * @returns {Array} Returns the key-value pairs.
 */
function mapToArray(map) {
  var index = -1,
      result = Array(map.size);

  map.forEach(function(value, key) {
    result[++index] = [key, value];
  });
  return result;
}

module.exports = mapToArray;


/***/ }),

/***/ 2634:
/***/ ((module) => {

/**
 * A specialized version of `matchesProperty` for source values suitable
 * for strict equality comparisons, i.e. `===`.
 *
 * @private
 * @param {string} key The key of the property to get.
 * @param {*} srcValue The value to match.
 * @returns {Function} Returns the new spec function.
 */
function matchesStrictComparable(key, srcValue) {
  return function(object) {
    if (object == null) {
      return false;
    }
    return object[key] === srcValue &&
      (srcValue !== undefined || (key in Object(object)));
  };
}

module.exports = matchesStrictComparable;


/***/ }),

/***/ 4523:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var memoize = __webpack_require__(8306);

/** Used as the maximum memoize cache size. */
var MAX_MEMOIZE_SIZE = 500;

/**
 * A specialized version of `_.memoize` which clears the memoized function's
 * cache when it exceeds `MAX_MEMOIZE_SIZE`.
 *
 * @private
 * @param {Function} func The function to have its output memoized.
 * @returns {Function} Returns the new memoized function.
 */
function memoizeCapped(func) {
  var result = memoize(func, function(key) {
    if (cache.size === MAX_MEMOIZE_SIZE) {
      cache.clear();
    }
    return key;
  });

  var cache = result.cache;
  return result;
}

module.exports = memoizeCapped;


/***/ }),

/***/ 4536:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var getNative = __webpack_require__(852);

/* Built-in method references that are verified to be native. */
var nativeCreate = getNative(Object, 'create');

module.exports = nativeCreate;


/***/ }),

/***/ 6916:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var overArg = __webpack_require__(5569);

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeKeys = overArg(Object.keys, Object);

module.exports = nativeKeys;


/***/ }),

/***/ 3498:
/***/ ((module) => {

/**
 * This function is like
 * [`Object.keys`](http://ecma-international.org/ecma-262/7.0/#sec-object.keys)
 * except that it includes inherited enumerable properties.
 *
 * @private
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 */
function nativeKeysIn(object) {
  var result = [];
  if (object != null) {
    for (var key in Object(object)) {
      result.push(key);
    }
  }
  return result;
}

module.exports = nativeKeysIn;


/***/ }),

/***/ 1167:
/***/ ((module, exports, __webpack_require__) => {

/* module decorator */ module = __webpack_require__.nmd(module);
var freeGlobal = __webpack_require__(1957);

/** Detect free variable `exports`. */
var freeExports =  true && exports && !exports.nodeType && exports;

/** Detect free variable `module`. */
var freeModule = freeExports && "object" == 'object' && module && !module.nodeType && module;

/** Detect the popular CommonJS extension `module.exports`. */
var moduleExports = freeModule && freeModule.exports === freeExports;

/** Detect free variable `process` from Node.js. */
var freeProcess = moduleExports && freeGlobal.process;

/** Used to access faster Node.js helpers. */
var nodeUtil = (function() {
  try {
    // Use `util.types` for Node.js 10+.
    var types = freeModule && freeModule.require && freeModule.require('util').types;

    if (types) {
      return types;
    }

    // Legacy `process.binding('util')` for Node.js < 10.
    return freeProcess && freeProcess.binding && freeProcess.binding('util');
  } catch (e) {}
}());

module.exports = nodeUtil;


/***/ }),

/***/ 2333:
/***/ ((module) => {

/** Used for built-in method references. */
var objectProto = Object.prototype;

/**
 * Used to resolve the
 * [`toStringTag`](http://ecma-international.org/ecma-262/7.0/#sec-object.prototype.tostring)
 * of values.
 */
var nativeObjectToString = objectProto.toString;

/**
 * Converts `value` to a string using `Object.prototype.toString`.
 *
 * @private
 * @param {*} value The value to convert.
 * @returns {string} Returns the converted string.
 */
function objectToString(value) {
  return nativeObjectToString.call(value);
}

module.exports = objectToString;


/***/ }),

/***/ 5569:
/***/ ((module) => {

/**
 * Creates a unary function that invokes `func` with its argument transformed.
 *
 * @private
 * @param {Function} func The function to wrap.
 * @param {Function} transform The argument transform.
 * @returns {Function} Returns the new function.
 */
function overArg(func, transform) {
  return function(arg) {
    return func(transform(arg));
  };
}

module.exports = overArg;


/***/ }),

/***/ 292:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGet = __webpack_require__(7786),
    baseSlice = __webpack_require__(4259);

/**
 * Gets the parent value at `path` of `object`.
 *
 * @private
 * @param {Object} object The object to query.
 * @param {Array} path The path to get the parent value of.
 * @returns {*} Returns the parent value.
 */
function parent(object, path) {
  return path.length < 2 ? object : baseGet(object, baseSlice(path, 0, -1));
}

module.exports = parent;


/***/ }),

/***/ 5639:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var freeGlobal = __webpack_require__(1957);

/** Detect free variable `self`. */
var freeSelf = typeof self == 'object' && self && self.Object === Object && self;

/** Used as a reference to the global object. */
var root = freeGlobal || freeSelf || Function('return this')();

module.exports = root;


/***/ }),

/***/ 619:
/***/ ((module) => {

/** Used to stand-in for `undefined` hash values. */
var HASH_UNDEFINED = '__lodash_hash_undefined__';

/**
 * Adds `value` to the array cache.
 *
 * @private
 * @name add
 * @memberOf SetCache
 * @alias push
 * @param {*} value The value to cache.
 * @returns {Object} Returns the cache instance.
 */
function setCacheAdd(value) {
  this.__data__.set(value, HASH_UNDEFINED);
  return this;
}

module.exports = setCacheAdd;


/***/ }),

/***/ 2385:
/***/ ((module) => {

/**
 * Checks if `value` is in the array cache.
 *
 * @private
 * @name has
 * @memberOf SetCache
 * @param {*} value The value to search for.
 * @returns {number} Returns `true` if `value` is found, else `false`.
 */
function setCacheHas(value) {
  return this.__data__.has(value);
}

module.exports = setCacheHas;


/***/ }),

/***/ 1814:
/***/ ((module) => {

/**
 * Converts `set` to an array of its values.
 *
 * @private
 * @param {Object} set The set to convert.
 * @returns {Array} Returns the values.
 */
function setToArray(set) {
  var index = -1,
      result = Array(set.size);

  set.forEach(function(value) {
    result[++index] = value;
  });
  return result;
}

module.exports = setToArray;


/***/ }),

/***/ 7465:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var ListCache = __webpack_require__(8407);

/**
 * Removes all key-value entries from the stack.
 *
 * @private
 * @name clear
 * @memberOf Stack
 */
function stackClear() {
  this.__data__ = new ListCache;
  this.size = 0;
}

module.exports = stackClear;


/***/ }),

/***/ 3779:
/***/ ((module) => {

/**
 * Removes `key` and its value from the stack.
 *
 * @private
 * @name delete
 * @memberOf Stack
 * @param {string} key The key of the value to remove.
 * @returns {boolean} Returns `true` if the entry was removed, else `false`.
 */
function stackDelete(key) {
  var data = this.__data__,
      result = data['delete'](key);

  this.size = data.size;
  return result;
}

module.exports = stackDelete;


/***/ }),

/***/ 7599:
/***/ ((module) => {

/**
 * Gets the stack value for `key`.
 *
 * @private
 * @name get
 * @memberOf Stack
 * @param {string} key The key of the value to get.
 * @returns {*} Returns the entry value.
 */
function stackGet(key) {
  return this.__data__.get(key);
}

module.exports = stackGet;


/***/ }),

/***/ 4758:
/***/ ((module) => {

/**
 * Checks if a stack value for `key` exists.
 *
 * @private
 * @name has
 * @memberOf Stack
 * @param {string} key The key of the entry to check.
 * @returns {boolean} Returns `true` if an entry for `key` exists, else `false`.
 */
function stackHas(key) {
  return this.__data__.has(key);
}

module.exports = stackHas;


/***/ }),

/***/ 4309:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var ListCache = __webpack_require__(8407),
    Map = __webpack_require__(7071),
    MapCache = __webpack_require__(3369);

/** Used as the size to enable large array optimizations. */
var LARGE_ARRAY_SIZE = 200;

/**
 * Sets the stack `key` to `value`.
 *
 * @private
 * @name set
 * @memberOf Stack
 * @param {string} key The key of the value to set.
 * @param {*} value The value to set.
 * @returns {Object} Returns the stack cache instance.
 */
function stackSet(key, value) {
  var data = this.__data__;
  if (data instanceof ListCache) {
    var pairs = data.__data__;
    if (!Map || (pairs.length < LARGE_ARRAY_SIZE - 1)) {
      pairs.push([key, value]);
      this.size = ++data.size;
      return this;
    }
    data = this.__data__ = new MapCache(pairs);
  }
  data.set(key, value);
  this.size = data.size;
  return this;
}

module.exports = stackSet;


/***/ }),

/***/ 3140:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var asciiToArray = __webpack_require__(4286),
    hasUnicode = __webpack_require__(2689),
    unicodeToArray = __webpack_require__(676);

/**
 * Converts `string` to an array.
 *
 * @private
 * @param {string} string The string to convert.
 * @returns {Array} Returns the converted array.
 */
function stringToArray(string) {
  return hasUnicode(string)
    ? unicodeToArray(string)
    : asciiToArray(string);
}

module.exports = stringToArray;


/***/ }),

/***/ 5514:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var memoizeCapped = __webpack_require__(4523);

/** Used to match property names within property paths. */
var rePropName = /[^.[\]]+|\[(?:(-?\d+(?:\.\d+)?)|(["'])((?:(?!\2)[^\\]|\\.)*?)\2)\]|(?=(?:\.|\[\])(?:\.|\[\]|$))/g;

/** Used to match backslashes in property paths. */
var reEscapeChar = /\\(\\)?/g;

/**
 * Converts `string` to a property path array.
 *
 * @private
 * @param {string} string The string to convert.
 * @returns {Array} Returns the property path array.
 */
var stringToPath = memoizeCapped(function(string) {
  var result = [];
  if (string.charCodeAt(0) === 46 /* . */) {
    result.push('');
  }
  string.replace(rePropName, function(match, number, quote, subString) {
    result.push(quote ? subString.replace(reEscapeChar, '$1') : (number || match));
  });
  return result;
});

module.exports = stringToPath;


/***/ }),

/***/ 327:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isSymbol = __webpack_require__(3448);

/** Used as references for various `Number` constants. */
var INFINITY = 1 / 0;

/**
 * Converts `value` to a string key if it's not a string or symbol.
 *
 * @private
 * @param {*} value The value to inspect.
 * @returns {string|symbol} Returns the key.
 */
function toKey(value) {
  if (typeof value == 'string' || isSymbol(value)) {
    return value;
  }
  var result = (value + '');
  return (result == '0' && (1 / value) == -INFINITY) ? '-0' : result;
}

module.exports = toKey;


/***/ }),

/***/ 346:
/***/ ((module) => {

/** Used for built-in method references. */
var funcProto = Function.prototype;

/** Used to resolve the decompiled source of functions. */
var funcToString = funcProto.toString;

/**
 * Converts `func` to its source code.
 *
 * @private
 * @param {Function} func The function to convert.
 * @returns {string} Returns the source code.
 */
function toSource(func) {
  if (func != null) {
    try {
      return funcToString.call(func);
    } catch (e) {}
    try {
      return (func + '');
    } catch (e) {}
  }
  return '';
}

module.exports = toSource;


/***/ }),

/***/ 676:
/***/ ((module) => {

/** Used to compose unicode character classes. */
var rsAstralRange = '\\ud800-\\udfff',
    rsComboMarksRange = '\\u0300-\\u036f',
    reComboHalfMarksRange = '\\ufe20-\\ufe2f',
    rsComboSymbolsRange = '\\u20d0-\\u20ff',
    rsComboRange = rsComboMarksRange + reComboHalfMarksRange + rsComboSymbolsRange,
    rsVarRange = '\\ufe0e\\ufe0f';

/** Used to compose unicode capture groups. */
var rsAstral = '[' + rsAstralRange + ']',
    rsCombo = '[' + rsComboRange + ']',
    rsFitz = '\\ud83c[\\udffb-\\udfff]',
    rsModifier = '(?:' + rsCombo + '|' + rsFitz + ')',
    rsNonAstral = '[^' + rsAstralRange + ']',
    rsRegional = '(?:\\ud83c[\\udde6-\\uddff]){2}',
    rsSurrPair = '[\\ud800-\\udbff][\\udc00-\\udfff]',
    rsZWJ = '\\u200d';

/** Used to compose unicode regexes. */
var reOptMod = rsModifier + '?',
    rsOptVar = '[' + rsVarRange + ']?',
    rsOptJoin = '(?:' + rsZWJ + '(?:' + [rsNonAstral, rsRegional, rsSurrPair].join('|') + ')' + rsOptVar + reOptMod + ')*',
    rsSeq = rsOptVar + reOptMod + rsOptJoin,
    rsSymbol = '(?:' + [rsNonAstral + rsCombo + '?', rsCombo, rsRegional, rsSurrPair, rsAstral].join('|') + ')';

/** Used to match [string symbols](https://mathiasbynens.be/notes/javascript-unicode). */
var reUnicode = RegExp(rsFitz + '(?=' + rsFitz + ')|' + rsSymbol + rsSeq, 'g');

/**
 * Converts a Unicode `string` to an array.
 *
 * @private
 * @param {string} string The string to convert.
 * @returns {Array} Returns the converted array.
 */
function unicodeToArray(string) {
  return string.match(reUnicode) || [];
}

module.exports = unicodeToArray;


/***/ }),

/***/ 8403:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var toString = __webpack_require__(9833),
    upperFirst = __webpack_require__(1700);

/**
 * Converts the first character of `string` to upper case and the remaining
 * to lower case.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category String
 * @param {string} [string=''] The string to capitalize.
 * @returns {string} Returns the capitalized string.
 * @example
 *
 * _.capitalize('FRED');
 * // => 'Fred'
 */
function capitalize(string) {
  return upperFirst(toString(string).toLowerCase());
}

module.exports = capitalize;


/***/ }),

/***/ 361:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseClone = __webpack_require__(5990);

/** Used to compose bitmasks for cloning. */
var CLONE_DEEP_FLAG = 1,
    CLONE_SYMBOLS_FLAG = 4;

/**
 * This method is like `_.clone` except that it recursively clones `value`.
 *
 * @static
 * @memberOf _
 * @since 1.0.0
 * @category Lang
 * @param {*} value The value to recursively clone.
 * @returns {*} Returns the deep cloned value.
 * @see _.clone
 * @example
 *
 * var objects = [{ 'a': 1 }, { 'b': 2 }];
 *
 * var deep = _.cloneDeep(objects);
 * console.log(deep[0] === objects[0]);
 * // => false
 */
function cloneDeep(value) {
  return baseClone(value, CLONE_DEEP_FLAG | CLONE_SYMBOLS_FLAG);
}

module.exports = cloneDeep;


/***/ }),

/***/ 7813:
/***/ ((module) => {

/**
 * Performs a
 * [`SameValueZero`](http://ecma-international.org/ecma-262/7.0/#sec-samevaluezero)
 * comparison between two values to determine if they are equivalent.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to compare.
 * @param {*} other The other value to compare.
 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
 * @example
 *
 * var object = { 'a': 1 };
 * var other = { 'a': 1 };
 *
 * _.eq(object, object);
 * // => true
 *
 * _.eq(object, other);
 * // => false
 *
 * _.eq('a', 'a');
 * // => true
 *
 * _.eq('a', Object('a'));
 * // => false
 *
 * _.eq(NaN, NaN);
 * // => true
 */
function eq(value, other) {
  return value === other || (value !== value && other !== other);
}

module.exports = eq;


/***/ }),

/***/ 3311:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var createFind = __webpack_require__(7740),
    findIndex = __webpack_require__(998);

/**
 * Iterates over elements of `collection`, returning the first element
 * `predicate` returns truthy for. The predicate is invoked with three
 * arguments: (value, index|key, collection).
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Collection
 * @param {Array|Object} collection The collection to inspect.
 * @param {Function} [predicate=_.identity] The function invoked per iteration.
 * @param {number} [fromIndex=0] The index to search from.
 * @returns {*} Returns the matched element, else `undefined`.
 * @example
 *
 * var users = [
 *   { 'user': 'barney',  'age': 36, 'active': true },
 *   { 'user': 'fred',    'age': 40, 'active': false },
 *   { 'user': 'pebbles', 'age': 1,  'active': true }
 * ];
 *
 * _.find(users, function(o) { return o.age < 40; });
 * // => object for 'barney'
 *
 * // The `_.matches` iteratee shorthand.
 * _.find(users, { 'age': 1, 'active': true });
 * // => object for 'pebbles'
 *
 * // The `_.matchesProperty` iteratee shorthand.
 * _.find(users, ['active', false]);
 * // => object for 'fred'
 *
 * // The `_.property` iteratee shorthand.
 * _.find(users, 'active');
 * // => object for 'barney'
 */
var find = createFind(findIndex);

module.exports = find;


/***/ }),

/***/ 998:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseFindIndex = __webpack_require__(1848),
    baseIteratee = __webpack_require__(7206),
    toInteger = __webpack_require__(554);

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeMax = Math.max;

/**
 * This method is like `_.find` except that it returns the index of the first
 * element `predicate` returns truthy for instead of the element itself.
 *
 * @static
 * @memberOf _
 * @since 1.1.0
 * @category Array
 * @param {Array} array The array to inspect.
 * @param {Function} [predicate=_.identity] The function invoked per iteration.
 * @param {number} [fromIndex=0] The index to search from.
 * @returns {number} Returns the index of the found element, else `-1`.
 * @example
 *
 * var users = [
 *   { 'user': 'barney',  'active': false },
 *   { 'user': 'fred',    'active': false },
 *   { 'user': 'pebbles', 'active': true }
 * ];
 *
 * _.findIndex(users, function(o) { return o.user == 'barney'; });
 * // => 0
 *
 * // The `_.matches` iteratee shorthand.
 * _.findIndex(users, { 'user': 'fred', 'active': false });
 * // => 1
 *
 * // The `_.matchesProperty` iteratee shorthand.
 * _.findIndex(users, ['active', false]);
 * // => 0
 *
 * // The `_.property` iteratee shorthand.
 * _.findIndex(users, 'active');
 * // => 2
 */
function findIndex(array, predicate, fromIndex) {
  var length = array == null ? 0 : array.length;
  if (!length) {
    return -1;
  }
  var index = fromIndex == null ? 0 : toInteger(fromIndex);
  if (index < 0) {
    index = nativeMax(length + index, 0);
  }
  return baseFindIndex(array, baseIteratee(predicate, 3), index);
}

module.exports = findIndex;


/***/ }),

/***/ 988:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var createFind = __webpack_require__(7740),
    findLastIndex = __webpack_require__(7436);

/**
 * This method is like `_.find` except that it iterates over elements of
 * `collection` from right to left.
 *
 * @static
 * @memberOf _
 * @since 2.0.0
 * @category Collection
 * @param {Array|Object} collection The collection to inspect.
 * @param {Function} [predicate=_.identity] The function invoked per iteration.
 * @param {number} [fromIndex=collection.length-1] The index to search from.
 * @returns {*} Returns the matched element, else `undefined`.
 * @example
 *
 * _.findLast([1, 2, 3, 4], function(n) {
 *   return n % 2 == 1;
 * });
 * // => 3
 */
var findLast = createFind(findLastIndex);

module.exports = findLast;


/***/ }),

/***/ 7436:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseFindIndex = __webpack_require__(1848),
    baseIteratee = __webpack_require__(7206),
    toInteger = __webpack_require__(554);

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeMax = Math.max,
    nativeMin = Math.min;

/**
 * This method is like `_.findIndex` except that it iterates over elements
 * of `collection` from right to left.
 *
 * @static
 * @memberOf _
 * @since 2.0.0
 * @category Array
 * @param {Array} array The array to inspect.
 * @param {Function} [predicate=_.identity] The function invoked per iteration.
 * @param {number} [fromIndex=array.length-1] The index to search from.
 * @returns {number} Returns the index of the found element, else `-1`.
 * @example
 *
 * var users = [
 *   { 'user': 'barney',  'active': true },
 *   { 'user': 'fred',    'active': false },
 *   { 'user': 'pebbles', 'active': false }
 * ];
 *
 * _.findLastIndex(users, function(o) { return o.user == 'pebbles'; });
 * // => 2
 *
 * // The `_.matches` iteratee shorthand.
 * _.findLastIndex(users, { 'user': 'barney', 'active': true });
 * // => 0
 *
 * // The `_.matchesProperty` iteratee shorthand.
 * _.findLastIndex(users, ['active', false]);
 * // => 2
 *
 * // The `_.property` iteratee shorthand.
 * _.findLastIndex(users, 'active');
 * // => 0
 */
function findLastIndex(array, predicate, fromIndex) {
  var length = array == null ? 0 : array.length;
  if (!length) {
    return -1;
  }
  var index = length - 1;
  if (fromIndex !== undefined) {
    index = toInteger(fromIndex);
    index = fromIndex < 0
      ? nativeMax(length + index, 0)
      : nativeMin(index, length - 1);
  }
  return baseFindIndex(array, baseIteratee(predicate, 3), index, true);
}

module.exports = findLastIndex;


/***/ }),

/***/ 5564:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseFlatten = __webpack_require__(1078);

/**
 * Flattens `array` a single level deep.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Array
 * @param {Array} array The array to flatten.
 * @returns {Array} Returns the new flattened array.
 * @example
 *
 * _.flatten([1, [2, [3, [4]], 5]]);
 * // => [1, 2, [3, [4]], 5]
 */
function flatten(array) {
  var length = array == null ? 0 : array.length;
  return length ? baseFlatten(array, 1) : [];
}

module.exports = flatten;


/***/ }),

/***/ 7361:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGet = __webpack_require__(7786);

/**
 * Gets the value at `path` of `object`. If the resolved value is
 * `undefined`, the `defaultValue` is returned in its place.
 *
 * @static
 * @memberOf _
 * @since 3.7.0
 * @category Object
 * @param {Object} object The object to query.
 * @param {Array|string} path The path of the property to get.
 * @param {*} [defaultValue] The value returned for `undefined` resolved values.
 * @returns {*} Returns the resolved value.
 * @example
 *
 * var object = { 'a': [{ 'b': { 'c': 3 } }] };
 *
 * _.get(object, 'a[0].b.c');
 * // => 3
 *
 * _.get(object, ['a', '0', 'b', 'c']);
 * // => 3
 *
 * _.get(object, 'a.b.c', 'default');
 * // => 'default'
 */
function get(object, path, defaultValue) {
  var result = object == null ? undefined : baseGet(object, path);
  return result === undefined ? defaultValue : result;
}

module.exports = get;


/***/ }),

/***/ 8721:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseHas = __webpack_require__(8565),
    hasPath = __webpack_require__(222);

/**
 * Checks if `path` is a direct property of `object`.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category Object
 * @param {Object} object The object to query.
 * @param {Array|string} path The path to check.
 * @returns {boolean} Returns `true` if `path` exists, else `false`.
 * @example
 *
 * var object = { 'a': { 'b': 2 } };
 * var other = _.create({ 'a': _.create({ 'b': 2 }) });
 *
 * _.has(object, 'a');
 * // => true
 *
 * _.has(object, 'a.b');
 * // => true
 *
 * _.has(object, ['a', 'b']);
 * // => true
 *
 * _.has(other, 'a');
 * // => false
 */
function has(object, path) {
  return object != null && hasPath(object, path, baseHas);
}

module.exports = has;


/***/ }),

/***/ 9095:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseHasIn = __webpack_require__(13),
    hasPath = __webpack_require__(222);

/**
 * Checks if `path` is a direct or inherited property of `object`.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Object
 * @param {Object} object The object to query.
 * @param {Array|string} path The path to check.
 * @returns {boolean} Returns `true` if `path` exists, else `false`.
 * @example
 *
 * var object = _.create({ 'a': _.create({ 'b': 2 }) });
 *
 * _.hasIn(object, 'a');
 * // => true
 *
 * _.hasIn(object, 'a.b');
 * // => true
 *
 * _.hasIn(object, ['a', 'b']);
 * // => true
 *
 * _.hasIn(object, 'b');
 * // => false
 */
function hasIn(object, path) {
  return object != null && hasPath(object, path, baseHasIn);
}

module.exports = hasIn;


/***/ }),

/***/ 6557:
/***/ ((module) => {

/**
 * This method returns the first argument it receives.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category Util
 * @param {*} value Any value.
 * @returns {*} Returns `value`.
 * @example
 *
 * var object = { 'a': 1 };
 *
 * console.log(_.identity(object) === object);
 * // => true
 */
function identity(value) {
  return value;
}

module.exports = identity;


/***/ }),

/***/ 5694:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsArguments = __webpack_require__(9454),
    isObjectLike = __webpack_require__(7005);

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/** Built-in value references. */
var propertyIsEnumerable = objectProto.propertyIsEnumerable;

/**
 * Checks if `value` is likely an `arguments` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an `arguments` object,
 *  else `false`.
 * @example
 *
 * _.isArguments(function() { return arguments; }());
 * // => true
 *
 * _.isArguments([1, 2, 3]);
 * // => false
 */
var isArguments = baseIsArguments(function() { return arguments; }()) ? baseIsArguments : function(value) {
  return isObjectLike(value) && hasOwnProperty.call(value, 'callee') &&
    !propertyIsEnumerable.call(value, 'callee');
};

module.exports = isArguments;


/***/ }),

/***/ 1469:
/***/ ((module) => {

/**
 * Checks if `value` is classified as an `Array` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an array, else `false`.
 * @example
 *
 * _.isArray([1, 2, 3]);
 * // => true
 *
 * _.isArray(document.body.children);
 * // => false
 *
 * _.isArray('abc');
 * // => false
 *
 * _.isArray(_.noop);
 * // => false
 */
var isArray = Array.isArray;

module.exports = isArray;


/***/ }),

/***/ 8612:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isFunction = __webpack_require__(3560),
    isLength = __webpack_require__(1780);

/**
 * Checks if `value` is array-like. A value is considered array-like if it's
 * not a function and has a `value.length` that's an integer greater than or
 * equal to `0` and less than or equal to `Number.MAX_SAFE_INTEGER`.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is array-like, else `false`.
 * @example
 *
 * _.isArrayLike([1, 2, 3]);
 * // => true
 *
 * _.isArrayLike(document.body.children);
 * // => true
 *
 * _.isArrayLike('abc');
 * // => true
 *
 * _.isArrayLike(_.noop);
 * // => false
 */
function isArrayLike(value) {
  return value != null && isLength(value.length) && !isFunction(value);
}

module.exports = isArrayLike;


/***/ }),

/***/ 4144:
/***/ ((module, exports, __webpack_require__) => {

/* module decorator */ module = __webpack_require__.nmd(module);
var root = __webpack_require__(5639),
    stubFalse = __webpack_require__(5062);

/** Detect free variable `exports`. */
var freeExports =  true && exports && !exports.nodeType && exports;

/** Detect free variable `module`. */
var freeModule = freeExports && "object" == 'object' && module && !module.nodeType && module;

/** Detect the popular CommonJS extension `module.exports`. */
var moduleExports = freeModule && freeModule.exports === freeExports;

/** Built-in value references. */
var Buffer = moduleExports ? root.Buffer : undefined;

/* Built-in method references for those with the same name as other `lodash` methods. */
var nativeIsBuffer = Buffer ? Buffer.isBuffer : undefined;

/**
 * Checks if `value` is a buffer.
 *
 * @static
 * @memberOf _
 * @since 4.3.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a buffer, else `false`.
 * @example
 *
 * _.isBuffer(new Buffer(2));
 * // => true
 *
 * _.isBuffer(new Uint8Array(2));
 * // => false
 */
var isBuffer = nativeIsBuffer || stubFalse;

module.exports = isBuffer;


/***/ }),

/***/ 1609:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseKeys = __webpack_require__(280),
    getTag = __webpack_require__(4160),
    isArguments = __webpack_require__(5694),
    isArray = __webpack_require__(1469),
    isArrayLike = __webpack_require__(8612),
    isBuffer = __webpack_require__(4144),
    isPrototype = __webpack_require__(5726),
    isTypedArray = __webpack_require__(6719);

/** `Object#toString` result references. */
var mapTag = '[object Map]',
    setTag = '[object Set]';

/** Used for built-in method references. */
var objectProto = Object.prototype;

/** Used to check objects for own properties. */
var hasOwnProperty = objectProto.hasOwnProperty;

/**
 * Checks if `value` is an empty object, collection, map, or set.
 *
 * Objects are considered empty if they have no own enumerable string keyed
 * properties.
 *
 * Array-like values such as `arguments` objects, arrays, buffers, strings, or
 * jQuery-like collections are considered empty if they have a `length` of `0`.
 * Similarly, maps and sets are considered empty if they have a `size` of `0`.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is empty, else `false`.
 * @example
 *
 * _.isEmpty(null);
 * // => true
 *
 * _.isEmpty(true);
 * // => true
 *
 * _.isEmpty(1);
 * // => true
 *
 * _.isEmpty([1, 2, 3]);
 * // => false
 *
 * _.isEmpty({ 'a': 1 });
 * // => false
 */
function isEmpty(value) {
  if (value == null) {
    return true;
  }
  if (isArrayLike(value) &&
      (isArray(value) || typeof value == 'string' || typeof value.splice == 'function' ||
        isBuffer(value) || isTypedArray(value) || isArguments(value))) {
    return !value.length;
  }
  var tag = getTag(value);
  if (tag == mapTag || tag == setTag) {
    return !value.size;
  }
  if (isPrototype(value)) {
    return !baseKeys(value).length;
  }
  for (var key in value) {
    if (hasOwnProperty.call(value, key)) {
      return false;
    }
  }
  return true;
}

module.exports = isEmpty;


/***/ }),

/***/ 8446:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsEqual = __webpack_require__(939);

/**
 * Performs a deep comparison between two values to determine if they are
 * equivalent.
 *
 * **Note:** This method supports comparing arrays, array buffers, booleans,
 * date objects, error objects, maps, numbers, `Object` objects, regexes,
 * sets, strings, symbols, and typed arrays. `Object` objects are compared
 * by their own, not inherited, enumerable properties. Functions and DOM
 * nodes are compared by strict equality, i.e. `===`.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to compare.
 * @param {*} other The other value to compare.
 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
 * @example
 *
 * var object = { 'a': 1 };
 * var other = { 'a': 1 };
 *
 * _.isEqual(object, other);
 * // => true
 *
 * object === other;
 * // => false
 */
function isEqual(value, other) {
  return baseIsEqual(value, other);
}

module.exports = isEqual;


/***/ }),

/***/ 3560:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetTag = __webpack_require__(4239),
    isObject = __webpack_require__(3218);

/** `Object#toString` result references. */
var asyncTag = '[object AsyncFunction]',
    funcTag = '[object Function]',
    genTag = '[object GeneratorFunction]',
    proxyTag = '[object Proxy]';

/**
 * Checks if `value` is classified as a `Function` object.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a function, else `false`.
 * @example
 *
 * _.isFunction(_);
 * // => true
 *
 * _.isFunction(/abc/);
 * // => false
 */
function isFunction(value) {
  if (!isObject(value)) {
    return false;
  }
  // The use of `Object#toString` avoids issues with the `typeof` operator
  // in Safari 9 which returns 'object' for typed arrays and other constructors.
  var tag = baseGetTag(value);
  return tag == funcTag || tag == genTag || tag == asyncTag || tag == proxyTag;
}

module.exports = isFunction;


/***/ }),

/***/ 1780:
/***/ ((module) => {

/** Used as references for various `Number` constants. */
var MAX_SAFE_INTEGER = 9007199254740991;

/**
 * Checks if `value` is a valid array-like length.
 *
 * **Note:** This method is loosely based on
 * [`ToLength`](http://ecma-international.org/ecma-262/7.0/#sec-tolength).
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a valid length, else `false`.
 * @example
 *
 * _.isLength(3);
 * // => true
 *
 * _.isLength(Number.MIN_VALUE);
 * // => false
 *
 * _.isLength(Infinity);
 * // => false
 *
 * _.isLength('3');
 * // => false
 */
function isLength(value) {
  return typeof value == 'number' &&
    value > -1 && value % 1 == 0 && value <= MAX_SAFE_INTEGER;
}

module.exports = isLength;


/***/ }),

/***/ 6688:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsMap = __webpack_require__(5588),
    baseUnary = __webpack_require__(1717),
    nodeUtil = __webpack_require__(1167);

/* Node.js helper references. */
var nodeIsMap = nodeUtil && nodeUtil.isMap;

/**
 * Checks if `value` is classified as a `Map` object.
 *
 * @static
 * @memberOf _
 * @since 4.3.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a map, else `false`.
 * @example
 *
 * _.isMap(new Map);
 * // => true
 *
 * _.isMap(new WeakMap);
 * // => false
 */
var isMap = nodeIsMap ? baseUnary(nodeIsMap) : baseIsMap;

module.exports = isMap;


/***/ }),

/***/ 3218:
/***/ ((module) => {

/**
 * Checks if `value` is the
 * [language type](http://www.ecma-international.org/ecma-262/7.0/#sec-ecmascript-language-types)
 * of `Object`. (e.g. arrays, functions, objects, regexes, `new Number(0)`, and `new String('')`)
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is an object, else `false`.
 * @example
 *
 * _.isObject({});
 * // => true
 *
 * _.isObject([1, 2, 3]);
 * // => true
 *
 * _.isObject(_.noop);
 * // => true
 *
 * _.isObject(null);
 * // => false
 */
function isObject(value) {
  var type = typeof value;
  return value != null && (type == 'object' || type == 'function');
}

module.exports = isObject;


/***/ }),

/***/ 7005:
/***/ ((module) => {

/**
 * Checks if `value` is object-like. A value is object-like if it's not `null`
 * and has a `typeof` result of "object".
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is object-like, else `false`.
 * @example
 *
 * _.isObjectLike({});
 * // => true
 *
 * _.isObjectLike([1, 2, 3]);
 * // => true
 *
 * _.isObjectLike(_.noop);
 * // => false
 *
 * _.isObjectLike(null);
 * // => false
 */
function isObjectLike(value) {
  return value != null && typeof value == 'object';
}

module.exports = isObjectLike;


/***/ }),

/***/ 2928:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsSet = __webpack_require__(9221),
    baseUnary = __webpack_require__(1717),
    nodeUtil = __webpack_require__(1167);

/* Node.js helper references. */
var nodeIsSet = nodeUtil && nodeUtil.isSet;

/**
 * Checks if `value` is classified as a `Set` object.
 *
 * @static
 * @memberOf _
 * @since 4.3.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a set, else `false`.
 * @example
 *
 * _.isSet(new Set);
 * // => true
 *
 * _.isSet(new WeakSet);
 * // => false
 */
var isSet = nodeIsSet ? baseUnary(nodeIsSet) : baseIsSet;

module.exports = isSet;


/***/ }),

/***/ 3448:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseGetTag = __webpack_require__(4239),
    isObjectLike = __webpack_require__(7005);

/** `Object#toString` result references. */
var symbolTag = '[object Symbol]';

/**
 * Checks if `value` is classified as a `Symbol` primitive or object.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a symbol, else `false`.
 * @example
 *
 * _.isSymbol(Symbol.iterator);
 * // => true
 *
 * _.isSymbol('abc');
 * // => false
 */
function isSymbol(value) {
  return typeof value == 'symbol' ||
    (isObjectLike(value) && baseGetTag(value) == symbolTag);
}

module.exports = isSymbol;


/***/ }),

/***/ 6719:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIsTypedArray = __webpack_require__(8749),
    baseUnary = __webpack_require__(1717),
    nodeUtil = __webpack_require__(1167);

/* Node.js helper references. */
var nodeIsTypedArray = nodeUtil && nodeUtil.isTypedArray;

/**
 * Checks if `value` is classified as a typed array.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Lang
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is a typed array, else `false`.
 * @example
 *
 * _.isTypedArray(new Uint8Array);
 * // => true
 *
 * _.isTypedArray([]);
 * // => false
 */
var isTypedArray = nodeIsTypedArray ? baseUnary(nodeIsTypedArray) : baseIsTypedArray;

module.exports = isTypedArray;


/***/ }),

/***/ 3674:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayLikeKeys = __webpack_require__(4636),
    baseKeys = __webpack_require__(280),
    isArrayLike = __webpack_require__(8612);

/**
 * Creates an array of the own enumerable property names of `object`.
 *
 * **Note:** Non-object values are coerced to objects. See the
 * [ES spec](http://ecma-international.org/ecma-262/7.0/#sec-object.keys)
 * for more details.
 *
 * @static
 * @since 0.1.0
 * @memberOf _
 * @category Object
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 * @example
 *
 * function Foo() {
 *   this.a = 1;
 *   this.b = 2;
 * }
 *
 * Foo.prototype.c = 3;
 *
 * _.keys(new Foo);
 * // => ['a', 'b'] (iteration order is not guaranteed)
 *
 * _.keys('hi');
 * // => ['0', '1']
 */
function keys(object) {
  return isArrayLike(object) ? arrayLikeKeys(object) : baseKeys(object);
}

module.exports = keys;


/***/ }),

/***/ 1704:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var arrayLikeKeys = __webpack_require__(4636),
    baseKeysIn = __webpack_require__(313),
    isArrayLike = __webpack_require__(8612);

/**
 * Creates an array of the own and inherited enumerable property names of `object`.
 *
 * **Note:** Non-object values are coerced to objects.
 *
 * @static
 * @memberOf _
 * @since 3.0.0
 * @category Object
 * @param {Object} object The object to query.
 * @returns {Array} Returns the array of property names.
 * @example
 *
 * function Foo() {
 *   this.a = 1;
 *   this.b = 2;
 * }
 *
 * Foo.prototype.c = 3;
 *
 * _.keysIn(new Foo);
 * // => ['a', 'b', 'c'] (iteration order is not guaranteed)
 */
function keysIn(object) {
  return isArrayLike(object) ? arrayLikeKeys(object, true) : baseKeysIn(object);
}

module.exports = keysIn;


/***/ }),

/***/ 928:
/***/ ((module) => {

/**
 * Gets the last element of `array`.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Array
 * @param {Array} array The array to query.
 * @returns {*} Returns the last element of `array`.
 * @example
 *
 * _.last([1, 2, 3]);
 * // => 3
 */
function last(array) {
  var length = array == null ? 0 : array.length;
  return length ? array[length - 1] : undefined;
}

module.exports = last;


/***/ }),

/***/ 7523:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseAssignValue = __webpack_require__(9465),
    baseForOwn = __webpack_require__(7816),
    baseIteratee = __webpack_require__(7206);

/**
 * The opposite of `_.mapValues`; this method creates an object with the
 * same values as `object` and keys generated by running each own enumerable
 * string keyed property of `object` thru `iteratee`. The iteratee is invoked
 * with three arguments: (value, key, object).
 *
 * @static
 * @memberOf _
 * @since 3.8.0
 * @category Object
 * @param {Object} object The object to iterate over.
 * @param {Function} [iteratee=_.identity] The function invoked per iteration.
 * @returns {Object} Returns the new mapped object.
 * @see _.mapValues
 * @example
 *
 * _.mapKeys({ 'a': 1, 'b': 2 }, function(value, key) {
 *   return key + value;
 * });
 * // => { 'a1': 1, 'b2': 2 }
 */
function mapKeys(object, iteratee) {
  var result = {};
  iteratee = baseIteratee(iteratee, 3);

  baseForOwn(object, function(value, key, object) {
    baseAssignValue(result, iteratee(value, key, object), value);
  });
  return result;
}

module.exports = mapKeys;


/***/ }),

/***/ 8306:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var MapCache = __webpack_require__(3369);

/** Error message constants. */
var FUNC_ERROR_TEXT = 'Expected a function';

/**
 * Creates a function that memoizes the result of `func`. If `resolver` is
 * provided, it determines the cache key for storing the result based on the
 * arguments provided to the memoized function. By default, the first argument
 * provided to the memoized function is used as the map cache key. The `func`
 * is invoked with the `this` binding of the memoized function.
 *
 * **Note:** The cache is exposed as the `cache` property on the memoized
 * function. Its creation may be customized by replacing the `_.memoize.Cache`
 * constructor with one whose instances implement the
 * [`Map`](http://ecma-international.org/ecma-262/7.0/#sec-properties-of-the-map-prototype-object)
 * method interface of `clear`, `delete`, `get`, `has`, and `set`.
 *
 * @static
 * @memberOf _
 * @since 0.1.0
 * @category Function
 * @param {Function} func The function to have its output memoized.
 * @param {Function} [resolver] The function to resolve the cache key.
 * @returns {Function} Returns the new memoized function.
 * @example
 *
 * var object = { 'a': 1, 'b': 2 };
 * var other = { 'c': 3, 'd': 4 };
 *
 * var values = _.memoize(_.values);
 * values(object);
 * // => [1, 2]
 *
 * values(other);
 * // => [3, 4]
 *
 * object.a = 2;
 * values(object);
 * // => [1, 2]
 *
 * // Modify the result cache.
 * values.cache.set(object, ['a', 'b']);
 * values(object);
 * // => ['a', 'b']
 *
 * // Replace `_.memoize.Cache`.
 * _.memoize.Cache = WeakMap;
 */
function memoize(func, resolver) {
  if (typeof func != 'function' || (resolver != null && typeof resolver != 'function')) {
    throw new TypeError(FUNC_ERROR_TEXT);
  }
  var memoized = function() {
    var args = arguments,
        key = resolver ? resolver.apply(this, args) : args[0],
        cache = memoized.cache;

    if (cache.has(key)) {
      return cache.get(key);
    }
    var result = func.apply(this, args);
    memoized.cache = cache.set(key, result) || cache;
    return result;
  };
  memoized.cache = new (memoize.Cache || MapCache);
  return memoized;
}

// Expose `MapCache`.
memoize.Cache = MapCache;

module.exports = memoize;


/***/ }),

/***/ 8491:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseNth = __webpack_require__(873),
    toInteger = __webpack_require__(554);

/**
 * Gets the element at index `n` of `array`. If `n` is negative, the nth
 * element from the end is returned.
 *
 * @static
 * @memberOf _
 * @since 4.11.0
 * @category Array
 * @param {Array} array The array to query.
 * @param {number} [n=0] The index of the element to return.
 * @returns {*} Returns the nth element of `array`.
 * @example
 *
 * var array = ['a', 'b', 'c', 'd'];
 *
 * _.nth(array, 1);
 * // => 'b'
 *
 * _.nth(array, -2);
 * // => 'c';
 */
function nth(array, n) {
  return (array && array.length) ? baseNth(array, toInteger(n)) : undefined;
}

module.exports = nth;


/***/ }),

/***/ 9601:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseProperty = __webpack_require__(371),
    basePropertyDeep = __webpack_require__(9152),
    isKey = __webpack_require__(5403),
    toKey = __webpack_require__(327);

/**
 * Creates a function that returns the value at `path` of a given object.
 *
 * @static
 * @memberOf _
 * @since 2.4.0
 * @category Util
 * @param {Array|string} path The path of the property to get.
 * @returns {Function} Returns the new accessor function.
 * @example
 *
 * var objects = [
 *   { 'a': { 'b': 2 } },
 *   { 'a': { 'b': 1 } }
 * ];
 *
 * _.map(objects, _.property('a.b'));
 * // => [2, 1]
 *
 * _.map(_.sortBy(objects, _.property(['a', 'b'])), 'a.b');
 * // => [1, 2]
 */
function property(path) {
  return isKey(path) ? baseProperty(toKey(path)) : basePropertyDeep(path);
}

module.exports = property;


/***/ }),

/***/ 2729:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseIteratee = __webpack_require__(7206),
    basePullAt = __webpack_require__(5742);

/**
 * Removes all elements from `array` that `predicate` returns truthy for
 * and returns an array of the removed elements. The predicate is invoked
 * with three arguments: (value, index, array).
 *
 * **Note:** Unlike `_.filter`, this method mutates `array`. Use `_.pull`
 * to pull elements from an array by value.
 *
 * @static
 * @memberOf _
 * @since 2.0.0
 * @category Array
 * @param {Array} array The array to modify.
 * @param {Function} [predicate=_.identity] The function invoked per iteration.
 * @returns {Array} Returns the new array of removed elements.
 * @example
 *
 * var array = [1, 2, 3, 4];
 * var evens = _.remove(array, function(n) {
 *   return n % 2 == 0;
 * });
 *
 * console.log(array);
 * // => [1, 3]
 *
 * console.log(evens);
 * // => [2, 4]
 */
function remove(array, predicate) {
  var result = [];
  if (!(array && array.length)) {
    return result;
  }
  var index = -1,
      indexes = [],
      length = array.length;

  predicate = baseIteratee(predicate, 3);
  while (++index < length) {
    var value = array[index];
    if (predicate(value, index, array)) {
      result.push(value);
      indexes.push(index);
    }
  }
  basePullAt(array, indexes);
  return result;
}

module.exports = remove;


/***/ }),

/***/ 6968:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseSet = __webpack_require__(2491);

/**
 * Sets the value at `path` of `object`. If a portion of `path` doesn't exist,
 * it's created. Arrays are created for missing index properties while objects
 * are created for all other missing properties. Use `_.setWith` to customize
 * `path` creation.
 *
 * **Note:** This method mutates `object`.
 *
 * @static
 * @memberOf _
 * @since 3.7.0
 * @category Object
 * @param {Object} object The object to modify.
 * @param {Array|string} path The path of the property to set.
 * @param {*} value The value to set.
 * @returns {Object} Returns `object`.
 * @example
 *
 * var object = { 'a': [{ 'b': { 'c': 3 } }] };
 *
 * _.set(object, 'a[0].b.c', 4);
 * console.log(object.a[0].b.c);
 * // => 4
 *
 * _.set(object, ['x', '0', 'y', 'z'], 5);
 * console.log(object.x[0].y.z);
 * // => 5
 */
function set(object, path, value) {
  return object == null ? object : baseSet(object, path, value);
}

module.exports = set;


/***/ }),

/***/ 479:
/***/ ((module) => {

/**
 * This method returns a new empty array.
 *
 * @static
 * @memberOf _
 * @since 4.13.0
 * @category Util
 * @returns {Array} Returns the new empty array.
 * @example
 *
 * var arrays = _.times(2, _.stubArray);
 *
 * console.log(arrays);
 * // => [[], []]
 *
 * console.log(arrays[0] === arrays[1]);
 * // => false
 */
function stubArray() {
  return [];
}

module.exports = stubArray;


/***/ }),

/***/ 5062:
/***/ ((module) => {

/**
 * This method returns `false`.
 *
 * @static
 * @memberOf _
 * @since 4.13.0
 * @category Util
 * @returns {boolean} Returns `false`.
 * @example
 *
 * _.times(2, _.stubFalse);
 * // => [false, false]
 */
function stubFalse() {
  return false;
}

module.exports = stubFalse;


/***/ }),

/***/ 8601:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var toNumber = __webpack_require__(4841);

/** Used as references for various `Number` constants. */
var INFINITY = 1 / 0,
    MAX_INTEGER = 1.7976931348623157e+308;

/**
 * Converts `value` to a finite number.
 *
 * @static
 * @memberOf _
 * @since 4.12.0
 * @category Lang
 * @param {*} value The value to convert.
 * @returns {number} Returns the converted number.
 * @example
 *
 * _.toFinite(3.2);
 * // => 3.2
 *
 * _.toFinite(Number.MIN_VALUE);
 * // => 5e-324
 *
 * _.toFinite(Infinity);
 * // => 1.7976931348623157e+308
 *
 * _.toFinite('3.2');
 * // => 3.2
 */
function toFinite(value) {
  if (!value) {
    return value === 0 ? value : 0;
  }
  value = toNumber(value);
  if (value === INFINITY || value === -INFINITY) {
    var sign = (value < 0 ? -1 : 1);
    return sign * MAX_INTEGER;
  }
  return value === value ? value : 0;
}

module.exports = toFinite;


/***/ }),

/***/ 554:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var toFinite = __webpack_require__(8601);

/**
 * Converts `value` to an integer.
 *
 * **Note:** This method is loosely based on
 * [`ToInteger`](http://www.ecma-international.org/ecma-262/7.0/#sec-tointeger).
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to convert.
 * @returns {number} Returns the converted integer.
 * @example
 *
 * _.toInteger(3.2);
 * // => 3
 *
 * _.toInteger(Number.MIN_VALUE);
 * // => 0
 *
 * _.toInteger(Infinity);
 * // => 1.7976931348623157e+308
 *
 * _.toInteger('3.2');
 * // => 3
 */
function toInteger(value) {
  var result = toFinite(value),
      remainder = result % 1;

  return result === result ? (remainder ? result - remainder : result) : 0;
}

module.exports = toInteger;


/***/ }),

/***/ 4841:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var isObject = __webpack_require__(3218),
    isSymbol = __webpack_require__(3448);

/** Used as references for various `Number` constants. */
var NAN = 0 / 0;

/** Used to match leading and trailing whitespace. */
var reTrim = /^\s+|\s+$/g;

/** Used to detect bad signed hexadecimal string values. */
var reIsBadHex = /^[-+]0x[0-9a-f]+$/i;

/** Used to detect binary string values. */
var reIsBinary = /^0b[01]+$/i;

/** Used to detect octal string values. */
var reIsOctal = /^0o[0-7]+$/i;

/** Built-in method references without a dependency on `root`. */
var freeParseInt = parseInt;

/**
 * Converts `value` to a number.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to process.
 * @returns {number} Returns the number.
 * @example
 *
 * _.toNumber(3.2);
 * // => 3.2
 *
 * _.toNumber(Number.MIN_VALUE);
 * // => 5e-324
 *
 * _.toNumber(Infinity);
 * // => Infinity
 *
 * _.toNumber('3.2');
 * // => 3.2
 */
function toNumber(value) {
  if (typeof value == 'number') {
    return value;
  }
  if (isSymbol(value)) {
    return NAN;
  }
  if (isObject(value)) {
    var other = typeof value.valueOf == 'function' ? value.valueOf() : value;
    value = isObject(other) ? (other + '') : other;
  }
  if (typeof value != 'string') {
    return value === 0 ? value : +value;
  }
  value = value.replace(reTrim, '');
  var isBinary = reIsBinary.test(value);
  return (isBinary || reIsOctal.test(value))
    ? freeParseInt(value.slice(2), isBinary ? 2 : 8)
    : (reIsBadHex.test(value) ? NAN : +value);
}

module.exports = toNumber;


/***/ }),

/***/ 9833:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseToString = __webpack_require__(531);

/**
 * Converts `value` to a string. An empty string is returned for `null`
 * and `undefined` values. The sign of `-0` is preserved.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Lang
 * @param {*} value The value to convert.
 * @returns {string} Returns the converted string.
 * @example
 *
 * _.toString(null);
 * // => ''
 *
 * _.toString(-0);
 * // => '-0'
 *
 * _.toString([1, 2, 3]);
 * // => '1,2,3'
 */
function toString(value) {
  return value == null ? '' : baseToString(value);
}

module.exports = toString;


/***/ }),

/***/ 9233:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var baseUnset = __webpack_require__(7406);

/**
 * Removes the property at `path` of `object`.
 *
 * **Note:** This method mutates `object`.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category Object
 * @param {Object} object The object to modify.
 * @param {Array|string} path The path of the property to unset.
 * @returns {boolean} Returns `true` if the property is deleted, else `false`.
 * @example
 *
 * var object = { 'a': [{ 'b': { 'c': 7 } }] };
 * _.unset(object, 'a[0].b.c');
 * // => true
 *
 * console.log(object);
 * // => { 'a': [{ 'b': {} }] };
 *
 * _.unset(object, ['a', '0', 'b', 'c']);
 * // => true
 *
 * console.log(object);
 * // => { 'a': [{ 'b': {} }] };
 */
function unset(object, path) {
  return object == null ? true : baseUnset(object, path);
}

module.exports = unset;


/***/ }),

/***/ 1700:
/***/ ((module, __unused_webpack_exports, __webpack_require__) => {

var createCaseFirst = __webpack_require__(8882);

/**
 * Converts the first character of `string` to upper case.
 *
 * @static
 * @memberOf _
 * @since 4.0.0
 * @category String
 * @param {string} [string=''] The string to convert.
 * @returns {string} Returns the converted string.
 * @example
 *
 * _.upperFirst('fred');
 * // => 'Fred'
 *
 * _.upperFirst('FRED');
 * // => 'FRED'
 */
var upperFirst = createCaseFirst('toUpperCase');

module.exports = upperFirst;


/***/ }),

/***/ 3607:
/***/ (function(__unused_webpack_module, exports, __webpack_require__) {

"use strict";

var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    Object.defineProperty(o, k2, { enumerable: true, get: function() { return m[k]; } });
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", ({ value: true }));
const beagle_web_1 = __importStar(__webpack_require__(9731));
const promise_1 = __webpack_require__(7866);
const promiseMap = (() => {
    const map = {};
    let nextId = 0;
    return {
        register: (promise) => {
            const id = `${nextId++}`;
            map[id] = promise;
            return id;
        },
        unregister: (id) => delete map[id],
        resolve: (id, value) => {
            if (!map[id])
                throw new Error(`Can't resolve promise with id ${id}. It doesn't exist.`);
            map[id].resolve(value);
        },
        reject: (id, value) => {
            if (!map[id])
                throw new Error(`Can't reject promise with id ${id}. It doesn't exist.`);
            map[id].reject(value);
        },
    };
})();
const functionMap = (() => {
    const map = {};
    let nextId = 0;
    return {
        register: (fn) => {
            const id = `__beagleFn:${nextId++}`;
            map[id] = fn;
            return id;
        },
        unregister: (id) => delete map[id],
        call: (id, argumentsMap) => {
            if (!map[id])
                throw new Error(`Can't call function with id ${id}. It doesn't exist.`);
            map[id](argumentsMap);
        },
    };
})();
const storage = (() => {
    let map = {};
    const api = {
        clear: () => map = {},
        getItem: key => map[key],
        key: index => Object.keys(map)[index],
        length: 0,
        removeItem: (key) => {
            delete map[key];
            // @ts-ignore
            api.length--;
        },
        setItem: (key, value) => {
            map[key] = value;
            // @ts-ignore
            api.length++;
        }
    };
    return api;
})();
const httpClient = {
    fetch: (url, options) => {
        console.log(`js: fetching ${url}`);
        const staticPromise = promise_1.createStaticPromise();
        const id = promiseMap.register(staticPromise);
        const request = {
            id,
            method: options === null || options === void 0 ? void 0 : options.method,
            url: url.toString(),
            headers: options === null || options === void 0 ? void 0 : options.headers,
            body: options === null || options === void 0 ? void 0 : options.body,
        };
        sendMessage('httpClient.request', JSON.stringify(request));
        return staticPromise.promise;
    },
};
function createResponseHeaders(headers = {}) {
    // @ts-ignore
    const result = {
        append: (name, value) => headers[name] = value,
        delete: (name) => delete headers[name],
        entries: () => {
            throw new Error('not implemented yet!');
        },
        forEach: fn => Object.keys(headers).forEach(key => fn(headers[key], key, result)),
        get: name => headers[name],
        has: name => !!headers[name],
        keys: () => {
            throw new Error('not implemented yet!');
        },
        set: (name, value) => headers[name] = value,
        values: () => {
            throw new Error('not implemented yet!');
        },
    };
    return result;
}
function createResponse({ status, body, headers }) {
    const responseHeaders = createResponseHeaders(headers);
    return {
        arrayBuffer: () => {
            throw new Error('not implemented yet!');
        },
        blob: () => {
            throw new Error('not implemented yet!');
        },
        clone: () => {
            throw new Error('not implemented yet!');
        },
        formData: () => {
            throw new Error('not implemented yet!');
        },
        json: () => Promise.resolve(body ? JSON.parse(body) : undefined),
        text: () => Promise.resolve(body || ''),
        body: null,
        bodyUsed: false,
        headers: responseHeaders,
        ok: status >= 200 && status < 400,
        redirected: status >= 300 && status < 400,
        status,
        statusText: '',
        trailer: Promise.resolve(responseHeaders),
        type: 'default',
        url: '',
    };
}
function serializeFunctions(value) {
    if (typeof value === 'function') {
        return functionMap.register(value);
    }
    if (value instanceof Array) {
        return value.map(serializeFunctions);
    }
    if (value && typeof value === 'object') {
        const result = {};
        const keys = Object.keys(value);
        keys.forEach((key) => {
            result[key] = serializeFunctions(value[key]);
        });
        return result;
    }
    return value;
}
// @ts-ignore
window.beagle = (() => {
    let service;
    let nextViewId = 0;
    const api = {
        // todo: handle actions different than "beagle:alert"
        start: (baseUrl, actions) => {
            console.log(`js: baseUrl: ${baseUrl}`);
            service = beagle_web_1.default({
                baseUrl,
                components: {},
                fetchData: httpClient.fetch,
                customStorage: storage,
                customActions: {
                    'beagle:alert': ({ action, executeAction }) => {
                        const { _beagleAction_, message, title, onPressOk } = action;
                        const fnId = onPressOk && functionMap.register(() => executeAction(onPressOk, 'onPressOk'));
                        sendMessage('action', JSON.stringify({
                            _beagleAction_,
                            message,
                            title,
                            onPressOk: fnId,
                        }));
                    },
                },
            });
            beagle_web_1.logger.setCustomLogFunction((_, ...messages) => {
                console.log(messages.join(' '));
            });
        },
        createBeagleView: (route) => {
            const view = service.createView();
            const id = `${nextViewId++}`;
            view.subscribe((tree) => {
                sendMessage('beagleView.update', JSON.stringify({ id, tree: serializeFunctions(tree) }));
            });
            view.getNavigator().pushView({ url: route });
            console.log(`js: route: ${route}`);
            return id;
        },
        httpClient: {
            respond: (id, httpClientResponse) => {
                const response = createResponse(httpClientResponse);
                console.log(`js: received response with status ${response.status}`);
                promiseMap.resolve(id, response);
                promiseMap.unregister(id);
            }
        },
        call: (id, argumentsMap) => {
            console.log(`js: called function with id ${id} and argument map: ${JSON.stringify(argumentsMap)}`);
            functionMap.call(id, argumentsMap);
        },
    };
    return api;
})();


/***/ }),

/***/ 7866:
/***/ ((__unused_webpack_module, exports) => {

"use strict";

Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.createStaticPromise = void 0;
function createStaticPromise() {
    const staticPromise = {};
    staticPromise.promise = new Promise((resolve, reject) => {
        staticPromise.resolve = resolve;
        staticPromise.reject = reject;
    });
    return staticPromise;
}
exports.createStaticPromise = createStaticPromise;


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		if(__webpack_module_cache__[moduleId]) {
/******/ 			return __webpack_module_cache__[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			id: moduleId,
/******/ 			loaded: false,
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/global */
/******/ 	(() => {
/******/ 		__webpack_require__.g = (function() {
/******/ 			if (typeof globalThis === 'object') return globalThis;
/******/ 			try {
/******/ 				return this || new Function('return this')();
/******/ 			} catch (e) {
/******/ 				if (typeof window === 'object') return window;
/******/ 			}
/******/ 		})();
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/node module decorator */
/******/ 	(() => {
/******/ 		__webpack_require__.nmd = (module) => {
/******/ 			module.paths = [];
/******/ 			if (!module.children) module.children = [];
/******/ 			return module;
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
/******/ 	// startup
/******/ 	// Load entry module
/******/ 	// This entry module is referenced by other modules so it can't be inlined
/******/ 	__webpack_require__(3607);
/******/ })()
;