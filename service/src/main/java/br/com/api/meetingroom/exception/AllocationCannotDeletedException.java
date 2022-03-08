package br.com.api.meetingroom.exception;

public class AllocationCannotDeletedException extends RuntimeException {
    public AllocationCannotDeletedException(String message) {
        super(message);
    }
}
