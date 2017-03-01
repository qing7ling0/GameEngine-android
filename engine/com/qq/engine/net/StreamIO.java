package com.qq.engine.net;

import com.qq.engine.utils.Debug;

public class StreamIO {
	/**  */
	private int length = 0;
	/**  */
	private int pointer;
	/***/
	private byte[] body;
	
	public void init(){
		length = 0;
		pointer = 0;
		body = null;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public byte[] getTrimBody()
	{
		byte[] bytes = new byte[length];
		System.arraycopy(body, 0, bytes, 0, length);
		return bytes;
	}

	public void setBody(byte[] body) {
		this.body = body;
		if(body != null)
		{
			setLength(body.length);
		}
	}
	
	public int getLength() {
		return length;
	}

	protected void setLength(int length) {
		this.length = length;
	}

	public int getPointer() {
		return pointer;
	}

	public void skip(int skipLength) {
		this.pointer += skipLength;
	}
	
	/**
	 * byte
	 * 
	 * @return
	 */
	public byte decodeByte() {
		byte result = body[getPointer()];
		skip(1);
		return result;
	}
	
	public boolean decodeBoolean() {
		byte result = body[getPointer()];
		skip(1);
		return result!=0;
	}

	/**
	 * short
	 * 
	 * @return
	 */
	public short decodeShort() {
		short result = Encoder.readShort(getPointer(), body);
		skip(2);
		return result;
	}

	/**
	 * short,int
	 * 
	 * @return
	 */
	public int decodeShort2Int() {
		return decodeShort() & 0xFFFF;
	}

	public int decodeInt(){
		int result = Encoder.readInt(getPointer(), body);
		skip(4);
		return result;
	}

	/**
	 * String
	 * 
	 * @return
	 */
	public String decodeString() {
		int length = decodeShort();
		String result = Encoder.readString(getPointer()-2, body);
		skip(length);
		return result;
	}

	/**
	 * String
	 * 
	 * @return
	 */
	public String decodeString(int strlength) {
		String result = null;
		try {
			if(body[getPointer()] == -17&&body[getPointer()+1] == -69&&body[getPointer()+2] == -65){
				skip(3);
				strlength-=3;
			}
			result = new String(body, getPointer(), strlength, "UTF-8");
			result = result.trim();
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
			Debug.initError(e, false);
		}
		skip(strlength);
		return result;
	}

	public void enter(byte[] bytes)
	{
		if(body == null)
		{
			body = bytes;
		}
		else if(body.length < pointer+bytes.length)
		{
			byte[] temp = new byte[pointer+bytes.length];
			System.arraycopy(body, 0, temp, 0, pointer);
			System.arraycopy(bytes, 0, temp, pointer, bytes.length);
			body = temp;
		}
		else
		{
			System.arraycopy(bytes, 0, body, pointer, bytes.length);
		}
		pointer += bytes.length;
		setLength(pointer);
	}
	
	public void enter(String str)
	{
		byte[] bytes = Encoder.getBytes(str);
//System.out.println(bytes.length);
		enter(bytes);
	}

	public void enter(byte onebyte)
	{
		byte[] bytes = new byte[1];
		bytes[0] = onebyte;
		enter(bytes);
	}
	
	public void enter(boolean bln)
	{
		byte[] bytes = new byte[1];
		bytes[0] = (byte)(bln?1:0);
		enter(bytes);
	}
	
	public void enter(int i)
	{
		enter(Encoder.int2Bytes(i));
	}
	
	public void enter(short s)
	{
		enter(Encoder.short2Bytes(s));
	}
	
	public byte[] decodeBytes(int count)
	{
		byte[] bytes = new byte[count];
		for(int i = 0; i < count; i++)
		{
			bytes[i]	= decodeByte();
		}
		return bytes;
	}
	
	public short[] decodeShorts(int count)
	{
		short[] shorts = new short[count];
		for(int i = 0; i < count; i++)
		{
			shorts[i]	= decodeShort();
		}
		return shorts;
	}
	
	public int[] decodeInts(int count)
	{
		int[] ints = new int[count];
		for(int i = 0; i < count; i++)
		{
			ints[i]	= decodeInt();
		}
		return ints;
	}
	
	public String[] decodeStrings(int count)
	{
		String[] ints = new String[count];
		for(int i = 0; i < count; i++)
		{
			ints[i]	= decodeString();
		}
		return ints;
	}
}
