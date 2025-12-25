# Sistema de Fintech

Esse documento contém um geral sobre o sistema da aplicação.

## Objetivo

O sistema permitirá a criação e gestão de carteiras bancárias, incluíndo a realização de depósitos, transferências e consultas de extratos. Adicionalmente, o projeto devéra incluir funcionalidades de logging para auditoria de todas as transações e acesso ao sistema.

## Funcionalidades do sistema

1 - **Criar uma carteira**
 - Permitir a criação de uma carteira bancária com informações como CPF, email e nome do titular.

2- **Encerrar uma carteira**
- Permitir o fechamento de uma carteira bancária existente, desde que o saldo esteja zerado.

3- **Depositar dinheiro**
- Realizar depósitos de dinheiro em uma carteira existente. Este serviço deve atualizar o saldo da carteira correspondente e registrar os dados de histórico de depósitos.

4- **Realizar Transferência**
- Permitir a transferência de fundos de uma carteira para outra. Deve verificar a disponibilidade de saldo suficiente antes de completar a transação.

5- **Consultar extrato**
- Gerar e fornecer um extrato detalhado das transações realizadas em uma carteira, incluindo depósitos, transferências recebidas e enviadas, com data e hora.

## Entidades e Relacionamentos

- **Carteira**

  - Código da Conta (uuid)
  - CPF (unique)
  - E-mail (unique)
  - Nome do Titular
  - Saldo Atual

- **Transferência**
  - Código da Transferência (uuid)
  - Carteira de Origem (Many-to-One): Carteira de onde o dinheiro será debitado.
  - Carteira de Destino (Many-to-One): Carteira para onde o dinheiro será creditado.
  - Valor da Transferência
  - Data e Hora da Transferência

- **Depósitos**
  - Código do deposito (uuid)
  - Carteira de destino do depósito (Many-to-One)
  - Valor depositado
  - Data e hora do depósito
  - Endereço IP do usuário que fez o depósito

## Regras do projeto

1 - Logging e Auditoria:

- **Filtro de IP**
  - Implementar um filtro para capturar e logar o endereço IP de cada usuário que faz uma solicitação ao sistema. Esse IP deve ser incluído no header de resposta.
- **Interceptor de Auditoria**
  - Criar um interceptor para logar todas as requisições e respostas do sistema. Este interceptor deve registrar detalhes como método HTTP, URL, código de status da resposta e endereço ip.

2 - Validações e Regras de Negócio:
- **Saldo Suficiente**
  - Antes de realizar uma transferência, o sistema deve verificar se a conta de origem possui saldo suficiente.
- **Encerramento de Conta**
  - Só permitir o encerramento de contas que possuem saldo igual a zero.
- **Transações Simultâneas**
  - Garantir a consistência das operações de depósito e transferência, utilizando mecanismos apropriados de controle de transações (transações ACID).
