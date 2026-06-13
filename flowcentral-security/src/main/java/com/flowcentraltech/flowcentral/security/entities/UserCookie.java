/*
 * Copyright 2021-2026 FlowCentral Technologies Limited.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Represents user cookie entity.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@Table(name = "FC_USERCOOKIE", 
    indexes = {
        @Index({ "userId" }),
        @Index({ "peer" }),
        @Index({ "cookieValue" }) })
public class UserCookie extends BaseAuditEntity {

    @ForeignKey(User.class)
    private Long userId;

    @Column(length = 64, nullable = true)
    private String peer;
    
    @Column(length = 2048)
    private String cookieValue;

    @Column(type = ColumnType.TIMESTAMP)
    private Date expiryDate;

    @ListOnly(key = "userId", property = "accountName")
    private String userAccountName;

    @ListOnly(key = "userId", property = "email")
    private String userEmail;

    @Override
    public String getDescription() {
        return userAccountName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}