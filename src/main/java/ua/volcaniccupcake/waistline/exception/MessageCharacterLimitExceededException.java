package ua.volcaniccupcake.waistline.exception;

public class MessageCharacterLimitExceededException extends RuntimeException{
    public MessageCharacterLimitExceededException(String message) {
        super(message);
    }
}
