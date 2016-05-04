package com.shopsy.bean;

public class SuggestGetSet 
{
	 
    String name;
    String Id;
    
	public SuggestGetSet(String name, String id)
    {
        this.setName(name);
        this.setId(id); 
    }
    
    public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
    
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
}