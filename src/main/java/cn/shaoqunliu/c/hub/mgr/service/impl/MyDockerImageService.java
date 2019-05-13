package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerImageRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerImageBasic;
import cn.shaoqunliu.c.hub.mgr.service.DockerImageService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.ObjectCopyingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service("myDockerImageService")
public class MyDockerImageService implements DockerImageService {

    private final DockerImageRepository imageRepository;

    @Autowired
    public MyDockerImageService(DockerImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    @Override
    public DockerImage updateIfExistsAndAddIfNot(DockerImage image) {
        Objects.requireNonNull(image);
        DockerImageBasic current = imageRepository.getDockerImageByRepositoryAndName(image.getRepository(), image.getName());
        if (current == null) {
            // add if not exists
            image.setCreated(new Date());
            return imageRepository.save(image);
        }
        DockerImage newerImage = ObjectCopyingUtils.copyProjectionToEntity(
                current, new DockerImage()
        );
        newerImage.setRepository(ObjectCopyingUtils.copyProjectionToEntity(
                current.getRepository(), new DockerRepository()
        ));
        // update if exists
        newerImage.setSize(image.getSize());
        newerImage.setSha256(image.getSha256());
        return imageRepository.save(newerImage);
    }

    @Override
    public Page<DockerImageBasic> getImagesFromRepository(DockerImageIdentifier identifier, int page) {
        Objects.requireNonNull(identifier);
        return imageRepository.findAllByRepositoryNamespaceNameAndRepositoryName(
                Objects.requireNonNull(identifier.getNamespace()),
                Objects.requireNonNull(identifier.getRepository()),
                PageRequest.of(page, 10)
        );
    }
}
