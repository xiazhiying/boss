package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午5:44:49 <br/>       
 */
public interface StandardService {

    void save(Standard standard);
    Page<Standard> findAll(Pageable pageable);

}
  
