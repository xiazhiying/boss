package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月19日 下午4:31:23 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class TakeTimeAction extends CommonAction<TakeTime> {

    public TakeTimeAction() {
          
        super(TakeTime.class);          
    }
    @Autowired
    private TakeTimeService service;

    /**  
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
     * @since JDK 1.6  
     */
    private static final long serialVersionUID = 1L;
    /**
     * takeTimeAction_findAvalible
     */

    @Action("takeTimeAction_findAll")
    public String findAll() throws IOException{
        List<TakeTime> list = service.findAll();
        JsonConfig jsonConfig = new JsonConfig();
        // 指定在生成json数据的时候要忽略的字段
        jsonConfig.setExcludes(new String[] { "fixedAreas", "takeTime" });
        list2json(list, jsonConfig);
        return NONE ;
    }
}
  
