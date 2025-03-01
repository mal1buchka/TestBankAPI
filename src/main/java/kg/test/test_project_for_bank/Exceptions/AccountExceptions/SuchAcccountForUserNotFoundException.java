package kg.test.test_project_for_bank.Exceptions.AccountExceptions;

public class SuchAcccountForUserNotFoundException extends RuntimeException {
    public SuchAcccountForUserNotFoundException(String message) {
        super(message);
    }
}
