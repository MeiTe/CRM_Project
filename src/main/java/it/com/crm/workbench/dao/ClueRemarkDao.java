package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> findByClueId(String clueId);
}
