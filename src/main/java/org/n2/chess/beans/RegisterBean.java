/*******************************************************************************
 * Copyright (c) 2011 Daniel Murygin.
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murygin <dm[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package org.n2.chess.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.n2.chess.beans.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Component("register")
@Scope("session")
public class RegisterBean {
    
    private static final Logger LOG = Logger.getLogger(RegisterBean.class);
    
    private String login;
    
    private String email;
    
    private String password;
    
    private String password2;
    
    @Autowired
    private UserBean userBean;
    
    @Autowired
    private IUserService userService;

    public void register() {
        LOG.error("register...");
        try {
            User user = new User();
            user.setEmail(getEmail());
            user.setLogin(getLogin());
            user.setPassword(getPassword());     
            getUserService().save(user);
            FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"User creted", "Login name is: " + getLogin()));
            if(user.getId()!=null) {
                getUserBean().setUser(user);
            }
        } catch (Exception e) {
            LOG.error("Error while creating user", e);
            FacesContext.getCurrentInstance().addMessage(
                    null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Registering failed", "Error while registering user: " + e.getMessage()));
        }
    }
    
    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * @param password2 the password2 to set
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * @return the userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * @return the userService
     */
    public IUserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

}
