import 'package:beagle/model/route.dart';

typedef NavigationListener = void Function(Route route);

abstract class BeagleNavigator {
  /// Subscribes to view navigations. The [listener] is executed before any change is done to the
  /// navigation history. If a [listener] throws an error, the navigation is aborted, i.e. the
  /// navigation history is not changed. The navigation history only changes after all listeners
  /// are successfully executed.
  ///
  /// The [listener] is called with two parameters: the first is the resulting route of the
  /// navigation. The second is the navigation controller to use for this navigation. A navigation
  /// controller is nothing more than a set of options to perform the navigation.
  ///
  /// Returns a function that, when called, unsubscribes the listener from the Navigator
  // todo: add the navigation controller as second parameter to the listener
  void subscribe(NavigationListener listener);

  /// Creates and navigates to a new navigation stack where the first route is the parameter
  /// [route].
  ///
  /// The [controllerId] is an optional parameter and it specifies the NavigationController to use
  /// for this specific stack.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> pushStack(Route route, [String controllerId]);

  /// Removes the entire current navigation stack and navigates back to the last route of the
  /// previous stack. Throws an error if there's only one navigation stack.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> popStack();

  /// Navigates to [route] by pushing it to the navigation history of the current navigation stack.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> pushView(Route route);

  /// Goes back one entry in the navigation history. If the current stack has only one view, this
  /// also pops the current stack. If only one stack and one view exist, it will throw an error.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> popView();

  /// Removes every navigation entry in the current stack until the route identified by
  /// [routeIdentifier] is found. A route is identified by a string if its url equals to the string
  /// (RemoteView) or if the screen id equals to the string (LocalView).
  ///
  /// When the desired route is found, a navigation will be performed to this route. Otherwise, if
  /// the route isn't found in the current stack, an error is thrown.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> popToView(String routeIdentifier);

  /// Removes the current navigation stack and navigates to the a new stack where the first [route]
  /// is the one passed as parameter.
  ///
  /// The parameter [controllerId] is optional and it specifies the NavigationController to use for
  /// this specific stack.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> resetStack(Route route, [String controllerId]);

  /// Removes the entire navigation history and starts it over by navigating to a new initial
  /// [route] (passed as parameter).
  ///
  /// The parameter [controllerId] is optional and it specifies the NavigationController to use for
  /// this specific stack.
  ///
  /// Returns a Future that resolves as soon as the navigation completes.
  Future<void> resetApplication(Route route, [String controllerId]);

  /// Verifies if the navigation history is empty, i.e. if there are no registered routes.
  ///
  /// Returns true for an empty navigation history, false otherwise.
  bool isEmpty();

  /// gets the current route
  /// Returns the current route or null if the navigator has not loaded its first route yet.
  Route getCurrentRoute();
}
