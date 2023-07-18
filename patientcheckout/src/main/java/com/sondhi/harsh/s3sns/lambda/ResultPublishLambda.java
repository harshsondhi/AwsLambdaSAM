package com.sondhi.harsh.s3sns.lambda;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sondhi.harsh.s3sns.event.PatentCheckoutEvent;
import com.sondhi.harsh.s3sns.event.StudentGradeEvent;

public class ResultPublishLambda {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void resultPublishingHandler(SNSEvent event){
        event.getRecords().forEach( snsRecord -> {
            try {
                StudentGradeEvent studentGradeEvent = objectMapper.readValue(snsRecord.getSNS().getMessage(), StudentGradeEvent.class);
                System.out.println(studentGradeEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}
