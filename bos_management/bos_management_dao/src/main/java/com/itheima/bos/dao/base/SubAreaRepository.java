package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:subareaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午3:20:41 <br/>       
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {

    List<SubArea> findByFixedAreaIsNull();
    //@Query("FROM SubArea WHERE fixedArea.id = ?")
    List<SubArea> findByFixedArea(FixedArea fixedArea);
   
    
    
}
  
