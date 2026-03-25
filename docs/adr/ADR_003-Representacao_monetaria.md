## **ADR 003: Representação de Valores Monetários**

**Status:** Aceito

**Contexto:** O uso de tipos de ponto flutuante (float, double) em sistemas financeiros causa
erros de arredondamento cumulativos que podem gerar prejuízos reais.

**Decisão:** Todos os valores monetários serão tratados como Inteiros (Long) na menor
unidade da moeda (centavos). A fórmula para saldo será:

	Balance=∑Credits−∑Debits

No Java, utilizaremos a classe BigDecimal para cálculos complexos e Long para persistência
e transporte.

**Consequências:**
- **Positivas:**
- **Negativas:**