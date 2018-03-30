package com.itheima.bos.web.action.system;


import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;



/**  
 * ClassName:MenuAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午5:15:05 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class MenuAction extends CommonAction<Menu> {

    public MenuAction() {
        super(Menu.class);          
    }
    @Autowired
    private MenuService menuService ;
    /**
     * menuAction_findLevelOne
     *  查询所有的一级菜单,二级菜单是通过一级菜单的children获取出来的
     * @throws IOException 
     */
   @Action(value = "menuAction_findLevelOne")
   public String findLevelOne() throws IOException{
       List<Menu> list = menuService.findLevelOne();
       JsonConfig jsonConfig = new JsonConfig();
       jsonConfig.setExcludes(new String[] { "roles", "childrenMenus", "parentMenu" });
       list2json(list, jsonConfig);
       return NONE ;
   }
   /**
    * menuAction_save
    * 保存新增菜单
    * @return
    * @throws IOException
    */
   @Action(value = "menuAction_save", results = {
           @Result(name = "success", location = "/pages/system/menu.html", type = "redirect") })
   public String save(){
       menuService.save(model);
       return SUCCESS ;
   }
   /**
    * menuAction_pageQuery
    */

   @Action(value = "menuAction_pageQuery")
   public String pageQuery() throws IOException{
       Pageable pageable = new PageRequest(Integer.parseInt(model.getPage())-1 , rows); 
       Page<Menu> page = menuService.findAll(pageable);
       JsonConfig jsonConfig = new JsonConfig();
       jsonConfig.setExcludes(new String[] { "roles", "childrenMenus", "parentMenu" });
       page2json(page, jsonConfig);
       return NONE ;
   }
   /**
    * 根据用户权限获取menu
    * menuAction_findbyUser
 * @throws IOException 
    */
   @Action("menuAction_findbyUser")
   public String findbyUser() throws IOException{
       // 获取当前用户
       Subject subject = SecurityUtils.getSubject();
       User user = (User) subject.getPrincipal();
       List<Menu> list = menuService.findbyUser(user);
       JsonConfig jsonConfig = new JsonConfig();
       jsonConfig.setExcludes(
               new String[] {"roles", "childrenMenus", "parentMenu" ,"children"});

       list2json(list, jsonConfig);
       return NONE ;
   }
}
  
