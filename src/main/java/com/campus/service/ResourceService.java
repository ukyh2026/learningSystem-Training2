package com.campus.service;

import com.campus.dao.ResourceDao;
import com.campus.dao.UserDao;
import com.campus.dao.impl.InMemoryResourceDao;
import com.campus.dao.impl.InMemoryUserDao;
import com.campus.index.InvertedIndex;
import com.campus.model.LearningResource;
import com.campus.model.Role;
import com.campus.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ResourceService {
    ResourceDao resourceDao = new InMemoryResourceDao();
    InvertedIndex invertedIndex;
    UserService userService;
//    UserDao userDao = new InMemoryUserDao();
//    UserService userService = new UserService(userDao);


    public ResourceService(ResourceDao resourceDao, UserService userService,InvertedIndex invertedIndex ) {
        this.resourceDao = resourceDao;
        this.invertedIndex = invertedIndex;
        this.userService = userService;
    }

    public boolean addResource(User operator, String title, String category, Set<String> tags, int hearSocre){
        if(operator == null || !userService.checkUserPermission(operator,Role.TEACHER)){
            System.out.println("该用户没有操作权限，是有教师或者管理者才有权限录入学习资料");
            return false;
        }
        if(title== null || title.trim().isEmpty()) {
            System.out.println("添加失败 资料标题不能为空 ");
            return false;
        }

        if(category== null || category.trim().isEmpty()) {
            System.out.println("添加失败 资料类别不能为空");
            return false;
        }
        int id= resourceDao.generateId();
        LearningResource resource = new LearningResource(id,title,category,tags,hearSocre);
        resourceDao.addResource(resource);
        invertedIndex.addResource(resource);
        System.out.println("学习资料添加成功,ID:"+id);
        return true;
    }

    public boolean deleteResource(User operator,int id){
        if(operator == null || !userService.checkUserPermission(operator,Role.TEACHER)){
            System.out.println("该用户没有操作权限，是有教师或者管理者才有权限删除学习资料");
            return false;
        }
        List<LearningResource> resources  = resourceDao.getAlResource();
        LearningResource resource = resources.get(id);
        if(resource == null ) {
            System.out.println("删除失败：学习资料不存在");
            return  false;
        }
        resourceDao.deleteResource(id);
        invertedIndex.removeResource(resource);
        System.out.println("学习资料删除成功");
        return true;
    }

    public List<LearningResource> getAllResources(){
        return resourceDao.getAlResource();
    }
    public List<LearningResource> searchResources(String keyword){
        Set<Integer> ids = invertedIndex.search(keyword);
        List<LearningResource> result = new ArrayList<>();
        for(int id:ids){
            LearningResource resource  =this.resourceDao.getResourceByID(id);
            if(resource!=null)
                result.add(resource);
        }
        return result;
    }
}
