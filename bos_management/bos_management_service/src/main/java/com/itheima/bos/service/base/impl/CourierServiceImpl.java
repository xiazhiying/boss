package com.itheima.bos.service.base.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;

/**  
 * ClassName:CourierServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午1:28:29 <br/>       
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository repository ;
    @Override
    public void save(Courier model) {
          
         repository.save(model); 
        
    }
    @RequiresPermissions("courier:delete")
    @Override
    public void batchDel(String ids) {
          
          if (StringUtils.isNotEmpty(ids)) {
           String[] split = ids.split(",");
           for (String sid : split) {
            long id = Long.parseLong(sid);
            Courier courier = repository.findOne(id);
            if (courier != null) {
                courier.setDeltag('1');  
            }
           
        }
        }
        
    }
    @Override
    public Page<Courier> findll(Specification<Courier> specification, Pageable pageable) {
          
        return repository.findAll(specification, pageable) ; 
    }

    @Override
    public List<Courier> findAvalible() {
          
        return repository.findByDeltagIsNull() ;
    }
    

}
  
