package it.com.crm.settings.dao;

import it.com.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueByCode(String code);
}
