//
//	CCIArgument.m
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



#import "CCIArgument.h"

@interface CCIArgument ()
@end
@implementation CCIArgument




/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
	self = [super init];

	
    if(dictionary[@"content"] != nil && ![dictionary[@"content"] isKindOfClass:[NSNull class]]){
        self.content = dictionary[@"content"];
    }
    if(dictionary[@"parsedRows"] != nil){
        self.rows = dictionary[@"parsedRows"];
    }else if(dictionary[@"rows"] != nil && [dictionary[@"rows"] isKindOfClass:[NSArray class]]){
        NSArray * rowsDictionaries = dictionary[@"rows"];
        NSMutableArray * rows = [NSMutableArray array];
        for(NSDictionary * rowsDictionary in rowsDictionaries){
            NSMutableArray * row = [NSMutableArray array];
            for(NSDictionary * cell in rowsDictionary[@"cells"]){
                [row addObject:cell[@"value"]];
            }
            [rows addObject:row];
        }
        if(rows.count > 0){
            self.rows = rows;
        }
    }
	return self;
}


/**
 * Returns all the available property values in the form of NSDictionary object where the key is the approperiate json key and the value is the value of the corresponding property
 */
-(NSDictionary *)toDictionary
{
	NSMutableDictionary * dictionary = [NSMutableDictionary dictionary];

	if(self.rows != nil){
		dictionary[@"parsedRows"] = self.rows;
	}
	
    if(self.content != nil){
        dictionary[@"content"] = self.content;
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
	if(self.rows != nil){
		[aCoder encodeObject:self.rows forKey:@"rows"];
	}

    if(self.content != nil){
        [aCoder encodeObject:self.content forKey:@"content"];
    }

}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
	self = [super init];
	self.rows = [aDecoder decodeObjectForKey:@"rows"];
    self.content = [aDecoder decodeObjectForKey:@"content"];
	return self;

}
@end