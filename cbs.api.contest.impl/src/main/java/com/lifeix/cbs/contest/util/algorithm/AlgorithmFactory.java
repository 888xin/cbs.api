package com.lifeix.cbs.contest.util.algorithm;

import com.lifeix.cbs.api.common.util.PlayType;

/**
 * 环境类生成工厂
 * 
 * @author peter-dell
 * 
 */
public class AlgorithmFactory {

    /**
     * 生成包装对象
     * 
     * @param type
     * @return
     */
    public static AlgorithmRole createContextRole(PlayType type) {
        if (type == PlayType.FB_SPF) {
            return new AlgorithmRole(new AlgorithmFbSPF());
        } else if (type == PlayType.FB_RQSPF) {
            return new AlgorithmRole(new AlgorithmFbRQSPF());
        } else if (type == PlayType.FB_SIZE) {
            return new AlgorithmRole(new AlgorithmFbSize());
        } else if (type == PlayType.FB_ODDEVEN) {
            return new AlgorithmRole(new AlgorithmFbDss());
        } else if (type == PlayType.BB_SPF) {
            return new AlgorithmRole(new AlgorithmBbSF());
        } else if (type == PlayType.BB_JC) {
            return new AlgorithmRole(new AlgorithmBbRQSPF());
        } else if (type == PlayType.BB_SIZE) {
            return new AlgorithmRole(new AlgorithmBbSize());
        } else if (type == PlayType.BB_ODDEVEN) {
            return new AlgorithmRole(new AlgorithmBbDss());
        } else {
            return null;
        }
    }
}
