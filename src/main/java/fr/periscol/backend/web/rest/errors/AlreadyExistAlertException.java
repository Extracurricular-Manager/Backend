package fr.periscol.backend.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AlreadyExistAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AlreadyExistAlertException(String detail) {
        super(null, "Already Exist", Status.BAD_REQUEST, detail);
    }

}
