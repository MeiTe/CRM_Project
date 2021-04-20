package it.com.crm.workbench.service;

import it.com.crm.workbench.domain.Clue;
import it.com.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueService {
    Boolean saveClue(Clue clue);

    Clue findOneByID(String id);

    List<String> findActivityIDByclueId(String clueId);

    Boolean deleteByIdInRelation(String id);

    Boolean saveActivityRelation(String clueId, String[] activityIds);
}
