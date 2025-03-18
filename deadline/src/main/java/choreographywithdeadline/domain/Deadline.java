package choreographywithdeadline.domain;

import choreographywithdeadline.DeadlineApplication;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Deadline_table")
@Data
//<<< DDD / Aggregate Root
public class Deadline {

    static final int DURATION = 10 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date deadline;

    private Long orderId;

    private Date startedTime;

    public static DeadlineRepository repository() {
        DeadlineRepository deadlineRepository = DeadlineApplication.applicationContext.getBean(
            DeadlineRepository.class
        );
        return deadlineRepository;
    }

    //<<< Clean Arch / Port Method
    public static void schedule(OrderCreated orderCreated) {
        //implement business logic here:

        Deadline deadline = new Deadline();
        deadline.setOrderId(orderCreated.getId());
        deadline.setStartedTime(new Date(orderCreated.getTimestamp()));
        deadline.setDeadline(new Date(
            deadline.getStartedTime().getTime() + DURATION
        ));

        repository().save(deadline);

        

    }

    public static void sendDeadlineEvents() {

        // 1. 모든 데드라인 레코드를 가져옴
        // 2. 현재시간 구하기
        // 3. 현재시간과 데드라인 field 비교해서 시간이 지난 레코드가 있다면
        // 4. 그 레코드를 이용햐서 DeadlineReached event를 만든다
        // 5. 그 레코드를 삭제

        repository().findAll().forEach(deadline -> {
            Date now = new Date();

            if (now.after(deadline.getDeadline())) {
                new DeadlineReached(deadline).publishAfterCommit();
                repository().delete(deadline);
            }
        });
        
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
