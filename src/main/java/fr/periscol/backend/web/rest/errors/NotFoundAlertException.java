package fr.periscol.backend.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NotFoundAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NotFoundAlertException(String detail) {
        super(null,"Resource not found", Status.NOT_FOUND, detail);
    }

}
