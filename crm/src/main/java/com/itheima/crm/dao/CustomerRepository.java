package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:17:47 <br/>       
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFixedAreaIdIsNull();
    List<Customer> findByFixedAreaId(String fixedAreaId);
   
    Customer findByTelephone(String telephon);
    @Modifying
    @Query("update Customer set type = 1 where telephone = ?")
    void active(String telephone);
    @Query("select fixedAreaId from Customer where address = ?")
    String findFixedAreaIdByAdddress(String address);
    
    
}
  
