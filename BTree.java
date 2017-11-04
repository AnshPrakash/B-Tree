package col106.a3;

import java.util.List;
import java.util.*;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
    Node root=new Node(2);///////////////
    int b=0;
    String s="";
    int size=0;
    int height=-1;
    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
        //throw new RuntimeException("Not Implemented");
    	if(b%2==0)
    	{
    		root=new Node(b/2);
    		size=0;
    		this.b=b;
    		root.leaf_node=true;
    		height=-1;//root does not have any value therefore empty root means height =-1
    	}
    	else 
    	{
    		throw new bNotEvenException();
    	}
    }

    @Override
    public boolean isEmpty() {
        //throw new RuntimeException("Not Implemented");
        return size==0;
    }

    @Override
    public int size() {
        //throw new RuntimeException("Not Implemented");
    	return size;//no of Key-value Pairs in tree
    }

    @Override
    public int height() {
        //throw new RuntimeException("Not Implemented");
    	if(root.vec.size()==0)
    		return -1;
    	return height;
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        
    	List<Value> values=new Vector<Value>();
        //int h=0;
        values=search(key,root,values,0);
        if(values.size()==0)
        	throw new IllegalKeyException();
        return values;
    }
    public List<Value> search(Key key,Node curr,List<Value> values,int index)
    {
    	
        int i=index;
        while(i<curr.vec.size() && curr.vec.get(i).k.compareTo(key)<0)//k<key
        {
        	//key cannot be be find in the left this element
        	i++;
        }
        if(i<curr.vec.size() && curr.vec.get(i).k.compareTo(key)==0)
        {
        	values.add((Value)curr.vec.get(i).val);//strange
        	if(!curr.leaf_node)
        	{
        		search(key,curr.children.get(i),values,0);
        		if(i+1<curr.vec.size() && curr.vec.get(i+1).k.compareTo(key)==0)
        		{
        			search(key,curr,values,i+1);
        		}
        		else
        		{
        			search(key,curr.children.get(i+1),values,0);
        		}
        	}
        	else if(curr.leaf_node)
        	{
        		if(i+1<curr.vec.size() && curr.vec.get(i+1).k.compareTo(key)==0)
        		{
        			search(key,curr,values,i+1);
        		}
        	}
        	return values;
        }
        else if(!curr.leaf_node)
        {
        	search(key,curr.children.get(i),values,0);
        	return values;
        }
        else if(curr.leaf_node)
        {
        	return values;
        }
    	return values;
    }

    @Override
    public void insert(Key key, Value val) {
        //throw new RuntimeException("Not Implemented");
    	//addition will only happen at leaf node
    	Node curr,prev;
    	prev=null;
    	curr=root;
    	Pairs p=new Pairs(key,val);
    	int index=0;
    	int previndex=0;
    	boolean leaf_node=false;
    	boolean inserted=false;
    	size+=1;
    	while(!inserted)
    	{
    		if(curr.leaf_node)//if curr node is leaf node then
    		{   if(prev==null && height==-1)//root node
    			{
    				height=0;
    			}
    			
    			if(curr.vec.size()<=2*curr.t-2)//insert only if leaf node is not overflowing
    			{	
    				boolean found=false;
    				int lower=0;
    	        	int upper=curr.vec.size();
    	        	//searching in a node
    	        	index=0;
    	        	while(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)<0)
    	        	{
    	        		index++;
    	        	}
    	        	//replacing this seaching algo
    	        	/*while(lower<=upper)//instead of this IndoxOf function can be used
    	        	{	int mid=(lower+upper)/2;
    	        		if(lower==upper)
    	        		{ 
    	        			index=lower;
    	        			break;
    	        		}
    	        		if(curr.vec.get(mid).k.compareTo(key)<0)//k<key
    	        		{
    	        			//upper=mid-1;
    	        			lower=mid+1;
    	        		}
    	        		else if(curr.vec.get(mid).k.compareTo(key)>0)//k>key
    	        		{
    	        			//lower=mid+1;
    	        			upper=mid-1;
    	        		}
    	        		else if(curr.vec.get(mid).k.compareTo(key)==0)
    	        		{
    	        			
    	        			index=mid;
    	        			found=true;
    	        			break;
    	        			
    	        		}
    	        	}
    	        	//insert at the index+1
    	        	if(!found)//if not found
    	        		index=lower;//
    	        	*/
    	        	curr.vec.insertElementAt(p,index); //It shifts the element from index to n to right by 1
    	        	inserted=true;
    	        	curr.no_of_keys+=1;
    			}
    			else if(curr.vec.size()==2*curr.t-1)//leaf node overflow (not inserting)
    			{
    				if(prev==null)  //curr node is root
    				{   //work in progress
    					//Difficult Task let's do it
        				int j=0;
        				prev=new Node(b/2);
        				int med=curr.vec.size()/2; 		//it is t-1
        				Node split=new Node(b/2);
        				//Pairs tmed=new Pairs(curr.vec.get(med).k,curr.vec.get(med).val);
        				//prev.vec.insertElementAt(curr.vec.get(med), previndex);//line just after two loops
        				for(int i=med+1;i<curr.vec.size();i++)
        				{	
        					Pairs tempp=new Pairs(curr.vec.get(i).k,curr.vec.get(i).val);
        					split.vec.addElement(tempp);
        					split.no_of_keys+=1;
        				}
        				for(int i=curr.vec.size()-1;i>med;i--)//each deletion will be O(1)
        				{	
        					curr.vec.remove(i);
        					curr.no_of_keys-=1;
        					j+=1;
        				}
        				Pairs temp=new Pairs(curr.vec.get(med).k,curr.vec.get(med).val);
        				prev.vec.addElement(temp);
        				//prev.vec.insertElementAt(temp, previndex);
        				prev.no_of_keys+=1;
        				prev.children.insertElementAt(curr,0);//this is here only as it is getting added to root
        				prev.children.insertElementAt(split, 1);
        				root=prev;
        				height+=1;
        				curr.vec.remove(med);
        				curr.leaf_node=true;////////////
        				split.leaf_node=true;//////////
        				curr.no_of_keys-=1;
        				//now start again
        				prev=null;
        				curr=root;
        				//Node Children=new Node(b/2);
        				//BUt it was the leaf node how it can have children
        				//for(int i=med+1;i<j+med+1;i++)//j=no_of_key-(med+1)//Copying the children
        				//{   
        					//Tricky #Difficult#Easy after I get it.
        				//	prev.children.add(curr.children.get(i));
        				//}
        				//for(int i=j+med+1;i>=med+1;i--)//j=no_of_key-(med+1)//Deleting the children
        				//{   
        					//Tricky #Difficult#Easy after I get it.
        				//	curr.children.remove(i);
        				//}
        				//We are done
    					
    					
    				}
    				else//It is not the root Node
    				{   //Only difference is that ,it does not have any child.
    					int j=0;
        				int med=curr.vec.size()/2; 		//it is t-1
        				Node split=new Node(b/2);
        				//prev.vec.insertElementAt(curr.vec.get(med), previndex);//line just after two loops
        				for(int i=med+1;i<curr.vec.size();i++)
        				{	
        					Pairs tempp=new Pairs(curr.vec.get(i).k,curr.vec.get(i).val);
        					split.vec.addElement(tempp);
        					split.no_of_keys+=1;
        				}
        				for(int i=curr.vec.size()-1;i>med;i--)//each deletion will be O(1)
        				{	
        					curr.vec.remove(i);
        					curr.no_of_keys-=1;
        					j+=1;
        				}
        				Pairs temp=new Pairs(curr.vec.get(med).k,curr.vec.get(med).val);
        				prev.vec.insertElementAt(temp, previndex);
        				prev.no_of_keys+=1;
        				prev.children.insertElementAt(split, previndex+1);
        				curr.vec.remove(med);
        				curr.no_of_keys-=1;
        				curr.leaf_node=true;//////
        				split.leaf_node=true;//////
        				//start again
        				prev=null;
        				curr=root;
    				}
    			}
    		}
    		else if(!curr.leaf_node)//internal node
    		{	
    			
    			if(prev==null && curr.vec.size()==2*curr.t-1)//if this internal node is root
    			{
    				int j=0;
    				int med=curr.vec.size()/2; 		//it is t-1
    				Node split=new Node(b/2);
    				prev=new Node(b/2);
    				//prev.vec.insertElementAt(curr.vec.get(med), previndex);//line just after two loops
    				for(int i=med+1;i<curr.vec.size();i++)
    				{	
    					Pairs tempp=new Pairs(curr.vec.get(i).k,curr.vec.get(i).val);
    					split.vec.addElement(tempp);
    					split.no_of_keys+=1;
    				}
    				for(int i=curr.vec.size()-1;i>med;i--)//each deletion will be O(1)
    				{	
    					curr.vec.remove(i);
    					curr.no_of_keys-=1;
    					j+=1;
    				}
    				Pairs temp=new Pairs(curr.vec.get(med).k,curr.vec.get(med).val);
    				prev.vec.add(temp);
    				prev.no_of_keys+=1;
    				prev.children.add(curr);
    				prev.children.add(split);
    				root=prev;
    				curr.vec.remove(med);
    				curr.no_of_keys-=1;
    				//j+=1;/////
    				split.leaf_node=curr.leaf_node;//They are at same level and curr was before split
    				//Node Children=new Node(b/2);
    				for(int i=med+1;i<curr.children.size();i++)//j=no_of_key-(med+1)+1//Copying the children
    				{   
    					//Tricky #Difficult#Easyafter I get it.
    					split.children.add(curr.children.get(i));
    				}
    				for(int i=curr.children.size()-1;i>=med+1;i--)//j=no_of_key-(med+1)//Deleting the children
    				{   
    					//Tricky #Difficult#Easy after I get it.
    					curr.children.remove(i);
    				}
    				height+=1;
    				//start again
    				prev=null;
    				curr=root;
    			}
    			////////////////////////////////////////////
    			if(curr.vec.size()==2*curr.t-1)//overflowing internal node
    			{
    				//Difficult Task let's do it
    				
    				int j=0;
    				int med=curr.vec.size()/2; 		//it is t-1
    				Node split=new Node(b/2);
    				//prev.vec.insertElementAt(curr.vec.get(med), previndex);//line just after two loops
    				for(int i=med+1;i<curr.vec.size();i++)
    				{	
    					Pairs tempp=new Pairs(curr.vec.get(i).k,curr.vec.get(i).val);
    					split.vec.addElement(tempp);
    					split.no_of_keys+=1;
    				}
    				for(int i=curr.vec.size()-1;i>med;i--)//each deletion will be O(1)
    				{	
    					curr.vec.remove(i);
    					curr.no_of_keys-=1;
    					j+=1;
    				}
    				Pairs temp=new Pairs(curr.vec.get(med).k,curr.vec.get(med).val);
    				prev.vec.insertElementAt(temp, previndex);
    				prev.no_of_keys+=1;
    				prev.children.insertElementAt(split, previndex+1);
    				curr.vec.remove(med);
    				curr.no_of_keys-=1;
    				split.leaf_node=curr.leaf_node;//They are at same level and curr was before split
    				//Node Children=new Node(b/2);
    				for(int i=med+1;i<curr.children.size();i++)//j=no_of_key-(med+1)//Copying the children
    				{   
    					//Tricky #Difficult#Easyafter I get it.
    					split.children.add(curr.children.get(i));
    				}
    				for(int i=curr.children.size()-1;i>=med+1;i--)//j=no_of_key-(med+1)//Deleting the children
    				{   
    					//Tricky #Difficult#Easy after I get it.
    					curr.children.remove(i);
    				}
    				//start again
    				prev=null;
    				curr=root;
    				//We are done
    				
    			}
    			else if(curr.vec.size()<2*curr.t-1)//non-full internal node
    			{
    				boolean found=false;
    				int lower=0;
    	        	int upper=curr.vec.size();
    	        	//searching in a node
    	        	//replace this searching Algo
    	        	index=0;
    	        	while(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)<0)
    	        	{
    	        		index++;
    	        	}
    	        	///////////////////////
    	        	/*while(lower<=upper)//instead of this IndoxOf function can be used
    	        	{	int mid=(lower+upper)/2;
    	        		if(lower==upper)
    	        		{ 
    	        			index=lower;
    	        			break;
    	        		}
    	        		if(curr.vec.get(mid).k.compareTo(key)<0)//k<key
    	        		{
    	        			lower=mid+1;
    	        		}
    	        		else if(curr.vec.get(mid).k.compareTo(key)>0)
    	        		{
    	        			upper=mid-1;
    	        		}
    	        		else if(curr.vec.get(mid).k.compareTo(key)==0)//found
    	        		{
    	        			index=mid;
    	        			found=true;
    	        			break;	
    	        		}
    	        	}
    	        	if(!found)//if not found
    	        		index=lower;//think when upper<lower
    	        	*/
    	        	prev=curr;
    	        	previndex=index;
    	        	curr=curr.children.get(index);//left child of index!
    			}
    		}
    	}
    	
    	
    }
    @Override
    public void delete (Key key ) throws IllegalKeyException{
    	//if(!key.getClass().equals(root.vec.get(0).getClass()))
    	//	throw new RuntimeException("Key is of different class");
    	int s=search(key).size();
    	for(int i=0;i<s;i++) {
    		Pairs<Key,Value> del=delete(key,root,-1,null);
    		if(del==null)
    		throw new RuntimeException("Key not found"); 	
    	}
    	if(s==0)
    	{
    		throw new IllegalKeyException();//"This key is not in the Tree"
    	}
    }
    
    public Pairs<Key,Value> delete(Key key,Node root,int previndex,Node prev) {
        
    	 //if(!key.getClass().equals(root.vec.get(0).getClass()))
    		// throw new RuntimeException("Key is of different class");
    	 Node curr=root;
    	 Pairs<Key,Value> del=null;
    	 boolean NotThere=false;
    	 int t=b/2;
    	 int index=-1;
    	 //int previndex=-1;
    	 //Node prev=null;
         boolean Deleted=false;
         while(!Deleted && !NotThere)
         {
        	 if(curr.leaf_node)
        	 {		
        		 if(curr.vec.size()==0)
        		 {	
        			 NotThere=true;
        			 return null;
        		 }
        		 if(curr.vec.size()>=t)
        		 {
        			 index=searchKey(curr,key);
        			 if(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)==0)
        			 {
        				 del=curr.vec.get(index);
        				 curr.vec.remove(index);
        				 curr.no_of_keys-=1;
        				 size-=1;
        				 Deleted=true;
        				 return del;
        			 }
        			 else
        			 {
        				 NotThere=true;
        				 return null;
        			 }       				 
        		 }
        		 else if(curr.vec.size()<=t-1)//Underflow of Leaf NOde//has to be root node if less than t-1
        		 {
        			 index=searchKey(curr,key);
        			 if(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)!=0)//If key is NOt THere Do Nothing 
        			 {
        				 NotThere=true;
        				 return null;
        			 }
        			 else if(index<curr.vec.size() && curr==this.root)//root as leaf  simply delete
        			 {
        				 del=curr.vec.get(index);
        				 curr.vec.remove(index);
        				 size-=1;
        				 curr.no_of_keys-=1;
        				 return del;
        				 
        			 }
        			 else if(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)==0)//Key is there hence Delete it
        			 {
        				 //split then delete
        				 if(previndex!=prev.vec.size() && prev.children.get(previndex+1).vec.size()>=t )
        				 {
        					 curr.vec.addElement(prev.vec.get(previndex));
        					 del=curr.vec.get(index);
        					 curr.vec.remove(index);//no_of_key of curr remains the same
        					 prev.vec.remove(previndex);
        					 prev.vec.insertElementAt(prev.children.get(previndex+1).vec.get(0), previndex);
        					 prev.children.get(previndex+1).vec.remove(0);
        					 prev.children.get(previndex+1).no_of_keys-=1;
        					 size-=1;
        					 Deleted=true;
        					 return del;
        					 
        				 }
        				 else if(previndex!=0 && prev.children.get(previndex-1).vec.size()>=t)
        				 {
        					 //this takes O(t)
        					 del=curr.vec.get(index);
        					 curr.vec.remove(index);
        					 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
        					 //In the above steps no_of_keys of the curr remains the same
        					 prev.vec.insertElementAt(prev.children.get(previndex-1).vec.lastElement(), previndex-1);
        					 prev.vec.remove(previndex);
        					 prev.children.get(previndex-1).vec.remove(prev.children.get(previndex-1).vec.size()-1);
        					 prev.children.get(previndex-1).no_of_keys-=1;
        					 size-=1;//AfterStressTest
        					 Deleted=true;
        					 return del;
        				 }
        				 else
        				 {
        					 if(previndex!=prev.vec.size())//try merging with the right sibling
        					 {
        						 del=curr.vec.get(index);
        						 curr.vec.remove(index);
        						 curr.vec.add(prev.vec.get(previndex));
        						 prev.vec.remove(previndex);
        						 prev.no_of_keys-=1;
        						 for(int i=0;i<prev.children.get(previndex+1).vec.size();i++)
        						 {	 
        							 curr.vec.add(prev.children.get(previndex+1).vec.get(i));
        						 	 curr.no_of_keys+=1;
        						 }
        						 prev.children.remove(previndex+1);
        						 size-=1;//AfterStressTest
        						 Deleted=true;
        						 return del;
        						 ///leaf node do not have children
        					 }
        					 else if(previndex!=0)// merging with left sibling 
        					 {
        						 //This is not an efficient code but I am lazy I copied it from above
        						 del=curr.vec.get(index);
        						 curr.vec.remove(index);
        						 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
        						 prev.vec.remove(previndex-1);
        						 prev.no_of_keys-=1;
        						 for(int i=prev.children.get(previndex-1).vec.size()-1;i>=0;i--)//reverse order addition
        						 {	 
        							 curr.vec.insertElementAt(prev.children.get(previndex-1).vec.get(i),0);
        						 	 curr.no_of_keys+=1;
        						 }
        						 prev.children.remove(previndex-1);
        						 size-=1;//AfterStressTest
        						 Deleted =true;
        						 return del;
        						 ///leaf node do not have children
        					 }
        				 }
        			 }
        				 
        		 }
        	 }
        	 else if(!curr.leaf_node)//internal node
        	 {
        		 //here we have write the code for search and deletion both! :<
        		 if(curr==this.root)//if this internal node is the root
        		 {
        			 index=searchKey(curr,key);
        			 if(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)==0)
        			 {
        				 if(curr.vec.size()>1)
        				 {
        					 if(curr.children.get(index).vec.size()>=t)//borrowing from left child
        					 {
        						 //here we are Going to do things recursively
        						 Node temp=curr.children.get(index);
        						 while(!temp.leaf_node)
        						 {
        							 temp=temp.children.get(temp.children.size()-1);
        						 }
        						 Key kk=(Key)temp.vec.get(temp.vec.size()-1).k;//Think about it(type casting)!
        						 Pairs<Key,Value> del_k=null;
        						 del_k=delete(kk,curr.children.get(index),index,curr);//size is reduced in the recursive call
        						 Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 curr.vec.get(index).k=del_k.k;
        						 curr.vec.get(index).val=del_k.val;
        						 Deleted=true;
        						 return temmp;
        						 
        						 
        					 }
        					 else if(curr.children.get(index+1).vec.size()>=t)//borrowing from right child
        					 {
        						 Node temp=curr.children.get(index+1);
        						 while(!temp.leaf_node)
        						 {
        							 temp=temp.children.get(0); 
        						 }
        						 Key kk=(Key)temp.vec.get(0).k;
        						 Pairs<Key,Value> del_k=null;
        						 del_k=delete(kk,curr.children.get(index+1),index+1,curr);//size is reduced in the recursive call
        						 Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 curr.vec.get(index).k=del_k.k;
        						 curr.vec.get(index).val=del_k.val;
        						 Deleted=true;
        						 return temmp;
        					 }
        					 else//merge it:/ then do some recursion
        					 {
        						 curr.children.get(index).vec.add(curr.vec.get(index));
        						 curr.children.get(index).no_of_keys+=1;
        						 curr.vec.remove(index);
        						 curr.no_of_keys-=1;
        						 for(int i=0;i<curr.children.get(index+1).vec.size();i++)
        						 {
        							 curr.children.get(index).vec.add(curr.children.get(index+1).vec.get(i));
        							 curr.children.get(index).no_of_keys+=1;
        						 }
        						 for (int i=0;i<curr.children.get(index+1).children.size();i++)
        							 curr.children.get(index).children.add(curr.children.get(index+1).children.get(i));
        						 curr.children.remove(index+1);
        						 //Pairs<Key,Value> temmp=delete(key,this.root);//size is reduced in the recursive call
        						 Pairs<Key,Value> temmp=delete(key,curr.children.get(index),index,curr);//size is reduced in the recursive call,cuur=root here
        						 Deleted=true;
        						 return temmp;
        						 
        					 }
        				 }
        				 else if(curr.vec.size()==1)//if root have the size equal to 1
        				 {
        					 if(curr.children.get(index).vec.size()>=t)//borrowing from left child
        					 {
        						 //here we are Going to do things recursively
        						 if(height()>1)
        						 {
        							Node temp=curr.children.get(index);
        						 	while(!temp.leaf_node)
        						 	{
        							 	temp=temp.children.get(temp.children.size()-1);
        						 	}
        						 	Key kk=(Key)temp.vec.get(temp.vec.size()-1).k;//Think about it(type casting)!
        						 	Pairs<Key,Value> del_k=null;
        						 	//next line have to make sure have that  curr.children.get(index) have t keys
        						 	del_k=delete(kk,curr.children.get(index),index,curr);//size is reduced in the recursive call
        						 	//del_k=delete(kk,this.root);//size is reduced in the recursive call
        						 	Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 	curr.vec.get(index).k=del_k.k;
        						 	curr.vec.get(index).val=del_k.val;
        						 	Deleted=true;
        						 	return temmp;
        						 }
        						 else if(height==1)//left child have at t element and is a leaf
        						 {
        							 Node temp=curr.children.get(0);//left child
        							 curr.vec.insertElementAt(temp.vec.lastElement(), 0);
        							 Pairs<Key,Value> del_k=null;
        							 del_k=curr.vec.get(1);
        							 curr.vec.remove(1);
        							 temp.vec.remove(temp.vec.size()-1);
        							 temp.no_of_keys-=1;
        							 size-=1;
        							 //this.root=curr;
        							 Deleted=true;
        							 return del_k;
        							 
        						 }
        						 
        					 }
        					 else if(curr.children.get(index+1).vec.size()>=t)//borrowing from right child
        					 {
        						 if(height>1)
        						 {	
        							Node temp=curr.children.get(index+1);
        						 	while(!temp.leaf_node)
        						 	{
        							 	temp=temp.children.get(0); 
        						 	}
        						 	Key kk=(Key)temp.vec.get(0).k;
        						 	Pairs<Key,Value> del_k=null;
        						 	del_k=delete(kk,curr.children.get(index+1),index+1,curr);//size is reduced in the recursive call
        						 	Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 	curr.vec.get(index).k=del_k.k;
        						 	curr.vec.get(index).val=del_k.val;
        						 	Deleted=true;
        						 	return temmp;
        						 }
        						 else if(height==1)
        						 {
        							 Node temp=curr.children.get(1);//right child
        							 curr.vec.insertElementAt(temp.vec.get(0), 0);
        							 Pairs<Key,Value> del_k=null;
        							 del_k=curr.vec.get(1);
        							 curr.vec.remove(1);
        							 temp.vec.remove(0);
        							 temp.no_of_keys-=1;
        							 size-=1;
        							 //this.root=curr;
        							 Deleted=true;
        							 return del_k;
        						 }
        					 }
        					 else//merge it:/ then do some recursion//here is the DIFFERENCE from >=1
        					 {
        						 //none of the child have enough elements
        						 if(height>1)
        						 {
        							 curr.children.get(index).vec.add(curr.vec.get(index));
        							 curr.children.get(index).no_of_keys+=1;
        							 curr.vec.remove(index);
        							 curr.no_of_keys-=1;
        							 for(int i=0;i<curr.children.get(index+1).vec.size();i++)
        							 {
        								 curr.children.get(index).vec.add(curr.children.get(index+1).vec.get(i));
        								 curr.children.get(index).no_of_keys+=1;
        							 }
        							 for (int i=0;i<curr.children.get(index+1).children.size();i++)
        								 curr.children.get(index).children.add(curr.children.get(index+1).children.get(i));
        							 curr.children.remove(index+1);
        							 this.root=curr.children.get(index);//root is changed
    						 	     height-=1;
        							 Pairs<Key,Value> temmp=delete(key,curr.children.get(index),index,curr);//size is reduced in the recursive call
        							 Deleted=true;
        						 	 return temmp;
        						 }
        						 else if(height==1)
        						 {
        							 Node temp=curr.children.get(0);//left child
        							 Pairs<Key,Value> temmp=curr.vec.get(0);
        							 //temp.vec.add(curr.vec.get(0));
        							 //temp.no_of_keys+=1;
        							 for(int i=0;i<curr.children.get(1).vec.size();i++)
        							 {
        								 temp.vec.add(curr.children.get(1).vec.get(i));
        								 temp.no_of_keys+=1;
        							 }
        							 //this.root=temp;
        							 this.root=temp;
        							 height-=1;
        							 size-=1;
        							 Deleted=true;
        							 return temmp;
        						 }
        					 }
        				 }
        			 }
        			 else//root Internal node which does not  contain the key
        			 {  
        				 //here We don't have to check whether it is underflowing or not because it is the root
        				 previndex=index;
        				 prev=curr;
        				 curr=curr.children.get(index);
        			 }
        			 
        		 }
        		 else//This is a internal node which is not root
        		 {
        			 index=searchKey(curr,key);
    				 //here we have to check whether it is underflowing
    				 if(curr.vec.size()==t-1)//Underflowing:<
    				 {
    					 if(previndex!=0 && prev.children.get(previndex-1).vec.size()>=t)//borrowing from the left child
    					 {
    						 curr.vec.insertElementAt(prev.vec.get(previndex-1), 0);
    						 curr.no_of_keys+=1;
    						 prev.vec.insertElementAt(prev.children.get(previndex-1).vec.get(prev.children.get(previndex-1).vec.size()-1), previndex-1); 
    						 prev.vec.remove(previndex);
    						 prev.children.get(previndex-1).vec.remove(prev.children.get(previndex-1).vec.size()-1);
    						 prev.children.get(previndex-1).no_of_keys-=1;
    						 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(prev.children.get(previndex-1).children.size()-1), 0);
    						 prev.children.get(previndex-1).children.remove(prev.children.get(previndex-1).children.size()-1);
    						 //next node
    						 previndex=index+1;
            				 prev=curr;
            				 curr=curr.children.get(previndex);
    					 }
    					 else if(previndex<prev.vec.size() && prev.children.get(previndex+1).vec.size()>=t)//we are going to borrow from the right sibling
    					 {
    						 curr.vec.insertElementAt(prev.vec.get(previndex), curr.vec.size());//here was the error
    						 curr.no_of_keys+=1;
    						 prev.vec.remove(previndex);
    						 prev.vec.insertElementAt(prev.children.get(previndex+1).vec.get(0), previndex);
    						 prev.children.get(previndex+1).vec.remove(0);
    						 prev.children.get(previndex+1).no_of_keys-=1;
    						 curr.children.add(prev.children.get(previndex+1).children.get(0));
    						 prev.children.get(previndex+1).children.remove(0);
    						 //next node
    						 previndex=index;
    						 prev=curr;
    						 curr=curr.children.get(index);
    					 }
    					 else//merge:< it is becoming too large
    					 {
    						 if(prev.vec.size()==1)//root have just one element
    						 {
    							 if(previndex==1)//if curr have left sibling
    							 {
    								 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
    								 curr.no_of_keys+=1;
    								 prev.vec.remove(previndex-1);
    								 //copying  the elements of left sibling
    								 for(int i=prev.children.get(previndex-1).vec.size()-1;i>=0;i--)//reverse order addition
    								 {
    									 curr.vec.insertElementAt(prev.children.get(previndex-1).vec.get(i), 0);
    									 curr.no_of_keys+=1;
    								 }
    								 //copying children of left sibling
    								 int childtransfer=prev.children.get(previndex-1).vec.size();//vec<->children
    								 for(int i=prev.children.get(previndex-1).children.size()-1;i>=0;i--)//copying in other direction
    								 {
    									 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(i), 0);
    								 }
    								 prev.children.remove(previndex-1);
    								 this.root=curr;
    								 height-=1;
    								 //traveling
    								 prev=curr;
    								 previndex=index+1+childtransfer;
    								 curr=curr.children.get(previndex);
    							 }
    							 else if(previndex==0)//if curr have right sibling
    							 {
    								 curr.vec.add(prev.vec.get(previndex));
    								 curr.no_of_keys+=1;
    								 prev.vec.remove(previndex);
    								 prev.no_of_keys-=1;
    								 //coping the elements of its right sibling
    								 for(int i=0;i<prev.children.get(previndex+1).vec.size();i++)
    								 {
    									 curr.vec.add(prev.children.get(previndex+1).vec.get(i));
    									 curr.no_of_keys+=1;
    								 }
    								 //coping children
    								 for(int i=0;i<prev.children.get(previndex+1).children.size();i++)
    								 {
    									 curr.children.add(prev.children.get(previndex+1).children.get(i));
    								 }
    								 prev.children.remove(previndex+1);
    								 this.root=curr;
    								 height-=1;
    								 //traveling
    								 prev=curr;
    								 previndex=index;
    								 curr=curr.children.get(index);
    								 

    							 }
    						 }
    						 else//prev will have at least t element .we know that!
    						 {
    							 if(previndex<prev.vec.size())//if curr have right sibling
    							 {
    								 curr.vec.add(prev.vec.get(previndex));
    								 curr.no_of_keys+=1;
    								 prev.vec.remove(previndex);
    								 prev.no_of_keys-=1;
    								 //coping the elements of its right sibling
    								 for(int i=0;i<prev.children.get(previndex+1).vec.size();i++)
    								 {
    									 curr.vec.add(prev.children.get(previndex+1).vec.get(i));
    									 curr.no_of_keys+=1;
    								 }
    								 //coping children
    								 for(int i=0;i<prev.children.get(previndex+1).children.size();i++)
    								 {
    									 curr.children.add(prev.children.get(previndex+1).children.get(i));
    								 }
    								 prev.children.remove(previndex+1);
    								 //traveling
    								 prev=curr;
    								 previndex=index;
    								 curr=curr.children.get(index);
    								 
    								 //done 
    							 }
    							 else if(previndex!=0)//if curr have left sibling
    							 {
    								 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
    								 curr.no_of_keys+=1;
    								 prev.vec.remove(previndex-1);
    								 //copying  the elements of left sibling
    								 for(int i=prev.children.get(previndex-1).vec.size()-1;i>=0;i--)//reverse addition
    								 {
    									 curr.vec.insertElementAt(prev.children.get(previndex-1).vec.get(i), 0);
    									 curr.no_of_keys+=1;
    								 }
    								 //copying children of left sibling
    								 int childtransfer=prev.children.get(previndex-1).vec.size();//vec<->children
    								 for(int i=prev.children.get(previndex-1).children.size()-1;i>=0;i--)//reverse addition
    								 {
    									 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(i), 0);
    								 }
    								 prev.children.remove(previndex-1);
    								 //done
    								 //traveling
    								 prev=curr;
    								 previndex=index+1+childtransfer;
    								 curr=curr.children.get(previndex);
    							 }
    						 }
    					 }
    					 
    				 }
        			 else if(index<curr.vec.size() && curr.vec.get(index).k.compareTo(key)==0)
        			 {
        				 //if(curr.vec.size()>1)
        				 //{
        					 if(curr.children.get(index).vec.size()>=t)//borrowing from left child
        					 {
        						 //here we are Going to do things recursively
        						 Node temp=curr.children.get(index);
        						 while(!temp.leaf_node)
        						 {
        							 temp=temp.children.get(temp.children.size()-1);
        						 }
        						 Key kk=(Key)temp.vec.get(temp.vec.size()-1).k;//Think about it(type casting)!
        						 Pairs<Key,Value> del_k=null;
        						 del_k=delete(kk,curr.children.get(index),index,curr);//size is reduced in the recursive call
        						 Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 curr.vec.get(index).k=del_k.k;
        						 curr.vec.get(index).val=del_k.val;
        						 Deleted=true;
        						 return temmp;
        						 
        						 
        					 }
        					 else if(curr.children.get(index+1).vec.size()>=t)//borrowing from right child
        					 {
        						 Node temp=curr.children.get(index+1);
        						 while(!temp.leaf_node)
        						 {
        							 temp=temp.children.get(0); 
        						 }
        						 Key kk=(Key)temp.vec.get(0).k;
        						 Pairs<Key,Value> del_k=null;
        						 del_k=delete(kk,curr.children.get(index+1),index+1,curr);//size is reduced in the recursive call
        						 Pairs<Key,Value> temmp=new Pairs(curr.vec.get(index).k,curr.vec.get(index).val);
        						 curr.vec.get(index).k=del_k.k;
        						 curr.vec.get(index).val=del_k.val;
        						 Deleted=true;
        						 return temmp;
        					 }
        					 else//merge it:/ then do some recursion
        					 {
        						 curr.children.get(index).vec.add(curr.vec.get(index));
        						 curr.children.get(index).no_of_keys+=1;
        						 curr.vec.remove(index);
        						 curr.no_of_keys-=1;
        						 for(int i=0;i<curr.children.get(index+1).vec.size();i++)
        						 {
        							 curr.children.get(index).vec.add(curr.children.get(index+1).vec.get(i));
        							 curr.children.get(index).no_of_keys+=1;
        						 }
        						 for (int i=0;i<curr.children.get(index+1).children.size();i++)
        							 curr.children.get(index).children.add(curr.children.get(index+1).children.get(i));
        						 curr.children.remove(index+1);
        						 Pairs<Key,Value> temmp=delete(key,curr.children.get(index),index,curr);//size is reduced in the recursive call
        						 //Pairs<Key,Value> temmp=delete(key,this.root);//size is reduced in the recursive call
        						 Deleted=true;
        						 return temmp;
        						 
        					 }
        				 //}
        				 
        			 }
        			 else//Internal node not the root node and does not contain the key
        			 {
        				 //here we have to check whether it is underflowing
        				 /*if(curr.no_of_keys==t-1)//Underflowing:<
        				 {
        					 if(previndex!=0 && prev.children.get(previndex-1).vec.size()>=t)//borrowing from the left child
        					 {
        						 curr.vec.insertElementAt(prev.vec.get(previndex-1), 0);
        						 curr.no_of_keys+=1;
        						 prev.vec.insertElementAt(prev.children.get(previndex-1).vec.get(prev.children.get(previndex-1).vec.size()-1), previndex-1); 
        						 prev.vec.remove(previndex);
        						 prev.children.get(previndex-1).vec.remove(prev.children.get(previndex-1).vec.size()-1);
        						 prev.children.get(previndex-1).no_of_keys-=1;
        						 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(prev.children.get(previndex-1).children.size()-1), 0);
        						 prev.children.get(previndex-1).children.remove(prev.children.get(previndex-1).children.size()-1);
        						 //next node
        						 previndex=index+1;
                				 prev=curr;
                				 curr=curr.children.get(index);
        					 }
        					 else if(previndex<prev.vec.size() && prev.children.get(previndex+1).vec.size()>=t)//we are going to borrow from the right sibling
        					 {
        						 curr.vec.insertElementAt(prev.vec.get(previndex), curr.vec.size()-1);
        						 curr.no_of_keys+=1;
        						 prev.vec.remove(previndex);
        						 prev.vec.insertElementAt(prev.children.get(previndex+1).vec.get(0), previndex);
        						 prev.children.get(previndex+1).vec.remove(0);
        						 prev.children.get(previndex+1).no_of_keys-=1;
        						 curr.children.add(prev.children.get(previndex+1).children.get(0));
        						 prev.children.get(previndex+1).children.remove(0);
        						 //next node
        						 previndex=index;
        						 prev=curr;
        						 curr=curr.children.get(index);
        					 }
        					 else//merge:< it is becoming too large
        					 {
        						 if(prev.vec.size()==1)//root have just one element
        						 {
        							 if(previndex==1)//if curr have left sibling
        							 {
        								 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
        								 curr.no_of_keys+=1;
        								 prev.vec.remove(previndex-1);
        								 //copying  the elements of left sibling
        								 for(int i=0;i<prev.children.get(previndex-1).vec.size();i++)
        								 {
        									 curr.vec.insertElementAt(prev.children.get(previndex-1).vec.get(i), 0);
        									 curr.no_of_keys+=1;
        								 }
        								 //copying children of left sibling
        								 int childtransfer=prev.children.get(previndex-1).children.size();
        								 for(int i=0;i<prev.children.get(previndex-1).children.size();i++)
        								 {
        									 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(i), 0);
        								 }
        								 prev.children.remove(previndex-1);
        								 this.root=curr;
        								 height-=1;
        								 //traveling
        								 prev=curr;
        								 previndex=index+1+childtransfer;
        								 curr=curr.children.get(previndex);
        							 }
        							 else if(previndex==0)//if curr have right sibling
        							 {
        								 curr.vec.add(prev.vec.get(previndex));
        								 curr.no_of_keys+=1;
        								 prev.vec.remove(previndex);
        								 prev.no_of_keys-=1;
        								 //coping the elements of its right sibling
        								 for(int i=0;i<prev.children.get(previndex+1).vec.size();i++)
        								 {
        									 curr.vec.add(prev.children.get(previndex+1).vec.get(i));
        									 curr.no_of_keys+=1;
        								 }
        								 //coping children
        								 for(int i=0;i<prev.children.get(previndex+1).children.size();i++)
        								 {
        									 curr.children.add(prev.children.get(previndex+1).children.get(i));
        								 }
        								 prev.children.remove(previndex+1);
        								 this.root=curr;
        								 height-=1;
        								 //traveling
        								 prev=curr;
        								 previndex=index;
        								 curr=curr.children.get(index);
        								 

        							 }
        						 }
        						 else//prev will have at least t element .we know that!
        						 {
        							 if(previndex<prev.vec.size())//if curr have right sibling
        							 {
        								 curr.vec.add(prev.vec.get(previndex));
        								 curr.no_of_keys+=1;
        								 prev.vec.remove(previndex);
        								 prev.no_of_keys-=1;
        								 //coping the elements of its right sibling
        								 for(int i=0;i<prev.children.get(previndex+1).vec.size();i++)
        								 {
        									 curr.vec.add(prev.children.get(previndex+1).vec.get(i));
        									 curr.no_of_keys+=1;
        								 }
        								 //coping children
        								 for(int i=0;i<prev.children.get(previndex+1).children.size();i++)
        								 {
        									 curr.children.add(prev.children.get(previndex+1).children.get(i));
        								 }
        								 prev.children.remove(previndex+1);
        								 //traveling
        								 prev=curr;
        								 previndex=index;
        								 curr=curr.children.get(index);
        								 
        								 //done 
        							 }
        							 else if(previndex!=0)//if curr have left sibling
        							 {
        								 curr.vec.insertElementAt(prev.vec.get(previndex-1),0);
        								 curr.no_of_keys+=1;
        								 prev.vec.remove(previndex-1);
        								 //copying  the elements of left sibling
        								 for(int i=0;i<prev.children.get(previndex-1).vec.size();i++)
        								 {
        									 curr.vec.insertElementAt(prev.children.get(previndex-1).vec.get(i), 0);
        									 curr.no_of_keys+=1;
        								 }
        								 //copying children of left sibling
        								 int childtransfer=prev.children.get(previndex-1).children.size();
        								 for(int i=0;i<prev.children.get(previndex-1).children.size();i++)
        								 {
        									 curr.children.insertElementAt(prev.children.get(previndex-1).children.get(i), 0);
        								 }
        								 prev.children.remove(previndex-1);
        								 //done
        								 //traveling
        								 prev=curr;
        								 previndex=index+1+childtransfer;
        								 curr=curr.children.get(previndex);
        							 }
        						 }
        					 }
        					 
        				 }*/
        				 //previndex=index;
        				 //prev=curr;
        				 //curr=curr.children.get(index);
        				 if(curr.no_of_keys>=t)//if internal node is not underflowing (why I missed it)
            			 {
        					 
        					 previndex=index;
            				 prev=curr;
            				 curr=curr.children.get(index);
            				 
            			 }
        			 }
        			 
        		 }
        	 }
         }
               
        return  null;
    }
    private int searchKey(Node curr,Key key)
    { 	
    	int i=0;
    	while(i<curr.vec.size())
    	{
    		if(curr.vec.get(i).k.compareTo(key)<0)
    			i++;
    		else
    			break;
    	}
    	return i;//i is the index where key ==k or key>k or key is larger than all the elements
    }
    @Override
    public String toString()
    {	s="";
    	String thes;
    	if(root.vec.size()==0)
    	{
    		s="[]";
    		return s;
    	}
    	else
    	{
    		thes=toString(root);
    		thes=thes.substring(0,thes.length()-1);//THE s
    	}
    	return thes;
    }
    
    public String toString(Node root)
    {	//int index=0;
    	s+="[";
    	if(!root.leaf_node)//if it is an internal node
    	{
    		for(int index=0;index<root.vec.size();index++)
    		{	
    			toString(root.children.get(index));
    			s+=root.vec.get(index).k+"="+root.vec.get(index).val+",";
    			//s+=root.leaf_node+",";
    		}
    		toString(root.children.get(root.vec.size()));
    		s=s.substring(0, s.length()-1);
    		s+="],";
    	}
    	else
    	{
    		for(int i=0;i<root.vec.size();i++)
    		{
    			if(i!=root.vec.size()-1)
    			{	
    				s+=root.vec.get(i).k+"="+root.vec.get(i).val+",";
    				//s+=root.leaf_node+",";
    			}
    			else
    			{	
    				s+=root.vec.get(i).k+"="+root.vec.get(i).val;
    				//s+=root.leaf_node;
    			}
    		}
    		s+="],";
    		//return s;
    	}
    	
    	return s;
    }
    //public int BinarySearch(Node curr,Key k)
    //{
    //	return null;
    //}
}
class Pairs <Key extends Comparable<Key>,Value> {
			Key k;
			Value val;
			Pairs(Key key,Value val )
			{
				k=key;
				this.val=val;
			}
	}
class Node {
	int t=-1;
	int no_of_keys=0;//no of Keys in this node
	boolean leaf_node=false;
	Vector<Node> children=new Vector<Node>();//There are no_of_keys+1//references to children//THIS IS A PROBLEM
	Vector<Pairs> vec;
	Node (int t)//creates an empty node
	{   
		this.t=t;
		vec=new Vector<Pairs>();
	}
}
