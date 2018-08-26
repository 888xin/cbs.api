package com.lifeix.cbs.api.common.util;

import java.util.Collection;

import com.lifeix.cbs.api.common.exception.MsgCode.BasicMsg;
import com.lifeix.common.basetype.PermissionType;
import com.lifeix.exception.service.L99IllegalParamsException;

/**
 * data assert
 * 
 * @author peter
 * 
 */
public class ParamemeterAssert {

	/**
	 * assert data null
	 *
	 * @param data
	 * @throws L99IllegalParamsException
	 */
	public static void assertStringNotNull(String... data) throws L99IllegalParamsException {
		if (data == null||"".equals(data)) {
			throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
		}
		for (Object param : data) {
			if (param == null||"".equals(data)) {
				throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
			}
		}
	}

    /**
     * assert data null
     * 
     * @param data
     * @throws L99IllegalParamsException
     */
    public static void assertDataNotNull(Object... data) throws L99IllegalParamsException {
	if (data == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
	for (Object param : data) {
	    if (param == null) {
		throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	    }
	}
    }

    /**
     * assert data null
     * 
     * @param data1
     * @param data2
     * @throws L99IllegalParamsException
     */
    public static void assertDataOrNotNull(Object data1, Object data2) throws L99IllegalParamsException {
	if (data1 == null && data2 == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * assert data null
     * 
     * @param data
     * @throws L99IllegalParamsException
     */
    @SuppressWarnings("rawtypes")
    public static void assertCollectionNotNull(Collection data) throws L99IllegalParamsException {
	if (data == null || data.size() == 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * assert data permission
     * 
     * @param permissionType
     * @throws L99IllegalParamsException
     */
    public static void assertDataPermission(Integer permissionType) throws L99IllegalParamsException {
	if (permissionType == null
	        || (permissionType != PermissionType.ALL_ONLY && permissionType != PermissionType.FRIEND_ONLY && permissionType != PermissionType.SELF_ONLY)) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * assert must have one data
     * 
     * @param image
     * @param content
     * @throws L99IllegalParamsException
     */
    public static void assertOneData(Object data1, Object data2) throws L99IllegalParamsException {
	if (data1 == null && data2 == null) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * assert data range
     * 
     * @param data
     * @param min
     * @param max
     * @throws L99IllegalParamsException
     */
    public static void assertRange(int data, int min, int max) throws L99IllegalParamsException {
	if (data < min || data > max) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    public static void assertPrice(double price) throws L99IllegalParamsException {
	if (Double.compare(price, 0d) < 0) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }

    /**
     * dashboard assert
     * 
     * @param dashboardId
     * @param dashboardType
     * @param dashboardData
     * @throws L99IllegalParamsException
     */
    public static void assertDashboardNotNull(Long dashboardId, Integer dashboardType, Long dashboardData)
	    throws L99IllegalParamsException {
	if (dashboardId == null && (dashboardType == null || dashboardData == null)) {
	    throw new L99IllegalParamsException(BasicMsg.CODE_PARAMEMETER, BasicMsg.KEY_PARAMEMETER);
	}
    }
}
