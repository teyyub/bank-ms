https://x-team.com/blog/set-up-rabbitmq-with-docker-compose/
https://blog.christian-schou.dk/rabbitmq-deployment-with-docker-compose/

docker-compose up
http://localhost:15672.

sudo lsof -i :15672

docker run -d --name rubbitmq -p 15672:15672 rabbitmq
