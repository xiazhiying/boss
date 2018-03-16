package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;

/**  
 * ClassName:AreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午8:22:57 <br/>       
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository repository ;

    @Override
    public void save(List<Area> list) {

        repository.save(list);
    }

    @Override
    public Page<Area> findAll(Pageable pageable) {
          
        
        return repository.findAll(pageable);
    }

    @Override
    public List<Area> findByQ(String q) {
          
        q = "%"+q.toUpperCase()+"%" ;
        return repository.findQ(q);
    }
}
  
