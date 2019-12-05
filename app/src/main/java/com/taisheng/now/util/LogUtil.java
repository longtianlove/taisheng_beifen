package com.taisheng.now.util;


import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.taisheng.now.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class LogUtil {

	// 根据需要将Log存放到SD卡中
	private static String path;
	private static FileOutputStream outputStream;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	static {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				path = Apputil.sdNormalPath
						+ "/xmq_Log/";
				File file_Path = new File(path);
				file_Path.mkdirs();
				File file = new File(path, "log.txt");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					outputStream = new FileOutputStream(file, true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
	}



	/**
	 * 打印出来Json信息；使用默认tag
	 * @param jsonMsg
	 */
	public static void printJson( String jsonMsg){
		printJsonWithHead("lz",jsonMsg,"");
	}
	/**
	 * 打印出来Json信息；使用自定义tag
	 * @param jsonMsg
	 */
	public static void printJson(String tag, String jsonMsg){
		printJsonWithHead(tag,jsonMsg,"");
	}

	/**
	 * 打印出来Json信息；使用自定义tag，并且添加Head信息。
	 * @param tag
	 * @param msg
	 * @param headString
	 */
	public static void printJsonWithHead(String tag, String msg, String headString) {
//		if(!Switch.LOG){
//			return;
//		}
		String message;

		try {
			if (msg.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(msg);
				message = jsonObject.toString(4);
			} else if (msg.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(msg);
				message = jsonArray.toString(4);
			} else {
				message = msg;
			}
		} catch (JSONException e) {
			message = msg;
		}

		printLine(tag, true);
		message = headString + LINE_SEPARATOR + message;
		String[] lines = message.split(LINE_SEPARATOR);
		for (String line : lines) {
			LogUtil.i(tag, "║ " + line);
		}
		printLine(tag, false);
	}


	public static void printLine(String tag, boolean isTop) {
		if (isTop) {
			LogUtil.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
		} else {
			LogUtil.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
		}
	}

    private static String getMessage(String msg) {
        StackTraceElement sts =new Exception().getStackTrace()[2];
        if (sts == null) {
            return null;
        }
        if(!TextUtils.isEmpty(sts.toString())){
        	return sts.getFileName()+"--->"+sts.getMethodName()+"():"+sts.getLineNumber()+"*****"+msg;
        }
        return null;
	}
    public static void d(String tag,String msg) {
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.d(tag, getMessage(msg));
    }
    public static void d(String msg){
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.d(Constants.DEFAULT_TAG,getMessage(msg));
    }
    public static void v(String tag,String msg) {
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.v(tag, getMessage(msg));
    }
    public static void v(String msg){
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.v(Constants.DEFAULT_TAG,getMessage(msg));
    }
    public static void i(String tag,String msg) {
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.i(tag, getMessage(msg));
    }
    public static void i(String msg){
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.i(Constants.DEFAULT_TAG, getMessage(msg));
    }
    public static void e(String tag,String msg) {
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.e(tag, getMessage(msg));
    }
    public static void e(String msg){
//    	if(!Switch.LOG){
//			return;
//		}
    	Log.e(Constants.DEFAULT_TAG, getMessage(msg));
    }
    public static void w(String tag,String msg) {
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.w(tag, getMessage(msg));
    }
    public static void w(String msg){
//    	if(!Switch.LOG){
//    		return;
//    	}
    	Log.w(Constants.DEFAULT_TAG, getMessage(msg));
    }


	// 将错误信息保存到SD卡中去！可选的操作！
	@SuppressWarnings("deprecation")
	public static void save2Sd(String msg) {
		Date date = new Date();
		String time = date.toLocaleString();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (outputStream != null) {
				try {
					outputStream.write(time.getBytes());
					outputStream.write(msg.getBytes());
					outputStream.write("\r\n".getBytes());
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}









}
