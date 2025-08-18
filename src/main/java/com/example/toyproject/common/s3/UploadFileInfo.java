package com.example.toyproject.common.s3;

/**
 * S3에 업로드된 파일의 URL과 키를 담는 DTO
 * @param fileUrl
 * @param fileKey
 */
public record UploadFileInfo(
        String fileUrl,
        String fileKey

) {
}
