package study.spring6jdbcweek3.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Getter
public class Log {
    private Long logId;
    private String msg;
    private LocalDateTime createdAt;

    private Log(String msg, LocalDateTime createdAt) {
        this.msg = msg;
        this.createdAt = createdAt;
    }

    private Log(Long logId, String msg, LocalDateTime createdAt) {
        this.logId = logId;
        this.msg = msg;
        this.createdAt = createdAt;
    }

    public static Log create(String msg) {
        return new Log(msg, LocalDateTime.now());
    }
}
