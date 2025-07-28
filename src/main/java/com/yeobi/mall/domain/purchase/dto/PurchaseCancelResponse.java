package com.yeobi.mall.domain.purchase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yeobi.mall.common.enums.PurchaseStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON에서 제외
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseCancelResponse {

  private Long purchaseId;

  private PurchaseStatus status;

  private LocalDateTime cancelledAt;

  private List<PurchaseProductResponse> cancelledProducts;

  private String message;
}
