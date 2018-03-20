package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
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

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.crm.domain.Customer;

import net.sf.json.JsonConfig;

/**
 * ClassName:FixedAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月16日 下午8:39:24 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class FixedAreaAction extends CommonAction<FixedArea> {

    public FixedAreaAction() {

        super(FixedArea.class);

    }

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.6
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private FixedAreaService service;

    /**
     * fixedAreaAction_save
     */

    @Action(value = "fixedAreaAction_save", results = {
            @Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html")})
    public String save() {
        service.save(model);
        return NONE;
    }

    /**
     * fixedAreaAction_pageQuery
     * 
     * @throws IOException
     */
    @Action("fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<FixedArea> page = service.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas", "couriers"});
        page2json(page, jsonConfig);
        return NONE;
    }

    /**
     * 通过crm查询未关联的客户 fixedAreaAction_findCustomersUnAssociated
     * 
     * @throws IOException
     */
    @Action("fixedAreaAction_findCustomersUnAssociated")
    public String findCustomersUnAssociated() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }

    @Action("fixedAreaAction_findCustomersAssociated2FixedArea")
    public String findCustomersAssociated2FixedArea() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .query("fixedAreaId", model.getId()).getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }

    /**
     * // 向CRM发起请求,关联客户到指定的定区 fixedAreaAction_assignCustomers2FixedArea
     */
    private Long[] customerIds;

    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }

    @Action(value = "fixedAreaAction_assignCustomers2FixedArea", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String assignCustomers2FixedArea() {
        if (customerIds == null) {
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/unbindByFixedAreaId")
                    .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .query("fixedAreaId", model.getId())
                    .put(null);
            return SUCCESS;
        }
        WebClient
                .create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .query("fixedAreaId", model.getId()).query("customerIds", customerIds).put(null);
        return SUCCESS;
    }
    /**
     * fixedAreaAction_associationCourierToFixedArea
     */

    private Long courierId ;
    private Long takeTimeId ;
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }
    @Action(value = "fixedAreaAction_associationCourierToFixedArea", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String associationCourierToFixedArea() {
        
        service.associationCourierToFixedArea(model.getId(),courierId,takeTimeId);
        System.out.println(model.getId()+"________________---------");
        return SUCCESS;
    }
}
