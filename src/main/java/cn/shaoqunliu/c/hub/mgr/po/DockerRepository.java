package cn.shaoqunliu.c.hub.mgr.po;

import javax.persistence.*;

@Entity
@Table(name = "docker_repository", indexes = {
        @Index(name = "idx_rep_nid", columnList = "nid"),
        @Index(name = "unique_repository_nid_name", columnList = "nid, name", unique = true)
})
public class DockerRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "nid", referencedColumnName = "id")
    private DockerNamespace namespace;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private DockerUser owner;

    private Boolean opened;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DockerNamespace getNamespace() {
        return namespace;
    }

    public void setNamespace(DockerNamespace namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DockerUser getOwner() {
        return owner;
    }

    public void setOwner(DockerUser owner) {
        this.owner = owner;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
}
