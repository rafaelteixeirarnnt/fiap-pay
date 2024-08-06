package br.com.fiap.fiappay.mappers;

import br.com.fiap.fiappay.models.Clientes;
import br.com.fiap.fiappay.vo.RequestClienteVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientesMapper {

    @Mapping(target = "id", ignore = true)
    Clientes toEntity(RequestClienteVO requestClienteVO);

    RequestClienteVO toVO(Clientes cliente);
}
