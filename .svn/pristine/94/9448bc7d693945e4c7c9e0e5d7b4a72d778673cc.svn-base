package com.cloudmachine.autolayout.widgets;

import com.cloudmachine.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;


import android.view.ViewGroup.LayoutParams;  
import android.widget.Button;  
import android.widget.LinearLayout;  
import android.widget.TextView; 
public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message1;
		private String message2;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private int buttonColorLeft = -1;
		private	int buttonColorRight = -1;

		public Builder setLeftButtonColor(int color){
			this.buttonColorLeft = color;
			return this;
		}
		
		public Builder setRightButtonColor(int color){
			this.buttonColorRight = color;
			return this;
		}
		
		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message1,String message2) {
			this.message1 = message1;
			this.message2 = message2;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message1,int message2) {
			this.message1 = (String) context.getText(message1);
			this.message2 = (String) context.getText(message2);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.custom_dialog, null);
			
			dialog.setContentView(layout, new LayoutParams(  
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			  // set the dialog title  
            ((TextView) layout.findViewById(R.id.title)).setText(title);  
            
         // set the confirm button  
            if (positiveButtonText != null) {  
                ((Button) layout.findViewById(R.id.positiveButton))  
                        .setText(positiveButtonText);  
                if (buttonColorLeft != -1) {
                	((Button) layout.findViewById(R.id.positiveButton))  
                    .setTextColor(buttonColorLeft);
				}
                if (positiveButtonClickListener != null) {  
                    ((Button) layout.findViewById(R.id.positiveButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    positiveButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_POSITIVE);  
                                }  
                            });  
                }  
            } else {  
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.positiveButton).setVisibility(  
                        View.GONE);  
            }  
            // set the cancel button  
            if (negativeButtonText != null) {  
                ((Button) layout.findViewById(R.id.negativeButton))  
                        .setText(negativeButtonText); 
                if (buttonColorRight != -1) {
                	 ((Button) layout.findViewById(R.id.negativeButton))  //更改按钮的颜色值
                     .setTextColor(buttonColorRight); 
				}
                if (negativeButtonClickListener != null) {  
                    ((Button) layout.findViewById(R.id.negativeButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    negativeButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_NEGATIVE);  
                                }  
                            });  
                }  
            } else {  
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.negativeButton).setVisibility(  
                        View.GONE);  
            }  
            // set the content message  
            if (message1 != null) {  
                ((TextView) layout.findViewById(R.id.message1)).setText(message1);  
                ((TextView) layout.findViewById(R.id.message2)).setText(message2);  
            } else if (contentView != null) {
                // if no message set  
                // add the contentView to the dialog body  
                ((LinearLayout) layout.findViewById(R.id.content))  
                        .removeAllViews();  
                ((LinearLayout) layout.findViewById(R.id.content))  
                        .addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));  
            }  
            dialog.setContentView(layout);  
            return dialog;
		}

	}

}
