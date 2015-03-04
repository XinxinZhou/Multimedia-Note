package com.example.multimedianote;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

public class NoteEditView extends EditText{

	 
    public NoteEditView(Context context) {  
        super(context);  
    }  
    public NoteEditView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }

    /**
     * insert Image, ContentResolver is from NoteEditActivity,Uri is the Bitmap Path you select.
     * 
     */
    public void insertDrawable(ContentResolver cr,Uri uri) {  
        final SpannableString ss = new SpannableString("\\"+uri.toString()+"\\");
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
        //�õ�drawable���󣬼���Ҫ�����ͼƬ  
        Drawable drawable = new BitmapDrawable(reSizeBm);
        ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);  
        //����0���ǲ�����"easy".length()����4��[0,4)��ֵ��ע����ǵ����Ǹ������ͼƬ��ʱ��ʵ���Ǹ�����"easy"����ַ�����  
        ss.setSpan(span, 0, ss.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);  
        drawable.setBounds(2 , 0 , drawable.getIntrinsicWidth()+20, drawable.getIntrinsicHeight()+20);
        this.append(ss);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }  
    
}
