package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class AppRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final PublisherRepository publisherRepository;

    public AppRunner(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        // --------------------------------------------------
        // 初始化
        // --------------------------------------------------
        init();
        display();

        // 精确查找
        findByName("中兴");
        findByName("中兴出版社");

        // 模糊查找，但不支持忽略大小写、忽略首尾空格
        findByNameContaining("社 ");
        findByNameContaining("出");

        deleteAll();
        display();
    }

    /**
     * 显示
     */
    private void display() {
        System.out.println("Display all publishers & books ...");

        publisherRepository.findAll().forEach(publisher -> System.out.println(publisher.toString()));
    }

    /**
     * 初始化
     */
    private void init() {
        System.out.println("Initial 2 publishers with 3 books ...");

        Publisher zx = new Publisher("中兴出版社");
        Book spring = new Book("Spring in Action", zx);
        Book springboot = new Book("Spring Boot in Action", zx);
        zx.getBooks().addAll(Arrays.asList(spring, springboot));

        publisherRepository.save(zx);
    }

    /**
     * 精确查找
     *
     * @param name 出版社名
     */
    private void findByName(String name) {
        System.out.println("findByName [name:{}] ...", name);

        Publisher publisher = publisherRepository.findByName(name);
        if (null == publisher) {
            System.out.println("Publisher [{}]", "<empty>");
        } else {
            System.out.println(publisher.toString());
        }
    }

    /**
     * 模糊查找
     *
     * @param name 出版社名
     */
    private void findByNameContaining(String name) {
        System.out.println("findByNameContaining [name:{}] ...", name);

        publisherRepository.findByNameContaining(name).forEach(publisher -> System.out.println(publisher.toString()));
    }

    /**
     * 删除全部
     */
    private void deleteAll() {
        System.out.println("Delete all ...");

        publisherRepository.deleteAll();
    }

}
