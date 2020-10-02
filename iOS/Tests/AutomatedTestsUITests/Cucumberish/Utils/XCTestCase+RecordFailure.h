//
//  XCTest+RecordFailure.h
//  Cucumberish
//
//  Created by Derk Gommers on 02/03/2018.
//  Copyright © 2018 Ahmed Ali. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "CCILocation.h"

@interface XCTestCase (RecordFailure)

-(void)recordFailureWithDescription:(NSString *)description atLocation:(CCILocation *)location expected:(BOOL)expected;

@end
