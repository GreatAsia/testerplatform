package com.okay.testcenter;



import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.middle.MiddleLoginType;
import com.okay.testcenter.domain.middle.MiddleProject;
import com.okay.testcenter.domain.middle.MiddleRunType;
import com.okay.testcenter.service.middle.MiddleLoginTypeService;
import com.okay.testcenter.service.middle.MiddleProjectService;
import com.okay.testcenter.service.middle.MiddleRunTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiddleProjectTest {


    private static final Logger logger = LoggerFactory.getLogger(MiddleProjectTest.class);
    @Resource
    MiddleProjectService middleProjectService;
    @Resource
    MiddleRunTypeService middleRunTypeService;
    @Resource
    MiddleLoginTypeService middleLoginTypeService;


    @Test
    public void testInsert(){

        MiddleProject middleProject = new MiddleProject();
        middleProject.setName("教师空间");
        middleProjectService.insertMiddleProject(middleProject);

    }

    @Test
    public void testUpdate(){

        MiddleProject middleProject = new MiddleProject();
        middleProject.setName("学生Pad");
        middleProject.setId(1);
        middleProjectService.updateMiddleProject(middleProject);

    }

    @Test
    public void testFindMiddleProjectById(){

       MiddleProject middleProject =  middleProjectService.findMiddleProjectById(1);
       logger.info("[middleProject]" + JSONObject.toJSONString(middleProject));

    }

    @Test
    public void testFindMiddleProjectByName(){

        MiddleProject middleProject =  middleProjectService.findMiddleProjectByName("教师空间");
        logger.info("[middleProject]" + JSONObject.toJSONString(middleProject));

    }

    @Test
    public void testFindMiddleProjectList(){

        List<MiddleProject> middleProject = middleProjectService.findMiddleProjectList();
        logger.info("[Size]" + JSONObject.toJSONString(middleProject.size()));
        logger.info("[middleProject]" + JSONObject.toJSONString(middleProject));
        PageInfo pageInfo = middleProjectService.findMiddleProjectList(2, 10);
        logger.info("[Size]" + pageInfo.getSize());
        logger.info("[pageInfo]" + JSONObject.toJSONString(pageInfo));
        PageInfo pageInfo1 = middleProjectService.findMiddleProjectList(1, 10);
        logger.info("[Size]" + pageInfo1.getSize());
        logger.info("[pageInfo1]" + JSONObject.toJSONString(pageInfo1));
    }

    @Test
    public void testDeleteMiddleProject(){

        middleProjectService.deleteMiddleProject(2);


    }

    @Test
    public void testMiddleRunType() {
        List<MiddleRunType> middleRunTypes = middleRunTypeService.findMiddleRunTypeList();
        logger.info("middleRun==" + JSONObject.toJSONString(middleRunTypes));
    }

    @Test
    public void testMiddleLoginType() {
        List<MiddleLoginType> middleLoginTypes = middleLoginTypeService.findMiddleLoginTypeList();
        logger.info("middleRun==" + JSONObject.toJSONString(middleLoginTypes));
    }
}
