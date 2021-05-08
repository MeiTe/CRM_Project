package it.com.crm.workbench.test;

import it.com.crm.utils.ServiceFactory;
import it.com.crm.utils.UUIDUtil;
import it.com.crm.workbench.domain.Activity;
import it.com.crm.workbench.service.ActivityService;
import it.com.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Test;

public class ActivityTest {


    @Test
    public void saveActivity(){
        Activity activity = new Activity();
        activity.setId(UUIDUtil.getUUID());
        activity.setName("迪斯尼啵啵熊");
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = service.activitySave(activity);
        System.out.println(flag);
    }

}
