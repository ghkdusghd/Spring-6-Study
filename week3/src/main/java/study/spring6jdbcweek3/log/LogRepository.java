package study.spring6jdbcweek3.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LogRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Log lg) {
        log.info("save log: {}", lg);
        String sql = "insert into log(msg, created_at) values(?, ?)";
        jdbcTemplate.update(sql, lg.getMsg(), Timestamp.valueOf(lg.getCreatedAt()));
    }

    public List<Log> findAll() {
        log.info("find all logs");
        String sql = "select * from log";
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(Log.class));
    }

    public void deleteAll() {
        log.info("delete all logs");
        String sql = "delete from log";
        jdbcTemplate.update(sql);
    }
}
