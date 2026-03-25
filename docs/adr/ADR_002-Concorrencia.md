## **ADR 002: Estratégia de Concorrência (Optimistic Locking)**

**Status:** Aceito

**Contexto:** O Ledger sofrerá alta concorrência em contas populares (ex: conta de recebimento de uma grande loja). Precisamos garantir que o saldo nunca fique inconsistente sem sacrificar o throughput global do banco de dados.

**Decisão:** Utilizaremos Optimistic Locking via controle de versão (@Version no JPA). Em caso de conflito, a aplicação capturará a exceção e aplicará uma estratégia de Retry com Exponential Backoff + Jitter.

**Consequências:**
- **Positivas:** Maior disponibilidade do banco (não há retenção de locks de linha por tempo indeterminado) e melhor performance para a maioria das transações.
- **Negativas:** Em cenários de altíssima contenção na mesma conta, a taxa de retentativas pode aumentar a latência percebida pelo usuário final.