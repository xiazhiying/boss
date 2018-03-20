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
    @Modifying
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    void unbindByFixedAreaId(String fixedAreaId);
    @Modifying
    @Query("update Customer set fixedAreaId = ? where id = ?")
    void bindFixedAreaById(String fixedAreaId, Long id);
    
}
  
