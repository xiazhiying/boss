package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午9:56:09 <br/>       
 */
public interface UserService {

    void save(User model, Long[] roleIds);

    Page<User> findAll(Pageable pageable);

}
  
