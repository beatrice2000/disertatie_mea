package com.beatrice.book.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String msg) {
        super(msg); // apel construcor clasa
    }
}
