package com.dev.loja.exception;

import java.net.ConnectException;

public class EmailSenderException extends ConnectException {
    public EmailSenderException(String message) {
        super(message);
    }
}
