package com.retirement.apiservice.validator;

public interface IValidator<T> {
    public T validated(int itemId, T toValidate, int userId);
}
