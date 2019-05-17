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
            ResourceType resourceType = null;
            if (uriPart.length < 3 ||
                    (resourceType = ResourceType.resolve(uriPart[2])) == null) {
                return null;
            }
            boolean authenticated;
            switch (resourceType) {
                case NAMESPACE:
                    authenticated = hasNamespacePermissions(
                            uriPart.length == 3 ? null : uriPart[3],
                            authenticationToken.getHttpMethod(),
                            authenticationToken.getAccessDetails());
                    break;
                case REPOSITORY:
                    authenticated = hasRepositoryPermissions(
                            new DockerImageIdentifier(
                                    uriPart.length < 4 ? null : uriPart[3],
                                    uriPart.length < 5 ? null : uriPart[4]
                            ),
                            authenticationToken.getHttpMethod(),
                            authenticationToken.getAccessDetails()
                    );
                    break;
                case IMAGE:
                    authenticated = hasImagePermissions(
                            new DockerImageIdentifier(
                                    uriPart.length < 4 ? null : uriPart[3],
                                    uriPart.length < 5 ? null : uriPart[4]
                            ),
                            authenticationToken.getHttpMethod(),
                            authenticationToken.getAccessDetails()
                    );
                    break;
                case STAR:
                    // anyone can star any repositories
                    authenticated = true;
                    break;
                case USER:
                    authenticated = hasUserPermissions(
                            authenticationToken.getHttpMethod(),
                            authenticationToken.getAccessDetails());
                    break;
                case PERMISSION:
                    authenticated = hasGrantOrRevokePermissions(
                            new DockerImageIdentifier(
                                    uriPart.length < 4 ? null : uriPart[3],
                                    uriPart.length < 5 ? null : uriPart[4]
                            ),
                            authenticationToken.getHttpMethod(),
                            authenticationToken.getAccessDetails()
                    );
                    break;
                default:
                    throw new BadCredentialsException("bad request resource type");
            }
            authenticationToken.setAuthenticated(authenticated);
            if (authenticated) {
                return authenticationToken;
            }
        }
        return null;
    }

    private boolean hasUserPermissions(HttpMethod method, MgrAccessDetails accessDetails) {
        switch (method) {
            case POST:
                // sign up
                return true;
            case PATCH:
                // change password
            case PUT:
                // update user info
                return accessDetails.getUid() != null;
            default:
                return false;
        }
    }

    private boolean hasNamespacePermissions(String namespace, HttpMethod method, MgrAccessDetails accessDetails) {
        switch (method) {
            case POST:
                // add a namespace
            case GET:
                // get info of a namespace
                return accessDetails.getUid() != null;
            case DELETE:
                // delete a namespace, check if the owner
                return checkNamespaceOwnership(namespace, accessDetails);
            default:
                return false;
        }
    }

    private boolean hasRepositoryPermissions(DockerImageIdentifier identifier, HttpMethod method, MgrAccessDetails accessDetails) {
        switch (method) {
            case DELETE:
                // delete a repository
            case PUT:
                // update info
                // n-owner, r-owner
                return checkRepositoryOwnership(identifier, accessDetails);
            case GET:
                if (identifier == null) {
                    return false;
                }
                if (identifier.getNamespace() != null) {
                    if (identifier.getRepository() != null) {
                        // get info of a specific repository
                        return repositoryService.isOpened(identifier) ||
                                checkRepositoryOwnership(identifier, accessDetails);
                    }
                    // get a list of repositories within a specific namespace
                    return checkNamespaceOwnership(identifier.getNamespace(), accessDetails);
                }
                // get public repositories by ...
                return true;
            case POST:
                // add a repository
            default:
                return false;
        }
    }

    private boolean hasImagePermissions(DockerImageIdentifier identifier, HttpMethod method, MgrAccessDetails accessDetails) {
        switch (method) {
            case GET:
                // get images within a specific repository
                return repositoryService.isOpened(identifier) ||
                        checkRepositoryOwnership(identifier, accessDetails);
            case DELETE:
                // delete an image
                return checkRepositoryOwnership(identifier, accessDetails);
            default:
                return false;
        }
    }

    private boolean hasGrantOrRevokePermissions(DockerImageIdentifier identifier, HttpMethod method, MgrAccessDetails accessDetails) {
        if (identifier == null || identifier.getNamespace() == null ||
                identifier.getRepository() == null || accessDetails == null) {
            return false;
        }
        switch (method) {
            case PATCH:
                // change owner of repository
                return checkNamespaceOwnership(identifier.getNamespace(), accessDetails);
            case PUT:
            case GET:
                return checkRepositoryOwnership(identifier, accessDetails);
            default:
                return false;
        }
    }

    private boolean checkNamespaceOwnership(String namespace, MgrAccessDetails accessDetails) {
        if (namespace == null || accessDetails == null) {
            return false;
        }
        return accessDetails.getAuthorities().getOwnership()
                .getNamespace().contains(namespace);
    }

    private boolean checkRepositoryOwnership(DockerImageIdentifier identifier, MgrAccessDetails accessDetails) {
        if (identifier == null || identifier.getNamespace() == null ||
                identifier.getRepository() == null || accessDetails == null) {
            return false;
        }
        return checkNamespaceOwnership(identifier.getNamespace(), accessDetails) ||
                accessDetails.getAuthorities().getOwnership()
                        .getRepository()
                        .contains(identifier.getFullRepositoryName());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(MgrAuthenticationToken.class);
    }
}
