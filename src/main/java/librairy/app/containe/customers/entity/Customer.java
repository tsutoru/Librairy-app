package librairy.app.containe.customers.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String address;

  public Customer() {}

  public Customer(
      UUID id, String firstName, String lastName, String email, String phone, String address) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }
}
