package com.example.standalone;

import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

public class _2用户与组Test extends _0BaseTests{


    @Test
    public void 创建组和用户() {
        for (int i = 0; i < 10; i++) {
            String random = UUID.randomUUID().toString().substring(0, 3);
            Group group = identityService.newGroup(random + i);
            group.setName("Group_" + random + i);
            group.setType("TYPE_" + random + i);
            identityService.saveGroup(group);
        }

        for (int i = 0; i < 50; i++) {
            String random = UUID.randomUUID().toString().substring(0, 3);
            User user = identityService.newUser(random + i);
            user.setFirstName("adam" + random + i);
            user.setLastName("yao" + random + i);
            user.setEmail("adam" + random + "@qq.com");
            user.setPassword(random);
            identityService.saveUser(user);
        }
    }

    @Test
    public void 查询组列表() {
        identityService.createGroupQuery().groupNameLike("%Group_8%").list().forEach(group -> System.out.println(group.getName()));
        System.out.println("======================");

        identityService.createGroupQuery().groupMember("0d2").list().forEach(group -> System.out.println(group.getName()));
    }

    @Test
    public void 查询用户列表() {
        identityService.createUserQuery().userLastNameLike("%adam8%").list().forEach(user -> System.out.println(user.getFirstName()));
    }

    @Test
    public void 分页查询用户列表() {
        identityService.createUserQuery().listPage(1,5).forEach(user -> System.out.println(user.getFirstName()));
    }

    @Test
    public void Sql查询用户列表() {
        identityService.createNativeUserQuery()
                .sql("SELECT * FROM ACT_ID_USER where FIRST_ LIKE #{name}")
                .parameter("name", "%adam8%")
                .list()
                .forEach(user -> System.out.println(user.getFirstName()));
    }
}
