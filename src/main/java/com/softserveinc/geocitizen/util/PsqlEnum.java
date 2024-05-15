/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 6:05 PM
 */
public class PsqlEnum extends EnumType {

	@Override
	public void nullSafeSet(
			final PreparedStatement st,
			final Object value,
			final int index,
			final SharedSessionContractImplementor session
	) throws SQLException {
		if (value == null) {
			st.setNull(index, Types.OTHER);
		} else {
			st.setObject(
					index,
					value.toString(),
					Types.OTHER
			);
		}
	}
}
