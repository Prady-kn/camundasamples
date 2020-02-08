package com.example.waitformsg;

import java.util.Collections;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.camunda.bpm.engine.variable.Variables.*;

public class DemoExternalTask implements ExternalTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoExternalTask.class);

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // TODO Auto-generated method stub


        // retrieve a variable from the Workflow Engine
        String triggeredMsg = externalTask.getVariable("Triggered_Message");


        LOGGER.info("Obtained " + triggeredMsg + " from workflow engine.");


        VariableMap variables = createVariables();
        variables.putValueTyped("ExternalTask_Message", stringValue("External task read this message: " + triggeredMsg));

         // complete the external task
         externalTaskService.complete(externalTask, variables);

    }


}