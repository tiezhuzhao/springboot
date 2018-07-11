package com.fast.boot.utils.lang;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

	
	//---------------------------------------------------------------------- isEmpty
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static <T> boolean isEmpty(final T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 *
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 *
	 * @param array
	 * @return 
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 *
	 * @param array 
	 * @return 
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}
	
	/**
	 * 
	 * @param collection 
	 * @return 
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	/**
	 * 
	 * @param map 
	 * @return 
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	//---------------------------------------------------------------------- isNotEmpty
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return (array != null && array.length != 0);
	}
	
	/**
	 *
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final long[] array) {
		return (array != null && array.length != 0);
	}
	
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final int[] array) {
		return (array != null && array.length != 0);
	}
	
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final short[] array) {
		return (array != null && array.length != 0);
	}
	
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final char[] array) {
		return (array != null && array.length != 0);
	}
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return (array != null && array.length != 0);
	}
	/**
	 *
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final double[] array) {
		return (array != null && array.length != 0);
	}
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final float[] array) {
		return (array != null && array.length != 0);
	}
	
	/**
	 * 
	 * @param array 
	 * @return 
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 
	 * @param collection 
	 * @return 
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return false == isEmpty(collection);
	}

	/**
	 * 
	 * @param map 
	 * @return 
	 */
	public static <T> boolean isNotEmpty(Map<?, ?> map) {
		return false == isEmpty(map);
	}

}
