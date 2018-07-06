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

    public S3Object get(String key) {
        return get(buckets.getDefaultBucket(), key);
    }

    public S3Object get(String bucketName, String key) {
        return amazonS3Client.getObject(bucketName, key);
    }

    public ObjectListing list() {
        return list(buckets.getDefaultBucket());
    }

    public ObjectListing list(String bucketName) {
        return amazonS3Client.listObjects(bucketName);
    }

    public PutObjectResult save(String key, MultipartFile multipartFile) throws IOException {
        return save(buckets.getDefaultBucket(), key, multipartFile);
    }

    public PutObjectResult save(String bucketName, String key, MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        return amazonS3Client.putObject(new PutObjectRequest(bucketName, key, multipartFile.getInputStream(), objectMetadata));
    }
}