package cn.shaoqunliu.c.hub.msg.consumer;

import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.service.DockerImageService;
import cn.shaoqunliu.c.hub.mgr.service.DockerNamespaceService;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.mgr.service.DockerUserService;
import cn.shaoqunliu.c.hub.msg.po.RegistryMessage;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@EnableJms
@Component
public class PushConsumer {

    private final DockerNamespaceService namespaceService;
    private final DockerRepositoryService repositoryService;
    private final DockerImageService imageService;
    private final DockerUserService userService;

    @Autowired
    public PushConsumer(DockerNamespaceService namespaceService, DockerRepositoryService repositoryService, DockerImageService imageService, DockerUserService userService) {
        this.namespaceService = namespaceService;
        this.repositoryService = repositoryService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @JmsListener(destination = "registry.push.queue")
    public void receiveMsg(RegistryMessage message) {
        if (message == null) {
            // will never happen theoretically
            return;
        }
        // check if the repository within this message exists
        DockerImageIdentifier identifier = new DockerImageIdentifier(message.getRepository());
        if (identifier.getNamespace() == null || identifier.getRepository() == null) {
            // fake identifier, will never happen theoretically
            // we only accepts the identifier with both namespace and repository
            return;
        }
        // get owner
        DockerUser owner = userService.getUserByName(message.getActor());
        if (owner == null) {
            // bad message, exit immediately
            return;
        }
        DockerRepository repository = repositoryService
                .getDockerRepositoryByIdentifier(identifier.getNamespace(),
                        identifier.getRepository());
        if (repository == null) {
            // this repository is not exist in current database
            // so that we add this repository to database
            repository = new DockerRepository();
            repository.setName(identifier.getRepository());
            repository.setOpened(true);
            repository.setOwner(owner);
            // must ensure the namespace exists
            // the namespace can be added only in the web console
            DockerNamespace namespace = namespaceService.getDockerNamespaceByName(identifier.getNamespace());
            if (namespace == null) {
                // fake message with invalid repository identifier
                // exit without any write operations in database
                return;
            }
            repository.setNamespace(namespace);
            // create the new repository
            repository = repositoryService.save(repository);
        }
        // the repository will never be null here and full described
        // update the tag info if it's exists or add if not
        DockerImage image = new DockerImage();
        image.setName(message.getTag());
        image.setRepository(repository);
        image.setSha256(message.getDigest().substring(7, 7 + 12));
        image.setSize(message.getSize());
        imageService.updateIfExistsAndAddIfNot(image);
    }
}
