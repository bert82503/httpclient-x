/*
 * Copyright (c)
 */
package com.hz.util;

import java.util.Collection;

/**
 * 提供集合实例的实用工具方法和装饰器。
 * <p>
 * Provides utility methods and decorators for {@link Collection} instances.
 * <p>
 * 参考自 {@link org.apache.commons.collections4.CollectionUtils}
 * 
 * @author huagang.li
 * @since 1.0
 */
public class CollectionUtils {

	// -----------------------------------------------------------------------
	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * 
	 * @param coll
	 *            the collection to check, may be null
	 * @return true if empty or null
	 * @since 3.2
	 */
	public static boolean isEmpty(final Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param coll
	 *            the collection to check, may be null
	 * @return true if non-null and non-empty
	 * @since 3.2
	 */
	public static boolean isNotEmpty(final Collection<?> coll) {
		return !isEmpty(coll);
	}

}
