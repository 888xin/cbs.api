package com.lifeix.cbs.api.action.inner;

import com.lifeix.biz.common.response.DataResponse;
import com.lifeix.cbs.api.action.BaseAction;
import com.lifeix.cbs.api.common.exception.MsgCode;
import com.lifeix.cbs.api.common.util.InternationalResources;
import com.lifeix.cbs.api.util.DataResponseFormat;
import com.lifeix.framework.memcache.MemcacheService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by lhx on 16-2-4 下午5:04
 *
 * @Description
 */
@Controller
@Path("/inner/operate/memcache")
public class OperateMemcacheInnerResource extends BaseAction {

    protected static Logger LOG = LoggerFactory.getLogger(FrontPageInnerResource.class);

    @Autowired
    protected MemcacheService memcacheService;

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("name") String name, @FormParam("ids") String ids) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            String[] idArray = ids.split(",");
            for (String s : idArray) {
                String idStr = String.format("%s:id:%s", name, s);
                memcacheService.delete(idStr);
            }
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @POST
    @Path("/delete/key")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteKey(@FormParam("name") String name) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            boolean flag = memcacheService.delete(name);
            if (flag){
                ret.setCode(DataResponse.OK);
            } else {
                ret.setCode(DataResponse.NO);
                ret.setMsg("删除失败，传入的键值可能不存在！");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }

    @POST
    @Path("/delete/more")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteMore(@FormParam("name") String name, @FormParam("min") Long minId, @FormParam("max") Long maxId) throws JSONException {
        DataResponse<Object> ret = new DataResponse<>();
        try {
            start();
            for (long i = minId; i < maxId + 1; i++) {
                String idStr = String.format("%s:id:%s", name, i+"");
                memcacheService.delete(idStr);
            }
            ret.setCode(DataResponse.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            ret.setCode(DataResponse.NO);
            ret.setMsg(InternationalResources.getInstance().locale(MsgCode.BasicMsg.KEY_BASIC_SERVCER, request.getLocale()));
        } finally {
            end();
        }
        return DataResponseFormat.response(ret);
    }
}
