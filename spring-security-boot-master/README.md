Assumption

1. There is no database included. Everything is in-memory.
2. Token is generated using UUID. For now we are not storing in database but later you can store in database and provide expire time.


Steps to build and deploy :

mvn clean install -DskipTests

java -jar target/spring-security-boot-0.1.0.jar

For Login 

http://localhost:9092/login

username : admin
password : admin

Output :

{
	"details": {
		"remoteAddress": "0:0:0:0:0:0:0:1",
		"sessionId": null
	},
	"authorities": [],
	"authenticated": true,
	"principal": "admin",
	"credentials": null,
	"token": "419f6a6b-cee0-4e74-a0f4-efd310c901fa",
	"name": "admin"
}

Response Headers :

content-type →application/json;charset=UTF-8
date →Wed, 24 Jan 2018 10:21:01 GMT
server →Apache-Coyote/1.1
transfer-encoding →chunked
x-api-token →dc63033a-dd72-4435-93dc-9db653157ceb
x-application-context →application:9092

For subsequent request web(browser) will work as it (due to cookie) is but if you are using HTTP Client then you have to send either X-API-TOKEN in either cookie
or header

User API

http://localhost:9092/user

Once authenticated it will work fine from browser but from HTTP client
you have to send header X-API-TOKEN and value of token which we will get once authenticated if token is not valid login page will appear.

