package com.jackylab.samples;

/**
 * 验证身份证合法性，主要做校验码的验证。
 * 
 * @author jacky
 * 
 * 2016-02-14
 */
public class IdValidation {
	private int[] coefficients = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	private String[] validations = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

	public IdValidation() {

	}

	public boolean validateId(String myId) {
		int length = coefficients.length;
		String[] idStr = myId.split("");
		int total = 0;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				int tmpIdNumber = Integer.parseInt(idStr[i]);
				total += tmpIdNumber * coefficients[i];
			}
		}
		String remainder = validations[(total % 11)];
		return (remainder.equals(idStr[17])) ? true : false;
	}

	public static void main(String args[]) {
		IdValidation sample = new IdValidation();
		String myId = ""; // 这里填写需要校验的身份证号码
		System.out.println("your id is " + sample.validateId(myId));
	}
}
