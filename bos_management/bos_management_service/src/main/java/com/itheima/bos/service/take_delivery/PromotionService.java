package com.itheima.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Promotion;

/**  
 * ClassName:PromotionService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午2:44:08 <br/>       
 */
public interface PromotionService {

    void save(Promotion promotion);

    Page<Promotion> findAll(Pageable pageable);

}
  
