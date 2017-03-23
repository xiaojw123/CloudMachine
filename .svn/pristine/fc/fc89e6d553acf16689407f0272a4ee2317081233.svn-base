package com.cloudmachine.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具箱
 * 
 * @author AFK
 */
public final class StringUtil {
	/**
	 * 将一个字符串的首字母改为大写或者小写
	 * 
	 * @param srcString
	 *            源字符串
	 * @param flag
	 *            大小写标识，ture小写，false大些
	 * @return 改写后的新字符串
	 */
	public static String toLowerCaseInitial(String srcString, boolean flag) {
		StringBuilder sb = new StringBuilder();
		if (flag) {
			sb.append(Character.toLowerCase(srcString.charAt(0)));
		} else {
			sb.append(Character.toUpperCase(srcString.charAt(0)));
		}
		sb.append(srcString.substring(1));
		return sb.toString();
	}

	/**
	 * 将一个字符串按照句点（.）分隔，返回最后一段
	 * 
	 * @param clazzName
	 *            源字符串
	 * @return 句点（.）分隔后的最后一段字符串
	 */
	public static String getLastName(String clazzName) {
		String[] ls = clazzName.split("\\.");
		return ls[ls.length - 1];
	}

	/**
	 * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号。
	 * 
	 * @param path
	 *            文件路径
	 * @return 格式化后的文件路径
	 */
	public static String formatPath(String path) {
		String reg0 = "\\\\＋";
		String reg = "\\\\＋|/＋";
		String temp = path.trim().replaceAll(reg0, "/");
		temp = temp.replaceAll(reg, "/");
		if (temp.endsWith("/")) {
			temp = temp.substring(0, temp.length() - 1);
		}
		if (System.getProperty("file.separator").equals("\\")) {
			temp = temp.replace('/', '\\');
		}
		return temp;
	}

	/**
	 * 格式化文件路径，将其中不规范的分隔转换为标准的分隔符,并且去掉末尾的"/"符号(适用于FTP远程文件路径或者Web资源的相对路径)。
	 * 
	 * @param path
	 *            文件路径
	 * @return 格式化后的文件路径
	 */
	public static String formatPath4Ftp(String path) {
		String reg0 = "\\\\＋";
		String reg = "\\\\＋|/＋";
		String temp = path.trim().replaceAll(reg0, "/");
		temp = temp.replaceAll(reg, "/");
		if (temp.endsWith("/")) {
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	public static void main(String[] args) {
		System.getProperties();
	}

	/**
	 * 获取文件父路径
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件父路径
	 */
	public static String getParentPath(String path) {
		return new File(path).getParent();
	}

	/**
	 * 获取相对路径
	 * 
	 * @param fullPath
	 *            全路径
	 * @param rootPath
	 *            根路径
	 * @return 相对根路径的相对路径
	 */
	public static String getRelativeRootPath(String fullPath, String rootPath) {
		String relativeRootPath = null;
		String _fullPath = formatPath(fullPath);
		String _rootPath = formatPath(rootPath);

		if (_fullPath.startsWith(_rootPath)) {
			relativeRootPath = fullPath.substring(_rootPath.length());
		} else {
			throw new RuntimeException("要处理的两个字符串没有包含关系，处理失败！");
		}
		if (relativeRootPath == null)
			return null;
		else
			return formatPath(relativeRootPath);
	}

	/**
	 * 获取当前系统换行符
	 * 
	 * @return 系统换行符
	 */
	public static String getSystemLineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 将用“|”分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
	 * 
	 * @param series
	 *            将用“|”分隔的字符串
	 * @return 字符串集合列表
	 */
	public static List<String> series2List(String series) {
		return series2List(series, "\\|");
	}

	/**
	 * 将用正则表达式regex分隔的字符串转换为字符串集合列表，剔除分隔后各个字符串前后的空格
	 * 
	 * @param series
	 *            用正则表达式分隔的字符串
	 * @param regex
	 *            分隔串联串的正则表达式
	 * @return 字符串集合列表
	 */
	private static List<String> series2List(String series, String regex) {
		List<String> result = new ArrayList<String>();
		if (series != null && regex != null) {
			for (String s : series.split(regex)) {
				if (s.trim() != null && !s.trim().equals(""))
					result.add(s.trim());
			}
		}
		return result;
	}

	/**
	 * @param strList
	 *            字符串集合列表
	 * @return 通过“|”串联为一个字符串
	 */
	public static String list2series(List<String> strList) {
		StringBuffer series = new StringBuffer();
		for (String s : strList) {
			series.append(s).append("|");
		}
		return series.toString();
	}

	/**
	 * 将字符串的首字母转为小写
	 * 
	 * @param resStr
	 *            源字符串
	 * @return 首字母转为小写后的字符串
	 */
	public static String firstToLowerCase(String resStr) {
		if (resStr == null) {
			return null;
		} else if ("".equals(resStr.trim())) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			Character c = resStr.charAt(0);
			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c))
					c = Character.toLowerCase(c);
				sb.append(resStr);
				sb.setCharAt(0, c);
				return sb.toString();
			}
		}
		return resStr;
	}

	/**
	 * 将字符串的首字母转为大写
	 * 
	 * @param resStr
	 *            源字符串
	 * @return 首字母转为大写后的字符串
	 */
	public static String firstToUpperCase(String resStr) {
		if (resStr == null) {
			return null;
		} else if ("".equals(resStr.trim())) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			Character c = resStr.charAt(0);
			if (Character.isLetter(c)) {
				if (Character.isLowerCase(c))
					c = Character.toUpperCase(c);
				sb.append(resStr);
				sb.setCharAt(0, c);
				return sb.toString();
			}
		}
		return resStr;
	}

	public static String getTelphone(String contactInfo) {
		String telpone = null;
		int startPosition = contactInfo.indexOf(":") + 1; // msg.indexOf(":")+1;
		int endPosition = 0; // msg.indexOf("\n");

		if (contactInfo.contains("\n")) {
			endPosition = contactInfo.indexOf("\n");
		} else {
			endPosition = contactInfo.length();
		}
		telpone = contactInfo.substring(startPosition, endPosition);
		return telpone;
	}
	
    /* 邮箱合法性的检查 */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
    
    /* 验证手机号码的合法性 */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    
    public static boolean canDecode(InputStream input, Charset charset) throws IOException {   
        ReadableByteChannel channel = Channels.newChannel(input);   
        CharsetDecoder decoder = charset.newDecoder();   

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);   
        CharBuffer charBuffer = CharBuffer.allocate(1024);   

        boolean endOfInput = false;   
        while (!endOfInput) {   
            int n = channel.read(byteBuffer);   
            byteBuffer.flip(); // flip so it can be drained   
               
            endOfInput = (n == -1);   
            CoderResult coderResult = decoder.decode(byteBuffer, charBuffer, endOfInput);   
            charBuffer.clear();   
            if (coderResult == CoderResult.OVERFLOW) {   
                while (coderResult == CoderResult.OVERFLOW) {   
                    coderResult = decoder.decode(byteBuffer, charBuffer, endOfInput);   
                    charBuffer.clear();   
                }   
            }   
            if (coderResult.isError()) {   
                return false;   
            }   
            byteBuffer.compact(); // compact so it can be refilled   
        }   
        CoderResult coderResult;   
        while ((coderResult = decoder.flush(charBuffer)) == CoderResult.OVERFLOW) {   
            charBuffer.clear();   
        }   
        if (coderResult.isError()) {   
            return false;   
        }   
           
        return true;   
    } 
    
    /** 
    * 使用指定的字符集解码字节输入流，并将它写入到字符输出流中，如果发生解码错误则返回false，否则返回true， 
    * 输入中的无效字节序列将被忽略。 
    */ 
    public static boolean decode(InputStream input, Writer output, Charset charset) throws IOException {   
        ReadableByteChannel channel = Channels.newChannel(input);   
        CharsetDecoder decoder = charset.newDecoder();   

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);   
        CharBuffer charBuffer = CharBuffer.allocate(1024);   

        boolean endOfInput = false;   
        boolean error = false;   
        while (!endOfInput) {   
            int n = channel.read(byteBuffer);   
            byteBuffer.flip(); // flip so it can be drained   
               
            endOfInput = (n == -1);   
            CoderResult coderResult = decoder.decode(byteBuffer, charBuffer, endOfInput);   
            error = drainCharBuffer(error, byteBuffer, charBuffer, coderResult, output);   
            if (coderResult != CoderResult.UNDERFLOW) {   
                while (coderResult != CoderResult.UNDERFLOW) {   
                    coderResult = decoder.decode(byteBuffer, charBuffer, endOfInput);   
                    error = drainCharBuffer(error, byteBuffer, charBuffer, coderResult, output);   
                }   
            }   
            byteBuffer.compact(); // compact so it can be refilled   
        }   
        CoderResult coderResult;   
        while ((coderResult = decoder.flush(charBuffer)) != CoderResult.UNDERFLOW) {   
            error = drainCharBuffer(error, byteBuffer, charBuffer, coderResult, output);   
        }   
        error = drainCharBuffer(error, byteBuffer, charBuffer, coderResult, output);   
           
        output.flush();   
        return !error;   
    }   
    
    private static boolean drainCharBuffer(boolean error, ByteBuffer byteBuffer,    
            CharBuffer charBuffer, CoderResult coderResult, Writer output) throws IOException {   
        // write charBuffer to output   
        charBuffer.flip();   
        if (charBuffer.hasRemaining())   
            output.write(charBuffer.toString());   
        charBuffer.clear();   
           
        if (coderResult.isError()) {   
            error = true;   
            byteBuffer.position(byteBuffer.position() + coderResult.length()); // ignore invalid byte sequence   
        }   
        return error;   
    }
}