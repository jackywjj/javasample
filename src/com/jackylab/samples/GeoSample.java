package com.jackylab.samples;

import java.util.HashMap;
import java.util.Map;

public class GeoSample {
	public static void main(String[] args) {
		Geo geo = new Geo();

		String positionStr = "121.485654,31.257713|121.492661,31.252156|121.477174,31.247186|121.47049,31.254657|121.482528,31.250829";

		Map<String, Double> point = new HashMap<String, Double>();
		point.put("lat", 31.251724);
		point.put("lng", 121.482168);

		boolean result = geo.isPointInPolygon(point, positionStr);

		System.out.println("Result is : " + result);
	}
}
