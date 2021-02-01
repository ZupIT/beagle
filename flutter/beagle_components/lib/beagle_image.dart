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

import 'package:flutter/widgets.dart';
import 'package:transparent_image/transparent_image.dart';

class BeagleImage extends StatelessWidget {
  const BeagleImage({Key key, this.path, this.mode}) : super(key: key);

  final ImagePath path;
  final ImageContentMode mode;

  // final designSystem DesignSystem;

  @override
  Widget build(BuildContext context) {
    return path.runtimeType == LocalImagePath
        ? createImageFromAsset(path)
        : createImageFromNetwork(path);
  }

  Image createImageFromAsset(LocalImagePath path) {
    //   // TODO: adicionar design system
    //   // return Image.asset(path.mobileId)
    return Image.asset('name');
  }

  Widget createImageFromNetwork(RemoteImagePath path) {
    // TODO: check placeholder
    // return Image.network(
    //   path.url,
    //   fit: getBoxFit(mode),
    // );
    if (path.placeholder != null) {
      // TODO: adicionar design system
      return FadeInImage.assetNetwork(
        placeholder: 'assets/loading.gif',
        image: path.url,
        fit: getBoxFit(mode),
      );
    } else {
      return FadeInImage.memoryNetwork(
        placeholder: kTransparentImage,
        image: path.url,
        fit: getBoxFit(mode),
      );
    }
  }

  // TODO: check Box Fit
  BoxFit getBoxFit(ImageContentMode mode) {
    if (mode == ImageContentMode.CENTER) {
      return BoxFit.cover;
    } else if (mode == ImageContentMode.CENTER_CROP) {
      return BoxFit.cover;
    } else if (mode == ImageContentMode.FIT_CENTER) {
      return BoxFit.cover;
    } else if (mode == ImageContentMode.FIT_XY) {
      return BoxFit.fill;
    } else {
      return BoxFit.none;
    }
  }
}

abstract class ImagePath {
  ImagePath._();

  factory ImagePath.local(String mobileId) = LocalImagePath;

  factory ImagePath.remote(String url, LocalImagePath placeholder) =
      RemoteImagePath;

  factory ImagePath.fromJson(Map<String, dynamic> json) {
    if (json['_beagleImagePath_'] == 'local') {
      return LocalImagePath.fromJson(json);
    } else {
      return RemoteImagePath.fromJson(json);
    }
  }
}

class LocalImagePath extends ImagePath {
  LocalImagePath(this.mobileId) : super._();

  LocalImagePath.fromJson(Map<String, dynamic> json)
      : mobileId = json['mobileId'],
        super._();

  final String mobileId;
}

class RemoteImagePath extends ImagePath {
  RemoteImagePath(this.url, this.placeholder) : super._();

  RemoteImagePath.fromJson(Map<String, dynamic> json)
      : url = json['url'],
        placeholder = json['placeholder'] != null
            ? LocalImagePath.fromJson(json['placeholder'])
            : null,
        super._();

  final String url;
  final LocalImagePath placeholder;
}

enum ImageContentMode {
  /// Compute a scale that will maintain the original aspect ratio,
  /// but will also ensure that it fits entirely inside the destination view.
  /// At least one axis (X or Y) will fit exactly. The result is centered inside the destination.
  FIT_XY,

  /// Compute a scale that will maintain the original aspect ratio,
  /// but will also ensure that it fits entirely inside the destination view.
  /// At least one axis (X or Y) will fit exactly.
  /// The result is centered inside the destination.
  FIT_CENTER,

  /// Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions
  /// (width and height) of the image will be equal to or larger than
  /// the corresponding dimension of the view (minus padding).
  CENTER_CROP,

  /// Center the image in the view but perform no scaling.
  CENTER
}
