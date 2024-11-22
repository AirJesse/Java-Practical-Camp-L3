package org.bank.domain.service;


import org.bank.domain.entity.SysPermission;
import org.bank.domain.vo.req.PermissionAddReqVO;
import org.bank.domain.vo.req.PermissionPageReqVO;
import org.bank.domain.vo.req.PermissionUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.PermissionRespNode;

import java.util.List;
import java.util.Set;


public interface PermissionService {

    List<SysPermission> getPermission(String userId);

    SysPermission addPermission(PermissionAddReqVO vo);

    SysPermission detailInfo(String permissionId);

    void updatePermission(PermissionUpdateReqVO vo);

    void deleted(String permissionId);

    void deleted(List<String> permissionIds);

    PageVO<SysPermission> pageInfo(PermissionPageReqVO vo);

    PageVO<PermissionRespNode> pageInfoForVue(PermissionPageReqVO vo);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(String userId);

    List<String> getChildrenIds(String permId);

    List<PermissionRespNode> permissionTreeList(String userId);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode>  getALLmenuTreeList(String filterMenuId);

    List<PermissionRespNode> selectAllMenuByTree(String permissionId);

}
