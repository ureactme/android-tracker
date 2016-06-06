package me.ureact.tracker.exceptions;

/**
 * Created by pappacena on 15/12/15.
 */
public class EmptyTokenException extends RuntimeException {
    public EmptyTokenException(String message) {
        super(message);
    }
}
