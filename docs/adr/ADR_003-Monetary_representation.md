## **ADR 003: Representation of Monetary Values**

**Status:** Accepted

**Context:** The use of floating-point types (float, double) in financial systems causes
cumulative rounding errors that can result in actual financial losses.

**Decision:** All monetary values will be treated as Integers (Long) in the smallest
currency unit (cents). The formula for the balance will be:

	Balance=∑Credits−∑Debits

In Java, we will use the BigDecimal class for complex calculations and the Long class for persistence and transport.