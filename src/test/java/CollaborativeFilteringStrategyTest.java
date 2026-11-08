

import com.campus.model.BehaviorType;
import com.campus.model.UserBehavior;
import com.campus.repository.BehaviorRepository;
import com.campus.repository.InMemoryBehaviorRepository;
import com.campus.recommend.CollaborativeFilteringStrategy;

import java.time.LocalDateTime;
import java.util.Map;

public class CollaborativeFilteringStrategyTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        testSingleView();
        testViewAndFavorite();
        testRepeatedView();
        testDownload();
        testDifferentUsers();
        testMultipleResources();
        testEmptyRepository();

        System.out.println();
        System.out.println("====================");
        System.out.println("通过：" + passed);
        System.out.println("失败：" + failed);
        System.out.println("====================");
    }


    // 测试1：单次 VIEW
    private static void testSingleView() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1,
                101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试1：单次VIEW，评分为1.0",
                matrix.get(1).get(101) == 1.0
        );
    }


    // 测试2：VIEW + FAVORITE
    private static void testViewAndFavorite() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.FAVORITE,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试2：VIEW + FAVORITE，累计为4.0",
                matrix.get(1).get(101) == 4.0
        );
    }


    // 测试3：重复 VIEW
    private static void testRepeatedView() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试3：重复VIEW，累计为2.0",
                matrix.get(1).get(101) == 2.0
        );
    }


    // 测试4：DOWNLOAD
    private static void testDownload() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1, 102,
                BehaviorType.DOWNLOAD,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试4：DOWNLOAD权重为5.0",
                matrix.get(1).get(102) == 5.0
        );
    }


    // 测试5：不同用户分开
    private static void testDifferentUsers() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.FAVORITE,
                LocalDateTime.now()
        ));

        repository.save(new UserBehavior(
                2, 101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试5：不同用户的评分相互独立",
                matrix.get(1).get(101) == 3.0
                        && matrix.get(2).get(101) == 1.0
        );
    }


    // 测试6：同一用户操作多个资源
    private static void testMultipleResources() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        repository.save(new UserBehavior(
                1, 101,
                BehaviorType.VIEW,
                LocalDateTime.now()
        ));

        repository.save(new UserBehavior(
                1, 102,
                BehaviorType.DOWNLOAD,
                LocalDateTime.now()
        ));

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试6：同一用户的多个资源分别保存",
                matrix.get(1).size() == 2
                        && matrix.get(1).get(101) == 1.0
                        && matrix.get(1).get(102) == 5.0
        );
    }


    // 测试7：没有任何行为
    private static void testEmptyRepository() {

        BehaviorRepository repository =
                new InMemoryBehaviorRepository();

        CollaborativeFilteringStrategy strategy =
                new CollaborativeFilteringStrategy(repository);

        Map<Integer, Map<Integer, Double>> matrix =
                strategy.buildRatingMatrix();

        check(
                "测试7：没有行为记录时返回空matrix",
                matrix != null && matrix.isEmpty()
        );
    }


    // 统一输出测试结果
    private static void check(
            String testName,
            boolean condition) {

        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }
}