package com.zinglabs.zwerewolf.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/20.
 */
public class FileUtil {

    /**
     * 从保存原图的地址读取图片
     */
    public static Bitmap file2Bitmap(String parentPath, String fileName) {
        String filePath = parentPath + "/" + fileName;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    /**
     * 从文件中读取字符串
     */
    public static String file2String(File file) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            //每次读取文件的缓存
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    /**
     * bitmap转file
     */
    public static File saveBitmap2file(Bitmap bmp, String fileName) {
        if (bmp == null || TextUtils.isEmpty(fileName)) {
            LogUtil.e("图片保存失败，图片null或保存名null");
            return null;
        }

        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), fileName);
        try {
            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            LogUtil.e("图片保存成功，地址：" + imageFile.toString() + ", w = " + bmp.getWidth() + " h = " + bmp.getHeight() + " length = " + bmp.getByteCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 复制文件
     */
    public static File copyFile(String filePath, String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath(), fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            File f = new File(filePath);
            Long l = f.length();
            int length = l.intValue();
            FileInputStream in = new FileInputStream(f);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[length];
            while (true) {
                int ins = in.read(buffer);
                if (ins == -1) {
                    in.close();
                    out.flush();
                    out.close();
                } else
                    out.write(buffer, 0, ins);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
