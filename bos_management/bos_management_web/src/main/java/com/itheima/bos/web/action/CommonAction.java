package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Standard;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CommonAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午3:03:15 <br/>
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

    protected T model;
    private Class<T> clazz;

    // 通过有参构造获得
    public CommonAction(Class<T> clazz) {
        this.clazz = clazz;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public T getModel() {

        return model;
    }
    protected int page;
    protected int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    public void page2json(Page<T> page,JsonConfig jsonConfig) throws IOException{
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",page.getTotalElements() );
        map.put("rows", page.getContent());
        String json ;
        if (jsonConfig == null) {
            json = JSONObject.fromObject(map).toString();
        }else {
            json = JSONObject.fromObject(map, jsonConfig ).toString();
        }
        ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
        ServletActionContext.getResponse().getWriter().write(json);
    }
    public void list2json(List list,JsonConfig jsonConfig) throws IOException{
        String json ;
        if (jsonConfig == null) {
            json = JSONArray.fromObject(list).toString();
        }else {
            json = JSONArray.fromObject(list,jsonConfig).toString();
        }
        ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
        // 写出内容
        ServletActionContext.getResponse().getWriter().write(json);
    }


}
