package cn.shaoqunliu.c.hub.mgr.security.token;

import cn.shaoqunliu.c.hub.mgr.security.details.MgrAccessDetails;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

public class MgrAuthenticationToken extends AbstractAuthenticationToken {

    private String uri;
    private HttpMethod httpMethod;
    private MgrAccessDetails accessDetails;
    private Map<String, String[]> parameterMap;

    public MgrAuthenticationToken(String uri, HttpMethod httpMethod, MgrAccessDetails accessDetails, Map<String, String[]> parameterMap) {
        super(null);
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.accessDetails = accessDetails;
        this.parameterMap = parameterMap;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return accessDetails.getUsername();
    }

    public MgrAccessDetails getAccessDetails() {
        return accessDetails;
    }

    public void setAccessDetails(MgrAccessDetails accessDetails) {
        this.accessDetails = accessDetails;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
