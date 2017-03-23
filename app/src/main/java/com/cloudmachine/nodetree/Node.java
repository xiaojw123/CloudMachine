package com.cloudmachine.nodetree;

import java.util.ArrayList;
import java.util.List;

public class Node {
        public Node parent;//父节点
    public List<Node> children = new ArrayList<Node>();//子节点
    public String text;//节点显示的文字
    public long id;//节点的值
    public String email;
    public int type;//节点类型  0：组织；1：人员
    public int icon = -1;//是否显示小图标,-1表示隐藏图标
    public boolean isExpanded = false;//是否处于展开状态
    public boolean isSelected = false;//是否选中
    
    public boolean isHaveTwo = false; //是否有第二排
    public String two_email;
    public String two_text;
    public boolean two_isSelected = false;
    private long deviceId;
    private String macAddress;
    
    public String parent_icon,children_icon;
    
    private boolean isOpen = true;
    private int workStatus = -1;
    /**
     * Node构造函数
     * @param text 节点显示的文字
     * @param value 节点的值
     */
    public Node(String text,String email ,int type){
            this.text = text;
//            this.id = id;
            this.email = null!=email?email:"";
            this.type = type;
    }
    
    public void setTwo_email(String two_email){
        this.two_email = two_email;
        isHaveTwo = true;
    }
    public String getTwo_email(){
        return this.two_email;
    }
    public void setTwo_text(String two_text){
        this.two_text = two_text;
        isHaveTwo = true;
    }
    public String getTwo_text(){
        return this.two_text;
    }
    public void setTwo_isSelected(boolean two_isSelected){
        this.two_isSelected = two_isSelected;
    }
    public boolean getTwo_isSelected(){
        return this.two_isSelected;
    }
    
     
    /**
     * 设置父节点
     * @param node
     */
    public void setParent(Node node){
            this.parent = node;
    }
    /**
     * 获得父节点
     * @return
     */
    public Node getParent(){
            return this.parent;
    }
    /**
     * 设置节点文本
     * @param text
     */
    public void setText(String text){
        this.text = text;
    }
    /**
     * 获得节点文本
     * @return
     */
    public String getText(){
        return this.text;
    }
    /**
     * 设置节点参数
     * @param text
     */
    public void setEmail(String email){
            this.email = email;
    }
    /**
     * 获得节点参数
     * @return
     */
    public String getEmail(){
            return this.email;
    }
    /**
     * 设置节点值
     * @param value
     */
    public void setId(long id){
            this.id = id;
    }
    /**
     * 获得节点值
     * @return
     */
    public long getId(){
            return this.id;
    }
    public void setType(int type){
    	this.type = type;
    }
    public int getType(){
    	return this.type;
    }
    public void setDeviceId(long deviceId){
    	this.deviceId = deviceId;
    }
    public long getDeviceId(){
    	return this.deviceId;
    }
    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }
	public String getMacAddress(){
	        return this.macAddress;
	}
	
    
	/**
	 * 设置节点图标文件
	 * @param icon
	 */
	public void setIcon(int icon){
		this.icon = icon;
	}
	/**
	 * 获得图标文件
	 * @return
	 */
	public int getIcon(){
		return icon;
	}
	/**
	 * 设置父节点图标url
	 * @param icon
	 */
	public void setParentIcon(String parent_icon){
		this.parent_icon = parent_icon;
	}
	/**
	 * 获得父节点图标url
	 * @return
	 */
	public String getParentIcon(){
		return parent_icon;
	}
    /**
     * 设置父节点图标url
     * @param icon
     */
    public void setChildrenIcon(String children_icon){
            this.children_icon = children_icon;
    }
    /**
     * 获得父节点图标url
     * @return
     */
    public String getChildrenIcon(){
            return children_icon;
    }
    /**
     * 是否根节点
     * @return
     */
    public boolean isRoot(){
            return parent==null?true:false;
    }
    /**
     * 获得子节点
     * @return
     */
    public List<Node> getChildren(){
            return this.children;
    }
    /**
     * 添加子节点
     * @param node
     */
    public void add(Node node){
            if(!children.contains(node)){
                    children.add(node);
            }
    }
    /**
     * 清除所有子节点
     */
    public void clear(){
    	children.clear();
    }
//    public void clearAll(){
//    	int len = children.size();
//    	for(int i=0; i<len; i++){
//    		Node n = 
//    	}
//            children.clear();
//    }
    /**
     * 删除一个子节点
     * @param node
     */
    public void remove(Node node){
            if(!children.contains(node)){
                    children.remove(node);
            }
    }
    /**
     * 删除指定位置的子节点
     * @param location
     */
    public void remove(int location){
            children.remove(location);
    }
    /**
     * 获得节点的级数,根节点为0
     * @return
     */
    public int getLevel(){
            return parent==null?0:parent.getLevel()+1;
    }
    /**
     * 是否叶节点,即没有子节点的节点
     * @return
     */
    public boolean isLeaf(){
//    	return children.size()<1?true:false;
            return type == 1?true:false;
    }
    /**
     * 当前节点是否处于展开状态 
     * @return
     */
    public boolean isExpanded(){
        return isExpanded;
    }
    /**
     * 设置节点展开状态
     * @return
     */
    public void setExpanded(boolean isExpanded){
        this.isExpanded =  isExpanded;
    }
    /**
    * 当前节点是否处于选中状态 
    * @return
    */
    public boolean isSelected(){
        return isSelected;
    }
    /**
     * 设置节点选中状态
     * @return
     */
    public void setSelected(boolean isSelected){
             this.isSelected =  isSelected;
    }
    
    public boolean isOpen(){
    	return isOpen;
    }
    public void setIsOpen(boolean isOpen){
    	this.isOpen = isOpen;
    }
 
    public int getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}
}