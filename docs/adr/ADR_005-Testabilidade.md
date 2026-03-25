## **ADR 005: Testabilidade do sistema no ambiente de desenvolvimento**

**Status:** Pendente

**Contexto:** A aplicação utiliza os conceitos de Double-entry Bookkeeping e Cryptographic Chaining para garantir integridade dos dados financeiros, auditabilidade completa e impossibilidade de criação/destruição de valor sem rastreabilidade.
Entretanto, em ambientes de desenvolvimento e teste, não há integração com sistemas externos (ex: instituições financeiras, gateways de pagamento), o que impede a entrada legítima de recursos no sistema. Essa limitação dificulta:

- Testar fluxos financeiros ponta a ponta.
- Validar a consistência do ledger.
- Demonstrar o funcionamento da aplicação para terceiros (ex: recrutadores).

Portanto, é necessário um mecanismo controlado para introdução de saldo fictício, sem comprometer os princípios arquiteturais da aplicação.

**Decisão:** Será implementado um mecanismo de provisionamento de saldo em ambiente de desenvolvimento composto por duas estratégias:

**Data Seeder (Bloco Gênese)**
Ao iniciar a aplicação no profile de desenvolvimento uma conta Treasury será criada junto de uma transação inicial de minting (Bloco Gênese):
- Crédito: conta Treasury.
- Débito: conta abstrata "Capital Social".

Usuários e contas de teste serão criados para:
- ADMIN
- Alice
- Bob

A Treasury realizará transferências iniciais para contas de teste:

- R$ 5.000,00 para Alice
    
- R$ 5.000,00 para Bob
    

Todas as operações seguem o fluxo padrão da aplicação, garantindo consistência do ledger. Credencias das contas criadas (ADMIN, Alice e Bob) estarão disponíveis para acesso e documentadas no README da aplicação. Um usuário poderá então utilizá-las para movimentar saldo para sua própria conta e visualizar como os dados das transações são persistidos.

**Consequências:**
- **Positivas:** 
	-  Permite testes completos de fluxos financeiros
	- Preserva a integridade do modelo de Double-entry
	- Mantém auditabilidade total (todas as transações são rastreáveis)
	- Facilita demonstrações técnicas (ex: recrutadores)
	- Evita a necessidade de bypass direto no banco de dados  
	- Reutiliza o fluxo real da aplicação (alta fidelidade de teste)
- **Negativas:**
	- Dependência do profile de desenvolvimento para execução correta do seeder.
	- Risco de erro humano ao habilitar configurações indevidas em outros ambientes.
	- O conceito de _minting_ (Bloco Gênese) não existe no domínio real da aplicação, sendo introduzido apenas para fins de teste.
	- O seeder pode introduzir estado compartilhado, dificultando a execução de testes isolados e determinísticos.