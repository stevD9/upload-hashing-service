package me.stev.uploadservice.exception;

public class HashesDoNotMatchException extends RuntimeException {

    public HashesDoNotMatchException(String message) {
        super(message);
    }
}
