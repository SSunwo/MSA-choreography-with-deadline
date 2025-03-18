package choreographywithdeadline.domain;

import choreographywithdeadline.DeliveryApplication;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Delivery_table")
@Data
//<<< DDD / Aggregate Root
public class Delivery {

    @Id
    private String orderId;

    private String productId;

    private String productName;

    private Integer qty;

    private String customerId;

    private String address;

    private String status;

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void startDelivery(OrderCreated orderCreated) {
        //implement business logic here:

        Delivery delivery = new Delivery();
        delivery.setOrderId(String.valueOf(orderCreated.getId()));
        delivery.setCustomerId(orderCreated.getCustomerId());
        delivery.setProductId(orderCreated.getProductId());
        delivery.setProductName(orderCreated.getProductName());
        delivery.setQty(orderCreated.getQty());
        delivery.setAddress(orderCreated.getAddress());
        delivery.setStatus("Delivery Started");

        repository().save(delivery);

        DeliveryStarted deliveryStarted = new DeliveryStarted();
        deliveryStarted.publishAfterCommit();

        /** Example 2:  finding and process
        

        repository().findById(orderCreated.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);

        });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void compensate(OrderRejected orderRejected) {
        //implement business logic here:

        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        */

        /** Example 2:  finding and process
        

        repository().findById(orderRejected.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
