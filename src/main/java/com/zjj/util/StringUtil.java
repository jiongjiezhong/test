package com.zjj.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 编码转换iso8859-1 --> utf-8
	 * 
	 * @param sor
	 * @return
	 */
	public static String changeEncode(String sor) {
		try {
			return new String(sor.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sor;
	}

	/**
	 * 编码转换iso8859-1 <-- gb2312
	 * 
	 * @param sor
	 * @return
	 */
	public static String changeEncode2(String sor) {
		try {
			return new String(sor.getBytes("gb2312"), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sor;
	}

	/**
	 * 转换富文本内�?使�?合于html页面显示
	 * 
	 * @param sor
	 * @return
	 */
	public static String change4html(String sor) {
		if (sor != null) {
			String dst = sor.replaceAll("\"", "'");// 目前只进�?转换�?
			return dst;
		} else {
			return sor;
		}

	}

	/**
	 * 判断一个字符串是否是数字（负数与正数）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isSignNumeric(String str) {
		Pattern pattern = PatternBean.getNumberPattern();
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断一个字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = PatternBean.getNumberPattern();
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断一个字符串是否是非负浮点数（正浮点数 + 0）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (str != null) {
			if (str.length() == 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * url解码
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		if (StringUtil.isEmpty(src))
			return src;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * url编码
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		if (StringUtil.isEmpty(src)) {
			return "";
		}
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str == "")
			return true;
		if (str.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * 不够长度自动补全0
	 */
	public static String fillZero(String value, int length) {
		int len = value.length();
		if (len < length) {
			int cha = length - len;
			String tem = "";
			for (int i=0; i < cha; i++) {
				tem += "0";
			}
			return tem + value;
		}
		return value;
	}
	
	/**
	 * 生成28位订单号
	 * @return
	 */
	public static String getOutTradeNo() {
		String now = DateUtil.date2String_format(DateUtil.YYYYMMDDHHMMSSSSS);
		return now + getRandomString(11);
	}

	/**
	 * 生成订单号
	 * @return
	 */
	public static String getOrderNo() {
		String now = DateUtil.date2String_format(DateUtil.YYYYMMDDHHMMSSSSS);
		return now + getRandomNumString(9);
	}

	public static String getRandomString(Integer strlength) {
		String str2[] = new String[] {"0","1","2","3","4","5","6","7","8","9",
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};// 将字符串以,分割
		Random random = new Random();
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < strlength; i++) {
			int number = random.nextInt(str2.length);
			randStr.append(str2[number]);
		}
		return randStr.toString();
	}
	public static String getRandomNumString(Integer strlength) {
		String str2[] = new String[] {"0","1","2","3","4","5","6","7","8","9"};// 将字符串以,分割
		Random random = new Random();
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < strlength; i++) {
			int number = random.nextInt(str2.length);
			randStr.append(str2[number]);
		}
		return randStr.toString();
	}

    public static String getStringValue(Object valObj, String defaultValue) {
		if (null == valObj) {
			return defaultValue;
		}
		return valObj.toString();
    }

    /**
	 * 对象内部数组排序 根据数组 idx 排序后的顺序对后面的数组排序 返回值为排好序的 obj
	 * 
	 * @Author: CYF
	 * @Date: 2016年12月7日
	 */
	public Object[][] objectInnerArraySort(Integer[] idx, Object[]... obj) {
		Integer[] new_i = Arrays.copyOf(idx, idx.length);
		Arrays.sort(new_i);
		for (int j = 0; j < idx.length; j++) {
			if (!idx[j].equals(new_i[j])) {
				for (int k = j; k < idx.length; k++) {
					if (idx[k].equals(new_i[j])) {
						for (int m = 0; m < obj.length; m++) {
							if (k < j) {
								Object temp = obj[m][j];
								for (int n = j; n > k; n--) {
									obj[m][n] = obj[m][n - 1];
								}
								obj[m][k] = temp;
							} else if (k > j) {
								Object temp = obj[m][k];
								for (int n = k; n > j; n--) {
									obj[m][n] = obj[m][n - 1];
								}
								obj[m][j] = temp;
							}
						}
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 一个字符串中某个字符出现次数
	 * 
	 * @param source
	 *            原字符串
	 * @param targert
	 *            待检查的字符
	 * @return 出现次数
	 */
	public static int occurrenceTimes(String source, String targert) {
		int count = 0;
		int index = 0;
		while (true) {
			index = source.indexOf(targert, index + 1);
			if (index > 0) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}
	
	/**
	 * 计算文字长度
	 */
	public static int getWordLength(String nickName, int fontSize) {
		int len = 0;
		if(isNotEmpty(nickName)) {
			for(int i = 0; i < nickName.length(); i++) {
				String word = nickName.substring(i, i+1);
				if(isNumeric(word) || (nickName.charAt(i) <= 'Z' && nickName.charAt(i) >= 'A')
                        || (nickName.charAt(i) <= 'z' && nickName.charAt(i) >= 'a')) {
					len += fontSize / 2 + fontSize / 5;
				} else {
					len += fontSize;
				}
			}
		}
		return len;
	}

	public static boolean validPhoneFormat(String phone) {
		if (null == phone || phone == "" || "null".equals(phone))
			return false;
		String phoneReg = "^((\\+86)|(86))?[1][3,4,5,6,7,8,9][0-9]\\d{8}$";
		Pattern p = Pattern.compile(phoneReg);
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	/**
	 * 判断字符串是否是纯中文
	 */
	public static boolean isChinese(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		String reg = "^[\u4e00-\u9fa5]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher match = pattern.matcher(str);
		return match.find();
	}

	/**
	 * 生成随机字符串（不重复，去除相似字符）
	 * @param Len 位数
	 * @return
	 */
	public static String getRandomLenString(int Len) {
		String[] baseString =
				{ "2", "3", "4", "5", "6", "7", "8", "9","a","b","c","d","e","f","g","h","j","k","m","n","p","q","r","s","t","u","v","w","x","y","z",
						"A", "B", "C", "D", "E", "F","G", "H", "I", "J", "K", "L", "M", "N",  "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		Random random = new Random();
		int length = baseString.length;
		String randomString = "";
		for (int i = 0; i < length; i++) {
			randomString += baseString[random.nextInt(length)];
		}
		random = new Random(System.currentTimeMillis());
		String resultStr = "";
		for (int i = 0; i < Len; i++) {
			resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
		}
		return resultStr;
	}

    /**
     * 判断对象里的属性是否全为null值
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean checkObjFieldIsNull(Object obj) throws Exception {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();// 得到属性集合
        boolean flag = true;
        for (Field f : fs) {// 遍历属性
            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
            Object val = f.get(obj);// 得到此属性的值
            if (val != null) {// 只要有1个属性不为空,那么就不是所有的属性值都为空
                flag = false;
                break;
            }
        }
        return flag;
    }
    
    /** 姓名脱敏 */
    public static String plateNoEncrypt(String plateNo) {
    	if (StringUtils.isEmpty(plateNo) || (plateNo.length() != 7)) {
			return plateNo;
		}
		return plateNo.replaceAll("(.{2}).{1}(.{4})", "$1*$2");
    }
    /** 姓名脱敏 */
    public static String nameEncrypt(String name) {
    	if (StringUtils.isEmpty(name)) {
    		return name;
    	}
    	if (name.length() == 2) {
    		return name.replaceAll("(.{1})(.{1})", "$1*");
    	} else if (name.length() == 3) {
    		return name.replaceAll("(.{1}).{1}(.{1})", "$1*$2");
    	} else if (name.length() == 4){
    		return name.replaceAll("(.{1}).{2}(.{1})", "$1**$2");
    	} else if (name.length() == 5){
    		return name.replaceAll("(.{1}).{3}(.{1})", "$1***$2");
    	}
    	return name;
    }
	/** 手机号码前三后四脱敏 */
	public static String mobileEncrypt(String mobile) {
		if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
			return mobile;
		}
		return mobile.replaceAll("(\\d{3}).{4}(\\d{4})", "$1****$2");
	}
	
	public static void main(String[] args) {
		System.out.println(validPhoneFormat("18599205551 ".trim()));
		System.out.println(validPhoneFormat("15199112230"));
		System.out.println(validPhoneFormat("131160383666"));
		System.out.println();
	}
}
