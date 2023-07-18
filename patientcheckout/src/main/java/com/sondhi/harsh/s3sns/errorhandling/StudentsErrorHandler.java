package com.sondhi.harsh.s3sns.errorhandling;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentsErrorHandler {
    public void studentDeadletterSNSHandler(SNSEvent event){
        Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
        event.getRecords().forEach(
                record -> logger.info("Dead letter QUEUE Event:= " + record.toString())
        );

    }
}
