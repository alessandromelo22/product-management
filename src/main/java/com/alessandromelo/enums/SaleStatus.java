package com.alessandromelo.enums;

public enum SaleStatus {
    PENDING,       // aguardando pagamento
    PAID,          // pago à vista
    INSTALLMENT,   // parcelado em andamento
    OVERDUE,       // parcela em atraso
    CANCELLED      // cancelada
}
