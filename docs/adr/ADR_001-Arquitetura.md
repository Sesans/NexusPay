## **ADR 001: Estratégia de Arquitetura (Modular Monolith)**

**Status:** Aceito

**Contexto:** A complexidade operacional de microsserviços (latência de rede, distributed tracing, falhas parciais) é desproporcional ao estágio atual do NexusPay. Precisamos de velocidade de entrega e consistência forte (ACID), mas mantendo a possibilidade de escala futura.

**Decisão:** Adotaremos o padrão Modular Monolith utilizando Spring Modulith. O sistema será um único deploy, mas com separação física de pacotes por contexto (Auth, Ledger, Notification). Comunicações entre módulos devem ser feitas via interfaces de domínio ou eventos internos.

**Consequências:**
- **Positivas:** Facilidade de refatoração, transações em nível de banco de dados (atomicidade garantida) e ambiente de teste simplificado.
- **Negativas:** Escala vertical única (não podemos escalar apenas o Ledger sem escalar o Auth), exigindo monitoramento rigoroso para evitar acoplamento indevido entre pacotes.