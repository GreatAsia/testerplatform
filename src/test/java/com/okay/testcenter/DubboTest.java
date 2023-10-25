package com.okay.testcenter;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DubboTest {

    public static <T> T getDubboService(String interfStr, String zookeeperUrl, String appName) throws ClassNotFoundException {
        ApplicationConfig application = new ApplicationConfig();
        application.setName(appName);
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zookeeperUrl);
        Class<?> interf = Class.forName(interfStr);
        ReferenceConfig<T> rc = new ReferenceConfig<T>();
        rc.setApplication(application);
        rc.setInterface(interf);
        return rc.get();

    }

    @Test
    public void testDubboInvoke() {
        Object result;

        try {
            result = getDubboService("com.noriental.usersvr.service.okuser.SchoolYearService", "zookeeper://10.10.6.3:2181", "group");
            System.out.println("[result]==" + JSONObject.toJSONString(result));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

  @Test
    public void test(){

        String test = "{\"code\":0,\"data\":\"{\"idCard\":\"\",\"verifyTel\":\"\",\"studentAppStatus\":0,\"areaOrgId\":110298,\"type\":2,\"subjectId\":0,\"belonging\":1,\"graduationStatus\":0,\"orgType\":2,\"password\":\"\",\"otherSystemId\":0,\"padUser\":1,\"tel\":\"\",\"id\":365569,\"credit\":0,\"schoolNumber\":\"\",\"studentPadStatus\":1,\"padTeacher\":0,\"openRegister\":0,\"teacherSpaceStatus\":0,\"studentSpaceStatus\":0,\"creatorType\":0,\"uname\":\"\",\"klassIdsStr\":\"\",\"fatherSystemId\":0,\"shareArea\":0,\"classStatus\":1,\"teachingVersionId\":0,\"motherSystemId\":0,\"name\":\"小明明明\",\"cityOrgType\":2,\"pinyinNameFirstChar\":\"xmmm\",\"teacherTitle\":\"\",\"shareLevel\":0,\"teachingDirectoryId\":0,\"relative\":0,\"status\":1,\"birthday\":\"\",\"intelligentCorrectStatus\":0,\"studentSchoolList\":[{\"applyType\":1,\"gradeId\":10,\"updateTime\":1567654282000,\"orgId\":80,\"orgType\":2,\"createTime\":1538993050000,\"sourceType\":2,\"studentName\":\"小明明明\",\"comment\":\"{\"gender\":1,\"tel\":\"\",\"email\":\"\"}\",\"id\":307342,\"schoolNumber\":\"\",\"stageId\":2,\"status\":1},{\"applyType\":1,\"gradeId\":10,\"updateTime\":1567654282000,\"orgId\":640,\"orgType\":4,\"createTime\":1538993050000,\"sourceType\":2,\"studentName\":\"小明明明\",\"comment\":\"{\"gender\":1,\"tel\":\"\",\"email\":\"\"}\",\"id\":307343,\"schoolNumber\":\"\",\"stageId\":2,\"status\":1}],\"studentSystemId\":0,\"gender\":1,\"shareCity\":0,\"creatorId\":0,\"orgId\":80,\"shareProvince\":0,\"checkStatus\":0,\"regionCode\":0,\"shareSchool\":0,\"identities\":0,\"identity\":\"\",\"klassIds\":[30394,40617],\"nickname\":\"\",\"cityOrgId\":110299,\"email\":\"\",\"stageId\":2,\"gradeId\":10,\"systemId\":81951332031,\"areaOrgType\":2,\"headImage\":\"hd_1hR6lPX7So0.jpg\",\"provinceOrgType\":2,\"userId\":256299,\"lastLoginTime\":1591237819000,\"pinyinName\":\"xiaomingmingming\",\"shareWinner\":0,\"activeStatus\":0,\"createTime\":1538993050000,\"topLevel\":0,\"teacherAppStatus\":0,\"comment\":\"\",\"provinceOrgId\":110300,\"userRegisterType\":0}\",\"message\":\"success\",\"class\":\"com.noriental.validate.bean.CommonResponse\"}";
      System.out.println("test==" + JSONObject.toJSONString(test));
  }
}
