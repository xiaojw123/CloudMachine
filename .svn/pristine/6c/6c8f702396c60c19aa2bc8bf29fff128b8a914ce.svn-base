package com.cloudmachine.nodetree;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class NodeTreeAdapterd extends BaseAdapter{
        private List<Node> allNodes;
        private List<Node> allNodesCache;
        private Context context;
        private LayoutInflater lif;
        private NodeTreeAdapterd oThis = this;
        private int expanderIcon=-1;
        private int collsapsedIcon=-1;
        private DisplayImageOptions displayImageOptions;
        private ImageLoadingListener commImageLoadingLis;
        private int lastChildrenPoint;
        
        public NodeTreeAdapterd(Context convertView){
                allNodes = new ArrayList<Node>();
                allNodesCache= new ArrayList<Node>();
                this.context=convertView;
                this.lif = (LayoutInflater) convertView.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                
                displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.mc_default_icon)
				.showImageForEmptyUri(R.drawable.mc_default_icon)
				.showImageOnFail(R.drawable.mc_default_icon).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
				.build();
//                addAllnode(rootNode);
        }
        
        @Override
        public int getCount() {
                // TODO Auto-generated method stub
                return allNodes.size();
        }

        @Override
        public Object getItem(int position) {
                // TODO Auto-generated method stub
            try{
                return allNodes.get(position);
            }catch(Exception e){
                return null;
            }
            
                
        }

        @Override
        public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
        }
        /****View显示区*****/
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                       convertView = this.lif.inflate(R.layout.node_tree_list_item, null);
                        //得到页面控件
                        holder = new ViewHolder();
                        
                        holder.trans_layout = convertView.findViewById(R.id.trans_layout);
                        holder.line_bottom1 = (TextView)convertView.findViewById(R.id.line_bottom1);
                        holder.line_bottom2 = (TextView)convertView.findViewById(R.id.line_bottom2);
                        holder.line_bottom = (ImageView)convertView.findViewById(R.id.line_bottom);
                        holder.parent_icon = (ImageView)convertView.findViewById(R.id.parent_icon);
                        holder.children_icon = (ImageView)convertView.findViewById(R.id.children_icon);
                        holder.arrow_r = (ImageView)convertView.findViewById(R.id.arrow_r);
                        holder.name_text = (TextView)convertView.findViewById(R.id.name_text);
                        convertView.setTag(holder);
                }else{
                                holder = (ViewHolder)convertView.getTag();
                }
                //将节点的值赋给他们
                        Node node = allNodes.get(position);
                        if(node != null){
                            holder.trans_layout.setVisibility(View.GONE);
//                            holder.arrow_r.setVisibility(View.GONE);
//                            holder.line_bottom.setVisibility(View.GONE);
//                            holder.line_top.setVisibility(View.GONE);
                        		String tempText = node.getText();
//                        		if(tempText.equals("notnull")){
//                        			convertView.setVisibility(View.GONE);
//                        		}else{
                        		if(node.getWorkStatus() == 0 ){
                        			holder.name_text.setTextColor(context.getResources().getColor(R.color.work_status_text0));
                        		}else{
                        			holder.name_text.setTextColor(context.getResources().getColor(R.color.work_status_text1));
                        		}
                    			  holder.name_text.setText(tempText);
                        			  
                             if(position > 0){
                            	 if(!node.isLeaf()){
                            		 holder.trans_layout.setVisibility(View.VISIBLE);
                            		 holder.line_bottom1.setVisibility(View.VISIBLE);
                            	 }else{
                            		 holder.trans_layout.setVisibility(View.GONE);
                            		 holder.line_bottom1.setVisibility(View.GONE);
                            	 }
                             }else{
                            	 holder.line_bottom1.setVisibility(View.VISIBLE);
                            	 holder.trans_layout.setVisibility(View.GONE);
                             }
                             if(!node.isLeaf()){
                            	 if(node.isExpanded&&node.getChildren().size()>0){
                            		 holder.line_bottom.setVisibility(View.VISIBLE);
                            		 holder.line_bottom2.setVisibility(View.GONE);
                            	 }else{
                            		 holder.line_bottom.setVisibility(View.GONE);
                            		 holder.line_bottom2.setVisibility(View.VISIBLE);
                            	 }
                            	 
                            	 holder.parent_icon.setVisibility(View.VISIBLE);
                            	 ImageLoader.getInstance().displayImage(node.getParentIcon(), holder.parent_icon);
                            	 holder.children_icon.setVisibility(View.GONE);
                            	 
//                            	 ImageLoader.getInstance().displayImage("drawable://"+R.drawable.top, viewHolder.blogFlag);
                            	 
                            	 lastChildrenPoint = node.getChildren().size()+position;
                             }else{
                            	 holder.children_icon.setVisibility(View.VISIBLE);
//                            	 ImageLoader.getInstance().displayImage(node.getChildrenIcon(),  holder.children_icon);
                            	 ImageLoader.getInstance().displayImage(node.getChildrenIcon(),  holder.children_icon,
                     					displayImageOptions, commImageLoadingLis);
                            	 holder.parent_icon.setVisibility(View.INVISIBLE);
                            	 if(position == lastChildrenPoint){
                            		 holder.line_bottom.setVisibility(View.GONE);
                            		 holder.line_bottom2.setVisibility(View.VISIBLE);
                            	 }else{
                            		 holder.line_bottom.setVisibility(View.VISIBLE);
                            		 holder.line_bottom2.setVisibility(View.GONE);
                            	 }
                             }
                                     
//                                      holder.imageView.setOnClickListener(new lvLayoutListener(position,0));
//                        		}
                                      
                        }
//                        convertView.setPadding(30*node.getLevel(), 5, 5, 5);
//                        convertView.setPadding(20, 0, 5, 0);
                //Log.i("level", allNodes.get(position).getLevel()+"");
                return convertView;
        }
        
        //根据传入的根节点  得到当前所有节点
        public void addAllnode(Node node){
                allNodes.add(node);
                allNodesCache.add(node);
                /*if(node.isLeaf())return;
                for(int i=0;i<node.getChildren().size();i++){
                        addAllnode(node.getChildren().get(i));
                }*/
        }
        public void clearAll(){
        	if(null != allNodes)
        		allNodes.clear();
        	if(null != allNodesCache)
        		allNodesCache.clear();
        }
        public void clearAllnode(Node node){
        	node.clear();
        	if(node.isLeaf())return;
        	for(int i=0;i<node.getChildren().size();i++){
        		clearAllnode(node.getChildren().get(i));
        	}
        	
        }
        public int getNowLevel(int position){
        	Node n = allNodes.get(position);
        	return n.getLevel();
        }
        //当用户点击某项LIST的时候  控制节点收缩
        public void ExpandOrCollapse(int position){
        	Node n = allNodes.get(position);
        	if(n != null){
        		if(!n.isLeaf()){
        			
//                                n.setExpanded(!n.isExpanded());
//                                filterNode();
        			if(!n.isExpanded){
        				for(int i =0;i<n.getChildren().size();i++){
        					//allNodes.add(n.getChildren().get(i));
        					//存在一个数据显示顺序的问题  要求显示在他的父节点下
        					//点击的节点位置
        					allNodes.add(position+1+i, n.getChildren().get(i));
        				}
        			}else{
        				for(int i =0;i<n.getChildren().size();i++){
        					allNodes.remove(n.getChildren().get(i));
        				}
        			}        
        			n.setExpanded(!n.isExpanded);
        			this.notifyDataSetChanged();
        		}
        	}
        }
        public boolean newExpandOrCollapse(int position){
        	boolean rb = false;
                Node n = allNodes.get(position);
                if(n != null){
                        if(!n.isLeaf()){
                        	
//                                n.setExpanded(!n.isExpanded());
//                                filterNode();
                                if(!n.isExpanded){
                                	int len = n.getChildren().size();
                                	if(len>0){
                                		for(int i =0;i<len;i++){
                        					//allNodes.add(n.getChildren().get(i));
                        					//存在一个数据显示顺序的问题  要求显示在他的父节点下
                        					//点击的节点位置
                        					allNodes.add(position+1+i, n.getChildren().get(i));
                        				}
                                		
                                	}else{
                                		rb = true;
                                	}
                                }else{
                                        for(int i =0;i<n.getChildren().size();i++){
                                                allNodes.remove(n.getChildren().get(i));
                                        }
                                }        
                                n.setExpanded(!n.isExpanded);
                                this.notifyDataSetChanged();
                        }
                }
                return rb;
        }
        
        public void updateItem(int position,List<Node> addListNode){
        	 Node n = allNodes.get(position);
             int listLen = addListNode.size();
             for(int listi=0;listi<listLen;listi++){
            	 n.getChildren().add(addListNode.get(listi));
            	 addListNode.get(listi).setParent(n);
             }
             if(n != null){
                     if(!n.isLeaf()){
                     	
//                             n.setExpanded(!n.isExpanded());
//                             filterNode();
                                     for(int i =0;i<n.getChildren().size();i++){
                                             //allNodes.add(n.getChildren().get(i));
                                             //存在一个数据显示顺序的问题  要求显示在他的父节点下
                                             //点击的节点位置
                                             allNodes.add(position+1+i, n.getChildren().get(i));
                                     }
                             this.notifyDataSetChanged();
                     }
             }
        }
        
        //设置默认打开时展开级别
        public void ExpanderLevel(int level){
                allNodes.clear();
                for(int i =0;i<allNodesCache.size();i++){
                        //得到每一个节点
                        Node n = allNodesCache.get(i);
                        if(n.getLevel()<=level){
                                if(n.getLevel()<level){
                                        n.setExpanded(true);
                                        
                                }else{
                                        n.setExpanded(false);
                                }
                                allNodes.add(n);
                        }else{
                                n.setExpanded(false);
                        }
                }
                oThis.notifyDataSetChanged();
        }
      
        //设置图标
        public void setIcon(int expandedIcon,int collsapsedIcon){
                this.expanderIcon = expandedIcon;
                this.collsapsedIcon= collsapsedIcon;
        }
        public class ViewHolder{
                
        	    View trans_layout; //空白透明区域
        	    ImageView line_bottom;//线图标
                ImageView parent_icon;//父图标
                ImageView children_icon;//子图标
                ImageView arrow_r;//右箭头图标
                TextView name_text;//名称文本〉〉〉
                TextView line_bottom1;//
                TextView line_bottom2;//
                
        }
}