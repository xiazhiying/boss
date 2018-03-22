package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.bos.utils.MailUtils;
import com.itheima.bos.utils.SmsUtils;
import com.itheima.crm.domain.Customer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CustomerAction <br/>
 * Function: <br/>
 * Date: 2018年3月20日 下午5:35:05 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    private Customer model = new Customer();

    @Override
    public Customer getModel() {

        return model;
    }

    /**
     * customerAction_sendSMS
     * 
     * @throws ClientException
     */

    @Action("customerAction_sendSMS")
    public String sendSMS() throws ClientException {
        // 获取验证码
        String code = RandomStringUtils.randomNumeric(6);
        // 将code存起来
        ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
        // 发送验证码
        SmsUtils.sendSms(model.getTelephone(), code);
        return NONE;
    }
    /**
     * customerAction_checkTelephone
     * 校验手机号是否被注册 
     * @throws IOException 
     */
    
    @Action("customerAction_checkTelephone")
    public String checkTelephone() throws IOException{
      //处理中文乱码
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        if (model.getTelephone().length()!= 11) {
            response.getWriter().print("");
        }else {
            
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/checkTelephone")
                    .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .get(Customer.class);
            
            if(customer != null){
                response.getWriter().print("您的用户名已经注册");
            }else {
                response.getWriter().print("验证成功");
            }
        }
        return NONE;
    }

    /**
     * customerAction_regist
     */
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate ;
    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "/signup-success.html", type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html", type = "redirect")})
    public String regist() {
        String serverCode =
                (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        if (StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(serverCode)
                && serverCode.equals(checkcode)) {
            WebClient.create("http://localhost:8180/crm/webService/customerService/save")
                    .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .post(model);
            
            //生成邮箱验证码
            String activeCode = RandomStringUtils.randomNumeric(32);
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode,1, TimeUnit.DAYS);
            String emailBody =  "感谢您注册本网站的帐号，请在24小时之内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="
                    + activeCode + "&telephone=" + model.getTelephone()
                    + "'>本链接</a>激活您的帐号";
            //发送邮件
            MailUtils.sendMail(model.getEmail(), "激活用户", emailBody );
            return SUCCESS;
        }
        return NONE;
    }
    /**
     * 激活用户
     * customerAction_active
     */
    //使用属性驱动获取验证码
    private String activeCode ;
    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }
    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active(){
        String serverCode = redisTemplate.opsForValue().get(model.getTelephone());
        if (StringUtils.isNotEmpty(activeCode)
                && StringUtils.isNotEmpty(serverCode)
                && activeCode.equals(serverCode)) {
            WebClient.create("http://localhost:8180/crm/webService/customerService/active")
            .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .query("telephone", model.getTelephone())
            .put(null);
            return SUCCESS ;
        }
        return ERROR ;
    }
    /**
     * 用户登陆
     * customerAction_login.action
     */
    @Action(value="customerAction_login" ,
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/login.html",
                            type = "redirect")})
    public String login(){
        String code = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
        if (code != null && code.equalsIgnoreCase(checkcode)) {
            
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/checkTelephone")
                    .type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .get(Customer.class);
            if (customer != null && customer.getType() !=null) {
                if (customer.getType() == 1 && customer.getPassword().equals(model.getPassword())) {
                    ServletActionContext.getRequest().getSession().setAttribute("user", customer);
                    return SUCCESS ;
                }
            }
        }
        return ERROR ;
    }
}
