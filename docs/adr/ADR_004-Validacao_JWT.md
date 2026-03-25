## **ADR 004: Validação de Identidade Stateless via JWT Claims**

**Status:** Aceito

**Contexto:** Dado o uso de uma arquitetura Modular Monolith que visa escalabilidade e
futura migração para microsserviços, precisamos que a validação de permissões seja rápida
e não onere o banco de dados em cada requisição.

**Decisão:** Utilizaremos Claims customizadas no JWT para carregar o estado de validação do
usuário.

**Consequências:**
- **Positivas:** 
	- Alta performance e baixa latência;
	- facilidade de integração com gateways de API futuros.
	- Token pode guardar roles/status do usuário, podendo ser utilizado para garantir a autorização em endpoints protegidos.
- **Negativas:** 
	- Se um usuário for banido, o token permanece válido até expirar (resolvido futuramente com uma blocklist ou tempos de expiração curtos).
	- Após a verificação do usuário, um novo token deve ser gerado e retornado com o novo status integrado. E para que o backend responda as novas requisições futuras, esse novo token deve ser enviado, para garantir que um usuário já validado não seja impedido de acessar endpoints.