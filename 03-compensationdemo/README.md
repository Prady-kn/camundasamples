# Compensation and Cancel Event Demo

## About demo

The demo shows how compensation transaction can be implemented using BPM. When card charge step fails, if the sub-process which confirms the booking is still in progress then compensation handler wouldnt be executed (since  compensation is only applicable if something has already taken place and we want to revert it. But, in this case sub-process is still in progress).

If sub-process gets completed before card charge fails then compensation handler gets executed since we have to revert back the changes happened as part of sub-process execution.



## Starting workflow instance:

### Start workflow

> Positive scenario : Card charge succeeds.  

```
POST http://localhost:8080/engine-rest/process-definition/key/Compensation_Demo/start

Body:

{
  "variables": {
    "scenario" : {
        "value" : "",   
        "type": "String"
    }
  },
 "businessKey" : "somekey"
}


Response:

{
    "links": [
        {
            "method": "GET",
            "href": "http://localhost:8080/engine-rest/process-instance/3e34a194-4a5a-11ea-bc2a-e09467594f30",
            "rel": "self"
        }
    ],
    "id": "3e34a194-4a5a-11ea-bc2a-e09467594f30",
    "definitionId": "Compensation_Demo:5:e9ce256b-4a58-11ea-bc2a-e09467594f30",
    "businessKey": "somekey",
    "caseInstanceId": null,
    "ended": false,
    "suspended": false,
    "tenantId": null
}
```


> Negative scenario : Card charge fails - This would throw error in card charge step since we are passing "invcrd" (Invalid card) for scenario variable.

```
POST http://localhost:8080/engine-rest/process-definition/key/Compensation_Demo/start

Body:

{
  "variables": {
    "scenario" : {
        "value" : "invcrd",   
        "type": "String"
    }
  },
 "businessKey" : "somekey"
}
```

### Check workflow variables

```
GET http://localhost:8080/engine-rest/history/variable-instance?processInstanceIdIn=3e34a194-4a5a-11ea-bc2a-e09467594f30

```

> Note: Here 3e34a194-4a5a-11ea-bc2a-e09467594f30 is the workflow instance id obtained as part of start workflow response.