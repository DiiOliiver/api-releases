## Rodando localmente
Para iniciar o projeto, siga os passos abaixo:

1. **Inicie os Containers com Docker Compose**

   Na raiz do projeto, execute o comando abaixo no terminal para levantar os containers em modo *detached*:


```bash
docker compose up -d
```

2. **Configure o Banco de Dados**
- Com o container do PostgreSQL em execução, crie um novo banco de dados chamado **bank**.
3. **Migração do Banco de Dados com Flyway**

   Ao iniciar o projeto, o Flyway criará automaticamente as tabelas necessárias e inserirá um usuário mockado para autenticação.

4. **Dados de Autenticação do Usuário Mockado**

   Use as credenciais abaixo para autenticar-se no sistema:


```json
{
  "username": "diego.feitosa.oliveira@hotmail.com",
  "password": "Admin@123"
}
```

5. **Importe o Arquivo do Insomnia**

   Se desejar testar a API, selecione o arquivo de collection do Insomnia disponível no projeto para importar as requisições.

   [Arquivo insommia](bank_insommia.json)