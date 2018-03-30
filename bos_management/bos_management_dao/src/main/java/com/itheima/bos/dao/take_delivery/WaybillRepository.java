package com.itheima.bos.dao.take_delivery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.take_delivery.WayBill;

/**  
 * ClassName:WaybillRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:18:54 <br/>       
 */
public interface WaybillRepository extends JpaRepository<WayBill, Long> {

    WayBill findByWayBillNum(String wayBillNum);

}
  
