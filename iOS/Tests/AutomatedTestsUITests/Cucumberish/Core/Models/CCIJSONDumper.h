//
//  CCIJSONDumper.h
//  AMBluetooth
//
//  Created by ereynolds on 11/11/16.
//  Copyright © 2016 Ahmed Ali. All rights reserved.
//

#import <Foundation/Foundation.h>
@class CCIFeature;
@interface CCIJSONDumper : NSObject

/*!
 *   @author Erik Call Reynolds 
 *   @date 16-11-15 12:11:00
 *
 *   @brief creates the Cucumber JSON Output String similar to the ruby/cucumber JSON output option
 *
 *   @param features The array of features to output as JSON
 *
 *   @return JSON String
 *
 *   @note Omitted the Match Dictionary for each Step definition as it no longer applies in Objective-C
 *   @note Omitted the Execution Duration of each Step due to the complexity level   
 */
+(NSString*)buildJSONOutputString:(NSArray<CCIFeature*>*)features;
+(NSData*)buildJSONOutputData:(NSArray<CCIFeature*>*)features;

+(NSString*)writeJSONToFile:(NSString*)filename
           forFeatures:(NSArray<CCIFeature*>*)features;

+(NSString*)writeJSONToFile:(NSString*)filename
                inDirectory:(NSString*) directory
                forFeatures:(NSArray<CCIFeature*>*)features;


@end
