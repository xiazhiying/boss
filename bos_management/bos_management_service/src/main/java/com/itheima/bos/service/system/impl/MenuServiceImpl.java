package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService;

/**  
 * ClassName:MenuServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午5:14:02 <br/>       
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository ;
    @Override
    public List<Menu> findLevelOne() {
          
        return menuRepository.findByParentMenuIsNull();
    }
    @Override
    public void save(Menu model) {
        Menu parentMenu = model.getParentMenu();
        if (parentMenu.getId() == null) {
            model.setParentMenu(null);
        }
        menuRepository.save(model);
    }
    @Override
    public Page<Menu> findAll(Pageable pageable) {
          
        return menuRepository.findAll(pageable);
    }
    @Override
    public List<Menu> findbyUser(User user) {
       // admin用户
        String username = user.getUsername();
        if("admin".equals(username)){
            return menuRepository.findAll();
        }
        return menuRepository.findbyUser(user.getId());
    }

}
  
