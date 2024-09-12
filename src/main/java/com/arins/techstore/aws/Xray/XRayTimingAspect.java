package com.arins.techstore.aws.Xray;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Segment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class XRayTimingAspect {
    private static final AWSXRayRecorder xrayRecorder = AWSXRay.getGlobalRecorder();

    @Around("@annotation(XRayTimed)")
    public Object timeXRaySegment(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        XRayTimed xRayTimed = signature.getMethod().getAnnotation(XRayTimed.class);
        String segmentName = xRayTimed.segmentName().isEmpty() ? signature.getMethod().getName() : xRayTimed.segmentName();
        Segment segment = xrayRecorder.beginSegment(segmentName);

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            segment.addException(e);
            throw e;
        } finally {
            xrayRecorder.endSegment();
        }
    }
}