package com.sky.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtils {

    /**
     * 文件根路径
     */
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + "/dingdong/";

    /**
     * 脉搏波数据
     */
    public static final String PATH_PULSE = PATH_ROOT + "pulse/";

    /**
     * 数据模型路径
     */
    public static final String PATH_MODEL = PATH_ROOT + "model";

    /**
     * 保存文本内容
     *
     * @param context
     * @param inputText
     */
    public static void save(Context context, String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput("data.txt", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文本内容
     *
     * @param context
     * @return
     */
    public static String load(Context context) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput("data.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    /**
     * 将脉搏波写出到文件输出流中
     * 已当前时间命名文件
     * 文件写入方式为追加
     *
     * @param data
     */
    public static void savePulse(String data, String fileName) {
        String url = PATH_PULSE + fileName;
        File file = new File(url);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, true));
            bos.write(data.getBytes());
            bos.write("\n".getBytes()); //添加换行符
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createFile(String fileName) {
        File f = new File(PATH_PULSE);
        if (!f.exists()) {
            f.mkdirs();
        }
        String url = PATH_PULSE + fileName;
        File file = new File(url);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Integer> getPulse(String fileName) {
        List<Integer> list = new ArrayList<>();
        String url = PATH_PULSE + fileName;
        File file = new File(url);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            int len = 0;
            byte[] b = new byte[1024];
            StringBuffer buffer = new StringBuffer();
            while ((len = bis.read(b)) != -1) {
                buffer.append(new String(b, 0, len));
            }
            String[] strs = buffer.toString().split("\n");
            int offset = 0;
            int size = strs.length;
            for (String str : strs) {
                /*Log.i("TAG",str);*/
                list.add(Integer.parseInt(str));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 格式化当前时间
     * 文件名字不能有冒号
     * 在自己手机上抛异常
     * 在公司已经root的测试机上正常运行
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatTime() {
        long time = System.currentTimeMillis();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");   都是冒号惹的错
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date date = new Date(time);
        return df.format(date) + ".txt";
    }

    /**
     * 获取PATH_ROOT目录下的所有脉搏波数据
     *
     * @return
     */
    public static List<String> getFileNames() {
        List<String> list = new ArrayList<>();
        File file = new File(PATH_PULSE);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                if (name.endsWith("txt")) {
                    list.add(name);
                }
            }
        }
        return list;
    }

}
