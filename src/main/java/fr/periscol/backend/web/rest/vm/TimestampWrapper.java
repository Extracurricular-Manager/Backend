package fr.periscol.backend.web.rest.vm;

import java.sql.Timestamp;

/**
 * Wrapper for data <i>T</i> adding a Timestamp
 * @param <T> the type of the data
 */
public class TimestampWrapper<T> {

    private Timestamp timestamp;
    private T data;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
