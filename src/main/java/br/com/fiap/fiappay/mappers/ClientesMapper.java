package br.com.fiap.fiappay.mappers;

import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.vo.RequestClienteVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientesMapper {

    @Mapping(target = "id", ignore = true)
    Cliente toEntity(RequestClienteVO requestClienteVO);

    RequestClienteVO toVO(Cliente cliente);
}
