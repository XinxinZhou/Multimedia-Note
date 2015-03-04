package com.example.multimedianote;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class NoteEditActivity extends Activity{
	private int state = -1;
	private int imageRequest = 1;
	private int audioRequest = 2 ;
	private int vedioRequest = 3 ;
	
	public static final int ADD_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int ALERT_STATE = 2;
	private static final int DELETE_ID = 0;

	public Button saveNoteButton;
	public Button back;
	public Button addImageButton;
	public Button addAudioButton;
	public Button addVedioButton;
	public EditText noteTitle;
	NoteEditView noteEditView;
	private ListView audioList = null; 
	private MediaRecorder mMediaRecorder = null;  
	private List<String> rec = new ArrayList<String>();// 贋慧村咄猟周  
	private List<File> mediaPath = new ArrayList<File>();// 贋慧村咄猟周  	
	public File path = null;  
	

	DatabaseManage dm=null;
	private int imageNum=0;
	private int audioNum=0;
	private int videoNum=0;
	
	Tools tool=new Tools();
	private String id = "";
	private String titleText = "";
	private String contentText = "";
	private String timeText = "";
	String audioPath="";
	String videoPath="";
	String imageNumString="";
	
//，，，，，，，，，for Select form Phone's Image Base，，，，，，，，，
//，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，，	
 
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noteedit);
		
		Intent intent = getIntent();
		state = intent.getIntExtra("state", ADD_STATE);
		Log.v("state",Integer.toString(state));
		
		back=(Button)this.findViewById(R.id.back);
		addImageButton=(Button)this.findViewById(R.id.addImage);
		addAudioButton=(Button)this.findViewById(R.id.addAudio);
		addVedioButton=(Button)this.findViewById(R.id.addVedio);
		saveNoteButton=(Button)this.findViewById(R.id.saveNote);	
		noteTitle=(EditText)this.findViewById(R.id.title);
		noteEditView=(NoteEditView)this.findViewById(R.id.editContent);
		audioList=(ListView)this.findViewById(R.id.audioList);
		
		dm=new DatabaseManage(this);
		
		saveNoteButton.setOnClickListener(new AddNoteListener());
		
		noteEditView.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				noteEditView.setSelection(noteEditView.getText().toString().length());	
				return false;
			}
		});
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				NoteEditActivity.this.finish();
			}
			
		});
		
	
		
//----------------------------------AddCilickListener-----------------------------------------
		addImageButton.setOnClickListener(new AddImageListener());
		addAudioButton.setOnClickListener(new AddAudioListener());
		addVedioButton.setOnClickListener(new AddVedioListener());		
		audioList.setOnItemClickListener(new OnItemClickListener(){  			  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                // TODO Auto-generated method stub    
            	if(rec.get(arg2).contains(".amr"))
            	{
            		PlayMusic(mediaPath.get(arg2));
            	}
            	if(rec.get(arg2).contains(".mp4")||rec.get(arg2).contains(".3gp"));
            	{
            		Log.v("mediaPath.get(arg2).toString()",mediaPath.get(arg2).toString());
            		PlayVedio(mediaPath.get(arg2));
            	}
                  
            }  
              
        });  
		 registerForContextMenu(audioList);
				
//-----------------------------------UpdatedStates.----------------------------------------	
		if(state==EDIT_STATE)
		{
			id = intent.getStringExtra("id");
			titleText = intent.getStringExtra("title");
			contentText = intent.getStringExtra("content");			
			noteTitle.setText(titleText);
				
			Log.v("noteEditView",noteEditView.getText().toString());
			
			String imageUris=intent.getStringExtra("imagePath");
			
			noteEditView.setText(contentText);
			
			String audioPaths=intent.getStringExtra("audioPaths");
			String videoPaths=intent.getStringExtra("videoPaths");
			imageNumString = intent.getStringExtra("imageNum");
			imageNum += Integer.parseInt(imageNumString);
			Log.v("imageUris", imageUris);
			Log.v("audioPaths", audioPaths);
			Log.v("videoPaths", videoPaths);
			
			
			if(imageUris.length()>1)
			{
				String singleImageUri="";
				for(int i=0;i<imageUris.length();i++)
				{
					singleImageUri=imageUris.substring(0,imageUris.indexOf("\\"));	    	
					ContentResolver cr = this.getContentResolver();
			    	noteEditView.insertDrawable(cr,Uri.parse(singleImageUri)); 
			    	Log.v("noteEditViewT", noteEditView.getText().toString());
			    	Log.v("noteEditViewCT", noteEditView.getContext().toString());
			    	imageUris=imageUris.substring(imageUris.indexOf("\\")+1,imageUris.length());
				}
			}
			
			if(audioPaths.length()>1)
			{
				String singleAudioPath="";
				audioPath += audioPaths;
				Log.v("audioPathaudioPath", audioPath);
				for(int i=0;i<audioPaths.length();i++)
				{
					singleAudioPath=audioPaths.substring(0,audioPaths.indexOf("\\"));	    			    	
			    	audioPaths=audioPaths.substring(audioPaths.indexOf("\\")+1,audioPaths.length());
			    	rec.add(singleAudioPath.substring(singleAudioPath.lastIndexOf("/"), singleAudioPath.length()));
			    	mediaPath.add(new File(singleAudioPath));
				}
				  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
			                 android.R.layout.simple_list_item_1, rec);  
			         audioList.setAdapter(adapter);  	     				
			}
			
			if(videoPaths.length()>1)
			{
				String singleVideoPath="";
				videoPath=videoPaths;
				for(int i=0;i<videoPaths.length();i++)
				{
					singleVideoPath=videoPaths.substring(0,videoPaths.indexOf("\\"));	    			    	
			    	videoPaths=videoPaths.substring(videoPaths.indexOf("\\")+1,videoPaths.length());
			    	rec.add(singleVideoPath.substring(singleVideoPath.lastIndexOf("/"), singleVideoPath.length()));
			    	mediaPath.add(new File(singleVideoPath));
				}
				  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
			                 android.R.layout.simple_list_item_1, rec);  
			         audioList.setAdapter(adapter);  	     				
			}
			
		}
		
	
//--------------------------------------------------------------------------------			
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {	
	        	Intent intent=new Intent();
	        	intent.setClass(NoteEditActivity.this, MainActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	NoteEditActivity.this.startActivity(intent);
	        	NoteEditActivity.this.finish();
	        }
	        if(keyCode == KeyEvent.KEYCODE_DEL)
	        {
	        	Log.v("delete","delete");
	        	if(noteEditView.getText().toString().length()>0)
	        	{
	        	
	        		int cursor=noteEditView.getSelectionStart();
	        		Log.v("cursor",Integer.toString(cursor));
	        		int i = noteEditView.getText().toString().lastIndexOf("\\");
	        		Log.v("cursor+i",Integer.toString(i));
	        		if(cursor == i+1)
	        		{
	        			imageNum--;
	        		}
	        		//noteEditView.getText().delete(cursor-1, cursor);
	        	}
	        }
				return false;	        
	    }
	/**
	 * saveNoteButton Listener.
	 * @author Zxx
	 */
	public class AddNoteListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			titleText = noteTitle.getText().toString();
			contentText = noteEditView.getText().toString();	
			String pureText="";
			String imageIndex="";
			String imagePath="";
			String tempConttent=contentText;
			int falgsCount=0;
			
			Log.v("contentText", contentText);
			
			while(tempConttent.contains("\\"))
			{
				if(tempConttent.indexOf("\\")>=0)
					{
						falgsCount++;
						tempConttent=tempConttent.substring(tempConttent.indexOf("\\")+1,tempConttent.length());
					}	
				if(tempConttent.indexOf("\\") == tempConttent.lastIndexOf("\\"))
					{
						falgsCount++;
						tempConttent=tempConttent.substring(0,tempConttent.lastIndexOf("\\")-1);
					}
			}
			imageNum=(falgsCount/2);
			
			if(imageNum>0)
			{		
					int imageFlag[]=new int[2*imageNum];
					
					imageFlag[0]=tool.indexOfFlag(contentText,"\\",0);
					pureText=contentText.substring(0, imageFlag[0]);
					imageIndex=Integer.toString(imageFlag[0]);
					Log.v("NEA","imageNum>0");
					for(int i=1;i<2*imageNum;i++)
					{
						imageFlag[i]=tool.indexOfFlag(contentText,"\\",imageFlag[i-1]+1);	
						imageIndex+=","+Integer.toString(imageFlag[i]);					
					}
					Log.v("imageIndex", imageIndex);
					
					for(int i=1;i<2*imageNum;i++)
					{
						if(imageFlag[i]!=contentText.lastIndexOf("\\"))
						{
							pureText+=" "+contentText.substring(imageFlag[i]+1, imageFlag[++i]);
						}
						else
						{
							pureText+=" "+contentText.substring(imageFlag[i]+1,contentText.length());
						}
					}
					for(int i=0;i<2*imageNum;i++)
					{
						imagePath+=contentText.substring(imageFlag[i]+1,imageFlag[++i])+"\\";
						Log.v("NEAimagePath","123"+imagePath);
						
					}
					
					Log.v("NEAimagePath","bbbaas"+imagePath);
				}			
				else
				pureText=contentText;
			
			
			try{
				dm.open();
				Log.v("NEA", "before insertData");
					if(state == 0)//仟奐彜蓑
					{
						dm.insert(titleText, pureText,Integer.toString(imageNum),imageIndex, imagePath, audioPath, videoPath);	
						Log.v("ADD_STATE","ADD_STATE");
					}
					if(state == 1)
					{	
						Log.v("EDIT_STATE","EDIT_STATE");
						dm.update(Integer.parseInt(id),titleText, pureText,Integer.toString(imageNum),imageIndex, imagePath, audioPath, videoPath);	
					}
				dm.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
			}		
			Intent intent = new Intent();
			intent.setClass(NoteEditActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NoteEditActivity.this.startActivity(intent);
		}
		
	}
	
	public class AddImageListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	        intent.setType("image/*");  
	        intent.putExtra("return-data", true);         
	        startActivityForResult(intent, imageRequest);  
	    }  						
	}
	
	public class AddAudioListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub 
			 Intent intent = new Intent();  
			 intent.setClass(NoteEditActivity.this, MyAudioRecord.class);
			 
			 startActivityForResult(intent, audioRequest);  
	    }  						
	}
	
	public class AddVedioListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);  	
			 intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	         startActivityForResult(intent, vedioRequest);  
		}
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data); 
	     
	     if(requestCode == this.imageRequest)
	     {
	    	 if(data!=null)
	    	 {
		    	 Uri uri=data.getData();
		    	 ContentResolver cr = this.getContentResolver();
		    	 noteEditView.insertDrawable(cr,uri);	  
		    	// imageNum++;	  
	    	 }
	     }
	     
	     if(requestCode == this.audioRequest)
	     {	    	 
	    	 if(data!=null)
	    	 {
	    		 Uri uri=data.getData();
	    		 path=new File(uri.toString());
	    		 Log.v("audioUri", "aa"+uri.toString());
	    		 Log.v("audioPath","aabb"+path.getAbsolutePath().toString());
	    	 
	    	// File[] f = path.listFiles(new MusicFilter());  
	    	 Log.v("audioPath","aabbcc"+path.getAbsolutePath().toString());
	    	 audioPath+=path.getAbsolutePath().toString()+"\\";	 
	    	 Log.v("addaudio", audioPath);
	    	 rec.add(path.getName());  
	    	 mediaPath.add(path);
	         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
	                 android.R.layout.simple_list_item_1, rec);  
	         audioList.setAdapter(adapter);  	         
	         audioNum++;}
	         
	     }
	     
	     if(requestCode==this.vedioRequest)
	     {	  
	    	 if(data!=null)
	    	 {
	    	 Uri uri=data.getData();	 
	    	 //path=new File(uri.toString());
	    	 path=new File(getRealPathFromURI(uri));
	    	 String tempName=path.getName();	    	 
	    	 videoPath+=path.getAbsolutePath().toString()+"\\";	    
	    	 Log.v("videoPath",videoPath);
	    	 rec.add(tempName);  
	    	 mediaPath.add(path);
	         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
	                 android.R.layout.simple_list_item_1, rec);  
	         audioList.setAdapter(adapter);           
	         audioNum++;	
	    	 }
	     }
	   
	 }
	 
	/** 
     * 殴慧村咄猟周 
     * @param file 
     */  
    public void PlayMusic(File file){  
        Intent intent = new Intent();  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setAction(Intent.ACTION_VIEW);  
        intent.setDataAndType(Uri.fromFile(file), "audio");  
        this.startActivity(intent);  
    }  
    
    public void PlayVedio(File file){  
        Intent intent = new Intent(Intent.ACTION_VIEW);   
        if (file.getAbsoluteFile().toString().contains("mp4"))
        	intent.setDataAndType(Uri.fromFile(file),"video/mp4");
        if (file.getAbsoluteFile().toString().contains("3gp"))
        	intent.setDataAndType(Uri.fromFile(file),"video/3gp");
        this.startActivity(intent);  
    }  
    
    public String getRealPathFromURI(Uri contentUri) {
        String [] proj={MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                        proj, // Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,DELETE_ID,Menu.NONE,"Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
       
    }
    
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case DELETE_ID:
        	AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        	if(rec.get(menuInfo.position).contains("amr"))
        	{
        	    Log.v(audioPath, audioPath);
        	    Log.v("mediaPath.get(menuInfo.position).toString()",mediaPath.get(menuInfo.position).toString());
        	    Log.v("Integer",Integer.toString(audioPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\")));     	   
        		audioPath=audioPath.substring(0,audioPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\"))+audioPath.substring(audioPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\")+mediaPath.get(menuInfo.position).toString().length()+1,audioPath.length());
        		Log.v("audioPath", audioPath);
        	}
        	if(rec.get(menuInfo.position).contains("3gp")||rec.get(menuInfo.position).contains("mp4"))
        	{
        	    Log.v("videoPath", videoPath);
        	    Log.v("mediaPath.get(menuInfo.position).toString()",mediaPath.get(menuInfo.position).toString());
        	    Log.v("Integer",Integer.toString(videoPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\")));     	   
        	    videoPath=videoPath.substring(0,videoPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\"))+videoPath.substring(videoPath.indexOf(mediaPath.get(menuInfo.position).toString()+"\\")+mediaPath.get(menuInfo.position).toString().length()+1,videoPath.length());
        		Log.v("videoPath", videoPath);
        	}
        	rec.remove(menuInfo.position);		
        	mediaPath.remove(menuInfo.position);
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
	                 android.R.layout.simple_list_item_1, rec);  
	         audioList.setAdapter(adapter);  	
	         
        	return super.onContextItemSelected(item);
        }
		return false;
    }
}
