package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

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
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:PermissionAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午8:11:46 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class PermissionAction extends CommonAction<Permission> {

    public PermissionAction() {
        super(Permission.class);
    }

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询 permissionAction_pageQuery
     */

    @Action(value = "permissionAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Permission> page = permissionService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        page2json(page, jsonConfig);
        return NONE;
    }
    /**
     * permissionAction_save
     * 保存权限
     * @return
     * @throws IOException
     */
    @Action(value = "permissionAction_save", results = {
            @Result(name = "success", location = "/pages/system/permission.html", type = "redirect") })
    public String save(){
        permissionService.save(model);
        return SUCCESS ;
    }
    /**
     * permissionAction_findAll
     * 查询所有权限
     * @throws IOException 
     */
    @Action("permissionAction_findAll")
    public String findAll() throws IOException{
        Page<Permission> page = permissionService.findAll(null);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        list2json(page.getContent(), jsonConfig);
        return NONE ;
    }

}
