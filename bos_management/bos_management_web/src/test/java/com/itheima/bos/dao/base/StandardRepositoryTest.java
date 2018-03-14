package com.itheima.bos.dao.base;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午4:32:06 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {

    @Autowired
    private StandardRepository standardRepository ;
    @Test
    public void test01() {
        Standard standard = new Standard();
        standard.setName("张君宝");
        standard.setMaxWeight(1000);
        standardRepository.save(standard) ;
    }

}
  
