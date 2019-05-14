package cn.shaoqunliu.c.hub.mgr.po;

import cn.shaoqunliu.c.hub.mgr.controller.MgrUserController;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "docker_auth", indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "email", unique = true)
})
public class MgrUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 60)
    private String username;

    @Email
    @NotNull
    @Size(max = 60)
    private String email;

    @NotNull
    @Size(min = 6, max = 32, groups = MgrUserController.class)
    private String mpassword;

    @NotNull
    @Size(min = 6, max = 32, groups = MgrUserController.class)
    private String cpassword;

    private Boolean enabled;
    private Integer role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMpassword() {
        return mpassword;
    }

    public void setMpassword(String mpassword) {
        this.mpassword = mpassword;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
