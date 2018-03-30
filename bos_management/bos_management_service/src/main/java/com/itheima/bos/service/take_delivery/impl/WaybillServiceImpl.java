package com.itheima.bos.service.take_delivery.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WaybillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WaybillService;

/**  
 * ClassName:WaybillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:20:47 <br/>       
 */
@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {

    @Autowired
    private WaybillRepository waybillRepository ;

    @Override
    public void save(WayBill model) {
        String wayBillNum = model.getWayBillNum();
        if (wayBillNum == null) {
            wayBillNum = UUID.randomUUID().toString().replaceAll("-", "");
            model.setWayBillNum(wayBillNum);
        }else {
            WayBill wayBill = waybillRepository.findByWayBillNum(wayBillNum);
            if (wayBill != null) {
                model.setId(wayBill.getId());  
            }
        }
          waybillRepository.save(model);        
    }

    @Override
    public Page<WayBill> findAll(Pageable pageable) {
          
        return waybillRepository.findAll(pageable) ;
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
          
        return waybillRepository.findByWayBillNum(wayBillNum);
    }

   
}
  
