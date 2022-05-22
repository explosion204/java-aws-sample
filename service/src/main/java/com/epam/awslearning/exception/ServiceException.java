package com.epam.awslearning.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String messageFormat, String ... variables) {
        super(String.format(messageFormat, (Object[]) variables));
    }
}
