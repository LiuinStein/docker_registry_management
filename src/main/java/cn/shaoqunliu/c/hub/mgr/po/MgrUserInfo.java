package cn.shaoqunliu.c.hub.mgr.po;

import javax.persistence.*;

@Entity
@Table(name = "mgr_userinfo")
public class MgrUserInfo {

    @Id
    // associated with MgrUser.id, not a generated value
    private Integer id;

    @Column(insertable = false)
    private String phone;

    @Column(insertable = false)
    private String realName;

    @Column(insertable = false)
    private String location;

    @Column(insertable = false)
    private String website;

    private String gravatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGravatar() {
        return gravatar;
    }

    public void setGravatar(String gravatar) {
        this.gravatar = gravatar;
    }
}
