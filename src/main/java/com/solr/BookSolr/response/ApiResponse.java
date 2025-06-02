package com.solr.BookSolr.response;

import java.util.Map;

public class ApiResponse<T> {
    private Map<String, Object> responseHeader;
    private T response;

    public ApiResponse() {}

    public ApiResponse(Map<String, Object> responseHeader, T response) {
        this.responseHeader = responseHeader;
        this.response = response;
    }

    public Map<String, Object> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(Map<String, Object> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
