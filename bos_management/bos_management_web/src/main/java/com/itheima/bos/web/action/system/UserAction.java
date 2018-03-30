package com.itheima.bos.web.action.system;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.UserService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:UserAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午4:22:28 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class UserAction extends CommonAction<User> {

    public UserAction() {
        super(User.class);  
    }
    /**
     * 登陆认证
     * login:. <br/>  
     *  
     * @return
     */
    private String checkCode ;
    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
    @Action(value = "userAction_login", results = {
            @Result(name = "success", location = "/index.html", type = "redirect"),
            @Result(name = "login", location = "/login.html", type = "redirect") })
    public String login(){
        //1.获取验证码
        String serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");

        if (StringUtils.isNotEmpty(serverCode) && serverCode.equalsIgnoreCase(checkCode)) {
         // 从框架中获取Subject,代表当前用户
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
            try {
                // 执行登录
                // 参数就传递给Realm中doGetAuthenticationInfo方法的参数
                subject.login(token);
                // 该方法的返回值是由Realm中创建SimpleAuthenticationInfo对象时,传入的第一个参数决定的
                User user = (User) subject.getPrincipal();
                // 把用户存入session
                ServletActionContext.getRequest().getSession().setAttribute("user", user);

                return SUCCESS;
            } catch (UnknownAccountException e) {
                System.out.println("账户不存在");
                e.printStackTrace();
            } catch (IncorrectCredentialsException e) {
                System.out.println("密码错误");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("其他异常:" + e.getMessage());
            }
        }
        return LOGIN;
    }
    /**
     * userAction_logout
     * 退出登陆
     */
    @Action(value = "userAction_logout" , results={@Result(name="success",location="/login.html",type="redirect")})
    public String logout(){
        //注销
        SecurityUtils.getSubject().logout();
        //清除session中保存的用户名和密码
        ServletActionContext.getRequest().getSession().removeAttribute("user");
        return SUCCESS ;
    }
    /**
     * userAction_save
     * 退出登陆
     */
    @Autowired
    private UserService userService ;
    private Long[]  roleIds ;
    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
    @Action(value = "userAction_save" , results={@Result(name="success",location="/pages/system/userlist.html",type="redirect")})
    public String save(){
        userService.save(model ,roleIds );
        return SUCCESS ;
    }
    /**
     * 
     *  userAction_pageQuery
     * @return
     * @throws IOException 
     */
    @Action(value = "userAction_pageQuery" )
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page - 1, rows);
        // 调用业务层进行查询
        Page<User> page = userService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] { "roles" });

        page2json(page, jsonConfig);
        return NONE;

    }

    

}
  
