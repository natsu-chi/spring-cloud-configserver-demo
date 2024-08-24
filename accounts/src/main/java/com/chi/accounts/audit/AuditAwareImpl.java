package com.chi.accounts.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;


// 自動填充審計欄位:
// Entity 使用 @CreatedBy 或 @LastModifiedBy 等審計註釋時，
// Spring Data JPA 會自動呼叫這個類來獲取審計員資訊並填充相應的欄位

// 這邊對應到 BaseEntity

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNT_MS");
    }
    
}
