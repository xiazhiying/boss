package com.itheima.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;

/**  
 * ClassName:SubAreaServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午3:23:40 <br/>       
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository repository ;
    @Autowired
    private FixedAreaRepository fixedAreaRepository ;

    @Override
    public void save(SubArea model) {

        repository.save(model);
        
    }

    @Override
    public Page<SubArea> findAll(Pageable pageable) {
          
        return repository.findAll(pageable) ;
    }

    @Override
    public List<SubArea> findSubAreaUnAssociated() {
          
         
        return repository.findByFixedAreaIsNull() ;
    }

    @Override
    public List<SubArea> findSubAreaAssociated(Long id) {
        FixedArea fixedArea = new FixedArea();
        //根据id查询
        if (id != null) {
            fixedArea = fixedAreaRepository.findOne(id);
        }
        return repository.findByFixedArea(fixedArea) ;
    }

    @Override
    public void assignSubArea2FixedArea(Long fixedAreaId, Long[] subAreaIds) {
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);;
         //1.1根据fixedAreaId查出List<SubArea>
        List<SubArea> list = repository.findByFixedArea(fixedArea);
        //1.2解绑
        if (list.size() > 0) {
            for (SubArea subArea : list) {
                subArea.setFixedArea(null);
            }
        }
        //2.1根据subAreaIds查出查出List<SubArea>
        if (subAreaIds != null && subAreaIds.length >0) {
            for (Long id : subAreaIds) {
                SubArea subArea = repository.findOne(id);
                if (subArea != null) {
                    subArea.setFixedArea(fixedArea);
                }
            }
        }

    }

  
}
  
