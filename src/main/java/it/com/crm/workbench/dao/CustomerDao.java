package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer findByName(String name);

    int saveCustomer(Customer customer);
}
