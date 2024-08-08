package br.com.fiap.fiappay.mappers;

import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartoesMapper {

    @Mapping(target = "cvv", source = "cvv")
    @Mapping(target = "numero", source = "numero")
    @Mapping(target = "limite", source = "limite")
    @Mapping(target = "dataValidade", source = "dataValidade")
    Cartao toCartao(RequestCartaoVO vo);
}
