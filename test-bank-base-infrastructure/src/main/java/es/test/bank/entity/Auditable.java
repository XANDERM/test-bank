package es.test.bank.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode
public abstract class Auditable implements SoftDeletable {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected OffsetDateTime lastModifiedDate;

    @Column(name = "deleted_by")
    protected String lastDeletedBy;
    
    @Column(name = "deleted")
    protected boolean deleted = Boolean.FALSE;
    
    @Transient
	public boolean isDeleted() {
		return this.deleted;
	}
    
}
