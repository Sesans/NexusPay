**ADR 006:** Deploy da aplicação em um provedor de nuvem

**Status:** Pendente

**Contexto:** Para que a aplicação seja corretamente disponibilizada para a internet, precisamos de um provedor compatível e confiável, seja acessível, respeitando o custo-benefício que demandamos, e consiga hospedar e receber corretamente a aplicação e as suas requisições. 

**Decisão:** Utilizaremos o serviço de Cloud da AWS. O provedor AWS é justificável para o nosso caso, pois além de ser um dos maiores serviços de Cloud com ampla documentação disponível e uma gama de serviços que podem ser utilizados em nosso benefício, como o RDS, SQS, Lambda, entre outros. Para garantir que não haja divergências na aplicação rodando em ambiente de teste local e de produção na nuvem, serão utilizados containeres **Docker** para garantir a consistência, com **Docker Compose** orquestrando estes containeres. Como forma de mitigação de custos no ambiente de desenvolvimento local, será usado o emulador de serviços Cloud **LocalStack**. Esta abordagem reduz custo, permite visualização mais rápida de erros ou correções necessárias e criação de pipelines CI/CD mais práticas.

**Consequências:**
- **Positivas:**
	- Custos no modelo pague o quanto usar.
	- Infraestrutura consolidada no mercado e com documentação abrangente.
	- Pipelines CI/CD garantem a entrega de software funcionando da forma esperada.
	- Sistema disponível para que qualquer pessoa com acesso à internet possa acessá-lo.
	- Facilidade de escalabilidade e elasticidade.
- **Negativas:**
	- Configuração complexa que exige análise definição apurada para evitar retrabalho e custos desnecessários (Já reduzido pela utilização de LocalStack).
	- A má configuração como de permissões de acesso podem gerar riscos de segurança.
	- Instâncias devem ser monitoradas de perto, pois o modelo Pay-Per-Use pode escalar de forma inesperada.
	- Migração dificultada e custosa devido à incompatibilidade de ferramentos e serviços com outros provedores.