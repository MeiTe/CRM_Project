package it.com.crm.workbench.service.impl;

import it.com.crm.utils.DateTimeUtil;
import it.com.crm.utils.SqlSessionUtil;
import it.com.crm.utils.UUIDUtil;
import it.com.crm.workbench.dao.*;
import it.com.crm.workbench.domain.*;
import it.com.crm.workbench.service.ClueService;

import java.util.List;

public class ClueServiceImpl implements ClueService {
    //线索
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao =SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao =SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //顾客
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    //转换
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistory = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) {
        Boolean flag=true;
        //```````````````````````````````````````````````````````````````````````````````````````````````````````````````
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clueInfoById=clueDao.findByID(clueId);
        //```````````````````````````````````````````````````````````````````````````````````````````````````````````````
        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        //因为客户是公司，故获取公司名称
        String company = clueInfoById.getCompany();
        //然后去客户这张表，查询这个公司是否存在
        Customer customer=customerDao.findByName(company);
        if (customer==null){
            customer=new Customer();
            //表示客户为空，那么需要添加客户信息
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(createBy);
            customer.setName(company);
            customer.setAddress(clueInfoById.getAddress());
            customer.setContactSummary(clueInfoById.getContactSummary());
            customer.setDescription(clueInfoById.getDescription());
            customer.setWebsite(clueInfoById.getWebsite());
            customer.setPhone(clueInfoById.getPhone());
            customer.setOwner(clueInfoById.getOwner());
            customer.setNextContactTime(clueInfoById.getNextContactTime());
            int count =customerDao.saveCustomer(customer);
            if (count!=1){
                //表示没有添加成功
                flag=false;
            }
        }
        //```````````````````````````````````````````````````````````````````````````````````````````````````````````````
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clueInfoById.getSource());
        contacts.setOwner(clueInfoById.getOwner());
        contacts.setNextContactTime(clueInfoById.getNextContactTime());
        contacts.setMphone(clueInfoById.getMphone());
        contacts.setJob(clueInfoById.getJob());
        contacts.setFullname(clueInfoById.getFullname());
        contacts.setEmail(clueInfoById.getEmail());
        contacts.setDescription(clueInfoById.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clueInfoById.getContactSummary());
        contacts.setAppellation(clueInfoById.getAppellation());
        contacts.setAddress(clueInfoById.getAddress());
        int countContacts=contactsDao.saveContacts(contacts);
        if (countContacts!=1){
            //表示添加失败
            flag=false;
        }
        //```````````````````````````````````````````````````````````````````````````````````````````````````````````````
        //(4) 线索备注转换到客户备注以及联系人备注
        //首先根据线索ID查出备注信息
        List<ClueRemark> clueRemarkLists=clueRemarkDao.findByClueId(clueId);
        for (ClueRemark clueRemarkList : clueRemarkLists) {
            //获取备注信息
            String noteContent = clueRemarkList.getNoteContent();
        }





        return flag;
    }



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
