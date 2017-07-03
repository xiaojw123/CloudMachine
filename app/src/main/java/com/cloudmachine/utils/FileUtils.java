package com.cloudmachine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.TextUtils;

import com.github.mikephil.charting.utils.AppLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author
 * @version 1.0
 * @fileName FileUtils.java
 * @package com.util
 * @description 文件工具类
 * @email
 */
public class FileUtils {

    //	public static String SDPATH = Environment.getExternalStorageDirectory()
//			+ "/Photo_LJ/";
    public static String SDPATH = Environment.getExternalStorageDirectory().getPath()
            + "/Photo_LJ/";

    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹的路径
     */
    public static void delFolder(String folderPath) {
        delAllFile(folderPath);
        String filePath = folderPath;
        filePath = filePath.toString();
        File myFilePath = new File(filePath);
        myFilePath.delete();
    }

    public static void delOneFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param path 文件的路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
            }
        }
    }

    /**
     * 获取文件的Uri
     *
     * @param path 文件的路径
     * @return
     */
    public static Uri getUriFromFile(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    /**
     * 换算文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "未知大小";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     */
    public InputStream getInputStreamFromURL(String urlStr) {
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    public static void zip(String inputFileName, String zipFileName)
            throws Exception {
        // String zipFileName="c:\\test.zip";//打包后文件名字
        zip(zipFileName, new File(inputFileName));
    }

    private static void zip(String zipFileName, File inputFile)
            throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base)
            throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    /**
     * 删除一个文件或者目录
     *
     * @param targetPath 文件或者目录路径
     * @IOException 当操作发生异常时抛出
     */
    public static void deleteFile(String targetPath) throws IOException {
        File targetFile = new File(targetPath);
        if (targetFile.isDirectory()) {
            deleteDirectory(targetFile);
        } else if (targetFile.isFile()) {
            targetFile.delete();
        }
    }

    /**
     * 删除一个目录
     *
     * @param dir 目录路径
     * @IOException 当操作发生异常时抛出
     */
    public static void deleteDirectory(File dir) throws IOException {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            dir.delete();
        } else if (dir.isDirectory()) {
            String[] list = dir.list();
            for (String string : list) {
                deleteFile(dir.getAbsolutePath() + "/" + string);
            }
            dir.delete();
        }
    }

    /**
     * 重命名文件或文件夹
     *
     * @param resFilePath 源文件路径
     * @param newFileName 重命名
     * @return 操作成功标识
     */
    public static boolean renameFile(String resFilePath, String newFileName) {
        String newFilePath = StringUtil.formatPath(StringUtil
                .getParentPath(resFilePath) + "/" + newFileName);
        File resFile = new File(resFilePath);
        File newFile = new File(newFilePath);
        return resFile.renameTo(newFile);
    }

    /**
     * 判断一个文件是否存在
     *
     * @param filePath 文件路径
     * @return 存在返回true，否则返回false
     */
    public static boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
     *
     * @param res      原字符串
     * @param filePath 文件路径
     * @return 成功标记
     */
    public static boolean string2File(String res, String filePath) {
        boolean flag = true;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            File distFile = new File(filePath);
            if (!distFile.getParentFile().exists())
                distFile.getParentFile().mkdirs();
            bufferedReader = new BufferedReader(new StringReader(res));
            bufferedWriter = new BufferedWriter(new FileWriter(distFile));
            char buf[] = new char[1024]; // 字符缓冲区
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * HTML标签过滤
     *
     * @param html HTML代码
     * @return 过滤后的HTML代码
     */
    public static String html2Text(String html) {
        Pattern p_html, p_html1, p_space;
        Matcher m_html, m_html1, m_space;

        try {
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            String regEx_space = "&nbsp;";

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(html);
            html = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(html);
            html = m_html1.replaceAll(""); // 过滤html标签

            p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
            m_space = p_space.matcher(html);
            html = m_space.replaceAll(" "); // 过滤html标签

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return html;// 返回文本字符串
    }

    public static String html2TextTwo(String inputString) {
        try {
            return Html.fromHtml(inputString).toString();
        } catch (Exception e) {
            return "";
        }
            /*String textStr = "";
			 if(TextUtils.isEmpty(inputString)){
				 textStr = "";
			 }else if(inputString.contains("<<")){
				 textStr = inputString;
			 }else{
	        String htmlStr = inputString; // 含html标签的字符串      
	            
	        java.util.regex.Pattern p_script;      
	        java.util.regex.Matcher m_script;      
	        java.util.regex.Pattern p_style;      
	        java.util.regex.Matcher m_style;      
	        java.util.regex.Pattern p_html;      
	        java.util.regex.Matcher m_html;      
	    
	        java.util.regex.Pattern p_html1;      
	        java.util.regex.Matcher m_html1;      
	    
	       try {      
	            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>      
	            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>      
	            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式      
	            String regEx_html1 = "<[^>]+";      
	            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);      
	            m_script = p_script.matcher(htmlStr);      
	            htmlStr = m_script.replaceAll(""); // 过滤script标签      
	    
	            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);      
	            m_style = p_style.matcher(htmlStr);      
	            htmlStr = m_style.replaceAll(""); // 过滤style标签      
	    
	            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);      
	            m_html = p_html.matcher(htmlStr);      
	            htmlStr = m_html.replaceAll(""); // 过滤html标签      
	    
	            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);      
	            m_html1 = p_html1.matcher(htmlStr);      
	            htmlStr = m_html1.replaceAll(""); // 过滤html标签      
	    
	            textStr = htmlStr;      
	    
	        } catch (Exception e) {      
	            System.err.println("Html2Text: " + e.getMessage());      
	        }      
		   }
	       return textStr;// 返回文本字符串      
	*/
    }

    /**
     * 由文件路径获得文件名，如"/folder2/folder3/fileName.txt"将得到"fileName.txt"
     *
     * @param name 文件路径
     * @return
     */
		/*public static String getFileName(String name) {
			File file1 = new File(name);
			return file1.getName();
		}*/
    public static boolean isExistRemote(String url) {
        boolean isExist = false;
        URL serverUrl;
        try {
            serverUrl = new URL(url);
            HttpURLConnection urlcon;
            urlcon = (HttpURLConnection) serverUrl.openConnection();

            int responseCode = urlcon.getResponseCode();
            if (responseCode == 200) { // 不存在
                isExist = true;
            } else {
                // TODO do something when net error;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isExist;

    }

    /**
     * 删除文件夹下面，为使用图片过期的图片
     *
     * @param fileAbsolutePath 文件夹的绝对路径
     * @param expiredTime      过期时间
     */
    public static void deleteFileByExpiredTime(String fileAbsolutePath,
                                               long expiredTime) {
        long currentTime = System.currentTimeMillis();
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(fileAbsolutePath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                list.add(file[i]);
            } else {
                long lastModified = file[i].lastModified();// 获取文件最后修改的时间
                if (currentTime - lastModified > expiredTime) {
                    file[i].delete();
                }
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory()) {
                        list.add(file[i]);
                    } else {
                        long lastModified = file[i].lastModified();// 获取文件最后修改的时间
                        if (currentTime - lastModified > expiredTime) {
                            file[i].delete();
                        }
                    }
                }
            } else {
                long lastModified = tmp.lastModified();// 获取文件最后修改的时间
                if (currentTime - lastModified > expiredTime) {
                    tmp.delete();
                }
            }
        }
    }

    public static File getFileFromUri(Uri uri, Activity mActivity) {

        File file = null;

        String[] proj = {MediaStore.Images.Media.DATA};

        @SuppressWarnings("deprecation")
        Cursor actualimagecursor = mActivity.managedQuery(uri, proj, null,
                null, null);

        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor
                .getString(actual_image_column_index);

        file = new File(img_path);
        return file;

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void saveObject(Context context, String fileName, Object o) throws Exception {

        String path = context.getFilesDir() + "/";

        File dir = new File(path);
        dir.mkdirs();

        File f = new File(dir, fileName);

        if (f.exists()) {
            f.delete();
        }
        FileOutputStream os = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(o);
        objectOutputStream.close();
        os.close();
    }

    public static Object readObject(Context context, String fileName) throws Exception {
        String path = context.getFilesDir() + "/";

        File dir = new File(path);
        dir.mkdirs();
        File file = new File(dir, fileName);
        InputStream is = new FileInputStream(file);

        ObjectInputStream objectInputStream = new ObjectInputStream(is);

        Object o = objectInputStream.readObject();

        return o;

    }

    public static String saveBitmap(Context context, Bitmap bm, String picName) {
        try {
            File f = new File(SDPATH, picName + ".JPEG");
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            Uri imageUri = FileProvider.getUriForFile(context, "com.cloudmachine.fileprovider", f);
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            context.startActivity(intent);
            if (f.exists()) {
                f.delete();
            }


            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveBitmap(Bitmap bm, String picName) {
        try {
            AppLog.print("media_mounted__"+Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED));
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
//            f.mkdirs();
//            f.createNewFile();

            AppLog.print("file__f_" + f + ", name_" + f.getName());
            FileOutputStream out = new FileOutputStream(f);
            AppLog.print("out 1");
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            AppLog.print("out 2");
            out.flush();
            out.close();
            AppLog.print("out 3");
            return f.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveBitmap2File(Context context, Bitmap bm, String picName) {
        try {
            File f = new File(SDPATH, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return f;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
