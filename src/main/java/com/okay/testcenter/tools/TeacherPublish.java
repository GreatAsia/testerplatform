package com.okay.testcenter.tools;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import com.okay.testcenter.request.RequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.okay.testcenter.tools.GetTime.getTime;

/**
 * @author zhou
 * @date 2020/11/16
 */
public class TeacherPublish {

    private static final Logger logger = LoggerFactory.getLogger(TeacherPublish.class);
    private static String username = "";
    private static String password = "";
    private static String url_header = "";
    private static String exerciseid = "";
    private static String courseid = "";
    private static String env = "";
    RequestSampler cookies = new RequestSampler();

    public static Boolean publishResource(JSONObject request, RequestSampler cookies) {

        username = request.getString("username");
        password = request.getString("password");
        url_header = request.getString("url_header");
        exerciseid = request.getString("exerciseid");
        courseid = request.getString("courseid");
        env = request.getString("env");


        //公用的参数
        RequestSampler common = new RequestSampler();
        common.setUrl(url_header + "/res_package/pkg_add_pub");
        common.setMethod("Post-Form");
        common.setCookies(cookies.getCookies());
        //发布测评
        common.setRequestId(getRequestId("01"));
        common.setParams(getExamParams(env));
        ResponseSampler examResponse = RequestFactory.build(common);
        boolean exerciseResult = examResponse.getResponse().contains("\"code\":0");
        //发布预习
        common.setRequestId(getRequestId("01"));
        common.setParams(getPreviewParams(env));
        ResponseSampler previewResponse = RequestFactory.build(common);
        boolean previewResult = previewResponse.getResponse().contains("\"code\":0");
        //发布课程
        common.setRequestId(getRequestId("01"));
        common.setParams(getCourseParams(env));
        ResponseSampler courseResponse = RequestFactory.build(common);
        boolean courseResult = courseResponse.getResponse().contains("\"code\":0");
        //发布作业
        common.setRequestId(getRequestId("01"));
        common.setParams(getHomeWorkParams(env));
        ResponseSampler homeWorkResponse = RequestFactory.build(common);
        boolean homeWorkResult = homeWorkResponse.getResponse().contains("\"code\":0");
        Boolean result = exerciseResult & previewResult & courseResult & homeWorkResult;

        return result;

    }


    public static String getRequestId(String productId) {
        return new RequestId().generateRequestId(productId);
    }

    public static String getExamParams(String env) {
        String params = "";
        switch (env) {
            case "dev":
                params = "resource_ids=" + exerciseid + "%2C2%2Csimul%2C1&type_publish=7&publish_name=测评" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=测评" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=60";
                break;
            case "hotfix":
                params = "resource_ids=" + exerciseid + "%2C2%2Csimul%2C1&type_publish=7&publish_name=测评" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=测评" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=60";
                break;
            case "online":
                params = "resource_ids=" + exerciseid + "%2C2%2Csimul%2C1&type_publish=7&publish_name=测评" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=测评" + getTime() + "&order_ids=&goods_id=&system_id=56310&custom_directory_id=3205979&end_time=60";
                break;
            default:
                logger.error("env is error");
        }
        return params;
    }


    public static String getPreviewParams(String env) {
        String params = "";
        switch (env) {
            case "dev":
                params = "resource_ids=2177813%2C3%2Cword%2C1%7C2177789%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1045949%2C1%2Cppt%2C1%7C809407%2C2%2Csimul%2C1&type_publish=8&course_package_ids=" + courseid + "&publish_name=预习" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=预习" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            case "hotfix":
                params = "resource_ids=1086548%2C3%2Cword%2C1%7C2180443%2C3%2Cpdf%2C1%7C2180450%2C3%2Cexcel%2C1%7C28054%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C818074%2C2%2Csimul%2C1&type_publish=8&course_package_ids=" + courseid + "&publish_name=预习" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=预习" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            case "online":
                params = "resource_ids=2177888%2C3%2Cpdf%2C1%7C2177898%2C8%2Cpic%2C1%7C2177816%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1975797%2C11%2Cppt%2C1%7C872935%2C2%2Csimul%2C1&type_publish=8&course_package_ids=" + courseid + "&publish_name=预习" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=预习" + getTime() + "&order_ids=&goods_id=&system_id=56310&custom_directory_id=3205979&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            default:
                logger.error("env is error");
        }
        return params;
    }

    public static String getCourseParams(String env) {
        String params = "";
        switch (env) {
            case "dev":
                params = "resource_ids=2177813%2C3%2Cword%2C1%7C2177789%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1045949%2C1%2Cppt%2C1%7C809407%2C2%2Csimul%2C1&type_publish=1&course_package_ids=" + courseid + "&publish_name=课程" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=课程" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&course_type=1";
                break;
            case "hotfix":
                params = "resource_ids=1086548%2C3%2Cword%2C1%7C2180443%2C3%2Cpdf%2C1%7C2180450%2C3%2Cexcel%2C1%7C28054%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C818074%2C2%2Csimul%2C1&type_publish=1&course_package_ids=" + courseid + "&publish_name=课程" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=课程" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&course_type=1";
                break;
            case "online":
                params = "resource_ids=2177888%2C3%2Cpdf%2C1%7C2177898%2C8%2Cpic%2C1%7C2177816%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1975797%2C11%2Cppt%2C1%7C872935%2C2%2Csimul%2C1&type_publish=1&course_package_ids=" + courseid + "&publish_name=课程" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=课程" + getTime() + "&order_ids=&goods_id=&system_id=56310&custom_directory_id=3205979&course_type=1";
                break;
            default:
                logger.error("env is error");
        }
        return params;
    }

    public static String getHomeWorkParams(String env) {
        String params = "";
        switch (env) {
            case "dev":
                params = "resource_ids=2177813%2C3%2Cword%2C1%7C2177789%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1045949%2C1%2Cppt%2C1%7C809407%2C2%2Csimul%2C1&type_publish=6&course_package_ids=" + courseid + "&publish_name=作业" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=作业" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            case "hotfix":
                params = "resource_ids=1086548%2C3%2Cword%2C1%7C2180443%2C3%2Cpdf%2C1%7C2180450%2C3%2Cexcel%2C1%7C28054%2C8%2Cpic%2C1%7C1803342%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C818074%2C2%2Csimul%2C1&type_publish=6&course_package_ids=" + courseid + "&publish_name=作业" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=作业" + getTime() + "&order_ids=&goods_id=&system_id=16013&custom_directory_id=653733&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            case "online":
                params = "resource_ids=2177888%2C3%2Cpdf%2C1%7C2177898%2C8%2Cpic%2C1%7C2177816%2C7%2Cvideo%2C1%7C28040%2C6%2Caudio%2C1%7C1975797%2C11%2Cppt%2C1%7C872935%2C2%2Csimul%2C1&type_publish=6&course_package_ids=" + courseid + "&publish_name=作业" + getTime() + "&type_target=1&class_ids=13058&student_ids=&group_root_id=&children_group_ids=&title=作业" + getTime() + "&order_ids=&goods_id=&system_id=56310&custom_directory_id=3205979&end_time=" + (System.currentTimeMillis() + 86400000L);
                break;
            default:
                logger.error("env is error");
        }
        return params;
    }

}
