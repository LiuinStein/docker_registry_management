package cn.shaoqunliu.c.hub.mgr.po;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "docker_repository", indexes = {
        @Index(name = "idx_rep_nid", columnList = "nid"),
        @Index(name = "unique_repository_nid_name", columnList = "nid, name", unique = true)
})
public class DockerRepositoryDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // set cannot insert with this entity
    @Column(name = "id", updatable = false, insertable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "nid", referencedColumnName = "id", updatable = false, insertable = false)
    private DockerNamespace namespace;

    @Column(name = "name", updatable = false, insertable = false)
    private String name;

    private Boolean opened;

    @Column(name = "stars", updatable = false)
    private Long stars;

    @Size(max = 150)
    private String description;

    @Size(max = 16777215)
    @JsonAlias("full_description")
    private String fullDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
