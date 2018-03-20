package com.itheima.bos.service.base.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;

/**  
 * ClassName:FixedAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午8:36:45 <br/>       
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository repository;
    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private TakeTimeRepository takeTimeRepository ;
    @Override
    public void save(FixedArea model) {
          
        repository.save(model); 
        
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
          
        return repository.findAll(pageable);
    }

    @Override
    public void associationCourierToFixedArea(Long id, Long courierId, Long takeTimeId) {
         //根据快递员id查询出快递员    
        if (id != null && courierId != null && takeTimeId != null) {
            Courier courier = courierRepository.findOne(courierId);
            //根据takeTimeId查询
            TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
            //根据定区id查询
            FixedArea fixedArea = repository.findOne(id);
            //进行关联
            if (courier != null && takeTime != null) {
                courier.setTakeTime(takeTime);
            }
            if (courier != null && fixedArea != null) {
                fixedArea.getCouriers().add(courier);
            }
        }
        
        
    }
}
  
