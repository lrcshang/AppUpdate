package com.bobo.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

public class Signatures {
	 private static String TAG="Signatures";

	/**
     * ��ȡ��Ŀ¼�µ�apkǩ��
     * @param path ��ǰapk·��
     * @return
     */
    public static String getSignature(String path) throws Exception{
        // 1.����ʵ����PackageParser����
        Object packageParser = getPackageParser(path);

        // 2.�����ȡparsePackage����
        Object packageObject = getPackageInfo(path,packageParser);

        // 3.����collectCertificates����
        Method collectCertificatesMethod = packageParser.getClass().
                getDeclaredMethod("collectCertificates",packageObject.getClass(),int.class);
        collectCertificatesMethod.invoke(packageParser,packageObject,0);

        // 4.��ȡmSignatures����
        Field signaturesField = packageObject.getClass().getDeclaredField("mSignatures");
        signaturesField.setAccessible(true);
        Signature[] mSignatures = (Signature[]) signaturesField.get(packageObject);
        return mSignatures[0].toCharsString();
    }
    /**
     * ��ȡ��ǰapk��ǩ��
     * @param context
     * @return
     */
    public static String getSignature(Context context){
        // ͨ��Context��ȡ��ǰ����
        String currentApkPackageName = context.getApplicationInfo().packageName;

        Log.e(TAG,"TAG -> "+currentApkPackageName);

        // ͨ��PackageManager��ȡ����Ӧ�õ�PackageInfo��Ϣ
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo packageInfo : packageInfos) {
            if(packageInfo.packageName.equals(currentApkPackageName)){
                // ��ȡǩ��
                return packageInfo.signatures[0].toCharsString();
            }
        }
        return null;
    }
    /**
     * ����PackageParser.Package�࣬����5.0
     * @param path
     * @return
     * @throws Exception
     */
    private static Object getPackageInfo(String path, Object packageParser) throws Exception {
        if(Build.VERSION.SDK_INT >=21){
            Class<?>[] paramClass = new Class[2];
            paramClass[0] = File.class;
            paramClass[1] = int.class;
            Method parsePackageMethod = packageParser.getClass().getDeclaredMethod("parsePackage",paramClass);

            // 3.����ִ��parsePackage����
            Object[] paramObject = new Object[2];
            paramObject[0] = new File(path);
            paramObject[1] = 0;
            parsePackageMethod.setAccessible(true);
            return parsePackageMethod.invoke(packageParser,paramObject);
        }else{
            Class<?>[] paramClass = new Class[4];
            paramClass[0] = File.class;
            paramClass[1] = String.class;
            paramClass[2] = DisplayMetrics.class;
            paramClass[3] = int.class;
            Method parsePackageMethod = packageParser.getClass().getDeclaredMethod("parsePackage",paramClass);

            // 3.����ִ��parsePackage����
            Object[] paramObject = new Object[4];
            paramObject[0] = new File(path);
            paramObject[1] = path;
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            paramObject[2] = metrics;
            paramObject[3] = 0;
            parsePackageMethod.setAccessible(true);
            return parsePackageMethod.invoke(packageParser,paramObject);
        }
    }

    /**
     * ����PackageParser��
     * @param path
     * @return
     * @throws Exception
     */
    private static Object getPackageParser(String path) throws Exception{
        Class<?> packageParserClazz = Class.forName("android.content.pm.PackageParser");
        // �汾����
        if(Build.VERSION.SDK_INT >=21 ){
            Constructor<?> packageParserConstructor = packageParserClazz.getDeclaredConstructor();
            return packageParserConstructor.newInstance();
        }else{
            Constructor<?> packageParserConstructor = packageParserClazz.getDeclaredConstructor(String.class);
            return packageParserConstructor.newInstance(path);
        }
    }
}
