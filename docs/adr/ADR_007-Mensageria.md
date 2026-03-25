 **ADR 007:** Utilização de mensageria para processamento assíncrono

**Status:** Pendente

**Contexto:** O processamento de mensagens assíncronas, como o envio de e-mails, é realizado pelo Event Publisher do Spring. Essa abordagem funciona bem para monólitos simples, mas com a crescente demanda de processamento, e as desvantagens do publisher, como: mensagens na memória, que podem ser perdidas caso o serviço fique offline, e dificuldade ou nulidade de escalabilidade individual, pois roda no mesmo container que o core da aplicação. Tudo isso resulta em um elo fraco do sistema.

**Decisão:** Utilizar o AWS SQS como serviço de mensageria externo. Este serviço ficará responsável por guardar mensagens  na fila para para serem consumidas por seus determinados listeners, como o módulo de notificações.

**Consequências:**
- **Positivas:** 
	- Compatibilidade total com o provedor.
	- Mensagens ficam persistidas na fila.
	- Facilidade de escalabilidade por ser um serviço externo.
	- Diminuição do custo de processamento no módulo principal, retirando a carga de processar eventos e envio de e-mails junto de transações do Ledger Core.
- **Negativas:** 
	- Custo maior do que do uso de eventos internos do Spring.
	- Configuração mais complexa, exigindo gerenciamento preciso.
	- Mensagens podem ser processadas mais de uma vez, necessitando de idempotência no consumer.
	- Vistoria de perto para evitar que os custos escalem de forma não esperada.