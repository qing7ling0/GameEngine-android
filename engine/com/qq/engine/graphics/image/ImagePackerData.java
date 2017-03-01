package com.qq.engine.graphics.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.stream.JsonReader;
import com.qq.engine.drawing.Rectangle;
import com.qq.engine.drawing.SizeF;
import com.qq.engine.utils.Debug;

public class ImagePackerData {
	
	public final static String FILE_TYPE = ".json";
	
	public ArrayList<ImageRegion> imgs;
	public String key;
	private int refCount;
	public boolean createOk;
	public float scale;

	public static ImagePackerData create(String key, InputStream packerIs)
	{
		ImagePackerData data = ImagePackerCache.getInstance().getImagePackerDataByKey(key);
		if (data == null)
		{
//			long time = System.currentTimeMillis();
			data = new ImagePackerData(key, packerIs);
//			Debug.e(data.getClass().getSimpleName(), "  parse josn time=", System.currentTimeMillis()-time);
		}
		data.retain();
		return data;
	}
	
	public ImagePackerData(String key, InputStream packerIs) {
		// TODO Auto-generated constructor stub
		this.key = key;
		this.createOk = true;
		if (packerIs == null)
		{
			createOk = false;
			Debug.e(this.getClass().getSimpleName(), "  create error json= null key=", key);
			return;
		}
//		String value = readFile(packerIs);
		try {
			imgs = new ArrayList<ImageRegion>();
			
			JsonReader reader = new JsonReader(new InputStreamReader(packerIs));
			reader.beginObject();
			reader.nextName();
			reader.beginArray();
			while (reader.hasNext()) {
				imgs.add(readerImage(reader));
			}
			reader.endArray();

			reader.nextName();
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("app"))
				{
					reader.nextString();
				}
				else if (name.equals("version"))
				{
					reader.nextString();
				}
				else if (name.equals("image"))
				{
					reader.nextString();
				}
				else if (name.equals("format"))
				{
					reader.nextString();
				}
				else if (name.equals("size"))
				{
					readSize(reader);
				}
				else if (name.equals("scale"))
				{
					scale = (float) reader.nextDouble();
				}
				else if (name.equals("smartupdate"))
				{
					reader.nextString();
				}
			}
			reader.endObject();
			reader.endObject();
			
//			JsonObject js = json.parseObject(value);
//			
//			JSONObject meta = js.getJSONObject("meta");
//			this.scale = meta.getFloatValue("scale");
//			
//			JSONArray frames = js.getJSONArray("frames");
//			int count = regions.size();
//			imgs = new ImageRegion[count];
//			for(int i=0; i<count; i++)
//			{
//				imgs[i] = new ImageRegion();
//				JSONObject frame = frames.getJSONObject(i);
//				imgs[i].filename = frame.getString("filename");
//				imgs[i].frameRect = readRect(frame.getJSONObject("frame"));
//				imgs[i].rotated = frame.getBoolean("rotated");
//				imgs[i].trimmed = frame.getBoolean("trimmed");
//				imgs[i].spriteSourceSize = readRect(frame.getJSONObject("spriteSourceSize"));
//				imgs[i].sourceSize = readSize(frame.getJSONObject("sourceSize"));
//			}
			ImagePackerCache.getInstance().add(this);
		} catch (Exception e) {
			e.printStackTrace();
			Debug.initError(e, false);
		}
//		Debug.e(this.getClass().getSimpleName(), "  readJSON time=", System.currentTimeMillis()-time, "  key=", key);
	}
	
	private ImageRegion readerImage(JsonReader reader) throws IOException
	{
		ImageRegion img = new ImageRegion();
		reader.beginObject();
		while(reader.hasNext())
		{
			reader.nextName();
			img.filename = reader.nextString();

			reader.nextName();
			img.frameRect = readRect(reader);
			
			reader.nextName();
			img.rotated = reader.nextBoolean();
			
			reader.nextName();
			img.trimmed = reader.nextBoolean();

			reader.nextName();
			img.spriteSourceSize = readRect(reader);

			reader.nextName();
			img.sourceSize = readSize(reader);
		}
		reader.endObject();
		
		return img;
	}
	
	private Rectangle readRect(JsonReader reader) throws IOException
	{
		Rectangle rect = new Rectangle();
		reader.beginObject();
		while(reader.hasNext())
		{
			reader.nextName();
			rect.x = reader.nextInt();
			reader.nextName();
			rect.y = reader.nextInt();
			reader.nextName();
			rect.w = reader.nextInt();
			reader.nextName();
			rect.h = reader.nextInt();
		}
		reader.endObject();
		
		return rect;
	}
	
	private SizeF readSize(JsonReader reader) throws IOException
	{
		SizeF size = new SizeF();
		reader.beginObject();
		while(reader.hasNext())
		{
			reader.nextName();
			size.width = reader.nextInt();
			reader.nextName();
			size.height = reader.nextInt();
		}
		reader.endObject();
		
		return size;
	}

//	private Rectangle readRect(com.alibaba.fastjson.JSONObject json)
//	{
//		Rectangle rect = new Rectangle();
//		try {
//			rect.x = json.getIntValue("x");
//			rect.y = json.getIntValue("y");
//			rect.w = json.getIntValue("w");
//			rect.h = json.getIntValue("h");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return rect;
//	}
//	
//	private SizeF readSize(JSONObject json)
//	{
//		SizeF size = new SizeF();
//		try {
//			size.width = json.getIntValue("w");
//			size.height = json.getIntValue("h");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return size;
//	}
	
	public String readFile(InputStream is)
	{
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try
		{
			reader = new BufferedReader(new InputStreamReader(is));
			String tempString = null;
			while ((tempString = reader.readLine()) != null)
			{
				sb.append(tempString);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public void retain()
	{
		refCount ++;
	}

	public void recycle()
	{
		refCount --;
		if (refCount <= 0)
		{
			imgs = null;
			ImagePackerCache.getInstance().remove(this);
		}
	}
}
