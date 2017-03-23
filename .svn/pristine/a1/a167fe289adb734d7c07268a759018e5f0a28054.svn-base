package com.cloudmachine.utils.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ImageArrayView extends LinearLayout {
	private Context context;
	private TextView title_text;
	private ImageView[] imageArray = new ImageView[9];
	private String[] imgaeUrl;
	private int clickId;
	private OnClickActionListener mClick = null;
	private boolean isAddEAble = true;
	private View layout1,layout2,layout3;
	
	 class myClickListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int len = imageArray.length;
				for(int i=0; i<len; i++){
					if(v.getId() == imageArray[i].getId()){
						clickId = i;
						if(null != mClick){
							mClick.OnClick(i);
						}
					}
				}
			}
	    	
	    }
	    
	    // 定义三个接口
	    public interface OnClickActionListener {
	        public void OnClick(int clickId);
	    }
	
    public ImageArrayView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray a = context.obtainStyledAttributes(attrs, 
//        		R.styleable.radiusbut_view); 
//        String text = a.getString(R.styleable.radiusbut_view_rbtn_text); 
        initView(context);
//        setText(text);
    }

    private void initView(Context context) {
    	this.context = context;
    	LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_image_array, this);

        title_text = (TextView) findViewById(R.id.title_text);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        imageArray[0] = (ImageView)findViewById(R.id.image1);
		imageArray[1] = (ImageView)findViewById(R.id.image2);
		imageArray[2] = (ImageView)findViewById(R.id.image3);
		imageArray[3] = (ImageView)findViewById(R.id.image4);
		imageArray[4] = (ImageView)findViewById(R.id.image5);
		imageArray[5] = (ImageView)findViewById(R.id.image6);
		imageArray[6] = (ImageView)findViewById(R.id.image7);
		imageArray[7] = (ImageView)findViewById(R.id.image8);
		imageArray[8] = (ImageView)findViewById(R.id.image9);
		imageArray[0].setOnClickListener(new myClickListener());
		imageArray[1].setOnClickListener(new myClickListener());
		imageArray[2].setOnClickListener(new myClickListener());
		imageArray[3].setOnClickListener(new myClickListener());
		imageArray[4].setOnClickListener(new myClickListener());
		imageArray[5].setOnClickListener(new myClickListener());
		imageArray[6].setOnClickListener(new myClickListener());
		imageArray[7].setOnClickListener(new myClickListener());
		imageArray[8].setOnClickListener(new myClickListener());
		initImageArray(0);
        
    }
    public void setText(String text){
    	title_text.setText(text);
    }
    public void setTitleVisibility(int visibility){
    	title_text.setVisibility(visibility);
    }
    public void setImageUrl(String[] imageUrl){
    	this.imgaeUrl = imageUrl;
    	if(imgaeUrl!=null&&imgaeUrl.length>0){
			int len = imgaeUrl.length;
			len = len<9?len:9;
//			DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//			.showImageOnFail(R.drawable.avator)
//			.showImageForEmptyUri(R.drawable.avator)
//			.showImageOnFail(R.drawable.avator).cacheInMemory(true)
//			.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
//			.build();
			initImageArray(len);
			for(int i=0; i<len; i++){
				imageArray[i].setImageResource(R.drawable.mc_default_icon);
				Utils.initViewHW_L(imageArray[i]);
				ImageLoader.getInstance().displayImage(imgaeUrl[i], imageArray[i],
						Constants.displayMcImageOptions, null);
				
			}
			
			
		}else{
			initImageArray(0);
		}
    }
    private void initImageArray(int len){
    	int size = imageArray.length;
    	layout1.setVisibility(View.GONE);
    	layout2.setVisibility(View.GONE);
    	layout3.setVisibility(View.GONE);
    	for(int i=0; i<size; i++){
    		imageArray[i].setImageResource(R.drawable.mc_add_image);
    		imageArray[i].setVisibility(View.VISIBLE);
    	}
    	if(isAddEAble){
    		len +=1;
    	}else{
    		if(len <1){
    			return;
    		}
    	}
		for(int j=len; j<size; j++){
			if(len<=3){
				layout1.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.GONE);
		    	layout3.setVisibility(View.GONE);
				if(j<3){
					imageArray[j].setVisibility(View.INVISIBLE);
				}else{
					imageArray[j].setVisibility(View.GONE);
				}
			}else if(len>3 && len <=6){
				layout1.setVisibility(View.VISIBLE);
		    	layout2.setVisibility(View.VISIBLE);
				layout3.setVisibility(View.GONE);
				if(j<6){
					imageArray[j].setVisibility(View.INVISIBLE);
				}else{
					imageArray[j].setVisibility(View.GONE);
				}
			}else{
				layout1.setVisibility(View.VISIBLE);
		    	layout2.setVisibility(View.VISIBLE);
		    	layout3.setVisibility(View.VISIBLE);
				imageArray[j].setVisibility(View.INVISIBLE);
			}
			
		}
    }
    
    public ImageView[] getImageArray(){
    	return this.imageArray;
    }
    
    public String[] getImageUrl(){
    	return this.imgaeUrl;
    }
    public void setOnClickActionListener(OnClickActionListener click) {
    	mClick = click;
    }
    public void setAddEAble(boolean able){
    	isAddEAble = able;
    }
//    public ImageView getLastImage(){
//    	initImageArray(clickId+1);
//    	return imageArray[clickId];
//    }
   

}
