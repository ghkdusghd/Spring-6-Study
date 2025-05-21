package study.spring6jdbcweek3.post;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DataSourceConnectionPoolTest {
    private static final String URL = "jdbc:h2:tcp://localhost/~/spring6";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "1234";

    @DisplayName("커넥션 풀 획득 테스트")
    @Test
    void dataSourceConnectionPool() throws InterruptedException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); // 커넥션 풀 최대 사이즈 10개로 설정
        dataSource.setPoolName("MyPool");
        Thread.sleep(1000); //커넥션 풀에서 커넥션 생성 시간 대기

        // 멀티스레드 구성 (20개 쓰레드)
        int taskCount = 20;
        CountDownLatch latch = new CountDownLatch(taskCount);
        ExecutorService executorService = Executors.newFixedThreadPool(taskCount);

        // 20개 쓰레드를 실행하여 커넥션 빌리고, 로그 출력
        for (int i = 0; i < taskCount; i++) {
            executorService.submit(() -> {
                // try-with-resources 구문을 사용하여 커넥션 풀에서 커넥션을 빌림 (try 블록 종료 시 자동으로 반납)
                try (Connection connection = dataSource.getConnection()) {
                    // 커넥션 정보 확인
                    System.out.println("Thread: " + Thread.currentThread().getName() + ", Connection: " + connection);
                    Thread.sleep(500);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 쓰레드 종료 대기
        latch.await();
        executorService.shutdown();

        // DataSource 종료
        dataSource.close();
    }
}
