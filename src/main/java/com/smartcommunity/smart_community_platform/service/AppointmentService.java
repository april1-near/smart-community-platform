package com.smartcommunity.smart_community_platform.service;

import com.smartcommunity.smart_community_platform.model.dto.AppointmentRequestDTO;
import com.smartcommunity.smart_community_platform.model.vo.AppointmentVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentService {
    @Transactional(rollbackFor = Exception.class)
    AppointmentVO createAppointment(AppointmentRequestDTO dto, Long userId);

    @Transactional(rollbackFor = Exception.class)
    AppointmentVO cancelAppointment(Long appointmentId, Long userId);

    List<AppointmentVO> listUserAppointments(Long userId);

    @Transactional(rollbackFor = Exception.class)
    void confirmCompletion(Long appointmentId, Long doctorId);
}
