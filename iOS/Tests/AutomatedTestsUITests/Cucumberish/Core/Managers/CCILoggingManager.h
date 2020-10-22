//
//  CCILoggingManager.h
//  CucumberishLibrary
//
//  Created by Titouan van Belle on 15.07.17.
//  Copyright © 2017 Ahmed Ali. All rights reserved.
//

#import <Foundation/Foundation.h>

void CCILog(NSString *format, ...);


@protocol CCILogger<NSObject>

- (void)logFormat:(NSString *)format arguments:(va_list)arguments;

@end


@interface CCILoggingManager : NSObject

+ (CCILoggingManager *)sharedInstance;
- (void)addLogger:(id<CCILogger>)logger;
- (void)logFormat:(NSString *)format arguments:(va_list)arguments;

@end
