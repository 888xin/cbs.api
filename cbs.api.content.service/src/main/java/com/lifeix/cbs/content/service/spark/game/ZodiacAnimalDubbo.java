package com.lifeix.cbs.content.service.spark.game;

import org.json.JSONException;

import com.lifeix.exception.service.L99IllegalDataException;
import com.lifeix.exception.service.L99IllegalParamsException;
import com.lifeix.exception.service.L99NetworkException;

public interface ZodiacAnimalDubbo {
    /**
     * 定时任务生成期号
     */
    public void createZodiacAnimalIssue() throws JSONException;
    
    public void toPrize() throws JSONException, L99IllegalParamsException, L99IllegalDataException, L99NetworkException;
}
