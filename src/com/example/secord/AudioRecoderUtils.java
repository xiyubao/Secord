package com.example.secord;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class AudioRecoderUtils {

    //�ļ�·��
    private String filePath;
    //�ļ���·��
    private String FolderPath;

    private MediaRecorder mMediaRecorder;
    private final String TAG = "fan";
    public static final int MAX_LENGTH = 1000 * 60 * 10;// ���¼��ʱ��1000*60*10;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * �ļ��洢Ĭ��sdcard/record
     */
    public AudioRecoderUtils(){

        //Ĭ�ϱ���·��Ϊ/sdcard/record/��
        this(Environment.getExternalStorageDirectory()+"/Secord/");
    }

    public AudioRecoderUtils(String filePath) {

        File path = new File(filePath);
        if(!path.exists())
            path.mkdirs();

        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;



    /**
     * ��ʼ¼�� ʹ��amr��ʽ
     *      ¼���ļ�
     * @return
     */
    public void startRecord() {
        // ��ʼ¼��
        /* ��Initial��ʵ����MediaRecorder���� */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ��setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// ������˷�
            /* ��������Ƶ�ļ��ı��룺AAC/AMR_NB/AMR_MB/Default �����ģ����Σ��Ĳ��� */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ����������ļ��ĸ�ʽ��THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp��ʽ
             * ��H263��Ƶ/ARM��Ƶ����)��MPEG-4��RAW_AMR(ֻ֧����Ƶ����Ƶ����Ҫ��ΪAMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = FolderPath + "1.amr" ;
            /* ��׼�� */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* �ܿ�ʼ */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* ��ȡ��ʼʱ��* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e("fan", "startTime" + startTime);
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * ֹͣ¼��
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        //��һЩ���ѷ�Ӧ��5.0�����ڵ���stop��ʱ��ᱨ��������һ�¹ȸ��ĵ���������ȷʵд���п��ܻᱨ�������������쳣����һ�¾����ˣ���л��ҷ�����
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

           // audioStatusUpdateListener.onStop(filePath);
            filePath = "";

        }catch (RuntimeException e){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }
        return endTime - startTime;
    }

    /**
     * ȡ��¼��
     */
    public void cancelRecord(){

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

        }catch (RuntimeException e){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// ���ȡ��ʱ��

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * �������״̬
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double)mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// �ֱ�
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if(null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db,System.currentTimeMillis()-startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * ¼����...
         * @param db ��ǰ�����ֱ�
         * @param time ¼��ʱ��
         */
        public void onUpdate(double db,long time);

        /**
         * ֹͣ¼��
         * @param filePath ����·��
         */
        public void onStop(String filePath);
    }

}