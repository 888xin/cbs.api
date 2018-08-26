package com.lifeix.cbs.contest.dao.statistic.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.lifeix.cbs.contest.dao.ContestDaoSupport;
import com.lifeix.cbs.contest.dao.statistic.ContestStatisticDao;
import com.lifeix.cbs.contest.dto.bet.Bet;

/**
 * Created by lhx on 15-12-21 上午11:19
 * 
 * @Description
 */
@Repository("contestStatisticDao")
public class ContestStatisticDaoImpl extends ContestDaoSupport implements ContestStatisticDao {

    @Override
    public int betsStatistic(String date) {
	Integer fbOpNum = sqlSession.selectOne("ContestStatisticMapper.getFbOpStatistic", date);
	Integer fbJcNum = sqlSession.selectOne("ContestStatisticMapper.getFbJcStatistic", date);
	Integer bbOpNum = sqlSession.selectOne("ContestStatisticMapper.getBbOpStatistic", date);
	Integer bbJcNum = sqlSession.selectOne("ContestStatisticMapper.getBbOpStatistic", date);
	return fbOpNum + fbJcNum + bbOpNum + bbJcNum;
    }

    @Override
    public int peopleStatistic(String date) {
	List<Bet> fbOpList = sqlSession.selectList("ContestStatisticMapper.getFbOpUserStatistic", date);
	List<Bet> fbJcList = sqlSession.selectList("ContestStatisticMapper.getFbJcUserStatistic", date);
	List<Bet> bbOpList = sqlSession.selectList("ContestStatisticMapper.getBbOpUserStatistic", date);
	List<Bet> bbJcList = sqlSession.selectList("ContestStatisticMapper.getBbJcUserStatistic", date);
	Set<Long> set = new HashSet<Long>();
	for (Bet bet : fbOpList) {
	    set.add(bet.getUserId());
	}
	for (Bet bet : fbJcList) {
	    set.add(bet.getUserId());
	}
	for (Bet bet : bbOpList) {
	    set.add(bet.getUserId());
	}
	for (Bet bet : bbJcList) {
	    set.add(bet.getUserId());
	}
	return set.size();
    }

    @Override
    public int opStatistic(String date) {
	Integer fbOpNum = sqlSession.selectOne("ContestStatisticMapper.getFbOpStatistic", date);
	Integer bbOpNum = sqlSession.selectOne("ContestStatisticMapper.getBbOpStatistic", date);
	return fbOpNum + bbOpNum;
    }

    @Override
    public int jcStatistic(String date) {
	Integer fbJcNum = sqlSession.selectOne("ContestStatisticMapper.getFbJcStatistic", date);
	Integer bbJcNum = sqlSession.selectOne("ContestStatisticMapper.getBbJcStatistic", date);
	return fbJcNum + bbJcNum;
    }

    @Override
    public int fbStatistic(String date) {
	Integer fbOpNum = sqlSession.selectOne("ContestStatisticMapper.getFbOpStatistic", date);
	Integer fbJcNum = sqlSession.selectOne("ContestStatisticMapper.getFbJcStatistic", date);
	return fbOpNum + fbJcNum;
    }

    @Override
    public int bbStatistic(String date) {
	Integer bbOpNum = sqlSession.selectOne("ContestStatisticMapper.getBbOpStatistic", date);
	Integer bbJcNum = sqlSession.selectOne("ContestStatisticMapper.getBbJcStatistic", date);
	return bbOpNum + bbJcNum;
    }
}
