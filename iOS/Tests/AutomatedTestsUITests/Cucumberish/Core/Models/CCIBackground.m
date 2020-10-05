//
//	CCIBackground.m
//
//	Created by Ahmed Ali on 2/1/2016
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.	Model file Generated using JSONExport: https://github.com/Ahmed-Ali/JSONExport



#import "CCIBackground.h"

@interface CCIBackground ()
@end
@implementation CCIBackground




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
	self = [super init];


	if(dictionary[@"location"] != nil && ![dictionary[@"location"] isKindOfClass:[NSNull class]]){
		self.location = [[CCILocation alloc] initWithDictionary:dictionary[@"location"]];
	}


	if(dictionary[@"steps"] != nil && [dictionary[@"steps"] isKindOfClass:[NSArray class]]){
		NSArray * stepsDictionaries = dictionary[@"steps"];
		NSMutableArray * stepsItems = [NSMutableArray array];
		for(NSDictionary * stepsDictionary in stepsDictionaries){
			CCIStep * stepsItem = [[CCIStep alloc] initWithDictionary:stepsDictionary];
			[stepsItems addObject:stepsItem];
		}
		self.steps = stepsItems;
	}

	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
	NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];

	if(self.location != nil){
		dictionary[@"location"] = [self.location toDictionary];
	}

	if(self.steps != nil){
		NSMutableArray * dictionaryElements = [NSMutableArray array];
		for(CCIStep * stepsElement in self.steps){
			[dictionaryElements addObject:[stepsElement toDictionary]];
		}
		dictionary[@"steps"] = dictionaryElements;
	}
	return dictionary;

}

/**
 * Implementation of NSCoding encoding method
 */
/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
- (void)encodeWithCoder:(NSCoder *)aCoder
{

	if(self.location != nil){
		[aCoder encodeObject:self.location forKey:@"location"];
	}

	if(self.steps != nil){
		[aCoder encodeObject:self.steps forKey:@"steps"];
	}

}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super init];
	self.location = [aDecoder decodeObjectForKey:@"location"];
	self.steps = [aDecoder decodeObjectForKey:@"steps"];
	return self;

}
@end