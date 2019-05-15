package cn.shaoqunliu.c.hub.mgr.security;

import cn.shaoqunliu.c.hub.mgr.security.details.MgrAccessDetails;
import cn.shaoqunliu.c.hub.mgr.security.token.MgrAuthenticationToken;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MgrAuthenticationProvider implements AuthenticationProvider {

    private final DockerRepositoryService repositoryService;

    @Autowired
    public MgrAuthenticationProvider(DockerRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    private enum ResourceType {
        NAMESPACE(0, "namespace"),
        REPOSITORY(1, "repository"),
        IMAGE(2, "image"),
        STAR(3, "star"),
        USER(4, "user"),
        PERMISSION(5, "permission");

        private int val;
        private String str;

        ResourceType(int v, String s) {
            val = v;
            str = s;
        }

        public int value() {
            return val;
        }

        public static ResourceType valueOf(int v) {
            if (v < 0 || v > 5) {
                return null;
            }
            ResourceType[] mapper = {NAMESPACE, REPOSITORY, IMAGE, STAR, USER, PERMISSION};
            return mapper[v];
        }

        // case insensitive
        public static ResourceType resolve(String s) {
            switch (s.toLowerCase()) {
                case "namespace":
                    return NAMESPACE;
                case "repository":
                    return REPOSITORY;
                case "image":
                    return IMAGE;
                case "star":
                    return STAR;
                case "user":
                    return USER;
                case "permission":
                    return PERMISSION;
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            return str;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof MgrAuthenticationToken) {
            MgrAuthenticationToken authenticationToken =
                    (MgrAuthenticationToken) authentication;
            // the first element of uriPart with split from URI is an empty string
            // due to the URI are always starts with '/'.
            // for example: the URI: /v1/namespace/{NAMESPACE}
            // and the split result is String[4] { "", "v1", "namespace", "{NAMESPACE}" }
            String[] uriPart = authenticationToken.getUri().split("/");
            ResourceType resourceType;
            if (uriPart.length < 3 ||
                    (resourceType = ResourceType.resolve(uriPart[2])) == null) {
                return null;
            }
            boolean authenticated;
            switch (resourceType) {
                case NAMESPACE:
                    authenticated = true; // for test use
//                    // {METHOD} /v1/namespace/{RESOURCE}
//                    authenticated = hasNamespacePermission(
//                            uriPart.length == 3 ? null : uriPart[3],
//                            authenticationToken.getHttpMethod(),
//                            authenticationToken.getParameterMap(),
//                            authenticationToken.getAccessDetails());
                    break;
                case REPOSITORY:
                    authenticated = true;
//                    authenticated = hasRepositoryPermission(
//                            new DockerImageIdentifier(uriPart[3]),
//                            authenticationToken.getHttpMethod(),
//                            authenticationToken.getAccessDetails());
                    break;
                case IMAGE:
                case STAR:
                case USER:
                case PERMISSION:
                    authenticated = true;
                    break;
                default:
                    throw new BadCredentialsException("bad request resource type");
            }
            authenticationToken.setAuthenticated(authenticated);
            // TODO: not to return null here
            // null will cause HTTP OK returned
            return authenticated ? authenticationToken : null;
        }
        return null;
    }


//    private boolean hasNamespacePermission(String namespace, HttpMethod method,
//                                           Map<String, String[]> parameters,
//                                           MgrAccessDetails accessDetails) {
//        switch (method) {
//            case POST:
//                // add a new namespace
//                return true;
//            case DELETE:
//                // delete a namespace, check if is the owner
//                return accessDetails.getAuthorities().getOwnership()
//                        .getNamespace().contains(namespace);
//            case GET:
//                // get a list of namespaces belongs to someone
//                // when the namespace field equals to null
//                return namespace == null ||
//                        // get info of a specific namespace such as owner
//                        accessDetails.getAuthorities().getOwnership()
//                                .getNamespace().contains(namespace);
//            default:
//                return false;
//        }
//    }
//
//    private boolean hasRepositoryPermission(DockerImageIdentifier identifier, HttpMethod method, MgrAccessDetails accessDetails) {
//        switch (method) {
//            case GET:
//                // check if is opened, and everybody can read infos of an opened repository
//                return repositoryService.isOpened(identifier.getNamespace(),
//                        identifier.getRepository()) ||
//                        // check if have the pull/push permission
//                        accessDetails.getAuthorities().getReadOnly()
//                                .contains(identifier.getFullIdentifier()) ||
//                        accessDetails.getAuthorities().getWritable()
//                                .contains(identifier.getFullIdentifier()) ||
//                        // check if is the owner
//                        accessDetails.getAuthorities().getOwnership()
//                                .getRepository()
//                                .contains(identifier.getFullIdentifier());
//            case PUT:
//            case DELETE:
//                // only the owner can update/delete the repository's info
//                // such as repository descriptions and isOpened
//                return accessDetails.getAuthorities().getOwnership()
//                        .getRepository()
//                        .contains(identifier.getFullIdentifier());
//            case POST:
//                // only the namespace owner can add a repository into this namespace
//                return accessDetails.getAuthorities().getOwnership()
//                        .getNamespace().contains(identifier.getNamespace());
//            default:
//                return false;
//        }
//    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(MgrAuthenticationToken.class);
    }
}
