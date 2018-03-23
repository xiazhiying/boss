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
            List<Customer> customers = repository.findByFixedAreaId(fixedAreaId);
            System.out.println();
            if (customers != null) {
                for (Customer customer : customers) {
                    customer.setFixedAreaId(null);
                }
            }
          //根据id在去关联客户
            
                for (Long id : ids) {
                    if (id != 0) {
                        Customer customer = repository.findOne(id);
                        if (customer != null) {
                            customer.setFixedAreaId(fixedAreaId);
                        }
                    }
                }
            
        }   
    }
    @Override
    public void save(Customer customer) {
          
        repository.save(customer);
    }
    @Override
    public Customer checkTelephone(String telephone) {
        Customer customer = repository.findByTelephone(telephone);
        return customer ;
    }
    @Override
    public void active(String telephone) {
        Customer customer = repository.findByTelephone(telephone);
        if (customer != null) {
            customer.setType(1);
        }
    }
    @Override
    public String findFixedAreaIdByAddress(String address) {
          
        return repository.findFixedAreaIdByAdddress(address) ;
    }
   

}
  
