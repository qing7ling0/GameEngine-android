package com.qq.engine.net;

import java.io.UnsupportedEncodingException;

import com.qq.engine.utils.Debug;


/**
 * 编码和格式转换类
 * 
 * @author wuqingqing
 */
public class Encoder {
	/**
	 * 从packetBody数组的offset位置读取一个short整数
	 * 
	 * @param offset
	 * @param packetBody
	 * @return
	 */
	public static short readShort(int offset, byte[] packetBody) {
		return getShortFrom2Byte(packetBody[offset++], packetBody[offset]);
	}
	
	/**
	 * 把两个字节组合为一个short数字
	 * 
	 * @param high8bit
	 *            short数字的高8位
	 * @param low8bit
	 *            short数字的低8位
	 * @return
	 */
	public static short getShortFrom2Byte(byte high8bit, byte low8bit) {
		return (short) ((low8bit << 8) | high8bit & 0xFF);
	}

	/**
	 * 包装一个UTF8字符串
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] getBytes(String input) {
//		byte[] temp_input = null;
//		try {
//			temp_input = input.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		byte[] result = new byte[temp_input.length + 2];
//
//		result[0] = (byte) temp_input.length;// 字符串长度
//
//		System.arraycopy(temp_input, 0, result, 1, temp_input.length);
//
//		result[result.length - 1] = 'E';
//		return result;
		return string2Bytes(input);
	}
	

	/**
	 * 返回GBK编码的字符长度（调用String默认方法)
	 * @param str
	 * @return 
	 */
	public static byte[] getBytes_GBK(String str){
		try {
			return str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			return str.getBytes();
//			e.printStackTrace();
			
		}
//		return null;
	}

	/**
	 * 从packetBody的offset位置读取一个字符串
	 * 
	 * @param offset
	 * @param packetBody
	 * @return
	 */
	public static String readString(int offset, byte[] packetBody) {
		int length = readShort(offset, packetBody);
		offset += 2;
		String result = null;
		try {
			result = new String(packetBody, offset, length, "UTF-8");//, "UTF-8"
		} catch (Exception e) {
			e.printStackTrace();
			Debug.initError(e, false);
		}
		result = result.trim();
		// Util.showMsg(result);
		return result;
	}
	
	/**
	 * 从packetBody的offset位置读取一个字符串
	 * 
	 * @param offset
	 * @param packetBody
	 * @return
	 */
	public static String readServerIP(int offset, byte[] packetBody) {

		int length = packetBody[offset++];
		if(length < 0)	length = 256 + length;

		String result = null;
		result=new String(packetBody, offset, length);
//		Util.errorMsg=""+result;
		result = result.trim();
		// Util.showMsg(result);
		return result;
	}

	/**
	 * 将一个short整数转换为一个byte数组
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] short2Bytes(short input) {
		byte[] result = new byte[2];
		result[0] = (byte) (input & 0xFF);
		result[1] = (byte) ((input >> 8) & 0xFF);
		return result;
	}
	
	public static byte[] packet2Bytes(Packet out)
	{

		byte[] b = new byte[12+out.getLength()];
		
		System.arraycopy(Encoder.short2Bytes((short)out.getType()), 0, b,0,2);
		if (out.getBody() != null) {
			System.arraycopy(Encoder.int2Bytes(out.getLength()), 0, b,2,4);
		} else {
			System.arraycopy(Encoder.int2Bytes(0), 0, b,2,4);
		}
		System.arraycopy(Encoder.int2Bytes(out.getId()), 0, b,6,4);
		b[10] = out.getOption();
		b[11] = out.getCallbackID();
		if(out.getBody() != null) {
			System.arraycopy(out.getBody(), 0, b, 12, b.length-12);
		}
		return b;
	}
	
	// 整数到字节数组的转换
	public static byte[] int2Bytes(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (temp & 0xff);
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * 从byte数组中读取一个int类型,Little_Indian编码
	 * @param offset
	 * @param packetBody
	 * @return 
	 */
	public static int readInt(int offset, byte[] packetBody) {
		int result = 0;
		int t1 = packetBody[offset++] & 0xff;
		int t2 = (packetBody[offset++] << 8) & 0xff00;
		int t3 = (packetBody[offset++] << 16) & 0xff0000;
		int t4 = (packetBody[offset++] << 24) & 0xff000000;
		result = t1 | t2 | t3 | t4;
		return result;
	}
	
	public static byte[] string2Bytes(String str) {
		try {
			byte[] strbytes = str.getBytes("UTF-8");
			short length = (short) strbytes.length;
			byte[] returnbytes = new byte[length + 2];
			returnbytes[0] = (byte) (length & 0xFF);
			returnbytes[1] = (byte) ((length >> 8) & 0xFF);
			System.arraycopy(strbytes, 0, returnbytes, 2, length);
			return returnbytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
