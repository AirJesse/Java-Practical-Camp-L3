package org.bank.domain.service;


import org.bank.domain.entity.SysDept;
import org.bank.domain.entity.SysUser;
import org.bank.domain.vo.req.DeptAddReqVO;
import org.bank.domain.vo.req.DeptPageReqVO;
import org.bank.domain.vo.req.DeptUpdateReqVO;
import org.bank.domain.vo.req.UserPageUserByDeptReqVO;
import org.bank.domain.vo.resp.DeptRespNodeVO;
import org.bank.domain.vo.resp.PageVO;

import java.util.List;


public interface DeptService {

    SysDept addDept(DeptAddReqVO vo);

    void updateDept(DeptUpdateReqVO vo);

    SysDept detailInfo(String id);

    void deleted(String id);

    void deleted(List<String> ids);

    PageVO<SysDept> pageInfo(DeptPageReqVO vo);

    PageVO<DeptRespNodeVO> pageInfoForVue(DeptPageReqVO vo);

    List<DeptRespNodeVO> deptTreeList(String deptId);

    List<DeptRespNodeVO> getALLdeptTreeList(String filterDeptId);

    PageVO<SysUser> pageDeptUserInfo(UserPageUserByDeptReqVO vo);

    List<SysDept> selectAll();
}
