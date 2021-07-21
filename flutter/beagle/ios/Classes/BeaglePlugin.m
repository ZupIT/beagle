#import "BeaglePlugin.h"
#if __has_include(<beagle/beagle-Swift.h>)
#import <beagle/beagle-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "beagle-Swift.h"
#endif

@implementation BeaglePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBeaglePlugin registerWithRegistrar:registrar];
}
@end
