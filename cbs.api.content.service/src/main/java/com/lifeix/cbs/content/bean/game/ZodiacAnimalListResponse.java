package com.lifeix.cbs.content.bean.game;

import java.util.List;

import com.lifeix.user.beans.ListResponse;
import com.lifeix.user.beans.Response;

public class ZodiacAnimalListResponse  extends ListResponse implements Response {

    private static final long serialVersionUID = 1L;

    private List<ZodiacAnimalResponse> zodiacs;

    /**
     * 旺气生肖轮播广告（中奖最多）
     */
    private Object[] quality_ad;

    /**
     * 潜力生肖轮播广告(未中奖)
     */
    private Object[] potential_ad;

    public Object[] getQuality_ad() {
        return quality_ad;
    }

    public void setQuality_ad(Object[] quality_ad) {
        this.quality_ad = quality_ad;
    }

    public Object[] getPotential_ad() {
        return potential_ad;
    }

    public void setPotential_ad(Object[] potential_ad) {
        this.potential_ad = potential_ad;
    }

    public List<ZodiacAnimalResponse> getZodiacs() {
        return zodiacs;
    }


    public void setZodiacs(List<ZodiacAnimalResponse> zodiacs) {
        this.zodiacs = zodiacs;
    }


    @Override
    public String getObjectName() {
	// TODO Auto-generated method stub
	return null;
    }
    
}
