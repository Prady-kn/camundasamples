# Http connector Demo


## Starting Mockserver

Create __start.bat__:
```
cd /D "%~dp0"
set MOCKSERVER_WATCH_INITIALIZATION_JSON=true 
set MOCKSERVER_INITIALIZATION_JSON_PATH=./initializerJson.json
java -Dmockserver.watchInitializationJson=true -jar mockserver-netty-5.9.0-jar-with-dependencies.jar -serverPort 9999
```

__initializerJson.json__ content:
```
[
    {
      "httpRequest": {
        "path": "/products/1",
        "method":"GET"
      },
      "httpResponse": {
       "body": "{\"id\":\"1\",\"name\":\"TV\",\"cost\":55232}",
               "headers":{
               	"content-Type":["application/json"]
          }
      }
  },
  {
      "httpRequest": {
        "path": "/products",
        "method":"POST"
      },
      "httpResponse": {
        "statusCode": 204,
        "headers":{
        	"content-Type":["application/json"]
        }
      }
  }
]

```

## Start workflow instance:


### Start workflow
```
POST http://localhost:8080/engine-rest/process-definition/key/Process_1bb4f0u/start

Body:

{
  "variables": {
    "pid" : {
        "value" : "1",
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
            "href": "http://localhost:8080/engine-rest/process-instance/d9bda951-4a3c-11ea-bc2a-e09467594f30",
            "rel": "self"
        }
    ],
    "id": "d9bda951-4a3c-11ea-bc2a-e09467594f30",
    "definitionId": "Process_1bb4f0u:13:d760a680-4a3c-11ea-bc2a-e09467594f30",
    "businessKey": "updateprod",
    "caseInstanceId": null,
    "ended": true,
    "suspended": false,
    "tenantId": null
}
```

### Check workflow variables

```
GET http://localhost:8080/engine-rest/history/variable-instance?processInstanceIdIn=d9bda951-4a3c-11ea-bc2a-e09467594f30

```

> Note: Here d9bda951-4a3c-11ea-bc2a-e09467594f30 is the workflow instance id obtained as part of start workflow response.