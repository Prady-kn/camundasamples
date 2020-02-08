# Message Event Demo

## About demo

This sample app demonstrates message event & external task oriented scenarios 
* Initiate workflow on message
* Wait for message to resume
* Trigger external task

The workflow instance gets triggered upon receiving the "StartProcMessage" Message and waits for intermediate message "WaitMessage" to resume the flow.

The demo springboot app monitors for json files inside "pathtowatch" folder(application.yml) and based on the value of "operation" property it triggers\resumes the worflow.


## Starting workflow instance:

### Run the springboot app by setting the "pathtowatch" to monitor for new files.

___application.yml___ : (Example) 
```
pathtowatch: "G:/Temp/droplocation"
```

To trigger\resume instance, place the .json files with below contents inside "pathtowatch" folder.

> Create workflow instance json file

```
{
"businessKey":"wait_for_emp_123",
"message":"{\"messageid\":1}",
"operation":"create",
"procDefKey":"WaitingForMsgDemo"
}
```

> Resume workflow instance json file
```
{
"businessKey":"wait_for_emp_123",
"message":"{\"messageid\":2}",
"messagename":"WaitMessage",
"operation":"resume"
}

```

### Check workflow variables

```
GET http://localhost:8080/engine-rest/history/variable-instance?processInstanceIdIn=3e34a194-4a5a-11ea-bc2a-e09467594f30
```

> Note: Here 3e34a194-4a5a-11ea-bc2a-e09467594f30 is the workflow instance id obtained as part of start workflow response.