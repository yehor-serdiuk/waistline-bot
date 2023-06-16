package ua.volcaniccupcake.waistline.exception;

public class EmptyItemListException extends RuntimeException {
    public EmptyItemListException() {
        super("Item list is empty");
    }
}
