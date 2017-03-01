package style;

import java.util.ArrayList;

import style.tag.Tag;

public class Style {
	
	private int width;
	
	private ArrayList<TagNode> tags;

	public Style() {
		// TODO Auto-generated constructor stub
		tags = new ArrayList<TagNode>();
	}
	
	public static Style create(String str, int width)
	{
		Style sty = new Style();
		sty.parse(str);
		
		sty.width = width;
		
		return sty;
	}
	
	class TagNode{
		boolean start;
		int index;
		String tagName;
	}
	
	public void parse(String str)
	{
		TagNode tn = new TagNode();
		
		int index = 0;
		int stepIndex = 0;
		while(true)
		{
			if (stepIndex >= str.length())
			{
				break;
			}
			if (str.charAt(index) == '<')
			{
				int blank = str.indexOf(' ', index);
				String tagName = str.substring(index, blank);
				
				int findTagIndex = -1;
				for(int i=0; i<Tag.TAGS.length; i++)
				{
					if (Tag.TAGS[i].equals(tagName))
					{
						findTagIndex = i;
						break;
					}
				}
				
				if (findTagIndex > -1)
				{
					Tag tag = Tag.create(Tag.TAGS[findTagIndex]);
					index = tag.getPro(str, index);
					if (index >= -1)
					{
						index = Style.indexChatCanBlank(str, '>', index);
						if (index > -1)
						{
							tag.setStyle(str.substring(index));
							break;
						}
					}
				}
			}
			stepIndex++;
		}
		
		
		
//		Tag tag = 
		
		
		
//		Tag tag = Tag.tagStart(str);
//		if (tag != null)
//		{
//			tag.tagEnd();
//		}
	}
	
	/**
	 * 按顺序查找某个字符，只忽略字符前面的空格
	 * @param str
	 * @param ch
	 * @param fromindex
	 * @return
	 */
	public static int indexChatCanBlank(String str, char ch, int fromindex)
	{
		int index = fromindex;
		boolean find = false;
		while(str.charAt(index)==ch || str.charAt(index)==' ')
		{
			char cr = str.charAt(index);
			if (cr == ' ')
			{
				index ++;
			}
			else
			{
				find = true;
				index ++;
				break;
			}
		}
		if (!find)
		{
			return -1;
		}
		else
		{
			return index;
		}
	}
	
	public static int getProName(String str, int fromindex, String pro)
	{
		for(int i=0; i<pro.length(); i++)
		{
			if (fromindex < str.length())
			{
				if (pro.charAt(i) != str.charAt(fromindex))
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
			
			fromindex++;
		}
		
		return fromindex;
	}
	
	public int getTag(String str, int fromIndex)
	{
		int index = fromIndex;
		for(int i=0; i<Tag.TAGS.length; i++)
		{
			index = str.indexOf(Tag.TAGS[i], index);
			if (index > -1)
			{
				Tag tag = Tag.create(Tag.TAGS[i]);
				index = tag.getPro(str, index);
				if (index > -1)
				{
					index = Style.indexChatCanBlank(str, '>', index);
				}
				if (index > -1)
				{
					tag.setValueStartIndex(index);
					return index;
				}
				else
				{
					return -1;
				}
			}
		}
		
		return index;
	}

}
