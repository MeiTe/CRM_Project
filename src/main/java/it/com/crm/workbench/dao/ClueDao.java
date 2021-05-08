package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {
    int saveClue(Clue clue);

    Clue findOneByID(String id);

    List<String> findActivityIDByclueId(String clueId);

    Clue findByID(String clueId);
}
