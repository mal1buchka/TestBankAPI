package kg.test.test_project_for_bank.Exceptions.AccountExceptions;

public class IllegalAccountOperationException extends RuntimeException {
    public IllegalAccountOperationException(String message) {
        super(message);
    }
}
