package choreographywithdeadline.domain;

import choreographywithdeadline.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class StockDecreaseFailed extends AbstractEvent {

    private Long id;
    private String productName;
    private String productImage;
    private Integer stock;
}
