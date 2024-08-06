package br.com.fiap.fiappay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_clientes", schema = "fiappay")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CPF
    @NotBlank
    @Column(name = "tx_cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @NotBlank
    @Column(name = "tx_nome", nullable = false, length = 100)
    private String nome;

    @Email
    @NotBlank
    @Column(name = "tx_email", nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank
    @Column(name = "tx_telefone", nullable = false, length = 14)
    private String telefone;

    @NotBlank
    @Column(name = "tx_rua", nullable = false, length = 30)
    private String rua;

    @NotBlank
    @Column(name = "tx_cidade", nullable = false, length = 20)
    private String cidade;

    @NotBlank
    @Column(name = "tx_estado", nullable = false, length = 11)
    private String estado;

    @NotBlank
    @Column(name = "tx_cep", nullable = false, length = 8)
    private String cep;

    @NotBlank
    @Column(name = "tx_pais", nullable = false, length = 10)
    private String pais;
}
