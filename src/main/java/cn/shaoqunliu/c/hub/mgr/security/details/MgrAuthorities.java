package cn.shaoqunliu.c.hub.mgr.security.details;

import java.util.List;

public class MgrAuthorities {

    private Ownership ownership;
    private List<String> readOnly;
    private List<String> writable;

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public List<String> getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(List<String> readOnly) {
        this.readOnly = readOnly;
    }

    public List<String> getWritable() {
        return writable;
    }

    public void setWritable(List<String> writable) {
        this.writable = writable;
    }

    public void addReadOnly(String repository) {
        if (!readOnly.contains(repository)) {
            readOnly.add(repository);
        }
    }

    public void addWritable(String repository) {
        if (!writable.contains(repository)) {
            writable.add(repository);
        }
    }
}
