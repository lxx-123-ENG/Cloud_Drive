package com.example.my_cloud_drive.pojo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_suffix")
    private String fileSuffix;

    @Column (nullable = false, name = "object_key")
    private String objectKey; // MinIO 中的对象键

    @Column (name = "folder_id")
    private Long folderId; // 所属文件夹 ID（null 表示根目录）

    @Column (nullable = false)
    private String path; // 文件路径

    @Column(nullable = false, name = "is_folder")
    private Boolean isFolder = false;

    @Column(nullable = false, name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(nullable = false, name = "upload_time")
    private LocalDateTime uploadTime;

    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
    @PrePersist
    public void prePersist() {
        if (uploadTime == null) {
            uploadTime = LocalDateTime.now();
        }
    }
}
