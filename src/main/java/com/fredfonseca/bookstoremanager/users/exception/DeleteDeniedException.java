package com.fredfonseca.bookstoremanager.users.exception;

public class DeleteDeniedException extends IllegalArgumentException {
    public DeleteDeniedException() {
        super("Deleção negada! Este usuário possui um ou mais livros alugados");
    }

    public DeleteDeniedException(String username) {
        super(String.format("O administrador padrão do sistema %s, não pode ser deletado", username));
    }
}