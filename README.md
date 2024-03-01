# Challenge III - MS-User - PB Springboot Dez 2023
> Status: Entregue
-------------------------------------------------------------------------------------------------------


O microsserviço User consiste em realizar as operações de Create, Update, UpdatePassword, buscar um usuário pelo Id e Login, onde no qual ele utiliza o microsserviço Address para consultar o endereço e se necessário utiliza a API do ViaCep, e também utiliza o micrroserviço Notification, para notificar se acontece algum evento como Login, Create, Update e UpdatePassword.

### Colaborador

<table>
  <tr>
    <td>E-mail</td>
    <td>GitHub</td>
  </tr>
   <tr>
    <td>leandro.paupitz.pb@compasso.com.br</td>
    <td>lepaupitz</td>
 </table>

-------------------------------------------------------------------------------------------------------

### Tecnologias Utilizadas
<table>
  <tr>
    <td>Java</td>
    <td>Spring</td>
    <td>MySql</td>
  </tr>
  <tr>
    <td>17</td>
    <td>3.2</td>
    <td>8.0</td>
  </tr>
</table>

-------------------------------------------------------------------------------------------------------

### Setup
1. Clone o repositório
```
git clone https://github.com/lepaupitz/Challenge3-MS-User.git
```
2. Configure o banco de dados no arquivo `application.yml`

3. Execute a aplicação
```
mvn spring-boot:run
```
A aplicação deverá estar em execução e acessível em http://localhost:8080/

-------------------------------------------------------------------------------------------------------
### Aplication

1. O microsserviço User contém alguns EndPoints no quais são os seguintes EndPoints com seus respectivos payloads:

`POST /v1/users`

Cria um novo usuário, onde a senha que é inserida vai ser criptografada e salva no banco de dados criptograda.

Payload:
```
  "firstName": "teste",
  "lastName": "Teste",
  "cpf": "33992884767",
  "birthdate": "01/01/1990",
  "email": "teste@gmail.com",
  "cep": "01311000",
  "password": "teste1",
  "active": false
```

`GET /v1/users/{id}`

Exibe um usuário pelo ID dele que está no banco de dados, é necessário a utilização do token gerado ao fazer a requisição de Login

Resposta:
```
  "firstName": "teste",
  "lastName": "Teste",
  "cpf": "33992884767",
  "birthdate": "01/01/1990",
  "email": "teste@gmail.com",
  "cep": "01311000",
  "password": "$2a$10$BqPuYvIxLfyuaWYmI1MkreED3/jIF66LbYSdT.w2RVvx2YOs4SKjS",
  "active": false
```
`PUT /v1/users/{id}`

Atualiza um usuário pelo seu id, é necessário a utilização do token gerado ao fazer a requisição de Login

Payload:
```
  "firstName": "teste",
  "lastName": "Teste",
  "cpf": "33992884767",
  "birthdate": "01/01/1990",
  "email": "teste@gmail.com",
  "cep": "01311000",
  "active": false
```

`PUT /v1/users/{id}/password`

Atualiza a senha do usuário pelo seu id, é necessário a utilização do token gerado ao fazer a requisição de Login

Payload:
```
  "senha": "teste",
```

`POST /v1/login`

Faz a requisição de Login

Payload:
```
  "email": "teste@gmail.com"
  "senha": "teste",
```

-------------------------------------------------------------------------------------------------------
### Dificuldades

1. A minha maior dificuldade foi fazer o link do User com o microsserviço de Notification, no qual até eu entender o que precisava demorei um pouco.

2. Também tive dificuldade em utilizar o JWT, porém ao tirar dúvidas com os colegas eles me mostraram onde que eu estava errando, e também tive alguns problemas com autorizar que o usuário pudesse fazer a requisição de Create e Login sem o token.

3. Os testes eu tive algumas dificuldades e também não consegui realizar todos os testes, necessários para cobrir os 70% dos requisitos, alguns testes estão dando erro ainda.
