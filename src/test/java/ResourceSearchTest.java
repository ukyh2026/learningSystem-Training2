import com.campus.dao.ResourceDao;
import com.campus.dao.UserDao;
import com.campus.dao.impl.InMemoryResourceDao;
import com.campus.dao.impl.InMemoryUserDao;
import com.campus.index.InvertedIndex;
import com.campus.model.LearningResource;
import com.campus.model.Role;
import com.campus.model.User;
import com.campus.service.ResourceService;
import com.campus.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResourceSearchTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println(" 功能4：学习资源检索验收测试");
        System.out.println("================================");

        // =========================
        // 1. 创建整个测试环境
        // =========================

        UserDao userDao = new InMemoryUserDao();
        UserService userService = new UserService(userDao);
        ResourceDao resourceDao = new InMemoryResourceDao();
        InvertedIndex invertedIndex = new InvertedIndex();

        ResourceService resourceService = new ResourceService(resourceDao, userService,invertedIndex );

        // =========================
        // 2. 创建测试用户
        // =========================

        User teacher = userService.register(
                "teacher01",
                "123456",
                Role.TEACHER
        );

        User student = userService.register(
                "student01",
                "123456",
                Role.STUDENT
        );

        // =========================
        // 测试1：添加资料并搜索
        // =========================

        Set<String> tags1 = new HashSet<>();
        tags1.add("Java");
        tags1.add("HashMap");

        boolean addResult1 =
                resourceService.addResource(
                        teacher,
                        "Java HashMap",
                        "Java",
                        tags1,
                        10
                );

        List<LearningResource> result1 =
                resourceService.searchResources("Java");

        check(
                "测试1：添加资料后搜索 Java",
                addResult1 && result1.size() == 1
        );

        // =========================
        // 测试2：两个资料都有 Java 标签
        // =========================

        Set<String> tags2 = new HashSet<>();
        tags2.add("Java");
        tags2.add("Collection");

        boolean addResult2 =
                resourceService.addResource(
                        teacher,
                        "Java Collection",
                        "Java",
                        tags2,
                        20
                );

        List<LearningResource> result2 =
                resourceService.searchResources("java");

        check(
                "测试2：两个资料都有 Java 标签",
                addResult2 && result2.size() == 2
        );

        // =========================
        // 测试3：标题和标签重复出现 Java
        // Set 中 ID 不应重复
        // =========================

        Set<Integer> javaIds =
                invertedIndex.search("java");

        check(
                "测试3：同一资料标题和标签重复 Java，ID 不重复",
                javaIds.size() == 2
        );

        // =========================
        // 测试4：大小写规范化
        // =========================

        List<LearningResource> upperResult =
                resourceService.searchResources("JAVA");

        List<LearningResource> lowerResult =
                resourceService.searchResources("java");

        check(
                "测试4：JAVA 和 java 搜索结果一致",
                upperResult.size() == lowerResult.size()
        );

        // =========================
        // 测试5：不存在的关键词
        // =========================

        List<LearningResource> noResult =
                resourceService.searchResources(
                        "Python123"
                );

        check(
                "测试5：搜索不存在的关键词",
                noResult != null && noResult.isEmpty()
        );

        // =========================
        // 测试6：Student 可以搜索
        // =========================
        // 搜索本身不需要教师权限。
        // 当前 searchResources 没有 operator 参数，
        // 因此只验证搜索业务可以正常执行。

        List<LearningResource> studentSearch =
                resourceService.searchResources("java");

        check(
                "测试6：普通搜索不要求 TEACHER 权限",
                studentSearch.size() == 2
        );

        // =========================
        // 测试7：Student 不能新增资料
        // =========================

        Set<String> studentTags = new HashSet<>();
        studentTags.add("Database");

        boolean studentAdd =
                resourceService.addResource(
                        student,
                        "Database Basic",
                        "Database",
                        studentTags,
                        5
                );

        check(
                "测试7：Student 不能新增资料",
                !studentAdd
        );

        // =========================
        // 测试8：删除一份资料后索引同步更新
        // =========================

        int deleteId = result1.get(0).getId();

        boolean deleteResult =
                resourceService.deleteResource(
                        teacher,
                        deleteId
                );

// 直接检查倒排索引
        Set<Integer> javaIdsAfterDelete =
                invertedIndex.search("java");

        check(
                "测试8：删除资料后，该 ID 从 Java 索引中消失",
                deleteResult
                        && !javaIdsAfterDelete.contains(deleteId)
        );

        // =========================
        // 测试9：删除后无任何资料的关键词
        // =========================

        List<LearningResource> hashMapResult =
                resourceService.searchResources("hashmap");

        check(
                "测试9：删除资料后，无资源关键词被正确清理",
                hashMapResult != null
                        && hashMapResult.isEmpty()
        );

        // =========================
        // 测试10：Student 不能删除资料
        // =========================

        Set<String> testTags = new HashSet<>();
        testTags.add("Test");

        boolean addTestResource =
                resourceService.addResource(
                        teacher,
                        "Student Delete Test",
                        "Test",
                        testTags,
                        1
                );

// 找到刚刚添加的测试资料
        List<LearningResource> testResources =
                resourceService.searchResources("test");

        boolean studentDeleteResult = false;
        boolean resourceStillExists = false;

        if (!testResources.isEmpty()) {

            int testId =
                    testResources.get(0).getId();

            // Student 尝试删除
            studentDeleteResult =
                    resourceService.deleteResource(
                            student,
                            testId
                    );

            // 检查资料是否仍然存在
            LearningResource resource =
                    resourceDao.getResourceByID(testId);

            resourceStillExists =
                    resource != null;
        }

        check(
                "测试10：Student 不能删除资料",
                addTestResource
                        && !studentDeleteResult
                        && resourceStillExists
        );

        // =========================
        // 测试结果
        // =========================

        System.out.println();
        System.out.println("================================");
        System.out.println(" 测试结束");
        System.out.println("================================");
        System.out.println("通过：" + passed);
        System.out.println("失败：" + failed);

        if (failed == 0) {
            System.out.println(
                    "全部测试通过，功能4验收成功！"
            );
        } else {
            System.out.println(
                    "存在测试失败，请检查代码。"
            );
        }
    }

    /**
     * 简单的测试结果输出方法
     */
    private static void check(
            String testName,
            boolean condition) {

        if (condition) {
            System.out.println(
                    "[PASS] " + testName
            );
            passed++;
        } else {
            System.out.println(
                    "[FAIL] " + testName
            );
            failed++;
        }
    }
}