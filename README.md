# lab8-673380598-1-sec3
# ส่วนที่ 1: หลักการออกแบบ

## 1. SOLID กับ Entity

ในการออกแบบระบบ Product Shop มีการนำหลักการ SOLID มาใช้ โดยเฉพาะ Single Responsibility Principle (SRP) และ Dependency Inversion Principle (DIP)

- **Product** ทำหน้าที่เก็บข้อมูลหลักของสินค้า เช่น ชื่อสินค้า ราคา และข้อมูลพื้นฐาน
- **ProductDetail** ทำหน้าที่เก็บรายละเอียดเพิ่มเติมของสินค้า
- **Review** ทำหน้าที่เก็บข้อมูลรีวิวและคะแนนของผู้ใช้

การแยก Entity ออกเป็นแต่ละส่วนช่วยให้แต่ละคลาสมีหน้าที่ชัดเจน ไม่รวมข้อมูลทุกอย่างไว้ใน Entity เดียว ทำให้แก้ไขและดูแลระบบได้ง่ายขึ้น นอกจากนี้ Controller, Service และ Repository ก็แยกหน้าที่ออกจากกันตามหลัก SRP เช่นกัน

---

## 2. 1:1 vs 1:N

### 1:1 (One-to-One)

เป็นความสัมพันธ์ที่ข้อมูลหนึ่งรายการมีความสัมพันธ์กับข้อมูลอีกหนึ่งรายการเท่านั้น

ตัวอย่าง:

Product 1 ---- 1 ProductDetail

หมายความว่า Product 1 รายการมี ProductDetail 1 รายการ และ ProductDetail นั้นเป็นของ Product เพียง 1 รายการ

### 1:N (One-to-Many)

เป็นความสัมพันธ์ที่ข้อมูลหนึ่งรายการสามารถมีข้อมูลอีกประเภทได้หลายรายการ

ตัวอย่าง:

Product 1 ---- N Review

หมายความว่า Product 1 รายการสามารถมี Review ได้หลายรายการ แต่ Review แต่ละรายการจะเป็นของ Product เพียง 1 รายการ

### สรุป

ควรใช้ **1:1** เมื่อข้อมูลทั้งสองฝั่งมีความสัมพันธ์แบบหนึ่งต่อหนึ่ง และใช้ **1:N** เมื่อข้อมูลหลักหนึ่งรายการสามารถมีข้อมูลย่อยได้หลายรายการ

---

## 3. Strategy Pattern

ใช้ Strategy Pattern เพื่อแยกวิธีการคำนวณส่วนลดออกจากส่วนของการทำงานหลัก ทำให้สามารถเปลี่ยนวิธีคำนวณส่วนลดได้ง่าย

ตัวอย่างเช่น ระบบสามารถมี Strategy สำหรับส่วนลดหลายรูปแบบ ได้แก่

- **PercentageDiscount** ลดราคาเป็นเปอร์เซ็นต์ เช่น 10%
- **FixedDiscount** ลดราคาเป็นจำนวนเงิน เช่น 50 บาท

โครงสร้างสามารถเป็นดังนี้:

DiscountStrategy
- PercentageDiscount
- FixedDiscount

เมื่อระบบต้องการคำนวณส่วนลด จะเลือก Strategy ที่ต้องการมาใช้งาน แทนการเขียนเงื่อนไขจำนวนมากไว้ใน Service เดียว

ข้อดีคือสามารถเพิ่มรูปแบบส่วนลดใหม่ได้ง่าย และไม่จำเป็นต้องแก้ไขโค้ดส่วนอื่นของระบบ

---

## 4. Execution Flow

ลำดับการทำงานตั้งแต่ HTTP Request เข้ามาจนถึงฐานข้อมูล มีขั้นตอนดังนี้

```text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
.


ส่วนที่ 2: Code + คำอธิบาย
Entity ทั้ง 3 ตัว
Product.java
```java
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name, category, brand, discountType;
    private int stock;
    private Double price;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "detail_id", referencedColumnName = "id")
    private ProductDetail detail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    // constructors, getters, setters...
}
```
`@Entity` / `@Table(name="product")` — บอก JPA ว่าคลาสนี้ map กับตาราง `product`
`@OneToOne` ฝั่งเจ้าของความสัมพันธ์ (มี `@JoinColumn` เก็บ FK `detail_id`) — Product เป็นฝ่ายถือ FK ไปยัง ProductDetail
`cascade = CascadeType.ALL, orphanRemoval = true` — เวลาบันทึก/ลบ Product ให้ทำกับ ProductDetail ที่ผูกอยู่ตามไปด้วย และถ้าตัด detail ออกจาก product ก็ให้ลบ orphan record ทิ้ง
`@OneToMany(mappedBy = "product", ...)` — ฝั่งนี้ไม่ใช่เจ้าของความสัมพันธ์ (ไม่มี FK ในตาราง product) แค่บอกว่า field `product` ใน Review เป็นตัวจับคู่กลับมา
ProductDetail.java
```java
@Entity
@Table(name = "product_Detail")
public class ProductDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description, warranty, dimensions, manufacturedCountry;
    private Double weight;

    @OneToOne(mappedBy = "detail")
    private Product product;
    // constructors, getters, setters...
}
```
`@OneToOne(mappedBy = "detail")` — ฝั่งนี้เป็นฝั่ง inverse (ไม่ถือ FK) เพราะ FK (`detail_id`) อยู่ในตาราง `product` แล้ว การทำแบบนี้ถูกต้องตามหลัก 1:1 ที่ควรมี FK อยู่ฝั่งเดียว ป้องกันข้อมูลซ้ำซ้อน
Review.java
```java
@Entity
@Table(name = "reviews")
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewer, comment;
    private int rating;
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    // constructors, getters, setters...
}
```
`@ManyToOne` + `@JoinColumn(name="product_id")` — Review เป็นฝั่ง "many" ถือ FK `product_id` ชี้กลับไปที่ Product เดียว ตรงกับความสัมพันธ์ 1:N ที่อธิบายในส่วนที่ 1
Service และ Controller พร้อมอธิบาย Constructor Injection
ProductService.java
```java
@Service
public class ProductService {
    private final ProductRepository reProductRepository;
    private final ProductDetailRepository reDetailRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(ProductRepository reProductRepository,
                           ProductDetailRepository reDetailRepository,
                           ReviewRepository reviewRepository) {
        this.reProductRepository = reProductRepository;
        this.reDetailRepository = reDetailRepository;
        this.reviewRepository = reviewRepository;
    }
    // showAllProduct, showSomeProduct, saveProduct, updateProduct, deleteProduct
}
```
ProductController.java
```java
@Controller
public class ProductController {
    ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }
    // showHomePage, showAddPage, saveProduct, showEditPage, updateProduct, showDeletePage, deleteProduct
}
```
อธิบาย Constructor Injection:
ทั้ง Service และ Controller รับ dependency (Repository / Service) ผ่าน constructor แทนการใช้ `@Autowired` บน field ตรง ๆ — Spring จะ inject bean ให้อัตโนมัติตอนสร้าง object
field ของ ProductService ประกาศเป็น `final` ได้ เพราะค่าถูกกำหนดครั้งเดียวใน constructor ทำให้ dependency ไม่ถูกเปลี่ยนแปลงทีหลัง (immutable) และไม่มีทาง null
ข้อดีเทียบกับ field injection: ทดสอบง่ายกว่า (ส่ง mock เข้า constructor ตอนเขียน unit test ได้ตรง ๆ โดยไม่ต้องพึ่ง Spring context) และเห็น dependency ทั้งหมดชัดเจนจาก signature ของ constructor
