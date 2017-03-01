package com.qq.engine.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import com.qq.engine.utils.Debug;
import com.qq.engine.utils.Utils;

/**
 * 日志系统
 * @author wuqingqing
 *
 */
public class LogOut implements Comparator<File> {

	private static LogOut instance;
	
	private ArrayList<String> messages = new ArrayList<String>();
	
	private final static int MAX_COUNT = 300;
	
	private final static int MAX_LOG_FILE_COUNT = 20;
	
	public static LogOut getInstance()
	{
		if (instance == null)
		{
			instance = new LogOut();
		}
		
		return instance;
	}
	
	private LogOut() {
	}
	
	public void addLog(String log)
	{
		synchronized(messages)
		{
	        String time = Utils.getDataFormat(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss ----- ");  
	        
			messages.add(time+log);
			if(messages.size() > MAX_COUNT)
			{
				messages.remove(0);
			}
		}
	}

	public void save(String path)
	{
		save(path, false);
	}

	public void save(String path, boolean force)
	{
		if (messages.size()==0) return;
		
		// 日志写满才保存
		if (messages.size()>= MAX_COUNT || force)
		{
			FileOutputStream fos = null;
			PrintWriter pw = null;
			path = path+File.separator+"log";
			try {
				// 初始化环境
				initFile(path);
		        
		        // 根据时间创建文件名
		        String fileName = Utils.getDataFormat(System.currentTimeMillis(), "yyyyMMdd hhmmss")+".txt";  
				
				fos = new FileOutputStream( new File(path, fileName), false );
				pw  = new PrintWriter(fos);

				// 写入数据
				for(String str : messages)
				{
					pw.write(str);
					pw.write("\n");
				}
				pw.flush();
				messages.clear();
//				Debug.i(this.getClass().getSimpleName(), "  save ", path, " count=", messages.size());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally
			{
				try {
					if (fos != null) fos.close();
					if (pw != null) pw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 初始化日志环境
	 * 检测日志文件个数，保证不能超过最大个数
	 * @param path
	 */
	private void initFile(String path)
	{
		File logDir = new File(path);
		if (!logDir.exists())
		{
			logDir.mkdir();
		}
		else
		{
			logDir.mkdir();
			File[] files = logDir.listFiles();
			
			Arrays.sort(files, this);
			
			// 最大保存MAX_LOG_FILE_COUNT个日志文件
			for(int i=0; i<files.length; i++)
			{
				if (i >= MAX_LOG_FILE_COUNT)
				{
					if (files[i].exists())
					{
						files[i].delete();
					}
				}
			}
		}
	}
	

	@Override
	public int compare(File file1, File file2) {
		long result = file2.lastModified() - file1.lastModified();
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		} else {
			return 0;
		}
	}  
}
