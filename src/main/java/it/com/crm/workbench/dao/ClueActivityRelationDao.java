package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int deleteByIdInRelation(String id);

    int bundSaveActivity(ClueActivityRelation activityRelation);
}
