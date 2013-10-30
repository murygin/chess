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

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.n2.chess.beans.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 * 
 */
@Component("register")
@Scope("session")
public class RegisterBean implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(RegisterBean.class);

    private final static int ITERATION_NUMBER = 1000;
    
    private String login;

    private String email;

    private String password;

    private String password2;
    
    private String salt;

    private boolean registerVisible;

    private boolean loginVisible;
    
    @Autowired
    private UserBean userBean;
    
    @Autowired
    private GameBean gameBean;

    @Autowired
    private IUserService userService;

    public void register() {
        LOG.debug("register...");
        try {
            validate();
            securePassword();
            User user = new User(getLogin(), getEmail(), getPassword(), getSalt());
            getUserService().save(user);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User created", "Login name is: " + getLogin()));
            if (user.getId() != null) {
                getUserBean().setUser(user);
            }
            setPassword(null);
            setPassword2(null);
            setSalt(null);
        } catch (ValidatorException e) {
            LOG.error("Validation exception: " + e.getFacesMessage().getSummary());
            LOG.error("Error while creating user", e);
            FacesContext.getCurrentInstance().addMessage(null, e.getFacesMessage());
        } catch (Exception e) {
            LOG.error("Error while creating user", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registering failed", "Error while registering user: " + e.getMessage()));
        }
    }
    
    public void login() {
        LOG.debug("login...");
        try {
            User user = getUserService().findUser(getLogin());
            String digest;
            String salt;
            if (user!=null) {
                digest = user.getPassword();
                salt = user.getSalt();
                // DATABASE VALIDATION
                if (digest == null || salt == null) {
                    throw new RuntimeException("Database inconsistant salt or hash altered");
                }          
            } else { // TIME RESISTANT ATTACK (Even if the user does not exist the
                // Computation time is equal to the time needed for a legitimate user
                digest = "000000000000000000000000000=";
                salt = "00000000000=";
            }
            
            byte[] bDigest = base64ToByte(digest);
            byte[] bSalt = base64ToByte(salt);
  
            // Compute the new DIGEST
            byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);
            
            if (user != null &&  Arrays.equals(proposedDigest, bDigest) ) {
                getUserBean().setUser(user);
                getGameBean().init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login succesful", "You are logged in. Welcome back: " + getLogin()));              
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Check username and password."));
            }
            setPassword(null);
            setPassword2(null);
            setSalt(null);
        } catch (Exception e) {
            LOG.error("Error while login: ", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Unknown error."));
        }
    }
    
    public String logout() {
        getUserBean().setUser(null);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        final HttpServletRequest request = (HttpServletRequest)ec.getRequest();
        request.getSession( false ).invalidate();
        return "logout";
    }
    
    /**
     * This method is called from client to keep session alive
     * and prevents session timeout.
     */
    public void keepSessionAlive() {
        getUserBean().getOk();
    }

    private void validate() throws ValidatorException {
        if (login == null || login.trim().isEmpty()) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Please enter a login name.");
            message.setSummary("Login name not set");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }
        if (login.length() >= 100) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Please enter a login name with less than 100 characters.");
            message.setSummary("Login name to long");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }
        if (email == null || email.trim().isEmpty()) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Please enter a email adress.");
            message.setSummary("Email adress not set");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }
        if (password == null || password.trim().isEmpty()) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Please enter a password.");
            message.setSummary("Passwords not set");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }
        if (!password.equals(password2)) {
            FacesMessage message = new FacesMessage();
            message.setDetail("Please retype the password correctly.");
            message.setSummary("Passwords doesn't match");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(message);
        }

    }

    /**
     * Creates a fixed length small fingerprint (digest / hash)
     * See: https://www.owasp.org/index.php/Hashing_Java
     */
    private void securePassword() {
        try {
            // Uses a secure Random not a simple Random
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Salt generation 64 bits long
            byte[] salt = new byte[8];
            random.nextBytes(salt);
            // Digest computation
            byte[] digest = getHash(ITERATION_NUMBER, password, salt);
            setPassword(byteToBase64(digest));
            setSalt(byteToBase64(salt));
        } catch (Exception e) {
           LOG.error("Error while creating password hash", e);
           throw new RuntimeException("Error while creating user.");
        }
    }
    
    /**
     * From a password, a number of iterations and a salt,
     * returns the corresponding digest
     * @param iterationNb int The number of iterations of the algorithm
     * @param password String The password to encrypt
     * @param salt byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     * @throws UnsupportedEncodingException 
     */
    public byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }
    
    /**
     * From a base 64 representation, returns the corresponding byte[] 
     * @param data String The base64 representation
     * @return byte[]
     * @throws IOException
     */
    public static byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }
  
    /**
     * From a byte[] returns a base 64 representation
     * @param data byte[]
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data){
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login
     *            the login to set
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
     * @param email
     *            the email to set
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
     * @param password
     *            the password to set
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
     * @param password2
     *            the password2 to set
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return the registerVisible
     */
    public boolean getRegisterVisible() {
        return registerVisible;
    }
    
    public String getRegisterStyle() {
        return (getRegisterVisible()) ? "current" : "" ;
    }
    
    public String getLoginStyle() {
        return (getLoginVisible()) ? "current" : "" ;
    }

    /**
     * @param registerVisible the registerVisible to set
     */
    public void setRegisterVisible(boolean registerVisible) {
        this.registerVisible = registerVisible;
    }
    
    public void toggleRegister() {
        registerVisible = !registerVisible;
        if(registerVisible) {
            loginVisible = false;
        }
    }

    /**
     * @return the loginVisible
     */
    public boolean getLoginVisible() {
        return loginVisible;
    }

    /**
     * @param loginVisible the loginVisible to set
     */
    public void setLoginVisible(boolean loginVisible) {
        this.loginVisible = loginVisible;
    }
    
    public void toggleLogin() {
        loginVisible = !loginVisible;
        if(loginVisible) {
            registerVisible = false;
        }
    }

    /**
     * @return the userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * @param userBean
     *            the userBean to set
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * @return the gameBean
     */
    public GameBean getGameBean() {
        return gameBean;
    }

    /**
     * @param gameBean the gameBean to set
     */
    public void setGameBean(GameBean gameBean) {
        this.gameBean = gameBean;
    }

    /**
     * @return the userService
     */
    public IUserService getUserService() {
        return userService;
    }

    /**
     * @param userService
     *            the userService to set
     */
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

}
