package giraffe.service.activity.complex;

import giraffe.domain.GiraffeException;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class TimeScheduledAndPeriodInconsistencyException extends GiraffeException {

    public TimeScheduledAndPeriodInconsistencyException() {
        super("\'time scheduled\' is inconsistent to \'period\' defined in the task settings.");
    }

    @Override
    public Integer getErrorCode() {
        return 1500;
    }

}