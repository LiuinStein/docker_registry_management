package cn.shaoqunliu.c.hub.mgr.po;

import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mgr_userinfo")
public class MgrUserInfo {

    @Id
    // associated with MgrUser.id, not a generated value
    private Integer id;

    @Size(max = 20)
    @Column(insertable = false)
    @Pattern(regexp = ParameterValidationConstraints.phone)
    private String phone;

    @Size(max = 40)
    @Column(insertable = false)
    @JsonAlias(value = "real_name")
    private String realName;

    @Size(max = 50)
    @Column(insertable = false)
    private String location;

    @Size(max = 100)
    @Column(insertable = false)
    private String website;

    @Email
    @Size(max = 60)
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
