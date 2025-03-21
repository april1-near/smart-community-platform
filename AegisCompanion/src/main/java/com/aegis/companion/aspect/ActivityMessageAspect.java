package com.aegis.companion.aspect;

import com.aegis.companion.model.dto.BookingApproveDTO;
import com.aegis.companion.model.dto.notification.RoomBookingNotification;
import com.aegis.companion.model.enums.BookingStatusEnum;
import com.aegis.companion.model.vo.BookingRecordVO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityMessageAspect {
    private final RabbitTemplate rabbitTemplate;

    @AfterReturning(
            pointcut = "execution(public * com.aegis.companion.service.impl.RoomBookingServiceImpl.*(..))",
            returning = "result"
    )
    public void sendActivityMessage(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        BookingStatusEnum eventType = parseEventType(methodName, joinPoint.getArgs());

        if (eventType != null &&
                result instanceof BookingRecordVO
        ) {
            String routingKey = "activity.message." + eventType.getCode().toLowerCase().replace("_", ".");

            rabbitTemplate.convertAndSend(
                    "activity.exchange",
                    routingKey,
                    RoomBookingNotification.build(eventType, (BookingRecordVO) result)
            );
        }
    }

    private BookingStatusEnum parseEventType(String methodName, Object[] args) {
        return switch (methodName){
            case "createBooking" -> BookingStatusEnum.PENDING;
            case "approveBooking" -> {
                BookingApproveDTO dto = (BookingApproveDTO) args[1];
                yield dto.getApproved() ?
                        BookingStatusEnum.APPROVED :
                        BookingStatusEnum.REJECTED;
            }
            case "cancelBooking" -> BookingStatusEnum.CANCELED;
            default -> null;
        };
    }

}
