package com.example.multimedianote;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;  
public class MyAudioRecord extends Activity {  
    
    private TextView stateView;       
    private Button btnStart,btnStop,btnPlay,btnFinish;       
    private MediaRecorder recorder;        
    private MediaPlayer player;  
          
	private int audioResult =2 ;
	
    private File home = null;  
    private File path = null;  
    private String temp = "recaudio_";// ��ʱ�ļ�ǰ׺  
	
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.audiorecoder);  
        stateView = (TextView)this.findViewById(R.id.audioState);  
       
        btnStart = (Button)this.findViewById(R.id.btn_start);  
        btnStop = (Button)this.findViewById(R.id.btn_stop);  
        btnPlay = (Button)this.findViewById(R.id.btn_play);  
        btnFinish = (Button)this.findViewById(R.id.btn_finish);   
        
        btnStop.setEnabled(false);  
        btnPlay.setEnabled(false);  
        btnFinish.setEnabled(false);
        //��ȡSD������·��
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
            home = Environment.getExternalStorageDirectory();  

        } else {  
            Toast.makeText(this, "���Ȳ���SD��", Toast.LENGTH_LONG).show();  
            return;  
        }  
          
        btnStart.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 try {
					path = File.createTempFile(temp, ".amr", home);
					setTitle("=="+path.getAbsolutePath());  	            
		            //������Ҫʵ����һ��MediaRecorder����Ȼ�������Ӧ������  
		            recorder = new MediaRecorder();  
		            //ָ��AudioSource ΪMIC(Microphone audio source ),������õ�  
		            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
		            //ָ��OutputFormat
		            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
		            //ָ��Audio���뷽ʽ
		            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
		            //������������Ҫָ��¼�ƺ��ļ��Ĵ洢·��  	      
		            //������ʱ�ļ�  
		            Log.v("ABpath",path.getAbsolutePath().toString());
		            recorder.setOutputFile(path.getAbsolutePath());
		            
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}                           
	          	              
	            //����Ϳ�ʼ¼����  
	            try {  
	                recorder.prepare();  
	                recorder.start();  
	            } catch (IllegalStateException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
		              
	            stateView.setText("Now recording.");  
	            btnStart.setEnabled(false);  
	            btnPlay.setEnabled(false);  
	            btnStop.setEnabled(true);  
	            
	            
			}
        	
        });
        
        btnStop.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				   recorder.stop();  
		            recorder.release();  		          
		            player = new MediaPlayer();  
		            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
		                  
		                @Override  
		                public void onCompletion(MediaPlayer arg0) {  
		                    //����״̬  
		                    stateView.setText("Audio recording is stopped.");  
		                    btnPlay.setEnabled(true);  
		                    btnStart.setEnabled(true);  
		                    btnStop.setEnabled(false);  
		                }  
		            });  
		              
		            //׼������  
		            try {  
		                player.setDataSource(path.getAbsolutePath());  
		                player.prepare();  
		            } catch (IllegalArgumentException e) {  
		                // TODO Auto-generated catch block  
		                e.printStackTrace();  
		            } catch (IllegalStateException e) {  
		                // TODO Auto-generated catch block  
		                e.printStackTrace();  
		            } catch (IOException e) {  
		                // TODO Auto-generated catch block  
		                e.printStackTrace();  
		            }  
		              
		            //����״̬  
		            stateView.setText("Now ready to play.");  
		            btnPlay.setEnabled(true);  
		            btnStart.setEnabled(true);  
		            btnStop.setEnabled(false); 
		            btnFinish.setEnabled(true);
			}
        	
        });
             
        
        btnPlay.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    //����¼��  
	            //ע�⣬������¼��������ʱ���Ѿ�ʵ������MediaPlayer�������˲��ŵ�׼��  
	            player.start();  
	            //����״̬  
	            stateView.setText("Audio playing.");  
	            btnStart.setEnabled(false);  
	            btnStop.setEnabled(false);  
	            btnPlay.setEnabled(false);  
	            btnFinish.setEnabled(true); 
	            //�ڲ��Ž�����ʱ��ҲҪ����״̬  
			}
        	
        });
        
        btnFinish.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();  
	            intent.setData(Uri.parse(path.getAbsolutePath().toString()));  
	            MyAudioRecord.this.setResult(audioResult, intent);  
	            MyAudioRecord.this.finish();  
	          //  MyAudioRecord.this.stopService(intent);
			}
        	
        });
        
    }  
            
    /** 
     * ����¼���ļ� 
     * @param file 
     */  
    public void PlayMusic(File file){  
        Intent intent = new Intent();  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setAction(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(file), "audio");  
        this.startActivity(intent);  
    }  
    

    
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {	        	
        	MyAudioRecord.this.finish();
        }
			return false;	        
    }
  
}  
