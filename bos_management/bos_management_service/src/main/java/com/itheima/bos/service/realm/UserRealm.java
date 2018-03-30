package com.itheima.bos.service.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.PermissionRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserRealm <br/>  
 * Function:  <br/>  
 * Date:     2018年3月26日 下午4:46:57 <br/>       
 */
@Component
public class UserRealm extends AuthorizingRealm {

    
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private RoleRepository roleRepository ;
    @Autowired
    private PermissionRepository permissionRepository ;
    /**
     * 
     * TODO 授权方法  
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
     // 需要根据当前的用户去查询对应的权限和角色
        User user = (User) SecurityUtils.getSubject();
        if ("admin".equals(user.getUsername())) {
            //赋予所有角色
            List<Role> roles = roleRepository.findAll();
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
            //赋予所有权限
            List<Permission> permissions = permissionRepository.findAll();
            for (Permission permission : permissions) {
                info.addStringPermission(permission.getKeyword());
            }
            
        }else {
            List<Role> roles = roleRepository.findbyUid(user.getId());
            for (Role role : roles) {
                info.addRole(role.getKeyword());
            }
            List<Permission> permissions = permissionRepository.findbyUid(user.getId());
            for (Permission permission : permissions) {
                info.addStringPermission(permission.getKeyword());
            }
        }
        return info;  
    }

    /**
     * 
     * TODO 登陆认证  
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token ;
        String username = usernamePasswordToken.getUsername();
        //根据用户名查user
        User user = userRepository.findByUsername(username);
        if (user != null) {
            /**
             * 
             * @param principal
             *            当事人,主体.往往传递从数据库中查询出来的用户对象
             * @param credentials
             *            凭证,密码(是从数据库中查询出来的密码)
             * @param realmName
             * 
             */

            AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
            return info;
        }
        return null;
    }

}
  
