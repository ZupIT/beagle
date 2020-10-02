//
//  NSArray+Hashes.h
//  CucumberishExample
//
//  Created by David Siebecker on 7/26/16.
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSArray (Hashes)
/**
 * Converts an NSArray<NSArray> to an array of dictionaries where the keys for the dictionary are the elements in the first array in self
 * @return the row hashes
 */
-(NSArray*)rowHashes;

/**
 * Converts an NSArray<NSArray> to an array of dictionaries where the kesy for the dictionary are the first elements in each array in self
 * @return the column hashes
 */
-(NSArray*)columnHashes;

@end
