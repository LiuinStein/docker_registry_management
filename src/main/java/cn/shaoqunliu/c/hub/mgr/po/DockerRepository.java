package cn.shaoqunliu.c.hub.mgr.po;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
    @JoinColumn(name = "nid", referencedColumnName = "id", updatable = false)
    private DockerNamespace namespace;

    @Column(updatable = false)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private DockerUser owner;

    private Boolean opened;

    @Column(insertable = false)
    private Long stars;

    @Size(max = 150)
    @Column(insertable = false)
    private String description;

    @Size(max = 16777215)
    @JsonAlias("full_description")
    @Column(insertable = false, length = 16777215)
    private String fullDescription;

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

    public Long getStars() {
        return stars;
    }

    public void setStars(Long stars) {
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }
}
