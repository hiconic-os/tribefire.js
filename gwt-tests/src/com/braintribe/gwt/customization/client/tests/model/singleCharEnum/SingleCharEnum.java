package com.braintribe.gwt.customization.client.tests.model.singleCharEnum;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * @author peter.gazdik
 */
public enum SingleCharEnum implements EnumBase {

	off;

	// @formatter:off
//	a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, 
//	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, 
	// @formatter:on

	public static final EnumType T = EnumTypes.T(SingleCharEnum.class);

	@Override
	public EnumType type() {
		return T;
	}

}
