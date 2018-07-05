/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-present IxorTalk CVBA
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ixortalk.aws.s3.library.config;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static com.amazonaws.regions.Regions.EU_CENTRAL_1;

@ConfigurationProperties(prefix = "com.ixortalk")
public class AwsS3Properties {

    @NestedConfigurationProperty
    private Aws aws = new Aws();

    @NestedConfigurationProperty
    private S3 s3 = new S3();

    public Aws getAws() {
        return aws;
    }

    public void setAws(Aws aws) {
        this.aws = aws;
    }

    public S3 getS3() {
        return s3;
    }

    public void setS3(S3 s3) {
        this.s3 = s3;
    }

    public static class Aws {

        private Regions region = EU_CENTRAL_1;

        public Regions getRegion() {
            return region;
        }

        public void setRegion(Regions region) {
            this.region = region;
        }
    }

    public static class S3 {

        private String defaultBucket = "default-bucket";
        private Map<String, String> buckets = new HashMap<>();

        public String getDefaultBucket() {
            return defaultBucket;
        }

        public void setDefaultBucket(String defaultBucket) {
            this.defaultBucket = defaultBucket;
        }
        
        public Map<String, String> getBuckets(){
        	return this.buckets;
        }
        
        public String getBucket(String bucketName){
        	return buckets.get(bucketName);
        }

        public void setBucket(String bucketName, String bucket){
        	this.buckets.put(bucketName,bucket);
        }
    }

}
