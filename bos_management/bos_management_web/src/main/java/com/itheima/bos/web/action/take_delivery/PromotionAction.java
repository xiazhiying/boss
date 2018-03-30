package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Promotion;
import com.itheima.bos.service.take_delivery.PromotionService;
import com.itheima.bos.web.action.CommonAction;
import org.apache.struts2.convention.annotation.Result;

/**  
 * ClassName:PromotionAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月24日 下午2:41:18 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class PromotionAction extends CommonAction<Promotion> {

    public PromotionAction() {
        super(Promotion.class);  
    }
    // 属性驱动获取上传的文件
    private File titleImgFile;
    // 属性驱动获取上传的文件名
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Autowired
    private PromotionService promotionService;

    /**
     * 保存宣传任务
     * save:. <br/>  
     *  
     * @return
     */
    @Action(value = "promotionAction_save",
            results = {
                    @Result(name = "success",
                            location = "/pages/take_delivery/promotion.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/error.html",
                            type = "redirect")})
    
    public String save() {
        Promotion promotion = getModel();
        try {
            if (titleImgFile != null) {
                // 获取保存文件的文件夹的绝对路径
                String saveDirPath = "upload";
                String saveDirRealPath = ServletActionContext
                        .getServletContext().getRealPath(saveDirPath);

                // 获取文件后缀名
                String suffix = titleImgFileFileName
                        .substring(titleImgFileFileName.lastIndexOf("."));
                // 生成文件名
                String fileName = UUID.randomUUID().toString()
                        .replaceAll("-", "").toUpperCase() + suffix;
                // 复制文件
                FileUtils.copyFile(titleImgFile,
                        new File(saveDirRealPath + "/" + fileName));
                // 设置封面图片保存的路径
                // 路径格式 : /upload/xxx.jpg
                promotion.setTitleImg(
"/upload/" + fileName);

            }
            // 设置活动状态
            promotion.setStatus("1");
            // 保存数据
            promotionService.save(promotion);

            return SUCCESS;
        } catch (IOException e) {

            e.printStackTrace();

        }

        return ERROR;
    }
    /**
     * promotionAction_pageQuery
     * @throws IOException 
     */
    
    @Action("promotionAction_pageQuery")
    public String pageQuery() throws IOException{
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Promotion> page = promotionService.findAll(pageable);
        page2json(page, null);
        return NONE ;
    }

}



  
