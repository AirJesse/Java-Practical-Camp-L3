package org.bank.domain.service;




import org.bank.domain.entity.SysRole;
import org.bank.domain.vo.req.RoleAddReqVO;
import org.bank.domain.vo.req.RolePageReqVO;
import org.bank.domain.vo.req.RoleUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;

import java.util.List;

public interface RoleService {

    SysRole addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo, String accessToken);

    SysRole detailInfo(String id);

    void deletedRole(String id);

    PageVO<SysRole> pageInfo(RolePageReqVO vo);

    List<SysRole> getRoleInfoByUserId(String userId);

    List<String> getRoleNames(String userId);

    List<SysRole> selectAllRoles();
}
