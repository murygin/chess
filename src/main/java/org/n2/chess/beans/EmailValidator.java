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
 
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component("emailValidator")
public class EmailValidator implements Validator{
 
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
            "[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";
 
    private Pattern pattern;
    private Matcher matcher;
    
    @Autowired
    UserValidator userValidator;
 
    public EmailValidator(){
          pattern = Pattern.compile(EMAIL_PATTERN);
    }
 
    @Override
    public void validate(FacesContext context, UIComponent component,Object value) throws ValidatorException {
        
        matcher = pattern.matcher(value.toString());
        if(!matcher.matches()){
            FacesMessage msg = new FacesMessage("Email validation failed.", "Invalid email format.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
 
        }
        if(value.toString().length()>=100){
            FacesMessage msg = new FacesMessage("Email to long (>99)", "Email longer than 100 characters.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
        }
        
 
        getUserValidator().validate(context, component, value);
    }

    /**
     * @return the userValidator
     */
    public UserValidator getUserValidator() {
        return userValidator;
    }

    /**
     * @param userValidator the userValidator to set
     */
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}
