package com.jackylab.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Geo {
	public static double EARTHRADIUS = 6370996.81;

	private List<Map<String, Double>> splitPositionString(String positionsString) {
		List<Map<String, Double>> positionList = new ArrayList<Map<String, Double>>();
		String positions[] = positionsString.split("\\|");
		if (positions.length > 0) {
			for (String positionStr : positions) {
				String pos[] = positionStr.split(",");
				Map<String, Double> posTmpMap = new HashMap<String, Double>();
				posTmpMap.put("lat", Double.parseDouble(pos[1].trim()));
				posTmpMap.put("lng", Double.parseDouble(pos[0].trim()));
				positionList.add(posTmpMap);
			}
		}
		return positionList;
	}

	public void reSortList(List<Map<String, Double>> polygon) {
		int polygonSize = polygon.size();
		Map<String, Double> p1 = polygon.get(0);
		Double latMax = p1.get("lat");
		Double latMin = p1.get("lat");
		Double lngMax = p1.get("lng");
		Double lngMin = p1.get("lng");
		for (int i = 1; i < polygonSize; i++) {
			Map<String, Double> tmpPoint = polygon.get(i);
			if (tmpPoint.get("lat") > latMax) {
				latMax = tmpPoint.get("lat");
			}
			if (tmpPoint.get("lat") < latMin) {
				latMin = tmpPoint.get("lat");
			}
			if (tmpPoint.get("lng") > lngMax) {
				lngMax = tmpPoint.get("lng");
			}
			if (tmpPoint.get("lng") < lngMin) {
				lngMin = tmpPoint.get("lng");
			}
		}
		System.out.println("Max Lat is : " + latMax);
		System.out.println("Min Lat is : " + latMin);
		System.out.println("Max Lng is : " + lngMax);
		System.out.println("Min Lng is : " + lngMin);
	}

	public boolean isPointInPolygon(Map<String, Double> point, String positionsString) {
		List<Map<String, Double>> polygon = splitPositionString(positionsString);
		int polygonSize = polygon.size();
		boolean boundOrVertex = true;
		int intersectCount = 0;
		double precision = 2e-10;
		Map<String, Double> p1, p2;

		p1 = polygon.get(0);
		for (int i = 1; i <= polygonSize; ++i) {
			if (point.equals(p1)) {
				return boundOrVertex;
			}

			p2 = polygon.get(i % polygonSize);
			if (point.get("lat") < Math.min(p1.get("lat"), p2.get("lat"))
					|| point.get("lat") > Math.max(p1.get("lat"), p2.get("lat"))) {
				p1 = p2;
				continue;
			}

			if (point.get("lat") > Math.min(p1.get("lat"), p2.get("lat"))
					&& point.get("lat") < Math.max(p1.get("lat"), p2.get("lat"))) {
				if (point.get("lng") <= Math.max(p1.get("lng"), p2.get("lng"))) {
					if (p1.get("lat") == p2.get("lat") && point.get("lng") >= Math.min(p1.get("lng"), p2.get("lng"))) {
						return boundOrVertex;
					}

					if (p1.get("lng") == p2.get("lng")) {
						if (p1.get("lng") == point.get("lng")) {
							return boundOrVertex;
						} else {
							++intersectCount;
						}
					} else {
						double xinters = (point.get("lat") - p1.get("lat")) * (p2.get("lng") - p1.get("lng"))
								/ (p2.get("lat") - p1.get("lat")) + p1.get("lng");
						if (Math.abs(point.get("lng") - xinters) < precision) {
							return boundOrVertex;
						}

						if (point.get("lng") < xinters) {
							++intersectCount;
						}
					}
				}
			} else {
				if (point.get("lat") == p2.get("lat") && point.get("lng") <= p2.get("lng")) {
					Map<String, Double> p3 = polygon.get((i + 1) % polygonSize);
					if (point.get("lat") >= Math.min(p1.get("lat"), p3.get("lat"))
							&& point.get("lat") <= Math.max(p1.get("lat"), p3.get("lat"))) {
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;
		}

		if (intersectCount % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}

	public double calculateArea(List<Map<String, Double>> polygon) {
		if (polygon.size() < 3) {
			return 0l;
		}
		double totalArea = 0;
		double lowX = 0.0;
		double LowY = 0.0;
		double MiddleX = 0.0;
		double MiddleY = 0.0;
		double HighX = 0.0;
		double HighY = 0.0;
		double AM = 0.0;
		double BM = 0.0;
		double CM = 0.0;
		double AL = 0.0;
		double BL = 0.0;
		double CL = 0.0;
		double AH = 0.0;
		double BH = 0.0;
		double CH = 0.0;
		double CoefficientL = 0.0;
		double CoefficientH = 0.0;
		double ALtangent = 0.0;
		double BLtangent = 0.0;
		double CLtangent = 0.0;
		double AHtangent = 0.0;
		double BHtangent = 0.0;
		double CHtangent = 0.0;
		double ANormalLine = 0.0;
		double BNormalLine = 0.0;
		double CNormalLine = 0.0;
		double OrientationValue = 0.0;
		double AngleCos = 0.0;
		double Sum1 = 0.0;
		double Sum2 = 0.0;
		double Count2 = 0;
		double Count1 = 0;
		double Sum = 0.0;
		double Radius = EARTHRADIUS;
		int Count = polygon.size();
		for (int i = 0; i < Count; i++) {
			if (i == 0) {
				lowX = polygon.get(Count - 1).get("lng") * Math.PI / 180;
				LowY = polygon.get(Count - 1).get("lat") * Math.PI / 180;
				MiddleX = polygon.get(0).get("lng") * Math.PI / 180;
				MiddleY = polygon.get(0).get("lat") * Math.PI / 180;
				HighX = polygon.get(1).get("lng") * Math.PI / 180;
				HighY = polygon.get(1).get("lat") * Math.PI / 180;
			} else if (i == Count - 1) {
				lowX = polygon.get(Count - 2).get("lng") * Math.PI / 180;
				LowY = polygon.get(Count - 2).get("lat") * Math.PI / 180;
				MiddleX = polygon.get(Count - 1).get("lng") * Math.PI / 180;
				MiddleY = polygon.get(Count - 1).get("lat") * Math.PI / 180;
				HighX = polygon.get(0).get("lng") * Math.PI / 180;
				HighY = polygon.get(0).get("lat") * Math.PI / 180;
			} else {
				lowX = polygon.get(i - 1).get("lng") * Math.PI / 180;
				LowY = polygon.get(i - 1).get("lat") * Math.PI / 180;
				MiddleX = polygon.get(i).get("lng") * Math.PI / 180;
				MiddleY = polygon.get(i).get("lat") * Math.PI / 180;
				HighX = polygon.get(i + 1).get("lng") * Math.PI / 180;
				HighY = polygon.get(i + 1).get("lat") * Math.PI / 180;
			}
			AM = Math.cos(MiddleY) * Math.cos(MiddleX);
			BM = Math.cos(MiddleY) * Math.sin(MiddleX);
			CM = Math.sin(MiddleY);
			AL = Math.cos(LowY) * Math.cos(lowX);
			BL = Math.cos(LowY) * Math.sin(lowX);
			CL = Math.sin(LowY);
			AH = Math.cos(HighY) * Math.cos(HighX);
			BH = Math.cos(HighY) * Math.sin(HighX);
			CH = Math.sin(HighY);
			CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
			CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);
			ALtangent = CoefficientL * AL - AM;
			BLtangent = CoefficientL * BL - BM;
			CLtangent = CoefficientL * CL - CM;
			AHtangent = CoefficientH * AH - AM;
			BHtangent = CoefficientH * BH - BM;
			CHtangent = CoefficientH * CH - CM;
			AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent)
					/ (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent)
							* Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));
			AngleCos = Math.acos(AngleCos);
			ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
			BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
			CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;
			if (AM != 0)
				OrientationValue = ANormalLine / AM;
			else if (BM != 0)
				OrientationValue = BNormalLine / BM;
			else
				OrientationValue = CNormalLine / CM;
			if (OrientationValue > 0) {
				Sum1 += AngleCos;
				Count1++;
			} else {
				Sum2 += AngleCos;
				Count2++;
			}
		}
		double tempSum1, tempSum2;
		tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2);
		tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2;
		if (Sum1 > Sum2) {
			if ((tempSum1 - (Count - 2) * Math.PI) < 1)
				Sum = tempSum1;
			else
				Sum = tempSum2;
		} else {
			if ((tempSum2 - (Count - 2) * Math.PI) < 1)
				Sum = tempSum2;
			else
				Sum = tempSum1;
		}
		totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;

		return totalArea;
	}

	/// <summary>
	/// Haversine function : hav(x) = (1-cos(x))/2
	/// </summary>
	/// <param name="x"></param>
	/// <returns>Returns the value of Haversine function</returns>
	public double Haversine(double x) {
		return (1.0 - Math.cos(x)) / 2.0;
	}

	/// <summary>
	/// Compute the Area of a Spherical Polygon
	/// </summary>
	/// <param name="lat">the latitudes of all vertices(in radian)</param>
	/// <param name="lon">the longitudes of all vertices(in radian)</param>
	/// <param name="r">spherical radius</param>
	/// <returns>Returns the area of a spherical polygon</returns>
	public double SphericalPolygonArea(double[] lat, double[] lon, double r) {
		double lam1 = 0, lam2 = 0, beta1 = 0, beta2 = 0, cosB1 = 0, cosB2 = 0;
		double hav = 0;
		double sum = 0;

		for (int j = 0; j < lat.length; j++) {
			int k = j + 1;
			if (j == 0) {
				lam1 = lon[j];
				beta1 = lat[j];
				lam2 = lon[j + 1];
				beta2 = lat[j + 1];
				cosB1 = Math.cos(beta1);
				cosB2 = Math.cos(beta2);
			} else {
				k = (j + 1) % lat.length;
				lam1 = lam2;
				beta1 = beta2;
				lam2 = lon[k];
				beta2 = lat[k];
				cosB1 = cosB2;
				cosB2 = Math.cos(beta2);
			}
			if (lam1 != lam2) {
				hav = Haversine(beta2 - beta1) + cosB1 * cosB2 * Haversine(lam2 - lam1);
				double a = 2 * Math.asin(Math.sqrt(hav));
				double b = Math.PI / 2 - beta2;
				double c = Math.PI / 2 - beta1;
				double s = 0.5 * (a + b + c);
				double t = Math.tan(s / 2) * Math.tan((s - a) / 2) * Math.tan((s - b) / 2) * Math.tan((s - c) / 2);

				double excess = Math.abs(4 * Math.atan(Math.sqrt(Math.abs(t))));

				if (lam2 < lam1) {
					excess = -excess;
				}

				sum += excess;
			}
		}
		return Math.abs(sum) * r * r;
	}
}