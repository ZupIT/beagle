//
//  CCILoggingManager.h
//  CucumberishLibrary
//
//  Created by Titouan van Belle on 15.07.17.
//  Copyright © 2017 Ahmed Ali. All rights reserved.
//

#import "CCILoggingManager.h"

void CCILog(NSString *format, ...)
{
    va_list args;
    va_start(args, format);
    [[CCILoggingManager sharedInstance] logFormat:format arguments:args];
    va_end(args);
}


@interface CCIConsoleLogger : NSObject<CCILogger>

@end

@implementation CCIConsoleLogger

- (void)logFormat:(NSString *)format arguments:(va_list)arguments
{
    NSLogv(format, arguments);
}

@end



@interface CCILoggingManager ()

@property (nonatomic, strong) NSSet<CCILogger> *loggers;

@end

@implementation CCILoggingManager

+ (CCILoggingManager *)sharedInstance
{
    static CCILoggingManager *sharedInstance;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        sharedInstance = [[CCILoggingManager alloc] init];
        sharedInstance.loggers = (NSSet<CCILogger> *)[NSSet new];
        id<CCILogger> consoleLogger = [[CCIConsoleLogger alloc] init];
        [sharedInstance addLogger:consoleLogger];
    });

    return sharedInstance;
}

- (void)addLogger:(id<CCILogger>)logger
{
    NSMutableSet *mutableLoggers = [self.loggers mutableCopy];
    [mutableLoggers addObject:logger];
    self.loggers = [mutableLoggers copy];
}

- (void)logFormat:(NSString *)format arguments:(va_list)arguments
{
    NSSet *loggers = [self.loggers copy];
    for (id<CCILogger> logger in loggers) {
        va_list args_copy;
        va_copy(args_copy, arguments);

        [logger logFormat:format arguments:args_copy];
        va_end(args_copy);
    };
}


@end
