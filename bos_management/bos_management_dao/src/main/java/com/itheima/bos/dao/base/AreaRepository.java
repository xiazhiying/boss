package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Area;

/**
 * ClassName:AreaRepository <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午8:25:14 <br/>
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query("FROM Area where province like ?1 or city like ?1 or district"
            + " like ?1 or postcode like ?1 or citycode like ?1 or shortcode like ?1")
    List<Area> findQ(String q);

}
