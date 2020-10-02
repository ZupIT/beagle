//
//	CCIExample.h
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
// THE SOFTWARE.

//	Model file Generated using JSONExport: https://github.com/Ahmed-Ali/JSONExport

#import "CCILocation.h"

/**
 Represents and example of a scanrio outline
 */
@interface CCIExample : NSObject

/**
 The location of the example in its file
 */
@property (nonatomic, strong) CCILocation * location;

/**
 Representaion of the example data where the dictionary keys are the table headers
 each key will contain array of example column. E. g
 @code
 | Field 1 | Field 2 | Field 3 |
 |  C1Ro1  |  C2Ro1  |  C3Ro1  |
 |  C1Ro2  |  C2Ro2  |  C3Ro2  |
 |  C1Ro3  |  C2Ro3  |  C3Ro3  |

 @endcode

 This will end up with the example data with the following structure
 @code
 {
    Field 1 : [
        C1Ro1,
        C1Ro2,
        C1Ro3
    ],
    Field 2 : [
        C2Ro1,
        C2Ro2,
        C2Ro3
    ],
    Field 3 : [
        C3Ro1,
        C3Ro2,
        C3Ro3
    ]
 }
 @endcode
 */
@property (nonatomic, strong) NSDictionary * exampleData;


/**
 Array of tags found on top of this example table
 */
@property (nonatomic, strong) NSArray <NSString *> * tags;

@property (nonatomic, strong) NSArray<NSDictionary*>* rawTags;


/**
 Creates an instance with properties filled from the passed dictionary

 @param dictionary the dictionary that contains all the example data

 @return example instance
 */
-(instancetype)initWithDictionary:(NSDictionary *)dictionary;

/**
 Creates a dictionary from the class properties

 @return the created dictionary
 */
-(NSDictionary *)toDictionary;
@end