<h1 align="center" style="font-family: Arial, sans-serif; color: #FFFF00;">
FIAP Pay
</h1>

## Sumário

1. [Introdução](#introdução)
2. [Escolha da Arquitetura](#escolha-da-arquitetura)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Requisitos](#requisitos)
5. [Instalação](#instalação)
    - [Clonando o Repositório](#clonando-o-repositório)
    - [Construindo o Projeto](#construindo-o-projeto)
    - [Executando o Projeto via Maven](#executando-o-projeto-via-maven)
    - [Executando o Projeto via Docker](#executando-o-projeto-via-docker)
6. [Considerações Finais](#considerações-finais)

---

## Introdução

**FIAP Pay** é uma solução desenvolvida para gerenciar pagamentos de forma eficiente e segura, permitindo o registro e a
consulta de transações realizadas por clientes. Este projeto é construído utilizando **Spring Boot**, integrado com
várias tecnologias modernas para garantir uma API robusta, escalável e segura.

## Escolha da Arquitetura

O projeto **FIAP Pay** foi desenvolvido com base em uma arquitetura em camadas, que separa claramente as
responsabilidades em diferentes componentes do sistema. Essa abordagem foi escolhida para garantir uma aplicação
modular, de fácil manutenção e escalável. A arquitetura em camadas permite a reutilização de código e facilita a
implementação de novas funcionalidades, minimizando o impacto em outras partes do sistema.

### Camadas Principais:

1. **Camada de Apresentação (Controller)**:
    - Responsável por lidar com as requisições HTTP e enviar as respostas adequadas ao cliente. Utiliza o Spring MVC
      para mapear as requisições para os métodos corretos nos controladores.

2. **Camada de Serviço (Service)**:
    - Contém a lógica de negócio da aplicação. As regras de negócio e os processos de pagamento são tratados nesta
      camada, garantindo que a lógica seja centralizada e reutilizável.

3. **Camada de Persistência (Repository)**:
    - Responsável pela comunicação com o banco de dados. Utiliza Spring Data JPA para realizar operações CRUD e
      consultas personalizadas de maneira simples e eficaz.

4. **Camada de Domínio (Model/Entity)**:
    - Define as entidades do domínio e as suas respectivas regras. Nesta camada, as entidades representam os principais
      objetos de negócio, como Clientes, Pagamentos e Cartões.

### Justificativa da Escolha:

- **Modularidade**: A arquitetura em camadas oferece uma separação clara das responsabilidades, permitindo que as
  alterações em uma camada tenham pouco ou nenhum impacto nas outras camadas.
- **Escalabilidade**: A estrutura modular facilita a escalabilidade horizontal, onde diferentes camadas podem ser
  escaladas independentemente, conforme necessário.
- **Manutenção**: A separação de preocupações facilita a manutenção do código, permitindo que desenvolvedores trabalhem
  em diferentes partes do sistema simultaneamente sem causar conflitos.
- **Reutilização**: Componentes como serviços e repositórios podem ser reutilizados em diferentes partes da aplicação,
  promovendo a DRY (Don't Repeat Yourself).

### Tecnologias Utilizadas

    •	Java 21: A mais recente versão do Java, oferecendo melhorias de desempenho e novos recursos.
	•	Spring Boot 3.3.2: Framework que simplifica a criação de aplicações Spring, permitindo rápido desenvolvimento e fácil configuração.
	•	Spring Security: Biblioteca de segurança que fornece autenticação e controle de acesso para aplicações Java.
	•	Spring Data JPA: Abstração de persistência que simplifica o acesso a bancos de dados usando a API JPA.
	•	Springdoc OpenAPI: Ferramenta para gerar documentação automática da API seguindo as especificações OpenAPI/Swagger.
	•	MapStruct: Framework de mapeamento de objetos que simplifica a conversão entre DTOs e entidades.
	•	Maven: Ferramenta de automação de build e gerenciamento de dependências.
	•	Docker (opcional): Containerização da aplicação para garantir consistência e facilidade de implantação em diferentes ambientes.

### Requisitos

	•	Java 21
	•	Maven 3.6+
	•	Docker (opcional, para execução em containers)

### Instalação

Para executar o projeto, basta clonar o repositório e executar o comando abaixo:

#### Clonando o Repositório

```shell
    git clone https://github.com/rafaelteixeirarnnt/fiap-pay.git
    cd fiap-pay
```

<br />

#### Construindo o Projeto

```shell
    mvn clean install
```

<br />

#### Executando o Projeto via Maven

```shell
    mvn spring-boot:run
```

<br />

#### Executando o projeto via Docker

```shell
    sudo docker-compose -f prod-compose.yaml up
```

**Observação: Se você já executou o projeto via Maven anteriormente, é recomendável remover qualquer referência ou
configuração pré-existente relacionada ao Docker para evitar conflitos.**

#### Considerações Finais

O FIAP Pay foi projetado com foco em segurança, escalabilidade e facilidade de manutenção. A integração com Docker
facilita a implantação em diferentes ambientes, enquanto o uso de tecnologias como Spring Security e Spring Data JPA
garante a robustez e a integridade da aplicação.

Para mais detalhes sobre a API e como utilizá-la, consulte a documentação gerada automaticamente pelo Springdoc OpenAPI
disponível no endpoint /swagger-ui.html(Ex:http://localhost:8080/swagger-ui/index.html) após iniciar a aplicação.

Essa versão da documentação oferece uma visão mais clara e organizada do projeto, com uma introdução mais detalhada
sobre cada tecnologia utilizada e instruções de instalação e execução bem definidas.