package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ClassName:StandardAction <br/>
 * Function: <br/>
 * Date: 2018年3月12日 下午5:36:40 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class StandardAction extends CommonAction<Standard> {

    public StandardAction() {

        super(Standard.class);

    }

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.6
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private StandardService standardService;

    // 储存
    @Action(value = "standardAction_save", results = {
            @Result(name = "success", type = "redirect", location = "/pages/base/standard.html")})
    public String save() {
        standardService.save(model);
        return SUCCESS;
    }

    // 分页查询
  

    @Action(value = "standardAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> page = standardService.findAll(pageable);
        // 获取总数据条数
        page2json(page, null);
        return NONE;
    }

    // 查询所有派件
    @Action("standardAction_findAll")
    public String findAll() throws IOException {
        Page<Standard> page = standardService.findAll(null);
        
        list2json(page.getContent(),null);

        return NONE;
    }

   

}
