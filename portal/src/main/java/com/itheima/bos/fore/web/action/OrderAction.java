package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:OrderAction <br/>
 * Function: <br/>
 * Date: 2018年3月23日 上午10:06:25 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class OrderAction extends ActionSupport implements ModelDriven<Order> {

    private Order model;

    @Override
    public Order getModel() {
        if (model == null) {
            model = new Order();
        }
        return model;
    }

    /**
     * orderAction_add.action 储存订单
     */
    private String recAreaInfo ;
    private String sendAreaInfo ;
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    @Action(value = "orderAction_add",
            results = {@Result(name = "success", location = "/index.html", type = "redirect")})
    public String add() {
        //收件人信息
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            String[] split = recAreaInfo.split("/");
            Area recArea = new Area();
            recArea.setProvince(split[0].substring(0, split[0].length()-1));
            recArea.setCity(split[1].substring(0, split[1].length()-1));
            recArea.setDistrict(split[2].substring(0, split[2].length()-1));
            model.setRecArea(recArea);
        }
        //寄件人信息
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
            String[] split = sendAreaInfo.split("/");
            Area sendArea = new Area();
            sendArea.setProvince(split[0].substring(0, split[0].length()-1));
            sendArea.setCity(split[1].substring(0, split[1].length()-1));
            sendArea.setDistrict(split[2].substring(0, split[2].length()-1));
            model.setSendArea(sendArea);
        }
        //调用后台系统
        WebClient
            .create("http://localhost:8080/bos_management_web/webService/orderService/orderSave")
            .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .post(model);
        
        return SUCCESS;
    }

}
