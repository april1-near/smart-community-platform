package com.smartcommunity.smart_community_platform.service;

import com.smartcommunity.smart_community_platform.model.entity.ParkingReservation;
import com.smartcommunity.smart_community_platform.model.entity.User;
import com.smartcommunity.smart_community_platform.model.enums.Role;

// RolePermissionService.java
public interface RolePermissionService {
    boolean hasRole(User user, String roleCode);

    void checkViewPermission(User currentUser, Long targetUserId);

    void checkAdminPermission(User currentUser);

    void checkUpdatePermission(User currentUser, Long targetUserId);

    void checkAssignPermission(User currentUser, Role targetRole);

    void checkDeletePermission(Long targetUserId);


    // RolePermissionServiceImpl.java

    void checkAssignPermission(User operator, User worker);

    void checkStatusChangePermission(User operator, String targetStatus);

    void checkReservationPermission(User user);

    void checkReservationViewPermission(User currentUser, Long targetUserId);

    void checkParkingManagePermission(User operator);

    void checkReservationOperationPermission(User currentUser, ParkingReservation reservation);

    void checkLeavePermission(User currentUser, ParkingReservation reservation);


    void checkCancelBookingPermission(User currentUser, Long bookingId);
}
