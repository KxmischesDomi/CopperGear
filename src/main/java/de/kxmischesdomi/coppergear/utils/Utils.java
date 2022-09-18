package de.kxmischesdomi.coppergear.utils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class Utils {

	public static <K, V> Map<V, K> inverseMap(Map<K, V> sourceMap) {
		return sourceMap.entrySet().stream().collect(
				Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
						(a, b) -> a) //if sourceMap has duplicate values, keep only first
		);
	}

}
