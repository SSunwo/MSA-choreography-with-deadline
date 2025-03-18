package choreographywithdeadline.domain;

import choreographywithdeadline.OrderApplication;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Order_table")
@Data
//<<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customerId;

    private String customerName;

    private String productId;

    private String productName;

    private Integer qty;

    private String address;

    private String status;

    @PrePersist // 저장하기 전에 하는 것. 보통 정해진 값을 넣음
    public void onPrePersist() {
        setStatus("Pending");
    }

    @PostPersist // event 만들어짐
    public void onPostPersist() {
        OrderCreated orderCreated = new OrderCreated(this);
        orderCreated.publishAfterCommit();
    }

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(
            OrderRepository.class
        );
        return orderRepository;
    }

    //<<< Clean Arch / Port Method
    public static void approve(StockDecreased stockDecreased) {

        repository().findById(Long.valueOf(stockDecreased.getOrderId()))
        .ifPresent(order->{
            
            order.setStatus("Approved");
            repository().save(order);

            OrderPlaced orderPlaced = new OrderPlaced(order);
            orderPlaced.publishAfterCommit();
        });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void reject(StockDecreaseFailed stockDecreaseFailed) {

        
        // repository().findById(stockDecreaseFailed.)
        // .ifPresent(order->{
            
        //     order // do something
        //     repository().save(order);


        // });

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void reject(DeliveryFailed deliveryFailed) {
        //implement business logic here:

        /** Example 1:  new item 
        Order order = new Order();
        repository().save(order);

        */

        /** Example 2:  finding and process
        

        repository().findById(deliveryFailed.get???()).ifPresent(order->{
            
            order // do something
            repository().save(order);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void reject(DeadlineReached deadlineReached) {

        repository().findById(deadlineReached.getOrderId())
        .ifPresent(order->{
            
            order.setStatus("Rejected");
            repository().save(order);

            new OrderRejected(order).publishAfterCommit();

        });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
