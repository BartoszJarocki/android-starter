package com.starter.data.model.mappers;


public interface Mapper<F, T> {
    T map(F from);
}
