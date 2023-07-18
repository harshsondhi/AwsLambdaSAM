package com.sondhi.harsh.s3sns.lambda;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sondhi.harsh.s3sns.event.PatentCheckoutEvent;

public class BillManagementLambda {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void billManagementHandler(SNSEvent event){
          event.getRecords().forEach( snsRecord -> {
              try {
                  PatentCheckoutEvent patentCheckoutEvent = objectMapper.readValue(snsRecord.getSNS().getMessage(), PatentCheckoutEvent.class);
                  System.out.println(patentCheckoutEvent);
              } catch (JsonProcessingException e) {
                  e.printStackTrace();
              }
          });
    }
}
