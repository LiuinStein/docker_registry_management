package cn.shaoqunliu.c.hub.mgr.po;

import javax.persistence.*;

@Entity
@Table(name = "cli_permission", indexes = {
        @Index(name = "unique_permission_uid_rid", columnList = "uid, rid", unique = true)
})
public class DockerPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "id")
    private DockerUser user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rid", referencedColumnName = "id")
    private DockerRepository repository;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DockerUser getUser() {
        return user;
    }

    public void setUser(DockerUser user) {
        this.user = user;
    }

    public DockerRepository getRepository() {
        return repository;
    }

    public void setRepository(DockerRepository repository) {
        this.repository = repository;
    }
}
