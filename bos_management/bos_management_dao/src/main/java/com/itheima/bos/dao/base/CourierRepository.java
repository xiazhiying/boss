package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.bos.domain.base.Courier;



/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午1:31:23 <br/>       
 */
public interface CourierRepository extends JpaRepository<Courier, Long> , JpaSpecificationExecutor<Courier>{

}
  
