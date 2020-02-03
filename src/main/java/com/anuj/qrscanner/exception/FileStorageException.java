package com.anuj.qrscanner.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String s) {
        super(s);
    }
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
