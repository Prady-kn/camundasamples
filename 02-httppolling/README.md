# Http connector polling demo

## Running demo

Create mockserver initialization json file such that it would return 404 for http://localhost:9999/products/10. Start the workflow instance & check the status of the running instance in Camunda Cockpit. You can see counter variable keep incrementing till you update mock server initialization json to return 200 for product id 10.

## Start workflow instance:

### Start workflow
```
POST http://localhost:8080/engine-rest/process-definition/key/httppollingdemo/start

Body:

{
  "variables": {
    "pid" : {
        "value" : "10",
        "type": "String"
    }
  },
 "businessKey" : "updateprod"
}


Response:

{
    "links": [
        {
            "method": "GET",
            "href": "http://localhost:8080/engine-rest/process-instance/c4c5ac40-4a41-11ea-bc2a-e09467594f30",
            "rel": "self"
        }
    ],
    "id": "c4c5ac40-4a41-11ea-bc2a-e09467594f30",
    "definitionId": "httppollingdemo:6:b2d6e9db-4a41-11ea-bc2a-e09467594f30",
    "businessKey": "updateprod",
    "caseInstanceId": null,
    "ended": false,
    "suspended": false,
    "tenantId": null
}
```

### Check workflow variables

```
GET http://localhost:8080/engine-rest/history/variable-instance?processInstanceIdIn=c4c5ac40-4a41-11ea-bc2a-e09467594f30

```

> Note: Here c4c5ac40-4a41-11ea-bc2a-e09467594f30 is the workflow instance id obtained as part of start workflow response.