package com.softserveinc.geocitizen.security.exception.interfaces;

import com.softserveinc.tools.model.JsonError;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/5/18 at 12:57 PM
 */
public interface ICitizenAuthenticationException {

	JsonError.Error getError();
}
