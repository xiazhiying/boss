package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

/**  
 * ClassName:CustomerService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:08:27 <br/>       
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerService {

    @GET
    @Path("/findAll")
    List<Customer> findAll();
    @GET
    @Path("/findCustomersUnAssociated")
    List<Customer> findCustomersUnAssociated();
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    List<Customer> findCustomersAssociated2FixedArea( @QueryParam("fixedAreaId") String fixedAreaId);
    @PUT
    @Path("/assignCustomers2FixedArea")
    void assignCustomers2FixedArea(@QueryParam("fixedAreaId")String fixedAreaId , @QueryParam("customerIds")Long[] customerIds);
    @POST
    @Path("/save")
    public void save(Customer customer);
    @GET
    @Path("/checkTelephone")
    Customer checkTelephone(@QueryParam("telephone")String telephone);
    @PUT
    @Path("/active")
    void active(@QueryParam("telephone")String telephone);

}
  
