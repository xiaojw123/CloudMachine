package com.cloudmachine.utils.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;


public class AddDeviceItemView extends RelativeLayout {
    private TextView xing,name_text,ctext_view;
    private ImageView arrowImage;
    private EditText edit_view;
    private boolean required,edit,arrow;
    private String title_name = "";
    private String hint = "";
    private String content = "";

    public AddDeviceItemView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AddDeviceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, 
        		R.styleable.addDItem_view); 
        title_name = a.getString(R.styleable.addDItem_view_name); 
        hint = a.getString(R.styleable.addDItem_view_hint); 
        content = a.getString(R.styleable.addDItem_view_content); 
        required = a.getBoolean(R.styleable.addDItem_view_required, false);
        edit = a.getBoolean(R.styleable.addDItem_view_edit, false);
        arrow = a.getBoolean(R.styleable.addDItem_view_arrow, false);
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
    }
    public void setContent(String content){
    	ctext_view.setText(content);
    	ctext_view.setTextColor(getResources().getColor(R.color.mc_device_hint_text));
    }
    private void setInitContent(String content){
    	ctext_view.setText(content);
    	ctext_view.setTextColor(getResources().getColor(R.color.mc_device_hint_text));
    }
    public void setEditHint(String hint){
    	edit_view.setHint(hint);
    }
    public void isEdit(boolean b){
    	edit = b;
    	if(b){
    		edit_view.setVisibility(View.VISIBLE);
    		ctext_view.setVisibility(View.GONE);
    		edit_view.requestFocus();
    		edit_view.setOnFocusChangeListener(new OnFocusChangeListenerEditText());
    	}else{
    		edit_view.setVisibility(View.GONE);
    		ctext_view.setVisibility(View.VISIBLE);
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
		   arrowImage.setVisibility(View.INVISIBLE);
	   }
   }
   public boolean getRequired(){
	   return required;
   }
   public String getTitle(){
	   return name_text.getText().toString();
   }
   public String getContent(){
   	return ctext_view.getText().toString();
   }
   public EditText getEdit_view(){
	   return edit_view;
   }
   private class OnFocusChangeListenerEditText implements OnFocusChangeListener{

	   public void onFocusChange(View v, boolean hasFocus) {
		   if(hasFocus){
			   edit_view.setText(ctext_view.getText());
		   }
		   else{
			   ctext_view.setText(edit_view.getText().toString());
			   isEdit(false);
		   }
	   }
   }
}
