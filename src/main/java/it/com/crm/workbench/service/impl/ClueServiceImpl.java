package it.com.crm.workbench.service.impl;

import it.com.crm.utils.SqlSessionUtil;
import it.com.crm.utils.UUIDUtil;
import it.com.crm.workbench.dao.ClueActivityRelationDao;
import it.com.crm.workbench.dao.ClueDao;
import it.com.crm.workbench.domain.Clue;
import it.com.crm.workbench.domain.ClueActivityRelation;
import it.com.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao =SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public Boolean saveClue(Clue clue) {
        Boolean flag=true;
        int count =clueDao.saveClue(clue);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Clue findOneByID(String id) {
        return clueDao.findOneByID(id);
    }

    @Override
    public List<String> findActivityIDByclueId(String clueId) {
        return clueDao.findActivityIDByclueId(clueId);
    }

    @Override
    public Boolean deleteByIdInRelation(String id) {
        Boolean flag=true;
        int count=clueActivityRelationDao.deleteByIdInRelation(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Boolean saveActivityRelation(String clueId, String[] activityIds) {
        Boolean flag = true;
        ClueActivityRelation activityRelation = new ClueActivityRelation();
        for (String activityId : activityIds) {
            activityRelation.setId(UUIDUtil.getUUID());
            activityRelation.setClueId(clueId);
            activityRelation.setActivityId(activityId);
            int count=clueActivityRelationDao.bundSaveActivity(activityRelation);
            if (count!=1){
                flag=false;
            }
        }
        return flag;
    }
}
