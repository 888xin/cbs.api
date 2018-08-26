package com.lifeix.cbs.api.bean.user;

import com.lifeix.user.beans.Response;

public class WeixinUserDataResponse implements Response{
    private static final long serialVersionUID = 1L;
    private Data first;
    private Data keynote1;
    private Data keynote2;
    private Data remark;
    @Override
    public String getObjectName() {
	return null;
    }
    
    public Data getFirst() {
        return first;
    }

    public void setFirst(Data first) {
        this.first = first;
    }

    public Data getKeynote1() {
        return keynote1;
    }

    public void setKeynote1(Data keynote1) {
        this.keynote1 = keynote1;
    }

    public Data getKeynote2() {
        return keynote2;
    }

    public void setKeynote2(Data keynote2) {
        this.keynote2 = keynote2;
    }

    public Data getRemark() {
        return remark;
    }

    public void setRemark(Data remark) {
        this.remark = remark;
    }

    public class Data implements Response{
        private static final long serialVersionUID = 1L;
	private String value;
	private String color;
	
	public Data(){
	    super();
	}
	
	public Data(String value, String color){
	    super();
	    this.value = value;
	    this.color = color;
	}
	
	public String getValue() {
	    return value;
	}

	public void setValue(String value) {
	    this.value = value;
	}

	public String getColor() {
	    return color;
	}

	public void setColor(String color) {
	    this.color = color;
	}

	@Override
        public String getObjectName() {
	    return null;
        }
    }

}
