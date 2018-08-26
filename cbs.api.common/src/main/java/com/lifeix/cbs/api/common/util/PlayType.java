package com.lifeix.cbs.api.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法类型
 *
 * @author roi
 */
public enum PlayType {

    FB_SPF(1, 0, "胜平负"),
    FB_RQSPF(2, 0, "让球胜平负"),
    FB_SIZE(4, 0, "大小球"),
    FB_ODDEVEN(5, 0, "单双数"),
    BB_SPF(6, 1, "胜负"),
    BB_JC(7, 1, "让球胜负"),
    BB_SIZE(9, 1, "大小球"),
    BB_ODDEVEN(10, 1, "单双数");


    PlayType(int id, int type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    private final int id;

    private final int type;

    private final String name;

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据id和类型查找对象
     *
     * @param id
     * @param type
     * @return
     */
    public static PlayType findPlayTypeByIdAndType(int id, int type) {
        PlayType[] playTypes = findPlayTypes(type);
        if (playTypes != null) {
            for (PlayType t : playTypes) {
                if (t.getId() == id) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 返回玩法类型
     *
     * @param type
     * @return
     */
    public static PlayType[] findPlayTypes(int type) {
        int len = values().length;
        List<PlayType> list = new ArrayList<PlayType>(len);
        for (int i = 0; i < len; i++) {
            PlayType playType = values()[i];
            if (playType.getType() == type)
                list.add(playType);
        }
        return list.toArray(new PlayType[list.size()]);
    }

    /**
     * 校验类型是否合法
     *
     * @param type
     * @return
     */
    public static boolean verifyType(int id, int type) {
        PlayType[] playTypes = findPlayTypes(type);
        if (playTypes != null) {
            for (PlayType t : playTypes) {
                if (t.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

}
