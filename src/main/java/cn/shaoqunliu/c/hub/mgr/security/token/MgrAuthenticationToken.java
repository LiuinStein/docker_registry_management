package cn.shaoqunliu.c.hub.mgr.security.token;

import cn.shaoqunliu.c.hub.mgr.security.details.MgrAccessDetails;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Map;

public class MgrAuthenticationToken extends AbstractAuthenticationToken {

    private String uri;
    private HttpMethod httpMethod;
    private MgrAccessDetails accessDetails;

    public MgrAuthenticationToken(String uri, HttpMethod httpMethod, MgrAccessDetails accessDetails) {
        super(null);
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.accessDetails = accessDetails;
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
}
