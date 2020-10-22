//
//	CCIExample.m
//
//	Created by Ahmed Ali on 17/1/2016
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



#import "CCIExample.h"

@interface CCIExample ()
@end


@implementation CCIExample


/**
 * Instantiate the instance using the passed dictionary values to set the properties values
 */

-(instancetype)initWithDictionary:(NSDictionary *)dictionary
{
    self = [super init];

    if(dictionary[@"location"] != nil && ![dictionary[@"location"] isKindOfClass:[NSNull class]]){
        self.location = [[CCILocation alloc] initWithDictionary:dictionary[@"location"]];
    }
    if(dictionary[@"exampleData"] != nil){
        self.exampleData = dictionary[@"exampleData"];
    }else{
        NSMutableDictionary * exampleData = [NSMutableDictionary dictionary];
        NSMutableArray * headers = [NSMutableArray array];
        if(dictionary[@"tableHeader"] != nil && ![dictionary[@"tableHeader"] isKindOfClass:[NSNull class]]){
            NSArray * cells = dictionary[@"tableHeader"][@"cells"];
            for(NSDictionary * cell in cells){
                NSString * cellValue = cell[@"value"];
                [headers addObject:cellValue];
                exampleData[cellValue] = [NSMutableArray array];
            }

        }
        if(dictionary[@"tableBody"] != nil && [dictionary[@"tableBody"] isKindOfClass:[NSArray class]]){
            NSArray * rows = dictionary[@"tableBody"];

            for(NSDictionary * row in rows){
                NSArray * columns = row[@"cells"];
                for(int i = 0; i < columns.count; i++){
                    NSString * header = headers[i];
                    NSMutableArray * tableColumn = exampleData[header];
                    NSDictionary * column = columns[i];
                    [tableColumn addObject:column[@"value"]];
                }
            }

        }

        if(dictionary[@"parsedTags"] != nil){
            self.tags = dictionary[@"parsedTags"];
        }else if(dictionary[@"tags"] != nil && [dictionary[@"tags"] isKindOfClass:[NSArray class]]){
            NSArray * tagsDictionaries = dictionary[@"tags"];
            self.rawTags = dictionary[@"tags"];
            NSMutableArray * tagsItems = [NSMutableArray array];
            for(NSDictionary * tagDictionary in tagsDictionaries){
                NSString * tagName = tagDictionary[@"name"];
                if([tagName hasPrefix:@"@"]){
                    tagName = [tagName stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:@""];
                }
                [tagsItems addObject:tagName];
            }
            self.tags = tagsItems;
        }

        self.exampleData = exampleData;
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

    dictionary[@"exampleData"] = self.exampleData;

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
    [aCoder encodeObject:self.exampleData forKey:@"exampleData"];


}

/**
 * Implementation of NSCoding initWithCoder: method
 */
- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    self.location = [aDecoder decodeObjectForKey:@"location"];
    self.exampleData = [aDecoder decodeObjectForKey:@"exampleData"];
    return self;

}
@end
