/*
 * Copyright 2021-2026 FlowCentral Technologies Limited.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for user cookie.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class UserCookieQuery extends BaseAuditEntityQuery<UserCookie> {

    public UserCookieQuery() {
        super(UserCookie.class);
    }

    public UserCookieQuery userId(Long userId) {
        return (UserCookieQuery) addEquals("userId", userId);
    }

    public UserCookieQuery peer(String peer) {
        return (UserCookieQuery) addEquals("peer", peer);
    }

    public UserCookieQuery isMaster() {
        return (UserCookieQuery) addIsNull("peer");
    }

    public UserCookieQuery cookieValue(String cookieValue) {
        return (UserCookieQuery) addEquals("cookieValue", cookieValue);
    }

    public UserCookieQuery isExpired(Date now) {
        return (UserCookieQuery) addLessThanEqual("expiryDate", now);
    }

}