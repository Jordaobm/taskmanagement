# Sistema de Gerenciamento de Tarefas Simplificado

<div align="center">
  <img alt="Java" title="Java" src="https://github.com/Jordaobm/taskmanagement/blob/main/doc/java.png" width="300px" />
</div>

## Descrição do Projeto

Este projeto é um sistema de gerenciamento de tarefas desenvolvido como um desafio técnico. A aplicação permite cadastro
de usuários e gerenciamento completo (CRUD) de tarefas. Cada tarefa possui um título, descrição, data de criação, data
de atualização e status (pendente, concluída e cancelada).

## Funcionalidades

- **Criação de Tarefa**: Adicionar uma nova tarefa com título, descrição, data de criação e status.
- **Criação de Usuários**: Adicionar novos usuários (role ADMINISTRATOR ou USER).
- **Usuário Administrador**: Pode realizar o CRUD completo em qualquer tarefa.
- **Usuário Comum**: Pode realizar o CRUD completo apenas em tarefas de sua autoria.
- **Listagem de Tarefas**: Visualizar todas as tarefas em uma lista, ordenadas pela data de criação.
- **Edição de Tarefa**: Editar o título e a descrição de uma tarefa existente.
- **Exclusão de Tarefa**: Excluir uma tarefa.
- **Alteração de Status**: Alterar o status de uma tarefa.

## Extras

- **Swagger Completo**: Um Swagger que documenta a existência dos endpoints e seus parâmetros está incluso na aplicação.
- **Docker**: Tendo docker instalado em sua máquina, basta alterar a configuração do seu arquivo application.properties
  para apontar corretamente ao IP da sua máquina e realizar o build do container do banco de dados e da aplicação em
  seguida. Dessa forma você terá o ambiente totalmente disponível para realizar os testes.

## Regras de Negócio

1. **Validação de Dados**:
    - Título e descrição são obrigatórios.
    - O título deve ter pelo menos 5 caracteres.
2. **Ordenação de Tarefas**:
    - Tarefas são ordenadas pela data de criação, das mais antigas para as mais recentes.
3. **Limitação de Criação**:
    - Um usuário não pode criar mais de 10 tarefas pendentes ao mesmo tempo.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para facilitar a configuração e o desenvolvimento da aplicação.
- **Spring Data JPA**: Para integração com bancos de dados através do Hibernate.
- **H2 Database**: Banco de dados em memória para persistência.
- **JUnit e Mockito**: Para realização de testes.
- **Maven**: Ferramenta de construção e gerenciamento de dependências.

## Pré-requisitos

- **Java 11** ou superior
- **Maven**

## Configuração e Execução

1. **Clonar o repositório**:
    ```bash
    git clone https://github.com/Jordaobm/taskmanagement
    cd taskmanagement
    ```

2. **Construir o projeto e executar os testes**:
    ```bash
    mvn clean install
    ```

3. **Executar a aplicação**:
    ```bash
    mvn spring-boot:run
    ```

4. **Acessar a aplicação**:
   A aplicação estará disponível em `http://localhost:8080`.

## Testes

Os testes estão localizados no diretório src/test/java/com/exemplo/gerenciadortarefas. Para executar os testes, utilize
o comando:

```bash
mvn test
```

## Contribuição

1. Faça um fork do projeto
2. Crie uma nova branch (git checkout -b feature/nova-funcionalidade)
3. Commit suas mudanças (git commit -am 'Adiciona nova funcionalidade')
4. Faça o push para a branch (git push origin feature/nova-funcionalidade)
5. Crie um novo Pull Request

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo LICENSE para mais detalhes.




