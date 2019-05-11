package cn.shaoqunliu.c.hub.utils;

import cn.shaoqunliu.c.hub.mgr.security.details.MgrAccessDetails;
import cn.shaoqunliu.c.hub.mgr.security.token.MgrAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityContextHolderUtils {

    private static MgrAccessDetails getAccessDetails() {
        if (SecurityContextHolder.getContext().getAuthentication()
                instanceof MgrAuthenticationToken) {
            return ((MgrAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                    .getAccessDetails();
        }
        throw new BadCredentialsException("Bad authentication");
    }

    // return value is not null
    public static Integer getUid() {
        return Objects.requireNonNull(getAccessDetails().getUid());
    }
}
