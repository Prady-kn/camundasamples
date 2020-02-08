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
```
