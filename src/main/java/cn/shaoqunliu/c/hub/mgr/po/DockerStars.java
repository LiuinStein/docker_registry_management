package cn.shaoqunliu.c.hub.mgr.po;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "mgr_star_user")
public class DockerStars {

    @EmbeddedId
    private Pk pk;

    public Pk getPk() {
        return pk;
    }

    public void setPk(Pk pk) {
        this.pk = Objects.requireNonNull(pk);
    }

    public void setPk(Integer uid, Integer rid) {
        this.pk = new Pk();
        pk.setUid(Objects.requireNonNull(uid));
        pk.setRid(Objects.requireNonNull(rid));
    }

    @Embeddable
    public static final class Pk implements Serializable {

        private Integer uid;
        private Integer rid;

        public Pk() {
            uid = null;
            rid = null;
        }

        public Pk(Integer uid, Integer rid) {
            this.uid = uid;
            this.rid = rid;
        }

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Integer getRid() {
            return rid;
        }

        public void setRid(Integer rid) {
            this.rid = rid;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pk &&
                    ((Pk) obj).getUid().equals(this.getUid()) &&
                    ((Pk) obj).getRid().equals(this.getRid());
        }
    }

}
