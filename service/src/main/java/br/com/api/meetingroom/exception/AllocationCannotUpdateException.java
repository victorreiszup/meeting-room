package br.com.api.meetingroom.exception;

public class AllocationCannotUpdateException extends RuntimeException {
    public AllocationCannotUpdateException(String message) {
        super(message);
    }
}
