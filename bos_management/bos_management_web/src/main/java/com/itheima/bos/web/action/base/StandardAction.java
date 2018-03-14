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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**  
 * ClassName:StandardAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午5:36:40 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard ;
    @Override
    public Standard getModel() {
        if (standard == null) {
            standard = new Standard();
        }  
          
        return standard;
    }
    @Autowired
    private StandardService standardService ;
    //储存
    @Action(value = "standardAction_save" ,
            results={@Result(name="success",type = "redirect",location="/pages/base/standard.html")})
    public String save(){
        standardService.save(standard);
        return SUCCESS ;
    }
    //分页查询
    private int page ;//当前是第几页
    private int rows ;//每页显示的行数
    @Action(value = "standardAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> page = standardService.findAll(pageable);
        // 获取总数据条数
        long totalElements = page.getTotalElements();
       // 获取当前页面要显示的数据
        List<Standard> list = page.getContent();
        // 由于目标页面需要的数据并不是Page对象,所以需要手动封装JSON数据
        Map<String, Object> map = new HashMap<>();
        map.put("total", totalElements);
        map.put("rows", list);
        // 生成JSON数据
        String json = JSONObject.fromObject(map).toString();
        ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
        // 写出内容
        ServletActionContext.getResponse().getWriter().write(json);
        return NONE ;
    }
    //查询所有派件
    @Action("standardAction_findAll")
    public String findAll() throws IOException{
          Page<Standard> page = standardService.findAll(null);
        String json = JSONArray.fromObject(page.getContent()).toString();
        ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
        // 写出内容
        ServletActionContext.getResponse().getWriter().write(json);
       
        return NONE;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

}
  
