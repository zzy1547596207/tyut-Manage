package com.tyut.managesystem.dto;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) { Result<T> r = new Result<>(); r.code = 200; r.message = "success"; r.data = data; return r; }
    public static <T> Result<T> ok() { return ok(null); }
    public static <T> Result<T> fail(String msg) { Result<T> r = new Result<>(); r.code = 500; r.message = msg; return r; }
    public static <T> Result<T> fail(Integer code, String msg) { Result<T> r = new Result<>(); r.code = code; r.message = msg; return r; }
}