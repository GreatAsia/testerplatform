package com.okay.testcenter.impl.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.bean.Group;
import com.okay.testcenter.domain.bean.Menu;
import com.okay.testcenter.domain.user.GroupSimpleDetailResponse;
import com.okay.testcenter.mapper.user.GroupMapper;
import com.okay.testcenter.service.user.GroupService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @auth 谢扬扬
 * @date 2020/8/28 14:19
 */
@Service
public class GroupServiceImpl implements GroupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    GroupMapper groupMapper;

    @Override
    public Group getGroupByGroup(Group Group) {
        return null;
    }

    @Override
    public Map<Integer, List<Menu>> getGroupMenuByGroup(Group Group) {
        return null;
    }

    @Override
    public int insertOrUpdateGroupArray(JSONObject json, HttpSession session) {
        JSONArray jsonArray = json.getJSONArray("params");

        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            logger.error("user not exist==" + userName);
            return 1;
        }

        Group DelGroup = new Group();
        DelGroup.setRoleId(json.getInteger("roleId"));
        DelGroup.setUpdateBy(userName);
        DelGroup.setUpdateAt(new Date());
        groupMapper.deleteByRoleId(DelGroup);

        if(jsonArray.size()<=0){
            return 1;
        }

        List<Group> groups = new ArrayList<>();

        for (Object g : jsonArray) {
            JSONObject groupJSON = JSONObject.parseObject(JSONObject.toJSONString(g));
            Group group = new Group();
            group.setId(groupJSON.getInteger("id"));
            group.setRoleId(json.getInteger("roleId"));
            group.setMenuId(groupJSON.getString("menuId"));
            group.setIsDelete("0");
            group.setCreateBy(userName);
            group.setUpdateBy(userName);
            group.setUpdateAt(new Date());
            groups.add(group);
        }
        return groupMapper.updateOrInsertGroup(groups);
    }
    /**
     * 加载url对应的roles
     *
     * @param group group
     * @return map key为url value为
     */
    @Override
    public Map<String, String> getAllMenusAndRole(Group group) {
        // 查询所有groupSimpleDetail
        List<GroupSimpleDetailResponse> groupSimpleDetails = groupMapper.selectGroupSimpleDetailByGroup(group);
        Map<String, String> filterAllMenusByRoles = new HashMap<String, String>();
        for (GroupSimpleDetailResponse groupSimpleDetailResponse : groupSimpleDetails) {
            if (groupSimpleDetailResponse.getMenuUrl() == null) {
                continue;
            }
            if (filterAllMenusByRoles == null || filterAllMenusByRoles.get(groupSimpleDetailResponse.getMenuUrl()) == null) {
                filterAllMenusByRoles.put(groupSimpleDetailResponse.getMenuUrl(), groupSimpleDetailResponse.getRoleCode());
                continue;
            }
            // 符合要求

            // roles整理
            String roles = filterAllMenusByRoles.get(groupSimpleDetailResponse.getMenuUrl()) + "," + groupSimpleDetailResponse.getRoleCode();
            filterAllMenusByRoles.put(groupSimpleDetailResponse.getMenuUrl(), roles);
        }
        return filterAllMenusByRoles;
    }
}
