package kg.test.test_project_for_bank.Exceptions.UserExceptions;

public class SuchUserNotFoundExeception extends RuntimeException {
    public SuchUserNotFoundExeception(String message) {
        super(message);
    }
}
