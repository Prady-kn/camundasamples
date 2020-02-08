package com.example.waitformsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.extension.rest.variables.PrettyPrinter;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.extension.rest.impl.RemoteRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.camunda.bpm.engine.variable.Variables.*;

@Component
public class CamundaClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaClient.class);
    
    private RuntimeService  runtimeService;

    public CamundaClient(@Qualifier("remote") RuntimeService  runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void start(String processDefKey,String businessKey,String message) {
        VariableMap variables = createVariables();
        variables.putValueTyped("created_Message", stringValue(message));
      ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefKey, businessKey, variables);
      LOGGER.trace("CLIENT-101: Started instance {} - {}", instance.getId(), instance.getBusinessKey());

        //this.runtimeService.activateProcessInstanceByProcessDefinitionKey("WaitingForMsgDemo");
    }

    public void correlate(String businessKey,String message,String messageName) {
        VariableMap variables = createVariables();
        variables.putValueTyped("Triggered_Message", stringValue(message));
        
       // processVariables.put("messagedata", message);
        List<MessageCorrelationResultWithVariables> result =  this.runtimeService         
        .createMessageCorrelation(messageName)        
        .processInstanceBusinessKey(businessKey)
        .setVariables(variables)
         .correlateAllWithResultAndVariables(true);

         result.stream().forEach(element -> LOGGER.info("CLIENT-301: {}", PrettyPrinter.toPrettyString(element)));

    }
}