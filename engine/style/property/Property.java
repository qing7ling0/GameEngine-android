package style.property;

import style.Style;

public class Property {

	public final static String PRO_ALIGN = "align";
	public final static String PRO_COLOR = "color";
	public final static String PRO_SIZE = "size";
	public final static String PRO_URL = "url";
	
	
	public final static String[] pros = new String[]{PRO_ALIGN, PRO_COLOR, PRO_SIZE, PRO_URL};

	public String proName;
	public String proValue;
	
	public Property(String pro) {
		// TODO Auto-generated constructor stub
		this.proName = pro;
		this.proValue="";
	}
	
	public static Property createPro(String pro)
	{
		if (pro != null)
		{
			if (pro == PRO_ALIGN)
			{
				return new AlignPro(pro);
			}
			else if (pro == PRO_COLOR)
			{
				return new ColorPro(pro);
			}
			else if (pro == PRO_SIZE)
			{
				return new SizePro(pro);
			}
			else if (pro == PRO_URL)
			{
				return new UrlPro(pro);
			}
		}
		
		return null;
	}
	
	public int getValue(String text, int fromIndex)
	{
		proValue = "";
		int index = Style.indexChatCanBlank(text, '=', fromIndex);
		
		// 没有找到'='
		if (index==-1)
		{
			return -1;
		}
		else
		{
			int startindex = text.indexOf("\"", index);
			if(startindex == -1)
			{
				return -1;
			}
			else
			{
				int endindex = text.indexOf("\"", startindex);
				if (endindex < -1)
				{
					return -1;
				}
				else
				{
					proValue = text.substring(startindex, endindex);
					return endindex;
				}
			}
		}
	}

}
