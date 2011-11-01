package org.n2.chess.beans;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("welcomeBean")
@Scope("session")
public class WelcomeBean implements Serializable{
    
    @Autowired
    private IUserService userService;
    
    public WelcomeBean() {
        System.out.println("WelcomeBean instantiated");
    }
    public String getMessage() {
        return "A message from a WelcomeBean";
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
