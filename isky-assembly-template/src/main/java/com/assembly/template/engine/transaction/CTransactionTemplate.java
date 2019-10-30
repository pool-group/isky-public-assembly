package com.assembly.template.engine.transaction;

import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务链模板
 *
 * @author ken.ny
 * @version Id: TransactionTemplate.java, v 0.1 2018年11月09日 下午10:45 ken.ny Exp $
 */
@Component
@Slf4j
public class CTransactionTemplate {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 事务链执行
     *
     * @param callback
     */
    public void doTransaction(CTransactionCallback callback) {

        //MDC.put("txID", "txID-" + String.valueOf(generator.next()));

        transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                try {

                    callback.execute();

                } catch (BizRuntimeException e) {

                    LogUtil.error(e,"biz exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                } catch (SysRuntimeException e) {

                    LogUtil.error(e,"system exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                } catch (Exception e) {

                    LogUtil.error(e,"unknown exception then transaction is rollback !");
                    status.setRollbackOnly();
                    throw e;
                }
                return status;
            }
        });
        //MDC.remove("txID");
    }
}
