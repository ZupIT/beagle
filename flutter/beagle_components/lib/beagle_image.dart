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

import 'dart:typed_data';

import 'package:beagle/interface/beagle_image_downloader.dart';
import 'package:beagle/logger/beagle_logger.dart';
import 'package:beagle/model/beagle_style.dart';
import 'package:beagle/service_locator.dart';
import 'package:beagle/setup/beagle_design_system.dart';
import 'package:beagle/style/style_builder.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';

/// Defines an image widget that renders local or remote resource depending on
/// the value passed to [path].
class BeagleImage extends StatefulWidget with YogaWidget {
  const BeagleImage({
    Key key,
    this.path,
    this.mode,
    this.style,
  }) : super(key: key);

  /// Defines the location of the image resource.
  final ImagePath path;

  /// Defines how the declared image will fit the view.
  final ImageContentMode mode;


  final BeagleStyle style;

  @override
  _BeagleImageState createState() => _BeagleImageState();
}

class _BeagleImageState extends State<BeagleImage> {
  Future<Uint8List> imageBytes;
  BeagleLogger logger = beagleServiceLocator<BeagleLogger>();

  @override
  void initState() {
    if (!isLocalImage()) {
      downloadImage();
    }

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final image = isLocalImage()
        ? createImageFromAsset(widget.path)
        : createImageFromNetwork(widget.path);
    return buildYogaNode(style: widget.style, child: image);
  }

  Future<void> downloadImage() async {
    final RemoteImagePath path = widget.path;
    try {
      final imageDownloader = beagleServiceLocator<BeagleImageDownloader>();
      imageBytes = imageDownloader.downloadImage(path.url);
    } catch (e) {
      logger.errorWithException(e.toString(), e);
    }
  }

  bool isLocalImage() => widget.path.runtimeType == LocalImagePath;

  Widget createImageFromAsset(LocalImagePath path) {
    if (isPlaceHolderValid(path)) {
      return Image.asset(
        getAssetName(path),
        fit: getBoxFit(widget.mode),
      );
    } else {
      return Container();
    }
  }

  Widget createImageFromNetwork(RemoteImagePath path) {
    return FutureBuilder(
      future: imageBytes,
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return createPlaceHolderWidget(path);
        } else {
          return createImageFromMemory(snapshot.data);
        }
      },
    );
  }

  Widget createPlaceHolderWidget(RemoteImagePath path) {
    if (isPlaceHolderValid(path.placeholder)) {
      return createImageFromAsset(path.placeholder);
    } else {
      return Container();
    }
  }

  Image createImageFromMemory(Uint8List bytes) {
    return Image.memory(
      bytes,
      fit: getBoxFit(widget.mode),
    );
  }

  bool isImageDownloaded() => imageBytes != null;

  String getAssetName(LocalImagePath imagePath) {
    final designSystem = beagleServiceLocator<BeagleDesignSystem>();

    return designSystem.image(imagePath.mobileId);
  }

  bool isPlaceHolderValid(LocalImagePath path) =>
      path != null && getAssetName(path) != null;

  BoxFit getBoxFit(ImageContentMode mode) {
    if (mode == ImageContentMode.CENTER) {
      return BoxFit.none;
    } else if (mode == ImageContentMode.CENTER_CROP) {
      return BoxFit.cover;
    } else if (mode == ImageContentMode.FIT_CENTER) {
      return BoxFit.contain;
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
    if (json[_jsonBeagleImagePathKey] == 'local') {
      return LocalImagePath.fromJson(json);
    } else {
      return RemoteImagePath.fromJson(json);
    }
  }

  static const _jsonBeagleImagePathKey = '_beagleImagePath_';
}

class LocalImagePath extends ImagePath {
  LocalImagePath(this.mobileId) : super._();

  LocalImagePath.fromJson(Map<String, dynamic> json)
      : mobileId = json[_jsonMobileIdKey],
        super._();

  final String mobileId;

  static const _jsonMobileIdKey = 'mobileId';
}

class RemoteImagePath extends ImagePath {
  RemoteImagePath(this.url, this.placeholder) : super._();

  RemoteImagePath.fromJson(Map<String, dynamic> json)
      : url = json[_jsonUrlKey],
        placeholder = json[_jsonPlaceholderKey] != null
            ? LocalImagePath.fromJson(json[_jsonPlaceholderKey])
            : null,
        super._();

  final String url;
  final LocalImagePath placeholder;

  static const _jsonUrlKey = 'url';
  static const _jsonPlaceholderKey = 'placeholder';
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
