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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.ixortalk.aws.s3.library.config.AwsS3Properties.S3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import static com.amazonaws.HttpMethod.GET;
import static java.lang.System.currentTimeMillis;

@Component
public class AwsS3Template {

    private S3 s3;

    @Autowired
    private AmazonS3 amazonS3Client;

    public AwsS3Template(S3 s3) {
        this.s3 = s3;
    }

    public URL getObjectUrl(String bucketName, String key) {
        return amazonS3Client.getUrl(bucketName, key);
    }

    public S3Object get(String key) {
        return get(s3.getDefaultBucket(), key);
    }

    public S3Object get(String bucketName, String key) {
        return amazonS3Client.getObject(bucketName, key);
    }

    public ObjectListing list() {
        return list(s3.getDefaultBucket());
    }

    public ObjectListing list(String bucketName) {
        return amazonS3Client.listObjects(bucketName);
    }

    public PutObjectResult save(String key, MultipartFile multipartFile) throws IOException {
        return save(s3.getDefaultBucket(), key, multipartFile);
    }

    public PutObjectResult save(String bucketName, String key, MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        return save(bucketName, key, objectMetadata, multipartFile.getInputStream());
    }

    public PutObjectResult save(String bucketName, String key, ObjectMetadata objectMetadata, InputStream inputStream) {
        return amazonS3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata));
    }

    public String generatePresignedURL(String bucketName, String key, long expirationTimeInMillis) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(GET)
                        .withExpiration(new Date(currentTimeMillis() + expirationTimeInMillis));
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public String generatePresignedURL(String bucketName, String key) {
        return generatePresignedURL(bucketName, key, s3.getDefaultPresignedUrlValidityInMillis());
    }

    public URL generatePresignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}