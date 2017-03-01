package com.qq.engine.utils;


import java.util.Date;
import java.util.GregorianCalendar;

import com.qq.engine.graphics.GFont;

public class Utils {

	/**
	 * 获得字符串绘制的宽度
	 * 
	 * @param string
	 * @param font
	 * @return
	 */
	public static float getStringWidth(String string, GFont font)
	{
		return font.stringWidth(string);
	}
	
	public static String getStringByNumber(int num, int length)
	{
		StringBuffer sb = new StringBuffer();
		int count = getIntLength(num);
		for(int i=length; i>0; i--)
		{
			if (count < i)
			{
				sb.append("0");
			}
			else
			{
				sb.append(num);
				break;
			}
		}
		return sb.toString();
	}
	
	public static int getIntLength(int x){
		int length = 1;
		while(x/10>0){
			x /= 10;
			length++;
		}
		return length;
	}	

	/**
	 * 把秒转换成天，不足一天按一天算
	 * @param time
	 * @return
	 */
	public static int getDaysBySecond(int time)
	{
		int days = 0;
		int dayTime = 24 * 60 * 60;
		days = time / dayTime;
		return time%dayTime > 0?days+1:days;
	}

	/**
	 * 把秒转换成小时，少于1小时返回0
	 * @param time
	 * @return
	 */
	public static int getHoursBySecond(int time)
	{
		int hours = 0;
		int hourTime = 60 * 60;
		hours = time / hourTime;
		return time%hourTime > 0 && hours>0?hours+1:hours;
	}
	
	/**
	 * 把秒转换成分钟，少于1分钟按1分钟计算
	 * @param time
	 * @return
	 */
	public static int getMinutesBySecond(int time)
	{
		int minutes = 0;
		int minTime = 60;
		minutes = time / minTime;
		return time%minTime > 0?minutes+1:minutes;
	}
	

	public static int pow2(int value)
	{
		int small = (int)(Math.log ((double)value) / Math.log (2.0f));
		if ((1 << small) >= value) {
			return 1 << small;
		} else {
			return 1 << (small + 1);
		}
	}
	
	/**
	 * 根据毫秒返回日期格式
	 * @param time
	 * @param format 日期格式类型  YYYY-MM-DD HH:MM:SS
	 * @return
	 */
	public static String getDataFormat(long time, String format)
	{
		Date dat = new Date(time);  
        GregorianCalendar gc = new GregorianCalendar();   
        gc.setTime(dat);  
        java.text.SimpleDateFormat dFormat = new java.text.SimpleDateFormat(format);  
        
        return dFormat.format(gc.getTime());
	}
	
}
