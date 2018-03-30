package com.itheima.bos.web.action.take_delivery;



import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**  
 * ClassName:WaybillAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:21:50 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class WaybillAction extends CommonAction<WayBill> {

    public WaybillAction() {
          
        super(WayBill.class);          
    }
    @Autowired
    private WaybillService waybillService ;
    /**
     * waybillAction_save
     * 保存订单
     * @throws IOException 
     */
    @Action("waybillAction_save")
    public String save() throws IOException{
        String msg = "1";
        try {
           
            waybillService.save(model);
            
        } catch (Exception e) {
              
            msg = "0" ;
            e.printStackTrace();  
            
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(msg);
        
        return NONE ;
    }
    /**
     * waybillAction_pageQuery
     * 分页查询
     * @throws IOException 
     */
    @Action("waybillAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page-1, rows);
        Page<WayBill> page = waybillService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"order","sendArea","recArea"});
        page2json(page, jsonConfig );
        return NONE ;
    }
    /**
     * waybillAction_findOne
     * 根据运单号查询运单
     * @throws IOException 
     */
    @Action("waybillAction_findByWayBillNum")
    public String findByWayBillNum() throws IOException{
        String wayBillNum = model.getWayBillNum().trim();
        WayBill wayBill = new WayBill();
        if (wayBillNum != null && wayBillNum.length() >10) {
            wayBill = waybillService.findByWayBillNum(wayBillNum);   
        }
        String json = JSONObject.fromObject(wayBill).toString();
        ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().write(json);
        return NONE ;
    }
    

}
  
