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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.ixortalk.aws.s3.library.config.AwsS3Properties.S3;

@Component
public class AwsS3Template {

	private S3 buckets;

    @Autowired
    private AmazonS3 amazonS3Client;

    public AwsS3Template(S3 buckets) {
        this.buckets = buckets;
    }

    public PutObjectResult save(String key, MultipartFile multipartFile) throws IOException {
    	return saveImpl(buckets.getDefaultBucket(), key, multipartFile);
    }

    public PutObjectResult save(String bucketName,String key, MultipartFile multipartFile) throws IOException {
    	return saveImpl(buckets.getBucket(bucketName), key, multipartFile);
    }

    public S3Object get(String key) {
        return getImpl(buckets.getDefaultBucket(), key);
    }
    public S3Object get(String bucketName,String key) {
        return getImpl(buckets.getBucket(bucketName), key);
    }

    public ObjectListing list() {
        return listImpl(buckets.getDefaultBucket());
    }
    
    public ObjectListing list(String bucketName) {
        return listImpl(buckets.getBucket(bucketName));
    }
    
    
    
    private ObjectListing listImpl(String bucket) {
        return amazonS3Client.listObjects(bucket);
    }
    private S3Object getImpl(String bucket,String key) {
        return amazonS3Client.getObject(bucket, key);
    }
    private PutObjectResult saveImpl(String bucket, String key, MultipartFile multipartFile) throws IOException {
        long size = multipartFile.getSize();

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(size);
        meta.setContentType(multipartFile.getContentType());
        return amazonS3Client.putObject(new PutObjectRequest(bucket, key, multipartFile.getInputStream(),meta));
    }

    

}