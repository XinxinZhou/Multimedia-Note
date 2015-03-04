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
    private String temp = "recaudio_";// 临时文件前缀  
	
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
        //获取SD卡绝对路径
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
            home = Environment.getExternalStorageDirectory();  

        } else {  
            Toast.makeText(this, "请先插入SD卡", Toast.LENGTH_LONG).show();  
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
		            //我们需要实例化一个MediaRecorder对象，然后进行相应的设置  
		            recorder = new MediaRecorder();  
		            //指定AudioSource 为MIC(Microphone audio source ),这是最长用的  
		            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
		            //指定OutputFormat
		            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
		            //指定Audio编码方式
		            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
		            //接下来我们需要指定录制后文件的存储路径  	      
		            //创建临时文件  
		            Log.v("ABpath",path.getAbsolutePath().toString());
		            recorder.setOutputFile(path.getAbsolutePath());
		            
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}                           
	          	              
	            //下面就开始录制了  
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
		                    //更新状态  
		                    stateView.setText("Audio recording is stopped.");  
		                    btnPlay.setEnabled(true);  
		                    btnStart.setEnabled(true);  
		                    btnStop.setEnabled(false);  
		                }  
		            });  
		              
		            //准备播放  
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
		              
		            //更新状态  
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
			    //播放录音  
	            //注意，我们在录音结束的时候，已经实例化了MediaPlayer，做好了播放的准备  
	            player.start();  
	            //更新状态  
	            stateView.setText("Audio playing.");  
	            btnStart.setEnabled(false);  
	            btnStop.setEnabled(false);  
	            btnPlay.setEnabled(false);  
	            btnFinish.setEnabled(true); 
	            //在播放结束的时候也要更新状态  
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
     * 播放录音文件 
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
