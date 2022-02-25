package fr.periscol.backend.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UserNotExistsException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super(null, "User does not exist.", Status.BAD_REQUEST);
    }
}
