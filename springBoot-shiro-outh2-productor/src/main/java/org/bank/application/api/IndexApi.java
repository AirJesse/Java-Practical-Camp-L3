package org.bank.application.api;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.controller
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/index")
public class IndexApi {

    @GetMapping("/login")
    public String logout(){
        return "login";
    }
    /**
     * 进入首页
     */
    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request){
        return "home";
    }
    /**
     * 更改密码页面
     */
    @GetMapping("/users/password")
    public String updatePassword(){
        return "users/update_password";
    }
    /**
     * 用户编辑个人信息 视图
     */
    @GetMapping("/users/info")
    public String userDetail(Model model){
        model.addAttribute("flagType","edit");
        return "users/user_edit";
    }
    /**
     * 菜单权限列表  视图
     */
    @GetMapping("/menus")
    public String menusList(){

        return "menus/menu_list";
    }


    /**
     * 角色列表 操作视图
     */
    @GetMapping("/roles")
    public String roleList(){
        return "roles/role_list";
    }
    /**
     * 用户列表操作 视图
     * @Author:      小霍
     * @CreateDate:  2019/10/29 10:37
     * @UpdateUser:
     * @UpdateDate:  2019/10/29 10:37
     * @Version:     0.0.1
     * @param
     * @return       java.lang.String
     * @throws
     */
    @GetMapping("/users")
    public String userList(){
        return "users/user_list";
    }
    /**
     * 系统操作日志 视图
     */
    @GetMapping("/logs")
    public String logList(){
        return "logs/log_list";
    }


    /**
     * 系统操作日志 视图
     */
    @GetMapping("/client")
    public String clientList(){
        return "client/client_list";
    }


    /**
     * 组织机构列表 试图
     */
    @GetMapping("/depts")
    public String deptList(){
        return "depts/dept_list";
    }

    @GetMapping("/403")
    public String error403(){
        return "error/403";
    }
    @GetMapping("/404")
    public String error404(){
        return "error/404";
    }

    @GetMapping("/500")
    public String error405(){
        return "error/500";
    }

    @GetMapping("/main")
    public String indexHome(){
        return "main";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
