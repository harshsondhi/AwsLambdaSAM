package com.sondhi.harsh.s3sns.lambda;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import com.sondhi.harsh.s3sns.event.PatentCheckoutEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.amazonaws.services.sns.AmazonSNS;
//import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.sondhi.harsh.s3sns.event.StudentGradeEvent;

public class StudentReceiveGradesLambda {
    private static final String STUDENT_RESULT_TOPIC = System.getenv("STUDENT_RESULT_TOPIC");
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void studentGradeLambdaHandler(S3Event event,Context context){

        event.getRecords().forEach( record -> {
            S3ObjectInputStream s3ObjectInputStream = s3.getObject(record.getS3().getBucket().getName(),
                    record.getS3().getObject().getKey()).getObjectContent();

            try {
                List<StudentGradeEvent> studentGradeEvents = Arrays
                        .asList(objectMapper.readValue(s3ObjectInputStream, StudentGradeEvent[].class));
                System.out.println(studentGradeEvents);
                s3ObjectInputStream.close();

                publishMessageToSNS(studentGradeEvents);

            }catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                //logger.error("Exception is:", e);
                throw new RuntimeException("Error while processing S3 event",e);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


    private void publishMessageToSNS(List<StudentGradeEvent> studentGradeEvents) {
        studentGradeEvents.forEach(gradeEvent -> {
            try {
                sns.publish(STUDENT_RESULT_TOPIC, objectMapper.writeValueAsString(gradeCalculation(gradeEvent)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
    private StudentGradeEvent gradeCalculation(StudentGradeEvent input){
        String grade = "C";
        if(input.testScore >= 80) grade = "A";
        if(input.testScore >= 70 && input.testScore < 80) grade = "B";
        input.grade = grade;
        return input;
    }

}
