package com.alessandromelo.exception.customer;

public class CpfAlreadyExistsException extends RuntimeException {
  public CpfAlreadyExistsException(String cpf) {
    super("cpf: " + cpf + " already registered!");
  }
}
