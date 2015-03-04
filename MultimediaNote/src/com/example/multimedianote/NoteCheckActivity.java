package com.example.multimedianote;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NoteCheckActivity extends Activity{

	public static final int ADD_STATE = 0;
	public static final int EDIT_STATE = 1;
	public static final int ALERT_STATE = 2;
	
	private TextView titleText = null;
	private TextView contentText = null;
	private TextView timeText = null;
	private Button editNote=null;
	private ListView checkAudioList = null;
	private List<String> rec = new ArrayList<String>();// 存放录音文件  
	
	private List<File> mediaPath = new ArrayList<File>();// 存放录音文件  	
	public File path = null;  
	//private ImageView imageView = null;
	Tools tool=new Tools();
	
	String id = "";
	String title= "";
	String time= "";
	String pureContent = "";
	String imageIndex="";
	String imagePath="";
	String imageNum="";
	String audioPaths="";
	String videoPaths="";
	
	String tempImagePaths="";
	String tempAudioPaths="";
	String tempVideoPaths="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_note);
		ContentResolver cr = getContentResolver();
		
		titleText = (TextView)findViewById(R.id.checkTitle);
		contentText = (TextView)findViewById(R.id.checkContent);
		timeText = (TextView)findViewById(R.id.checkTime);
		checkAudioList = (ListView)findViewById(R.id.checkMediaList);
		checkAudioList.setScrollbarFadingEnabled(true);
		editNote = (Button)findViewById(R.id.editNote);
		Intent intent = getIntent();//获取启动该Activity的intent对象
		
		 id = intent.getStringExtra("id");
		 title= intent.getStringExtra("title");
		 time= intent.getStringExtra("time");
		 pureContent = intent.getStringExtra("content");
		 imageIndex=intent.getStringExtra("imageIndex");
		 imagePath=intent.getStringExtra("imagePath");
		 imageNum=intent.getStringExtra("imageNum");
		 audioPaths=intent.getStringExtra("audioPath");
		 videoPaths=intent.getStringExtra("videoPath");
		 
		 tempImagePaths=imagePath;
		 tempAudioPaths=audioPaths;
		 tempVideoPaths=videoPaths;
		 
		Log.v("audioPathNCA", audioPaths);
		Log.v("NCA",title);
		Log.v("NCA", imageNum);
		
		long t = Long.parseLong(time);
		
		String datetime = DateFormat.format("yyyy-MM-dd kk:mm:ss", t).toString();
		
		this.titleText.setText(title);
		this.timeText.setText(datetime);
		this.contentText.setText(pureContent+"\n");
		
		this.contentText.setMovementMethod(LinkMovementMethod.getInstance());
		
		int iNum=Integer.parseInt(imageNum);
		Log.v("NCA","imageNum Parsed");
	
		if (iNum>0)
		{			
			int imageIndexSet[]=new int[iNum];
			String content="";
			String singleImagePath="";
			int endIndex=0;
			Bitmap bm = null;
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options(); 
			Uri uri[] = new Uri[iNum];
			
//			for(int i=0;i<2*iNum;i++)
//			{			
//				endIndex=tool.indexOfFlag(imageIndex, ",", 0);
//				Log.v("endIndex", Integer.toString(endIndex));
//				if(endIndex>0)
//				{	
//					imageIndexSet[i]=Integer.parseInt(tool.combineContext(imageIndex,0,endIndex));	
//					Log.v("imageIndex1",imageIndex);
//					
//					imageIndex=tool.combineContext(imageIndex, (endIndex+1), imageIndex.length());	
//					Log.v("imageIndex2",imageIndex);
//					continue;
//				}
//				else
//				{
//					Log.v("i",Integer.toString(i));
//					Log.v("imageIndex3",imageIndex);
//					imageIndexSet[i]=Integer.parseInt(tool.combineContext(imageIndex,0,imageIndex.length()));	
//					Log.v("imageIndexSet"+Integer.toString(i),Integer.toString(imageIndexSet[i]));					
//				}
				
//			}
			Log.v("fccc", "fccc");
			Log.v("ImagePath!", "aa"+imagePath);
			for(int j=0;j<iNum;j++)
			{
				
				Log.v("ImagePath!", imagePath);
				endIndex=tool.indexOfFlag(imagePath,"\\", 0);
				Log.v("ImagePath2", imagePath);
				if(endIndex<=imagePath.lastIndexOf("\\"))
				{	
					singleImagePath=tool.combineContext(imagePath, 0, endIndex);
					Log.v("singleImagePath", singleImagePath);
					
					uri[j]=Uri.parse(singleImagePath);
					imagePath=tool.combineContext(imagePath, endIndex+1, imagePath.length());
				}
				else
				{
					singleImagePath=imagePath;
					uri[j]=Uri.parse(singleImagePath);
				}
				
				//SpannableString ssc = new SpannableString(singleImagePath);
				this.insertDrawable(cr,uri[j]);
				
			}
		
		}
		
		
//---------------Audio------------------------------
		if(audioPaths.length()>2)
		{
			
			while(audioPaths.contains("\\"))
			{
				String singleAudioPath=audioPaths.substring(0, audioPaths.indexOf("\\"));
				audioPaths=audioPaths.substring(audioPaths.indexOf("\\")+1, audioPaths.length());
				rec.add("Audio:"+singleAudioPath.substring(singleAudioPath.lastIndexOf("/")+1,singleAudioPath.length()));				
		         mediaPath.add(new File(singleAudioPath));
			}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
	                 android.R.layout.simple_list_item_1, rec);  
	         checkAudioList.setAdapter(adapter);  
		}
		
//------------------------video---------------		
		if(videoPaths.length()>2)
		{
			while(videoPaths.contains("\\"))
			{
				String singleVideoPath=videoPaths.substring(0, videoPaths.indexOf("\\"));
				videoPaths=videoPaths.substring(videoPaths.indexOf("\\")+1, videoPaths.length());
				rec.add("Vedio:"+singleVideoPath.substring(singleVideoPath.lastIndexOf("/")+1,singleVideoPath.length()));				 
		         mediaPath.add(new File(singleVideoPath));
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
	                 android.R.layout.simple_list_item_1, rec);  
	         checkAudioList.setAdapter(adapter);  
		}
		
		checkAudioList.setOnItemClickListener(new OnItemClickListener(){  			  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                // TODO Auto-generated method stub    
            	if(rec.get(arg2).contains(".amr"))
            	{
            		PlayMusic(mediaPath.get(arg2));
            	}
            	if(rec.get(arg2).contains(".mp4")||rec.get(arg2).contains(".3gp"))
            	{
            		PlayVedio(mediaPath.get(arg2));
            	}
                
            }  
              
        });  

		editNote.setOnClickListener(new EditNoteListener());
		

		
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {	
	        	Intent intent=new Intent();
	        	intent.setClass(NoteCheckActivity.this, MainActivity.class);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	NoteCheckActivity.this.startActivity(intent);
	        	NoteCheckActivity.this.finish();
	        }
				return false;	        
	    }

	
	public void insertDrawable(ContentResolver cr,Uri uri) {  
        final SpannableString ss = new SpannableString(uri.toString());

        Bitmap bm;
        try {
			bm = BitmapFactory.decodeStream(cr.openInputStream(uri));
		
        int newWidth=160;
        int newHeight=200;
        float scaleWidth = ((float) newWidth) / bm.getWidth();
        float scaleHeight = ((float) newHeight) / bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap reSizeBm=Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),matrix,true);
        //得到drawable对象，即所要插入的图片  
        Drawable drawable = new BitmapDrawable(reSizeBm);
        ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);         
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。  
        ss.setSpan(span, 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
        Linkify.addLinks(ss, 1);
        drawable.setBounds(2 , 0 , drawable.getIntrinsicWidth()+20, drawable.getIntrinsicHeight()+20);
        this.contentText.append(ss);      	 
    
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  	
	
	
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
    
    public class EditNoteListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent =new Intent();		
			intent.putExtra("id", id);
			intent.putExtra("state", EDIT_STATE);
			intent.putExtra("title", title);
			intent.putExtra("content", pureContent);
			intent.putExtra("imagePath", tempImagePaths);
			intent.putExtra("audioPaths", tempAudioPaths);
			intent.putExtra("videoPaths", tempVideoPaths);
			intent.putExtra("imageNum", imageNum);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setClass(NoteCheckActivity.this, NoteEditActivity.class);
			NoteCheckActivity.this.startActivity(intent);
			NoteCheckActivity.this.finish();
		}
    	
    }
    
    
	/*
	public Uri getpath(Uri uri)
	{
		
		      String[] proj = { MediaStore.Images.Media.DATA };  
		      Log.v("ssss", uri.toString());
		      Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);  
		      int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
		      actualimagecursor.moveToFirst();   		
		      Log.v("nima",Integer.toString(actual_image_column_index));
		      String img_path = actualimagecursor.getString(actual_image_column_index);  
		      File file = new File(img_path);
		      Uri fileUri = Uri.fromFile(file);
		      return fileUri;
		      
		
	}
	*/	
	/**
	public String getPath(Uri uri) 
    {   
      // String[] projection = {MediaStore.Images.Media.DATA }; 
		
       //Cursor cursor = managedQuery(uri, projection, null, null, null);  
        ContentResolver cr = this.getContentResolver();
   		Cursor cursor = cr.query(uri, null, null, null, null);
   		Log.v("what", Integer.toString(cursor.getColumnCount()));
   		//  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
   		String path="";
   		if(cursor.moveToFirst())
   		{
	   	   for (int i = 1; i < cursor.getColumnCount(); i++) {
	   		path+=cursor.getString(i);	
	   		Log.v("tag", cursor.getString(i));
	   		}     
   		} 
   	 return path;	
    }
    */
/**	
		public void setKeyWordClickable(TextView textView, SpannableString ss, Pattern pattern, ClickableSpan cs)
		{
			Matcher matcher = pattern.matcher(ss.toString());
			while(matcher.find())
			{
				String key=matcher.group();
				if(!"".equals(key))
				{
					int start=ss.toString().indexOf(key);
					int end=start + key.length();
					setClickTextView(textView,ss,start,end,cs);
				}
			}
		}
		
		public void setClickTextView(TextView textview, SpannableString ss, int start, int end, ClickableSpan cs)
		{
			
		}
*/
}