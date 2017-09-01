package com.cloudmachine.autolayout.widgets;



import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.autolayout.utils.AutoUtils;
import com.cloudmachine.R;


public class CanBeEditItemView extends RelativeLayout {
    private TextView xing,name_text,ctext_view;
    private ImageView arrowImage;
    private EditText edit_view;
    private boolean required,edit,arrow;

    private String title_name = "";
    private String hint = " ";
    private String content = "";
    private String blankContent=" ";
    private float titleTextSize = 48,cTextSize = 48;

    public CanBeEditItemView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CanBeEditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, 
        		R.styleable.addDItem_view); 
        title_name = a.getString(R.styleable.addDItem_view_name); 
        hint = a.getString(R.styleable.addDItem_view_hint); 
        content = a.getString(R.styleable.addDItem_view_content); 
        required = a.getBoolean(R.styleable.addDItem_view_required, false);
        edit = a.getBoolean(R.styleable.addDItem_view_edit, false);
        arrow = a.getBoolean(R.styleable.addDItem_view_arrow, false);
        titleTextSize = a.getDimension(R.styleable.addDItem_view_titleTextSize, titleTextSize);
        cTextSize = a.getDimension(R.styleable.addDItem_view_cTextSize, cTextSize);
        
        initView(context);
        setTitle(title_name);
        setInitContent(content);
        setEditHint(hint);
        isEdit(edit);
        isRequired(required);
        isArrow(arrow);
        a.recycle();

    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_adddevice_item, this);

        xing = (TextView) findViewById(R.id.xing);
        name_text = (TextView) findViewById(R.id.name_text);
        ctext_view = (TextView) findViewById(R.id.ctext_view);
        arrowImage = (ImageView) findViewById(R.id.arrow);
        edit_view = (EditText) findViewById(R.id.edit_view);
        
        
    }
    public void setTitle(String title){
    	name_text.setText(title);
    	ctext_view.setTextColor(getResources().getColor(R.color.cor20));
    	if(titleTextSize!=0){
    		name_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSizeBigger((int)titleTextSize));
    	}
    	
    }
    public void setContent(String content){
    	ctext_view.setText(content);
    	this.content = content;
    	ctext_view.setTextColor(getResources().getColor(R.color.cor20));
    	if(cTextSize!=0){
    		ctext_view.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSizeBigger((int)cTextSize));
    	}
    }
    private void setInitContent(String content){
    	ctext_view.setText(content);
    	this.content = content;
    	ctext_view.setTextColor(getResources().getColor(R.color.mc_device_hint_text));
    }
    public void setEditHint(String hint){
    	edit_view.setHint(hint);
    	ctext_view.setText(hint);
    	ctext_view.setTextColor(getResources().getColor(R.color.common_text_hint));
    }
    public void initEditiHint(){
    	edit_view.setHint(hint);
    	ctext_view.setText(hint);
    	ctext_view.setTextColor(getResources().getColor(R.color.common_text_hint));
    }
    public void isEdit(boolean b){
    	edit = b;
    	if(b){
    		edit_view.setVisibility(View.VISIBLE);
    		ctext_view.setVisibility(View.GONE);
//    		edit_view.requestFocus();
    		edit_view.setOnFocusChangeListener(new OnFocusChangeListenerEditText());
    	}else{
    		edit_view.setVisibility(View.INVISIBLE);
    		ctext_view.setVisibility(View.VISIBLE);
    		
    		// 状态如果到了这里，什么情况
    	}
    }
   public void isRequired(boolean b){
	   required = b;
	   if(b){
		   xing.setVisibility(View.VISIBLE);
	   }else{
		   xing.setVisibility(View.GONE);
	   }
   }
   public void isArrow(boolean b){
	   arrow = b;
	   if(b){
		   arrowImage.setVisibility(View.VISIBLE);
	   }else{
		   arrowImage.setVisibility(View.GONE);
	   }
   }
   public boolean getRequired(){
	   return required;
   }
   public String getTitle(){
	   return name_text.getText().toString();
   }
   public String getContent(){
//	   return ctext_view.getText().toString();
   	return content;
   }
   public EditText getEdit_view(){
	   return edit_view;
   }
   private class OnFocusChangeListenerEditText implements OnFocusChangeListener{

	   public void onFocusChange(View v, boolean hasFocus) {
		   if(hasFocus){
//			   edit_view.setText(ctext_view.getText());
			   edit_view.setText(blankContent);
		   }
		   else{
			   if(!TextUtils.isEmpty(edit_view.getText().toString())){ 
//				   setContent(edit_view.getText().toString());
				   edit_view.setText(edit_view.getText().toString());
			   }else {
				
				edit_view.setHint(hint);
			}
			   
//			   ctext_view.setText();
//			   Constants.MyLog("失去焦点");
//			   isEdit(false);
		   }
	   }
   }
   
   public ImageView getImageView(){
	   return arrowImage;
   }
   public TextView getCtext_view(){
	   return ctext_view;
   }
   public EditText getEdit_Text(){
	   return edit_view;
   }
}
