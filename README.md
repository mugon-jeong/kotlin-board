# kotlin board

- ktlin
- kotest
- swagger
- querydsl

## run

### docker

```shell
docker run --name fc-board-mysql -e MYSQL_ROOT_PASSWORD=1234 -p 3306:3306 -d mysql:8.0.32

docker run --name fc-board-redis -p 6379:6379 -d redis:latest
```
