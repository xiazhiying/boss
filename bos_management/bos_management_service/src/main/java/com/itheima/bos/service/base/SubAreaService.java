package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午3:23:20 <br/>       
 */
public interface SubAreaService {

    void save(SubArea model);

    Page<SubArea> findAll(Pageable pageable);

    List<SubArea> findSubAreaUnAssociated();

    List<SubArea> findSubAreaAssociated(Long id);


    void assignSubArea2FixedArea(Long fixedAreaId, Long[] subAreaIds);

}
  
