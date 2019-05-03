package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerImageRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.service.DockerImageService;
import org.springframework.beans.factory.annotation.Autowired;
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
        DockerImage current = imageRepository.getDockerImageByRepositoryAndName(image.getRepository(), image.getName());
        if (current == null) {
            // add if not exists
            image.setCreated(new Date());
            return imageRepository.save(image);
        }
        // update if exists
        current.setSize(image.getSize());
        current.setSha256(image.getSha256());
        return imageRepository.save(current);
    }
}
