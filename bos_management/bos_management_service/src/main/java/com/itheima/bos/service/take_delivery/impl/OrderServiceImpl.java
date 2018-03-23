package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkbillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;

/**  
 * ClassName:OrderServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月22日 下午4:12:07 <br/>       
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository ;
    @Autowired
    private AreaRepository areaRepository ;
    @Autowired
    private FixedAreaRepository fixedAreaRepository ;
    @Autowired
    private WorkbillRepository workbillRepository ;
    @Override
    public void orderSave(Order order) {
        //根据order里面的 寄(收)件人省市区信息查出Area对象
        Area sendArea = order.getSendArea();
        if (sendArea != null) {
            Area sArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(), sendArea.getDistrict());
            order.setSendArea(sArea);
        }
        Area recArea = order.getRecArea();
        if (recArea != null) {
            Area cArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
            order.setRecArea(cArea);
        }
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderTime(new Date());
        orderRepository.save(order);
        //根据详细地址自动分单
        String sendAddress = order.getSendAddress();
        if (StringUtils.isNotEmpty(sendAddress)) {
           //1.根据详细地址客户表中自动匹配.调用crm系统
            String fixedAreaId = WebClient
                .create("http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAddress")
                .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .query("address", sendAddress)
                .get(String.class);
            if (StringUtils.isNotEmpty(fixedAreaId)) {
                
                FixedArea fixedArea = fixedAreaRepository.findOne(Long.parseLong(fixedAreaId));
                if (fixedArea != null) {
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (!couriers.isEmpty()) {
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        //指派快递员
                        order.setCourier(courier);
                        //生成工单
                        WorkBill workBill = new WorkBill();
                        workBill.setAttachbilltimes(0);//追单次数
                        workBill.setBuildtime(new Date());// 工单生成时间
                        workBill.setCourier(courier);
                        workBill.setOrder(order);
                        workBill.setPickstate("新单");// 取件状态
                        workBill.setRemark(order.getRemark());// 订单备注
                        workBill.setSmsNumber("12345");//短信序号
                        workBill.setType("新");
                        workbillRepository.save(workBill);
                        return ;
                        
                    }
                }
                
            }else {
                //进行模糊匹配,分区, sendAddress
                //获得定区
                Area sendArea2 = order.getSendArea();
                if (sendArea2 != null) {
                    Set<SubArea> subareas = sendArea2.getSubareas();
                    if (!subareas.isEmpty()) {
                        for (SubArea subArea : subareas) {
                            String keyWords = subArea.getKeyWords();
                            String assistKeyWords = subArea.getAssistKeyWords();
                            if (sendAddress.contains(keyWords) 
                                    || sendAddress.contains(assistKeyWords)) {
                                FixedArea fixedArea = subArea.getFixedArea();
                                if (fixedArea != null) {
                                    Set<Courier> couriers = fixedArea.getCouriers();
                                    if (!couriers.isEmpty()) {
                                        Iterator<Courier> iterator = couriers.iterator();
                                        Courier courier = iterator.next();
                                        //指派快递员
                                        order.setCourier(courier);
                                        //生成工单
                                        WorkBill workBill = new WorkBill();
                                        workBill.setAttachbilltimes(0);//追单次数
                                        workBill.setBuildtime(new Date());// 工单生成时间
                                        workBill.setCourier(courier);
                                        workBill.setOrder(order);
                                        workBill.setPickstate("新单");// 取件状态
                                        workBill.setRemark(order.getRemark());// 订单备注
                                        workBill.setSmsNumber("12345");//短信序号
                                        workBill.setType("新");
                                        workbillRepository.save(workBill);
                                        order.setOrderType("自动分单");
                                        return ;
                                        
                                    }
                                }
                            }
                        }
                    }
                }
                
                
                order.setOrderType("人工分单");
            }
        }

    }

  

}
  
