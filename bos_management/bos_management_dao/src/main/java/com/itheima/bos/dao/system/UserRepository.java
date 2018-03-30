package com.itheima.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午5:17:01 <br/>       
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
  
