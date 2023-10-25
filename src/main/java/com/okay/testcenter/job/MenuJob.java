package com.okay.testcenter.job;

import com.okay.testcenter.service.user.CacheService;
import com.okay.testcenter.service.user.MenuService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @author XieYangYang
 * @date 2019/11/22 10:07
 */
@Component
@Log4j
@EnableScheduling
public class MenuJob {

    @Autowired
    MenuService MenuService;

    @Autowired
    CacheService cacheService;

   /* @Scheduled(fixedDelay = 300000)
    public void getAllMenu() {
        log.info("----缓存写入开始----");
        List<Menu> menus = MenuService.getMenuByMenu(new Menu());
        //所有有url的menu
        List<Menu> hasUrlMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getMenuUrl() != null) {
                hasUrlMenus.add(menu);
            }
        }
        //组menu
        Map<Integer, List<Menu>> groupMenus = getOrderMenus(menus);
        log.info(Constant.CACHE_ALL_URL_MENU + "缓存写入:" + JSONObject.toJSONString(hasUrlMenus));
        cacheService.setCommonCache(Constant.CACHE_ALL_URL_MENU, hasUrlMenus);

        log.info(Constant.CACHE_GROUP_MENU + "缓存写入:" + JSONObject.toJSONString(groupMenus));
        cacheService.setCommonCache(Constant.CACHE_GROUP_MENU, groupMenus);
        log.info("----缓存写入结束----");
    }

    *//**
     * 获取所有url
     *
     * @param menus 菜单
     * @return 菜单
     *//*
    private Map<Integer, List<Menu>> getOrderURLMenus(List<Menu> menus) {
        Map<Integer, List<Menu>> allURLOrderMenus = new LinkedHashMap<>();
        //存放等级id（ParentId）
        Set<Integer> orderIndexs = new LinkedHashSet<>();
        for (Menu menu : menus) {
            if (orderIndexs.size() == 0 && menu.getMenuUrl() != null) {
                orderIndexs.add(menu.getParentId());
            } else if (!orderIndexs.contains(menu.getParentId()) && menu.getMenuUrl() != null) {
                orderIndexs.add(menu.getParentId());
            }
        }
        //整理url
        for (int orderIndex : orderIndexs) {
            List<Menu> newMenus = new ArrayList<>();
            for (Menu menu : menus) {
                if (menu.getParentId() == orderIndex) {
                    newMenus.add(menu);
                }
            }
            allURLOrderMenus.put(orderIndex, newMenus);
        }
        return allURLOrderMenus;
    }


    *//**
     * 查找菜单
     *
     * @param menus
     * @return allOrderMenus map的id为父菜单的id
     *//*
    private Map<Integer, List<Menu>> getOrderMenus(List<Menu> menus) {
        Map<Integer, List<Menu>> allOrderMenus = new LinkedHashMap<>();
        //存放最外层id
        Set<Integer> menuNodeIds = new LinkedHashSet<>();
        for (Menu menu : menus) {
            if (menu.getIsMenu().equals("0")) {
                menuNodeIds.add(menu.getId());
            }
        }
        //整理url
        for (int menuIndex : menuNodeIds) {
            List<Menu> newMenus = new ArrayList<>();
            newMenus = getOrderMenu(menuIndex, menus, newMenus);
            allOrderMenus.put(menuIndex, newMenus);
        }
        return allOrderMenus;
    }

    *//**
     * 递归查找菜单
     *
     * @param id
     * @param menus
     * @param newMenus
     * @return
     *//*
    public List<Menu> getOrderMenu(int id, List<Menu> menus, List<Menu> newMenus) {
        for (Menu menu : menus) {
            if (menu.getId() == id) {
                newMenus.add(menu);
            } else if (Long.valueOf(menu.getParentId()) == id) {
                newMenus.add(menu);
                getOrderMenu(menu.getId(), menus, newMenus);
            }
        }
        return newMenus;
    }*/
}
