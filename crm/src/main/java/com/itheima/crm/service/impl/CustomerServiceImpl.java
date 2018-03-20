package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:08:51 <br/>       
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository ;
    @Override
    public List<Customer> findAll() {
          
        return repository.findAll();
    }
    @Override
    public List<Customer> findCustomersUnAssociated() {
          
        return repository.findByFixedAreaIdIsNull();
    }
    @Override
    public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
          
        return repository.findByFixedAreaId(fixedAreaId) ;
    }
    @Override
    public void assignCustomers2FixedArea(String fixedAreaId, Long[] ids) {
          
        //先根据  fixedAreaId 进行解绑
        if (StringUtils.isNotEmpty(fixedAreaId)) {
            repository.unbindByFixedAreaId(fixedAreaId);
          //根据id在去关联客户
            if (ids != null && ids.length > 0) {
                for (Long id : ids) {
                    repository.bindFixedAreaById(fixedAreaId, id);
                }
            }
        }   
    }

}
  
