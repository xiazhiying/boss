package com.itheima.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.take_delivery.WayBill;

/**  
 * ClassName:WaybillService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:20:23 <br/>       
 */
public interface WaybillService {

    void save(WayBill model);

    Page<WayBill> findAll(Pageable pageable);

    WayBill findByWayBillNum(String wayBillNum);

    

}
  
