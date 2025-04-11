package com.example.board.common.validator;

import com.example.board.common.interfaces.DomainValidatorInterface;

public abstract class DomainValidator implements DomainValidatorInterface {

    @Override
    public void validateCreate(Object request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateCreate'");
    }

    @Override
    public void validateUpdate(Object request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateUpdate'");
    }

    @Override
    public void validateDelete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateDelete'");
    }

}
