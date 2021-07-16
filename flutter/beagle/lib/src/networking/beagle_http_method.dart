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

/// Http method to indicate the desired action
/// to be performed for a given resource.
enum BeagleHttpMethod {
  /// The PUT method replaces all current representations
  /// of the target resource with the request payload.
  put,

  /// The GET method requests a representation of the specified resource.
  /// Requests using GET should only retrieve data.
  get,

  /// The POST method is used to submit an entity to the specified resource,
  /// often causing a change in state or side effects on the server.
  post,

  /// The DELETE method deletes the specified resource.
  delete,

  /// The PATCH method is used to apply partial modifications to a resource.
  patch,

  /// The HEAD method asks for a response identical to that of a GET request,
  /// but without the response body.
  head
}
