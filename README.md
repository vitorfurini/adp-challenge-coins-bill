# adp-challenge-coins-bill

Podemos rodar a aplicação de duas maneiras, uma é localmente e outra é dentro de um container docker.

Para executar o projeto localmente é necessário que esteja rodando uma instancia do mongodb, para isso, rode o comando abaixo no terminal.

`docker run -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=vitorfurini -e MONGO_INITDB_ROOT_PASSWORD=gremiaodamassa --name mongodb mongo:latest`

E para rodar o projeto, basta abrir dentro da IDE de sua preferência e rodar.

Para rodar o projeto dentro de um container do docker é necessário criar a instancia do mongo citada 
acima e depois faça um build da aplicação com o comando abaixo

`docker build -t adp-challenge-vitor-furini:1.0 . `

Após buildar a aplicação no docker, rode o comando abaixo para rodar o app linkando com o mongo

`docker run -p 8080:8080 --name adp-challenge-vitor-furini --link mongodb:mongo -d adp-challenge-vitor-furini:1.0 `


Qualquer dúvida para executar o projeto, basta me mandar um email: vitorfurini@hotmail.com
