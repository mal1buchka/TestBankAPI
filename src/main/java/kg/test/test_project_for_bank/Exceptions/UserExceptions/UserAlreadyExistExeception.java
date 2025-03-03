package kg.test.test_project_for_bank.Exceptions.UserExceptions;

public class UserAlreadyExistExeception extends RuntimeException {
    public UserAlreadyExistExeception(String message) {
        super(message);
    }
}
