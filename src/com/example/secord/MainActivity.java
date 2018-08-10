package com.example.secord;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.R.string;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    public static TextView tv3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tv3 = (TextView)findViewById(R.id.textView3);
        mAudioRecoderUtils = new AudioRecoderUtils();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private File audioFile;

    AudioRecoderUtils mAudioRecoderUtils;
    
    public void Secord_Click(View v)
    {
    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "SD�������ڣ������SD����", Toast.LENGTH_SHORT).show();
            return;
        }
    	
    	mAudioRecoderUtils.startRecord();
    	

    }
    
    public void Stop_Click(View v)
    {
    	
    	 mAudioRecoderUtils.stopRecord();       
    }
    
    
    /**
     * �ϴ��ļ���������
     * @param context
     * @param uploadUrl     �ϴ���������ַ
     * @param oldFilePath       �����ļ�·��
     */
    public static void uploadLogFile(Context context, String uploadUrl, String oldFilePath){
        try {
            URL url = new URL(uploadUrl);  
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            // ����Input��Output����ʹ��Cache
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            // ���ô��͵�method=POST
            con.setRequestMethod("POST");
            //��һ��TCP�����п��Գ������Ͷ�����ݶ�����Ͽ�����
            con.setRequestProperty("Connection", "Keep-Alive");
            //���ñ���
            con.setRequestProperty("Charset", "UTF-8");
            //text/plain���ϴ����ı��ļ��ı����ʽ
            con.setRequestProperty("Content-Type", "text/plain");

            // ����DataOutputStream
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());

            // ȡ���ļ���FileInputStream
            FileInputStream fStream = new FileInputStream(oldFilePath);
            // ����ÿ��д��1024bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
            // ���ļ���ȡ������������
            while ((length = fStream.read(buffer)) != -1) {
                // ������д��DataOutputStream��
                ds.write(buffer, 0, length);
            }
            ds.flush();
            fStream.close();
            ds.close();
            if(con.getResponseCode() == 200){
                tv3.setText("�ļ��ϴ��ɹ����ϴ��ļ�Ϊ��" + oldFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tv3.setText("�ļ��ϴ�ʧ�ܣ��ϴ��ļ�Ϊ��" + oldFilePath);
            tv3.setText("������ϢtoString��" + e.toString());
        }
    }
}
