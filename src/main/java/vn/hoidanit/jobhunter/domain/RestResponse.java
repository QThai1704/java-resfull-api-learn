package vn.hoidanit.jobhunter.domain;

public class RestResponse<T> {
    private String error;
    private String message;
    private T data;
    private int statusCode;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public RestResponse() {
    }

    public RestResponse(String error, String message, T data, int statusCode) {
        this.error = error;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}
