package cn.shaoqunliu.c.hub.mgr.vo;

import java.util.HashMap;
import java.util.Map;

public class RestfulResult {

    private long code;

    private String message;

    private Map<String, Object> data;

    public RestfulResult(long code, String message) {
        this(code, message, null);
    }

    public RestfulResult(long code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message == null ? "" : message;
        setData(data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data == null ? new HashMap<>() : data;
    }

    public void addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }
}
