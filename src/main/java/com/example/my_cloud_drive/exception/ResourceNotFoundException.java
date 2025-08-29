package com.example.my_cloud_drive.exception;

public class ResourceNotFoundException extends BusinessException{
    public ResourceNotFoundException(String message) {
        super(message, 404);
    }

    // 快捷构造：比如“User with id 123 not found”
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " not found", 404);
    }
}
