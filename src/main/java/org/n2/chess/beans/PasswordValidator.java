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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.springframework.stereotype.Component;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 *
 */
@Component("passwordValidator")
public class PasswordValidator implements Validator{
 
    public PasswordValidator(){
    }
 
    @Override
    public void validate(FacesContext context, UIComponent component,Object value) throws ValidatorException {  
        String pwd = (String) value;
        if(pwd!=null && pwd.length()<6) {
            FacesMessage msg = new FacesMessage("Password is to short", "Enter a password with at least 6 characters.");
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            throw new ValidatorException(msg);
        }
    }


}
