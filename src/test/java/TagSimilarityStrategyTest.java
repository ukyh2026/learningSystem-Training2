

import com.campus.model.LearningResource;
import com.campus.model.Role;
import com.campus.model.Student;
import com.campus.model.User;
import com.campus.recommend.RecommendStrategy;
import com.campus.recommend.TagSimilarityStrategy;
import com.campus.service.RecommendationService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagSimilarityStrategyTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        // =========================
        // 1. 准备测试数据
        // =========================

        User student = new Student(
                1,
                "student01",
                "123456"
        );

        student.addTag("Java");
        student.addTag("算法");

        LearningResource resource1 =
                new LearningResource(
                        1,
                        "Java算法",
                        "编程",
                        new HashSet<>(
                                Set.of("Java", "算法")),
                        0
                );

        LearningResource resource2 =
                new LearningResource(
                        2,
                        "Java数据库",
                        "编程",
                        new HashSet<>(
                                Set.of("Java", "数据库")),
                        0
                );

        LearningResource resource3 =
                new LearningResource(
                        3,
                        "Python基础",
                        "编程",
                        new HashSet<>(
                                Set.of("Python")),
                        0
                );

        List<LearningResource> allResources =
                new ArrayList<>();

        allResources.add(resource1);
        allResources.add(resource2);
        allResources.add(resource3);


        // =========================
        // 2. 创建推荐策略
        // =========================

        RecommendationService recommendationService =
                new RecommendationService();

        RecommendStrategy strategy =
                new TagSimilarityStrategy(
                        recommendationService);


        // =========================
        // 3. 执行标签推荐打分
        // =========================

        Map<Integer, Double> scores =
                strategy.scoreAll(
                        student,
                        allResources);


        // =========================
        // 4. 验收测试
        // =========================

        check(
                "测试1：资源1标签完全匹配，得分为1.0",
                approximatelyEqual(
                        scores.get(1), 1.0)
        );

        check(
                "测试2：资源2部分匹配，得分约为0.333",
                approximatelyEqual(
                        scores.get(2), 1.0 / 3.0)
        );

        check(
                "测试3：资源3无标签交集，得分为0.0且仍保留",
                scores.containsKey(3)
                        && approximatelyEqual(
                        scores.get(3), 0.0)
        );

        check(
                "测试4：3份资源全部完成打分",
                scores.size() == 3
        );

        Map<Integer, Double> nullUserResult =
                strategy.scoreAll(
                        null,
                        allResources);

        check(
                "测试5：user为null时返回空Map",
                nullUserResult != null
                        && nullUserResult.isEmpty()
        );

        Map<Integer, Double> emptyResourceResult =
                strategy.scoreAll(
                        student,
                        new ArrayList<>());

        check(
                "测试6：allResources为空时返回空Map",
                emptyResourceResult != null
                        && emptyResourceResult.isEmpty()
        );

        check(
                "测试7：可以通过RecommendStrategy引用调用scoreAll",
                strategy instanceof RecommendStrategy
                        && scores.size() == 3
        );


        // =========================
        // 5. 输出结果
        // =========================

        System.out.println();
        System.out.println("====================");
        System.out.println("通过：" + passed);
        System.out.println("失败：" + failed);
        System.out.println("====================");
    }


    private static void check(
            String testName,
            boolean condition) {

        if (condition) {
            System.out.println(
                    "[PASS] " + testName);
            passed++;
        } else {
            System.out.println(
                    "[FAIL] " + testName);
            failed++;
        }
    }


    private static boolean approximatelyEqual(
            Double actual,
            double expected) {

        if (actual == null) {
            return false;
        }

        return Math.abs(actual - expected) < 0.0001;
    }
}