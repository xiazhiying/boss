package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午1:28:06 <br/>       
 */
public interface CourierService {

    void save(Courier model);

    Page<Courier> findll(Specification<Courier> specification, Pageable pageable);

    void batchDel(String ids);

    List<Courier> findAvalible();

}
  
