package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CourierAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午1:18:24 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends CommonAction<Courier> {

    public CourierAction() {
        super(Courier.class);
    }
    

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     * 
     * @since JDK 1.6
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private CourierService service;

    @Action(value = "courierAction_save", results = {
            @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
    // 储存快递员
    public String save() {
        
        service.save(model);
        return SUCCESS;
    }

    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // 多条件查询快递员
    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException {
        // 获取快递员编号
        final String courierNum = model.getCourierNum();
        // 获取收派标准,用户获取标准名字
        final Standard standard = model.getStandard();
        // 获取所属单位
        final String company = model.getCompany();
        // 获取类型
        final String type = model.getType();
        // 构造查询条件 Specification<T>
        Specification<Courier> specification = new Specification<Courier>() {

            /**
             * 用于构建where语句
             * 
             * @param root : 通常指实体类
             * @param query : 查询对象
             * @param cb : 构造查询条件的对象
             * @return 组合查询条件
             */
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                // 这个集合用来装载多个查询条件
                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(courierNum)) {
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                if (StringUtils.isNotEmpty(type)) {
                    Predicate p2 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p2);
                }
                if (StringUtils.isNotEmpty(company)) {
                    Predicate p3 = cb.like(root.get("company").as(String.class), company);
                    list.add(p3);
                }
                if (standard != null && StringUtils.isNotEmpty(standard.getName())) {
                    Join<Object, Object> join = root.join("standard");
                    Predicate p4 = cb.equal(join.get("name").as(String.class), standard.getName());
                    list.add(p4);
                }

                if (list.size() > 0) {
                    Predicate[] array = new Predicate[list.size()];
                    list.toArray(array);
                    return cb.and(array);
                }
                return null;
            }
        };
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Courier> page = service.findll(specification, pageable);
      
        //// 设置序列化时忽略的字段
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});
        page2json(page, jsonConfig);
        return NONE;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 作废快递员
    @Action(value = "courierAction_batchDel", results = {
            @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
    public String batchDel() {
        service.batchDel(ids);
        return SUCCESS;
    }
    //查询所有正常使用的快递员
    /**
     * ../../courierAction_findAvalible.action
     * @throws IOException 
     */
    @Action("courierAction_findAvalible")
    public String findAvalible() throws IOException{
        List<Courier> list = service.findAvalible();
        JsonConfig jsonConfig = new JsonConfig();
        // 指定在生成json数据的时候要忽略的字段
        jsonConfig.setExcludes(new String[] { "fixedAreas", "takeTime" });
        list2json(list, jsonConfig);
        return NONE ;
    }

    
}
