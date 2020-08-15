package com.guo.tools.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

	private CommonUtil() {
	}



	public static String getRandomLines(Integer count, String delemeter){
		String s = "abcdefghijk";
		StringBuffer sb = new StringBuffer();
		for(int i=1;i<=count;i++){
			sb.append(getRandomLine(s, 2));
			if(i != count){
				sb.append(delemeter);
			}
		}
		return sb.toString();
	}

	private static String getRandomLine(String line, Integer count){
		StringBuffer sb = new StringBuffer();
		int len = line.length()-1;
		for(int i=0;i<count;i++){
			int idx = new Random().nextInt(len)+1;
			sb.append(line.charAt(idx));
		}
		return sb.toString();
	}

	public static String getRandom(Integer count){
		String s = "0123456789abcdefghijkmnopqrstuvwxz";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<count;i++){
			int idx = new Random().nextInt(22)+1;
			sb.append(s.charAt(idx));
		}
		return sb.toString();
	}





	public static String getRandomChar(int count){
		String s = "abcdefghijkmnopqrstuvwxz";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<count;i++){
			int idx = new Random().nextInt(22)+1;
			sb.append(s.charAt(idx));
		}
		return sb.toString();
	}

	public static String getRandomNumStr(int count){
		String range = "123456789";
		int len = range.length();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<count;i++){
			int idx = new Random().nextInt(10)+1;
			if(idx >= len){
				sb.append(range.charAt(idx-len));
			}else {
				sb.append(range.charAt(idx));
			}
		}
		return sb.toString();
	}

	public static <T> T getRandomElementRangeInclude(List<T> elements, T include){
		T element = null;
		if(null != elements){
			elements.remove(include);

			int size = elements.size();
			int idx = new Random().nextInt(size);
			if(idx >= size){
				idx = size -1;
			}
			element = (T)elements.get(idx);
		}
		return element;
	}

	public static <T> T getRandomElementRange(List<T> elements){
		T element = null;
		if(null != elements){
			int size = elements.size();
			int idx = new Random().nextInt(size);
			if(idx >= size){
				idx = size -1;
			}
			element = (T)elements.get(idx);
		}
		return element;
	}

	public static List<String> getRandomSubElementRange(List<String> elements, int count){
		List<String> subElements = new ArrayList<String>();
		if(null != elements){
			int size = elements.size();
			if(size >= count){
				for(int i=1; i<=count; i++){
					int idx = new Random().nextInt(size-1)+1;
					if(idx >= size){
						idx = size -1;
					}
					subElements.add(elements.get(idx));
				}
			}
		}
		return subElements;
	}


	public static Double getRandomDouble(int intLen, int deciLen){
		String range = "0123456789";
		int len = range.length();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<intLen;i++){
			int idx = new Random().nextInt(10)+1;
			if(idx >= len){
				sb.append(range.charAt(idx-len));
			}else {
				sb.append(range.charAt(idx));
			}
		}

		sb.append(".");
		for(int i=0;i<deciLen;i++){
			int idx = new Random().nextInt(10)+1;
			if(idx >= len){
				sb.append(range.charAt(idx-len));
			}else {
				sb.append(range.charAt(idx));
			}
		}

		String s = sb.toString();

		return Double.valueOf(s);
	}

	public static int getRandomNum(int count){
		int num = 0;
		try{
			String range = "123456789";
			int len = range.length();
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<count;i++){
				int idx = new Random().nextInt(10)+1;
				if(idx >= len){
					sb.append(range.charAt(idx-len));
				}else {
					sb.append(range.charAt(idx));
				}
			}
			String numstr = sb.toString();
			num = Integer.parseInt(numstr);
		}catch(Exception e){
			e.printStackTrace();
		}

		return num;
	}

	public static List<String> getRangeNumber(int begin, int end, int sep){
		List<String> rangeNums = new ArrayList<String>();
		for(int i=begin; i<=end; i+=sep){
			rangeNums.add(String.valueOf(i));
		}
		return rangeNums;
	}

	/**
	 * 范围随机值
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getRandomRangeNum(int begin, int end){
		int rbegin = begin;
		int rend = end;
		if(begin > end){
			rbegin = end;
			rend = begin;
		}
		List<Integer> nums = new ArrayList<Integer>();
		for(int i=rbegin; i<=rend; i++){
			nums.add(i);
		}

		int result = getRandomElementRange(nums);
		return result;
	}


	//---日期相关----------------------------------------------


	public static long getSelectTimestamp(String cTimes, String formater){
		Date dt = parseText(cTimes,formater);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.getTimeInMillis();
	}

	public static Long getRandomTimestamp(){
		boolean sign = (new Random().nextInt(100)+1)%2==1;
		int random = new Random().nextInt(100)+1;
		int num = random * (sign?1:-1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTimeInMillis();
	}

	public static long getRandomTimestamp(int bound){
		Date dt = new Date();
		int secord = new Random().nextInt(bound);
		int add = secord * (secord%2==0?1:-1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.SECOND, add);
		return cal.getTimeInMillis();
	}


	/**
	 * 日期格式化
	 */
	public static String formatDate4Timestamp(Long ct, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		String result = null;
		try {
			if (null != ct) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(ct);
				result = sdf.format(cal.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 日期格式化
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		String result = null;
		try {
			if (null != date) {
				result = sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String formatDate4Def(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = null;
		try {
			if (null != date) {
				result = sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 文本转时间
	 * 
	 * @param content
	 * @return
	 */
	public static Date parseText(String content, String dateType) {
		Date date = null;
		if (!StringUtils.isEmpty(content)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(dateType);
				date = sdf.parse(content);
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
		return date;
	}
	
	public static Date parseText4Def(String content) {
		Date date = null;
		if (!StringUtils.isEmpty(content)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = sdf.parse(content);
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
		}
		return date;
	}

	//---加密--------------------------------------------------------

	/**
	 * md5
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' };
		try {
			MessageDigest md = MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}



	public static byte[] digest(byte[] pd, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(pd);
			return md.digest();
		} catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException("Digest the source failed.cause: "
					+ nsae.getMessage(), nsae);
		}
	}

	// -----------------------------------------------------------------------------


	/**
	 * MD5处理
	 * @param key the key to hash (variable length byte array)
	 * @return MD5 hash as a 32 character hex string.
	 */
	public static String getMD5AsHex(byte[] key) {
		return getMD5AsHex(key, 0, key.length);
	}

	/**
	 * MD5处理
	 * @param key the key to hash (variable length byte array)
	 * @param offset
	 * @param length
	 * @return MD5 hash as a 32 character hex string.
	 */
	private static String getMD5AsHex(byte[] key, int offset, int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key, offset, length);
			byte[] digest = md.digest();
			return new String(Hex.encodeHex(digest));
		} catch (NoSuchAlgorithmException e) {
			// this should never happen unless the JDK is messed up.
			throw new RuntimeException("Error computing MD5 hash", e);
		}
	}



    public static void main(String[] args) {
		List<String> nations = new ArrayList<String>();
		nations.add("China");
		nations.add("USA");
		nations.add("JPA");
		nations.add("RSA");

		int bound = 30;
		for(int i=1; i<=15; i++){
			String nation = getRandomElementRange(nations);

			String ct = formatDate4Timestamp(getRandomTimestamp(bound),"yyyyMMddHHmmss");

			System.out.println(nation+","+ct);
		}

    }

	/**
	 * 无异常状态调用 getFields() 方法
	 * @param o 输入的对象
	 * @return 输出对应格式的字符串
	 */
	public static String getFieldsV2(Object o) {
		try {
			return getFields(o);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将一个对象中所有的属性以 field1,field2,field3,...的格式输出
	 * @param o 输入的对象
	 * @return 输出对应格式的字符串
	 * @throws NoSuchMethodException 无getter方法异常
	 * @throws InvocationTargetException getter方法无法获取异常
	 * @throws IllegalAccessException 非法获取异常
	 * @author Gordon
	 */
	public static String getFields(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		StringBuilder builder = new StringBuilder();
		// 遍历所有属性
		Field[] field = o.getClass().getDeclaredFields();
		for (Field item : field) {
			//获取属性的名字
			String key = item.getName();
			// 获取java类的getter属性名称
			String javaGetterMethod = "get" + key.substring(0,1).toUpperCase() + key.substring(1);

			// 获取属性类型
			// class java.lang.String
			// class java.lang.Integer
			// class java.lang.Short
			// class java.lang.Double
			// class java.lang.Float
			// class java.lang.Boolean
			// class java.util.Date
			// int
			// short
			// double
			// float
			// boolean
			//String type = item.getGenericType().toString();

			// 获取属性的getter方法
			Method method;
			try {
				method = o.getClass().getMethod(javaGetterMethod);
			} catch (NoSuchMethodException e) {
				try {
					method = o.getClass().getMethod(key);
				} catch (NoSuchMethodException ex) {
					String message = String.format("Exception in thread \"%s\" %s: %s & %s",
							Thread.currentThread().getName(),
							ex.getClass().getName(), e.getMessage(), ex.getMessage());
					throw new NoSuchMethodException(message);
				}
			}

			// 获取属性值
			Object value = null;
			if (null != method) {
				value = method.invoke(o);
			}

			// 添加到StringBuilder 进行返回
			if (null != value) {
				builder.append(value.toString()).append(",");
			}
		}

		// 返回并截取末尾的", "
		int length = builder.length();
		return builder.substring(0, length - 1);
	}

	/**
	 * 无异常输出：通过指定的自定义类类型和该类所有字段的Object对象，创建对象并写入字段，同时返回
	 * @param objects 所有字段，要求顺序必须同类的全参构造器字段一致
	 * @param type 要写入的类
	 * @return 新建的类，以Object的形式进行返回
	 */
	public static Object setFieldsV2(Object[] objects, Class<?> type) {
		try {
			return setFields(objects, type);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 通过指定的自定义类类型和该类所有字段的Object对象，创建对象并写入字段，同时返回
	 * @param objects 所有字段，要求顺序必须同类的全参构造器字段一致
	 * @param type 要写入的类
	 * @return 新建的类，以Object的形式进行返回
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static Object setFields(Object[] objects, Class<?> type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		// 遍历所有属性
		Field[] field = type.getDeclaredFields();
		Class[] fieldClass = new Class[field.length];

		for (int i = 0; i < field.length; i++) {
			fieldClass[i] = field[i].getType();
		}

		// 3. 通过参数获取指定的构造方法
		//Constructor constructor = cls.getDeclaredConstructor();
		Constructor constructor = type.getDeclaredConstructor(fieldClass);

		constructor.setAccessible(false);

		// 通过指定的构造方法实例化对象，同时调用构造方法
		return constructor.newInstance(objects);
	}


	public static String getRandomNation() {
		return getRandomNation(Integer.MAX_VALUE);
	}

	public static String getRandomNation(int num) {
		StringBuilder builder = new StringBuilder();
		builder.append("China").append(",");
		builder.append("Japan").append(",");
		builder.append("Spain").append(",");
		builder.append("Korea").append(",");
		builder.append("Brazil").append(",");
		builder.append("Canada").append(",");
		builder.append("Greece").append(",");
		builder.append("America").append(",");

		builder.append("UN");
		String[] nation = builder.toString().split(",");
		int length = nation.length;
		int bound = Math.min(length, num);
		int index = new Random().nextInt(bound);
		return nation[index];
	}

}
