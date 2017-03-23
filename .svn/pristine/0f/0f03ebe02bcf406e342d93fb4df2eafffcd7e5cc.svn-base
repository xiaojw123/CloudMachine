package com.cloudmachine.utils.widgets.Dialog;


import com.cloudmachine.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class MyDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView txt, PositiveButton,NegativeButton;
    private View button_mid_line;
//    private Button   btnok,btnedit,btncancle,btnsave;
    private LeaveMyDialogListener listener;
    
    public interface LeaveMyDialogListener{   
        public void onClick(View view);   
    }   
    
    public MyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    
    public MyDialog(Context context,int theme,LeaveMyDialogListener listener) {
    	 super(context,theme);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listener = listener;
    }
    
    public void showOneButton(){
    	PositiveButton.setVisibility(View.GONE);
    	button_mid_line.setVisibility(View.GONE);
    }
    public void setNegativeText(String text){
    	NegativeButton.setVisibility(View.VISIBLE);
    	NegativeButton.setText(text);
    }
    public void setPositiveText(String text){
    	PositiveButton.setVisibility(View.VISIBLE);
    	button_mid_line.setVisibility(View.VISIBLE);
    	PositiveButton.setText(text);
    }
    
    public void setText(CharSequence text){
    	txt.setText(text);
    }
    
    public void setTextSize(int dimenDp){
    	txt.setTextSize(context.getResources().getDimension(dimenDp));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_dialog_my);
        txt       = (TextView)findViewById(R.id.dialog_text);
        PositiveButton       = (TextView)findViewById(R.id.positive_button);
        NegativeButton       = (TextView)findViewById(R.id.negative_button);
        button_mid_line       = findViewById(R.id.button_mid_line);
        txt.setOnClickListener(this);
        PositiveButton.setOnClickListener(this);
        NegativeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        listener.onClick(v);
        switch(v.getId()){
        case R.id.positive_button:
        case R.id.negative_button:
        	MyDialog.this.dismiss();
        	break;
        }
    }
}