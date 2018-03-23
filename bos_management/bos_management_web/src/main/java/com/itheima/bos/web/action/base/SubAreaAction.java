package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.Iterator;
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

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:SubAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月16日 下午3:25:57 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class SubAreaAction extends CommonAction<SubArea> {

    public SubAreaAction() {

        super(SubArea.class);

    }

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.6
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private SubAreaService service;

    @Action(value = "subareaAction_save", results = {
            @Result(name = "success", type = "redirect", location = "/pages/base/sub_area.html")})
    /**
     * 添加分区 save:. <br/>
     * 
     * @return
     */
    public String save() {
        service.save(model);
        return SUCCESS;
    }

    /**
     * 分页查询 subareaAction_pageQuery
     * 
     * @throws IOException
     */

    @Action("subareaAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);

        Page<SubArea> page = service.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedArea" ,"subareas"});
        page2json(page, jsonConfig );
        return NONE;
    }
    /**
     * subareaAction_findSubAreaUnAssociated
     *  
     * @return
     * @throws IOException
     */
    @Action("subareaAction_findSubAreaUnAssociated")
    public String findSubAreaUnAssociated() throws IOException {
      List<SubArea> list = service.findSubAreaUnAssociated();
      JsonConfig jsonConfig = new JsonConfig();
      jsonConfig.setExcludes(new String[]{"fixedArea" ,"subareas"});
      list2json(list, jsonConfig);
        return NONE;
    }
    /**
     * 
     */
    @Action("subareaAction_findSubAreaAssociated")
    public String findSubAreaAssociated() throws IOException {
        List<SubArea> list = service.findSubAreaAssociated(model.getId());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedArea"});
        list2json(list, jsonConfig);
        return NONE;
    }
    /**
     * subareaAction_assignSubArea2FixedArea
     */
    private Long[] subAreaIds ;
    public void setSubAreaIds(Long[] subAreaIds) {
        this.subAreaIds = subAreaIds;
    }
    @Action(value = "subareaAction_assignSubArea2FixedArea", results = {
            @Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html")})
    public String assignSubArea2FixedArea() throws IOException {
        service.assignSubArea2FixedArea(model.getId(),subAreaIds);
        return SUCCESS;
    }
}
