package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.MgrUser;
import cn.shaoqunliu.c.hub.mgr.po.MgrUserInfo;

public interface MgrUserService {

    Integer signUp(MgrUser user);

    Integer changeMasterPassword(int uid, String raw, String newer);

    Integer changeDockerClientPassword(int uid, String raw, String newer);

    Integer updateUserInfo(MgrUserInfo info);

    MgrUserInfo getUserInfoByUsername(String username);
}
