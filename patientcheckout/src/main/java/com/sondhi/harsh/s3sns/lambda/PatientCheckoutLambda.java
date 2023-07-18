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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientCheckoutLambda {

    private static final String PATIENT_CHECKOUT_TOPIC = System.getenv("PATIENT_CHECKOUT_TOPIC");
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //s3 notification
    public void checkoutLambdaHandler(S3Event event,Context context){

       // LambdaLogger lambdaLogger = context.getLogger();
       Logger logger = LoggerFactory.getLogger(PatientCheckoutLambda.class);

        event.getRecords().forEach( record -> {
                      S3ObjectInputStream s3ObjectInputStream = s3.getObject(record.getS3().getBucket().getName(),
                              record.getS3().getObject().getKey()).getObjectContent();

                  try {
                      logger.info("Read data from S3");
                      List<PatentCheckoutEvent> patientCheckoutEvents = Arrays
                              .asList(objectMapper.readValue(s3ObjectInputStream, PatentCheckoutEvent[].class));
                      logger.info(patientCheckoutEvents.toString());
                      s3ObjectInputStream.close();
                      logger.info("Message being published to sns");
                      publishMessageToSNS(patientCheckoutEvents);

                  }catch (JsonParseException e) {
                      e.printStackTrace();
                  } catch (JsonMappingException e) {
//                      StringWriter stringWriter = new StringWriter();
//                      e.printStackTrace(new PrintWriter(stringWriter));
//                      lambdaLogger.log(stringWriter.toString());
                      logger.error("Exception is :", e);
                      throw new RuntimeException("Error while processing S3 event:= ", e);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }

                });

        }


    private void publishMessageToSNS(List<PatentCheckoutEvent> patientCheckoutEvents) {
        patientCheckoutEvents.forEach(checkoutEvent -> {
            try {
                sns.publish(PATIENT_CHECKOUT_TOPIC, objectMapper.writeValueAsString(checkoutEvent));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    }


