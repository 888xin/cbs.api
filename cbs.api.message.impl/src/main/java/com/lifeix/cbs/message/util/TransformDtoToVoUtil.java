package com.lifeix.cbs.message.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lifeix.cbs.api.common.util.CbsTimeUtils;
import com.lifeix.cbs.message.bean.placard.PlacardTempletResponse;
import com.lifeix.cbs.message.dto.PlacardTemplet;

/**
 * Created by lhx on 15-10-19 下午4:32
 *
 * @Description
 */
public class TransformDtoToVoUtil {

    /**
     * 系统公告
     * @param templet
     * @return
     */
    public static PlacardTempletResponse transformPlacardTempletResponse(PlacardTemplet templet, boolean readFlag) {
        PlacardTempletResponse resp = null;
        if(templet != null) {
            resp = new PlacardTempletResponse();
            resp.setTemplet_id(templet.getTempletId());
            resp.setTitle(templet.getTitle());
            resp.setContent(templet.getContent());
            resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(templet.getCreateTime()));
            resp.setEnd_time(CbsTimeUtils.getUtcTimeForDate(templet.getEndTime()));
            resp.setDisable_flag(templet.getDisableFlag());
            resp.setPlacard_count(templet.getPlacardCount());
            resp.setLink_type(templet.getLinkType());
            resp.setLink_data(templet.getLinkData());
            resp.setPlacard_count(templet.getPlacardCount());
            resp.setRead_flag(readFlag);
        }
        return resp;
    }

    /**
     * 系统公告
     * @param templet
     * @return
     */
    public static PlacardTempletResponse transformPlacardResponse(PlacardTemplet templet, boolean readFlag) {
        PlacardTempletResponse resp = null;
        if(templet != null) {
            resp = new PlacardTempletResponse();

            String content = templet.getContent() ;
            String regEx_img = "<img.*src=(.*?)[^>]*?>" ;
            String regEx_img2 = "src=\"?(.*?)(\"|>|\\s+)" ;
            Pattern pat = Pattern.compile(regEx_img2);
            Matcher mat = pat.matcher(content);
            Set<String> set = new HashSet<String>();
            List<String> list = new ArrayList<String>();
            //StringBuilder stringBuilder = new StringBuilder();
            int num = 0 ;
            while(mat.find()){
                String src = mat.group();
                src = src.substring(5,src.length()-1);
                set.add(src);
                list.add("${lx-image-" + num + "}");
                num ++ ;
            }
            pat = Pattern.compile(regEx_img);
            mat = pat.matcher(content);
            num = 0 ;
            while(mat.find()){
                String src = mat.group();
                content = content.replace(src,list.get(num));
                num ++ ;
            }
            //content = content.replaceAll(regEx_img,"").replaceAll("\\s{1,}|\\n","").replaceAll("<p></p>","") ;
            //stringBuilder.append(content);
            resp.setText_images(set);
            resp.setTemplet_id(templet.getTempletId());
            resp.setTitle(templet.getTitle());
            resp.setContent(content);
            resp.setCreate_time(CbsTimeUtils.getUtcTimeForDate(templet.getCreateTime()));
            resp.setEnd_time(CbsTimeUtils.getUtcTimeForDate(templet.getEndTime()));
            resp.setDisable_flag(templet.getDisableFlag());
            resp.setPlacard_count(templet.getPlacardCount());
            resp.setLink_type(templet.getLinkType());
            resp.setLink_data(templet.getLinkData());
            resp.setPlacard_count(templet.getPlacardCount());
            resp.setRead_flag(readFlag);
        }
        return resp;
    }
}
