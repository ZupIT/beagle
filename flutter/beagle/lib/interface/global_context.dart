import 'package:beagle/service_locator.dart';

abstract class GlobalContext {

  static GlobalContext get getInstance => beagleServiceLocator();

  /// Gets a value in the global context according to the [path] passed as parameter. The [path] is
  /// optional, if not passed, the entire global context is returned. If no value is found for the
  /// provided [path], null is returned.
  ///
  /// Example of [path]: if the global context has an object named `user`, with an array named
  /// `documents` and you want the first document, use `user.documents[0]` as [path].
  ///
  /// The type returned by this function is always one of the following: Map, Array, num, bool or
  /// String.
  T get<T>([String path]);

  /// Sets a [value] in the global context according to the [path] passed as parameter. The [path]
  /// can be ommited, in this case, the [value] is set to the entire global context.
  ///
  /// The [path] follow the same rules as the method `get`. Example: `order.items`.
  ///
  /// All values in the GlobalContext must be encodable, i.e. Map, Array, number, bool or String.
  /// If the [value] is not encodable, an exception is thrown.
  void set<T>(T value, [String path]);

  /// Removes a value from the global context according to the [path] passed as parameter.
  ///
  /// - If the provided [path] doesn't exist, nothing happens.
  /// - If the provided [path] refers to a key of a map, the key is removed from the map.
  /// - If the provided [path] refers to an element of a list, the element is set to null.
  /// - If [path] is ommited, the entire global context is set to null.
  void clear([String path]);
}
