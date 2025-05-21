package study.spring6jdbcweek3.transaction;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxInternalCallTest {

    @Autowired
    private TxInternalCallService txInternalCallService;

    @DisplayName("프록시 확인")
    @Test
    void printProxy() {
        log.info("callService class={}", txInternalCallService.getClass());
    }

    @DisplayName("externalMethod에 의한 internalMethod 호출 테스트")
    @Test
    void external() throws Exception {
        txInternalCallService.externalMethod();
    }

    @TestConfiguration
    static class TxInternalCallTestConfig {
        @Bean
        public TxInternalCallService txInternalCallService() {
            return new TxInternalCallService(internalCall());
        }

        @Bean
        public InternalCall internalCall() {
            return new InternalCall();
        }
    }

    static class TxInternalCallService {
        private final InternalCall internalCall;

        public TxInternalCallService(InternalCall internalCall) {
            this.internalCall = internalCall;
        }

        public void externalMethod() {
            log.info("call externalMethod()");
            printTx();
            internalCall.internalMethod();
        }

        private void printTx() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive: {}", txActive);
        }
    }

    static class InternalCall {

        @Transactional
        public void internalMethod() {
            log.info("call internalMethod()");
            printTx();
        }

        private void printTx() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive: {}", txActive);
        }
    }
}
