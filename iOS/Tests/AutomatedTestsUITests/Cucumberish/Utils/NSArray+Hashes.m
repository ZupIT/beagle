//
//  NSArray+Hashes.m
//  CucumberishExample
//
//  Created by David Siebecker on 7/26/16.
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//

#import "NSArray+Hashes.h"

@implementation NSArray (Hashes)

-(NSArray *)rowHashes
{
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:self.count];
    NSArray *keys = self[0];
    for (NSInteger i = 1; i < self.count; i++) {
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        NSArray *values = self[i];
        [keys enumerateObjectsUsingBlock:^(NSString *  _Nonnull key, NSUInteger idx, BOOL * _Nonnull stop) {
            dict[key] = values[idx];
        }];
        [array addObject:dict];
    }
    return array;
}

-(NSArray *)columnHashes
{
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:[self[0] count]-1];
    NSArray *keys = [self valueForKey:@"firstObject"];
    for (NSInteger i = 0; i < keys.count; i++) {
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        [self enumerateObjectsUsingBlock:^(NSString *  _Nonnull key, NSUInteger idx, BOOL * _Nonnull stop) {
            if (idx != 0) {
                dict[key] = self[i][idx];
            }
        }];
        [array addObject:dict];
    }
    return array;
}

@end
