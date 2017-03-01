package style.tag;

import java.util.ArrayList;

import style.Style;
import style.property.Property;

public class Tag {

	public final static String TAG_IMG = "img";
	public final static String TAG_ENTER = "n";
	public final static String TAG_TEXT = "text";
	public final static String TAG_STYLE = "style";

	public final static String[] TAGS = new String[]{TAG_IMG, TAG_ENTER, TAG_TEXT, TAG_STYLE};
	private String value;
	private ArrayList<Property> pros;
	private Tag parent;
	private int valueStartIndex;
	private int valueEndIndex;
	private boolean end;
	private String style;
	
	private ArrayList<Tag> childens;
	
	public Tag() {
		// TODO Auto-generated constructor stub
		pros = new ArrayList<Property>();
		childens = new ArrayList<Tag>();
	}
	
	public static Tag create(String tag)
	{
		if (tag == TAG_IMG)
		{
			return new ImageTag();
		}
		else if (tag == TAG_ENTER)
		{
			return new EnterTag();
		}
		else if (tag == TAG_STYLE)
		{
			return new StyleTag();
		}
		else if (tag == TAG_TEXT)
		{
			return new TextTag();
		}
		
		return null;
	}
	
	public int getPro(String str, int fromindex)
	{
		int index = fromindex;
		
		for(int i=0; i<Property.pros.length; i++)
		{
			String pro = Property.pros[i];

			index = Style.getProName(str, fromindex, pro);
			if (index > -1)
			{
				Property p = Property.createPro(pro);
				index = p.getValue(str, index);
				if (index > -1)
				{
					pros.add(p);
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		
		return index;
	}
	
	
	
	public static Tag tagStart(String str)
	{
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
		
		return null;
	}
	
	public void tagEnd()
	{
		String str = style;
		
//		Tag tag = Tag.tagStart(str);
//		if (tag != null)
//		{
//			childens.add(tag);
//			tag.tagEnd();
//		}
//		
//		for(int i=0; i<Tag.TAGS.length; i++)
//		{
//			int index = str.indexOf(Tag.TAGS[i]);
//			if (index > -1)
//			{
//				Tag tag = Tag.create(Tag.TAGS[i]);
//				index = tag.getPro(str, index);
//				if (index > -1)
//				{
//					index = Styles.indexChatCanBlank(str, '>', index);
//					if (index > -1)
//					{
//						tag.setValueStartIndex(index);
//					}
//				}
//			}
//		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<Property> getPros() {
		return pros;
	}

	public void setPros(ArrayList<Property> pros) {
		this.pros = pros;
	}

	public Tag getParent() {
		return parent;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	public int getValueStartIndex() {
		return valueStartIndex;
	}

	public void setValueStartIndex(int valueStartIndex) {
		this.valueStartIndex = valueStartIndex;
	}

	public int getValueEndIndex() {
		return valueEndIndex;
	}

	public void setValueEndIndex(int valueEndIndex) {
		this.valueEndIndex = valueEndIndex;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
