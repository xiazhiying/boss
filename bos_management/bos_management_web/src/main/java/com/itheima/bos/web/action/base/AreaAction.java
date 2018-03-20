package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.utils.PinYin4jUtils;
import com.itheima.bos.web.action.CommonAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:AreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午8:16:06 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommonAction<Area> {

  

    public AreaAction() {
        super(Area.class);          
    }
    /**  
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).  
     * @since JDK 1.6  
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private AreaService service;
    private File areaFile;

    public void setAreaFile(File areaFile) {
        this.areaFile = areaFile;
    }

    /**
     * 一键上传文件
     * importXLS:. <br/>  
     *  
     * @return
     * @throws IOException
     */
    @Action("areaAction_importXLS")
    public String importXLS() throws IOException {
        // 加载文件
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(areaFile));
        // 读取第一页的内容
        HSSFSheet sheetAt = workbook.getSheetAt(0);
        // 用list来装Area对象
        List<Area> list = new ArrayList<Area>();
        for (Row row : sheetAt) {
            if (row.getRowNum() == 0) {
                // 跳过表头
                continue;
            }
            
            String province = row.getCell(1).getStringCellValue();
            String city = row.getCell(2).getStringCellValue();
            String district = row.getCell(3).getStringCellValue();
            String postcode = row.getCell(4).getStringCellValue();
            // 切掉省市区最后一个字符
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);
            // 拼接简码
            String[] string = PinYin4jUtils.getHeadByString(province + city + district);
            String shortcode = StringUtils.join(string).toUpperCase();
            // 获取城市编码
            String citycode = PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
            // 封装对象
           Area area = new Area();
           area.setProvince(province);
           area.setCity(city);
           area.setDistrict(district);
           area.setPostcode(postcode);
           area.setCitycode(citycode);
           area.setShortcode(shortcode);
            list.add(area);
        }
        service.save(list);
        // 释放资源
        workbook.close();
        return NONE;

    }
    /**
     * 分页查询
     */
 
    @Action("areaAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Area> page = service.findAll(pageable);
       
      /*  JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});*/
        page2json(page, null);
        return NONE ;
    }
    private String q ;
    public void setQ(String q) {
        this.q = q;
    }
    //查询所有
    @Action("areaAction_findAll")
    public String findAll() throws IOException{
        List<Area> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(q)) {
            list = service.findByQ(q);
        }else{
            Page<Area> page = service.findAll(null);
            list = page.getContent();
        }
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas"});
        list2json(list, jsonConfig);
        return NONE ;
    }

}
