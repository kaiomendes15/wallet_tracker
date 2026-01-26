package br.com.mywallet.app.domain.model.Transacao;

public class TransacaoMapper {

    public static TransacaoResponseDTO transacaoParaDto(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getData(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getCategoria().getTitulo(),
                transacao.getTipo()
        );
    }
}
